package main.java.components;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;
import main.java.connectors.ControllerConnector;
import main.java.interfaces.ControllerCI;
import main.java.interfaces.WasherCI;
import main.java.interfaces.WasherImplementationI;
import main.java.ports.ControllerOutboundPort;
import main.java.ports.WasherInboundPort;
import main.java.utils.Log;
import main.java.utils.WasherModes;

@OfferedInterfaces(offered = { WasherCI.class })
@RequiredInterfaces(required = { ControllerCI.class })
public class Washer extends AbstractComponent implements WasherImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	/**
	 * Serial number for registering on controller
	 */
	protected String serialNumber;

	/**
	 * Current state of the washer
	 */
	protected AtomicBoolean isOn;

	/**
	 * Program temperature in °C
	 */
	protected AtomicInteger programTemperature;

	/**
	 * mode currently used 0 ECO 1 STD 2 PERFORMANCE
	 */
	protected AtomicInteger mode;

	/**
	 * Inbound port of the washer
	 */
	protected WasherInboundPort wip;

	/**
	 * Outbound port of controller for registering purposes
	 */
	protected ControllerOutboundPort cop;

	/**
	 * uri of controller inbound port
	 */
	protected String cip_uri;

	/**
	 * boolean indicating whether the battery has plan
	 */
	protected AtomicBoolean hasPlan;

	/**
	 * last time the chrono has been triggered
	 */
	protected AtomicReference<LocalTime> lastStartTime;

	/**
	 * duration time of last planified task
	 */
	protected AtomicReference<Duration> durationLastPlanned;

	/**
	 * deadline of the last program
	 */
	protected AtomicReference<LocalTime> deadlineTime;

	/**
	 * delay the program
	 */
	protected AtomicReference<Duration> postponeDur;

	/**
	 *
	 * @param reflectionPortURI
	 * @param serialNumber      serial number of Washer component
	 * @param wipURI            inbound port's URI of Washer
	 * @param cip_URI           inbound port's URI of controller for registering
	 * @throws Exception
	 */

	protected Washer(String reflectionPortURI, boolean toogleTracing, String serialNumber, String wipURI,
			String cip_URI) throws Exception {
		super(reflectionPortURI, 1, 0);
		myUri = reflectionPortURI;
		this.serialNumber = serialNumber;
		this.cip_uri = cip_URI;
		initialise(wipURI);
		if (toogleTracing) {
			this.tracer.get().setTitle("Washer component");
			this.tracer.get().setRelativePosition(0, 3);
			this.toggleTracing();
		}
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * <pre>
	 *     pre      {@code washerInboundPortURI != null}
	 *     pre      {@code washerInboundPortURI.isEmpty()}
	 *     post     {@code getStateWasher == false }
	 *     post     {@code getTemperatureOperating == 0}
	 * </pre>
	 * 
	 * @param washerInboundPortURI
	 * @throws Exception
	 */
	protected void initialise(String washerInboundPortURI) throws Exception {
		assert washerInboundPortURI != null : new PreconditionException("washerInboundPortUri != null");
		assert !washerInboundPortURI.isEmpty() : new PreconditionException("washerInboundPortURI.isEmpty()");
		this.mode = new AtomicInteger();
		this.setMode(WasherModes.ECO.ordinal());
		this.isOn = new AtomicBoolean(true);
		this.hasPlan = new AtomicBoolean(false);
		this.deadlineTime = new AtomicReference<>(null);
		this.durationLastPlanned = new AtomicReference<>(null);
		this.lastStartTime = new AtomicReference<>(null);
		this.postponeDur = new AtomicReference<>(null);
		this.wip = new WasherInboundPort(washerInboundPortURI, this);
		this.wip.publishPort();
		this.cop = new ControllerOutboundPort(this);
		this.cop.publishPort();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.wip.unpublishPort();
			this.cop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();
		try {
			if (cip_uri.length() > 0)
				this.doPortConnection(this.cop.getPortURI(), this.cip_uri,
						ControllerConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
	}

	@Override
	public synchronized void finalise() throws Exception {
		this.cop.doDisconnection();
		super.finalise();
	}

	@Override
	public synchronized void execute() throws Exception {
		byte[] encoded = Files.readAllBytes(Paths.get("src/main/java/adapter/washer-control.xml"));
		String xmlFile = new String(encoded, "UTF-8");
		boolean isRegister = this.cop.register(this.serialNumber, wip.getPortURI(), xmlFile);
		if (!isRegister)
			throw new Exception("Washer can't register to controller");
	}
	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	@Override
	public boolean isTurnedOn() throws Exception {
		boolean res = this.isOn.get();
		Log.printAndLog(this, "isTurnedOn() service result : " + res);
		return res;
	}

	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		this.programTemperature.set(temperature);
		Log.printAndLog(this, "setProgramTemperature(" + temperature + ") service called");
	}

	@Override
	public int getProgramTemperature() throws Exception {
		int res = this.programTemperature.get();
		Log.printAndLog(this, "getProgramTemperature() service result : " + res);
		return res;
	}

	@Override
	public boolean turnOn() throws Exception {
		boolean succeed = false;
		succeed = this.isOn.compareAndSet(false, true);
		Log.printAndLog(this, "turnOn() service result : " + succeed);
		return succeed;
	}

	@Override
	public boolean turnOff() throws Exception {
		boolean succeed = false;
		succeed = this.isOn.compareAndSet(true, false);
		Log.printAndLog(this, "turnOff() service result : " + succeed);
		return succeed;
	}

	@Override
	public boolean upMode() throws Exception {
		boolean succeed = false;
		if (this.mode.get() == WasherModes.PERFORMANCE.ordinal()) // wheel restore to 0
			succeed = this.mode.compareAndSet(this.mode.get(), WasherModes.ECO.ordinal());
		else {
			succeed = this.mode.compareAndSet(this.mode.get(), this.mode.get() + 1);
		}

		Log.printAndLog(this, "upMode() service result : " + succeed);
		return succeed;
	}

	@Override
	public boolean downMode() throws Exception {
		boolean succeed = false;

		if (this.mode.get() == WasherModes.ECO.ordinal()) // wheel restore to 0
			succeed = this.mode.compareAndSet(this.mode.get(), WasherModes.PERFORMANCE.ordinal());
		else {
			succeed = this.mode.compareAndSet(this.mode.get(), this.mode.get() - 1);
		}

		Log.printAndLog(this, "downMode() service result : " + succeed);
		return succeed;
	}

	@Override
	public boolean setMode(int modeIndex) throws Exception {
		boolean succeed = this.mode.compareAndSet(this.mode.get(), modeIndex);
		Log.printAndLog(this, "setMode(" + modeIndex + ") service result : " + succeed);
		return succeed;
	}

	@Override
	public int currentMode() throws Exception {
		int res = this.mode.get();
		Log.printAndLog(this, "currentMode() service result : " + res);
		return res;
	}

	@Override
	public boolean hasPlan() throws Exception {
		boolean res = this.hasPlan.get();
		Log.printAndLog(this, "hasPlan() service result : " + res);
		return res;
	}

	@Override
	public LocalTime startTime() throws Exception {
		LocalTime res = this.lastStartTime.get();
		Log.printAndLog(this, "startTime() service result : " + res);
		return res;
	}

	@Override
	public Duration duration() throws Exception {
		Duration res = this.durationLastPlanned.get();
		Log.printAndLog(this, "duration() service result : " + res);
		return res;
	}

	@Override
	public LocalTime deadline() throws Exception {
		LocalTime res = this.deadlineTime.get();
		Log.printAndLog(this, "deadline() service result : " + res);
		return res;
	}

	@Override
	public boolean postpone(Duration d) throws Exception {
		boolean succeed = false;
		succeed = this.lastStartTime.compareAndSet(this.lastStartTime.get(),
				this.lastStartTime.get().plusHours(d.toHours()));
		Log.printAndLog(this, "postpone(" + d + ") service result : " + succeed);
		return succeed;
	}

	@Override
	public boolean cancel() throws Exception {
		boolean succeed = false;
		synchronized (this.lastStartTime) {
			succeed = this.lastStartTime.compareAndSet(this.lastStartTime.get(), null);
			succeed = this.hasPlan.compareAndSet(true, false);
			succeed = this.durationLastPlanned.compareAndSet(this.durationLastPlanned.get(), null);
			succeed = this.deadlineTime.compareAndSet(this.deadlineTime.get(), null);
		}
		Log.printAndLog(this, "cancel() service result : " + succeed);
		return succeed;
	}

	@Override
	public boolean planifyEvent(Duration durationLastPlanned, LocalTime deadline) {
		boolean succeed = false;
		synchronized (this.lastStartTime) {
			succeed = true;
			succeed &= this.hasPlan.compareAndSet(false, true);
			succeed &= this.durationLastPlanned.compareAndSet(this.durationLastPlanned.get(), durationLastPlanned);
			succeed &= this.deadlineTime.compareAndSet(this.deadlineTime.get(), deadline);
		}
		return succeed;
	}

	// TODO a voir si necessaire, mais a priori oui

	@Override
	public void setProgramDuration(int duration) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public int getProgramDuration() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
