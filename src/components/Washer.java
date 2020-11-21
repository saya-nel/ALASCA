package components;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import connectors.ControllerConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.ControllerCI;
import interfaces.WasherCI;
import interfaces.WasherImplementationI;
import ports.ControllerOutboundPort;
import ports.WasherInboundPort;
import utils.WasherModes;

@OfferedInterfaces(offered = { WasherCI.class })
@RequiredInterfaces(required = { ControllerCI.class })
public class Washer extends AbstractComponent implements WasherImplementationI {

	protected static final String CONTROL_INTERFACE_DESCRIPTOR = "";

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
	 * @param serialNumber			serial number of Washer component
	 * @param wipURI				inbound port's URI of Washer
	 * @param cip_URI				inbound port's URI of controller for registering
	 * @throws Exception
	 */

	protected Washer(String reflectionPortURI, String serialNumber, String wipURI, String cip_URI) throws Exception {
		super(reflectionPortURI, 1, 0);
		myUri = reflectionPortURI;
		this.serialNumber = serialNumber;
		this.cip_uri = cip_URI;
		initialise(wipURI);
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
		this.setMode(0);
		this.turnOn();
		this.deadlineTime = null;
		this.durationLastPlanned = null;
		this.postponeDur = null;
		this.lastStartTime = null;
		this.hasPlan.set(false);
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
			this.doPortConnection(this.cop.getPortURI(), this.cip_uri, ControllerConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
	}

	@Override
	public synchronized void execute() throws Exception {
		boolean isRegister = this.cop.register(this.serialNumber, wip.getPortURI(),
				Washer.CONTROL_INTERFACE_DESCRIPTOR);
		if (!isRegister)
			throw new Exception("can't register to controller");
	}
	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	@Override
	public boolean isTurnedOn() throws Exception {
		return this.isOn.get();
	}

	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		this.programTemperature.set(temperature);
	}

	@Override
	public int getProgramTemperature() throws Exception {
		return this.programTemperature.get();
	}

	@Override
	public boolean turnOn() throws Exception {
		boolean succeed = false;
		succeed = this.isOn.compareAndSet(false, true);
		return succeed;
	}

	@Override
	public boolean turnOff() throws Exception {
		boolean succeed = false;
		succeed = this.isOn.compareAndSet(true, false);
		return succeed;
	}

	@Override
	public boolean upMode() throws Exception {
		boolean succeed = false;
		synchronized (this.lastStartTime) {
			if (this.mode.get() == WasherModes.PERFORMANCE.ordinal()) // wheel restaure to 0
				succeed = this.mode.compareAndSet(this.mode.get(), WasherModes.ECO.ordinal());
			else {
				succeed = this.mode.compareAndSet(this.mode.get(), this.mode.getAndIncrement());
			}
		}

		return succeed;
	}

	@Override
	public boolean downMode() throws Exception {
		boolean succeed = false;
		synchronized (this.lastStartTime) {
			if (this.mode.get() == WasherModes.ECO.ordinal()) // wheel restaure to 0
				succeed = this.mode.compareAndSet(this.mode.get(), WasherModes.PERFORMANCE.ordinal());
			else {
				succeed = this.mode.compareAndSet(this.mode.get(), this.mode.getAndDecrement());
			}
		}

		return succeed;
	}

	@Override
	public boolean setMode(int modeIndex) throws Exception {
		boolean succeed = this.mode.compareAndSet(this.mode.get(), modeIndex);
		return succeed;
	}

	@Override
	public int currentMode() throws Exception {
		return this.mode.get();
	}

	@Override
	public boolean hasPlan() throws Exception {
		return this.hasPlan.get();
	}

	@Override
	public LocalTime startTime() throws Exception {
		return this.lastStartTime.get();
	}

	@Override
	public Duration duration() throws Exception {
		return this.durationLastPlanned.get();
	}

	@Override
	public LocalTime deadline() throws Exception {
		return this.deadlineTime.get();
	}

	@Override
	public boolean postpone(Duration d) throws Exception {
		boolean succeed = false;
		succeed = this.lastStartTime.compareAndSet(this.lastStartTime.get(),
				this.lastStartTime.get().plusHours(d.toHours()));
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
