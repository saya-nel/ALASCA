package main.java.components;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
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
import main.java.interfaces.BatteryCI;
import main.java.interfaces.BatteryImplementationI;
import main.java.interfaces.ControllerCI;
import main.java.ports.BatteryInboundPort;
import main.java.ports.ControllerOutboundPort;
import main.java.utils.BatteryState;
import main.java.utils.Log;

/**
 * Class representing the Battery component
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { BatteryCI.class })
@RequiredInterfaces(required = { ControllerCI.class })
public class Battery extends AbstractComponent implements BatteryImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	/**
	 * Serial number for registering on controller
	 */
	protected String serialNumber = "SERIALNUMBER";

	/**
	 * Actual charge of battery in mA/h
	 */
	protected float batteryCharge;

	/**
	 * Actual battery mode 0 for RECHARGING 1 for DRAINING 2 for SLEEPING
	 */
	protected AtomicInteger operatingMode;

	/**
	 * Maximum energy of the battery
	 */
	protected float maximumEnergy;

	/**
	 * Inbound port of the battery
	 */
	protected BatteryInboundPort bip;

	/**
	 * Outbound port of the battery for registering
	 */
	protected ControllerOutboundPort cop;

	/**
	 *
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

	protected AtomicReference<Duration> postponeDur;

	/**
	 * Constructor of battery
	 * 
	 * @param reflectionPortURI URI battery component
	 * @param bipURI            URI inbound port battery
	 * @throws Exception
	 */
	protected Battery(String reflectionPortURI, boolean toogleTracing, String serialNumber, String bipURI,
			String cip_URI, float maxEnergy) throws Exception {
		super(reflectionPortURI, 1, 0);
		myUri = reflectionPortURI;
		this.serialNumber = serialNumber;
		this.cip_uri = cip_URI;
		this.initialise(bipURI, maxEnergy);
		if (toogleTracing) {
			this.tracer.get().setTitle("Battery component");
			this.tracer.get().setRelativePosition(0, 1);
			this.toggleTracing();
		}
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * Initialize the battery component
	 *
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 *     pre		{@code batteryInboundPortURI != null}
	 *     pre 		{@code batteryInboundPortURI.isEmpty()}
	 *     post 	{@code getBatteryState() == BatteryState.SLEEPING }
	 *     post 	{@code getBatteryCharge() == 0}
	 * </pre>
	 * 
	 * @param batteryInboundPortURI
	 * @throws Exception
	 */
	protected void initialise(String batteryInboundPortURI, float maximumEnergy) throws Exception {
		assert batteryInboundPortURI != null : new PreconditionException("batteryInboundPortURI != null");
		assert !batteryInboundPortURI.isEmpty() : new PreconditionException("batteryInboundPortURI.isEmpty()");
		this.operatingMode = new AtomicInteger();
		this.setMode(BatteryState.SLEEPING.ordinal());
		this.hasPlan = new AtomicBoolean(false);
		this.maximumEnergy = maximumEnergy;
		this.deadlineTime = new AtomicReference<>(null);
		this.durationLastPlanned = new AtomicReference<>(null);
		this.lastStartTime = new AtomicReference<>(null);
		this.postponeDur = new AtomicReference<>(null);
		this.bip = new BatteryInboundPort(batteryInboundPortURI, this);
		this.bip.publishPort();
		this.cop = new ControllerOutboundPort(this);
		this.cop.publishPort();
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
	public synchronized void finalise() throws Exception {
		this.cop.doDisconnection();
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.cop.unpublishPort();
			this.bip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void execute() throws Exception {

		byte[] encoded = Files.readAllBytes(Paths.get("src/main/java/adapter/battery-control.xml"));
		String xmlFile = new String(encoded, "UTF-8");
		boolean isRegister = this.cop.register(this.serialNumber, bip.getPortURI(), xmlFile);
		if (!isRegister)
			throw new Exception("Battery can't register to controller");
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		float result = 0;
		Log.printAndLog(this, "getBatteryCharge() service result : " + result);
		return result;
	}

	/**
	 * @see interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		boolean succeed = false;
		if (this.operatingMode.get() == BatteryState.SLEEPING.ordinal()) {// return to 0
			succeed = this.operatingMode.compareAndSet(this.operatingMode.get(), BatteryState.RECHARGING.ordinal());
		} else {
			succeed = this.operatingMode.compareAndSet(this.operatingMode.get(), this.operatingMode.get() +1);
		}
		Log.printAndLog(this, "upMode() service result : " + succeed);
		return succeed;
	}

	/**
	 * @see interfaces.BatteryImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		boolean succeed = false;
		if (this.operatingMode.get() == BatteryState.RECHARGING.ordinal()) {
			succeed = this.operatingMode.compareAndSet(this.operatingMode.get(), BatteryState.SLEEPING.ordinal());
		} else {
			succeed = this.operatingMode.compareAndSet(this.operatingMode.get(), this.operatingMode.get() - 1);
		}
		Log.printAndLog(this, "downMode() service result : " + succeed);
		return succeed;
	}

	/**
	 * @see interfaces.BatteryImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {

		boolean succeed = false;
		try {
			if (modeIndex < BatteryState.RECHARGING.ordinal() || modeIndex > BatteryState.SLEEPING.ordinal()) {
				throw new Exception("wrong mode for set mode in Battery");
			} else {
				succeed = this.operatingMode.compareAndSet(this.operatingMode.get(), modeIndex);
			}
		} catch (Exception e) {
			System.err.println("wrong mode for set mode in battery");
		}
		Log.printAndLog(this, "setMode(" + modeIndex + ") service result : " + succeed);
		return succeed;

	}

	/**
	 * @see interfaces.BatteryImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		int result = this.operatingMode.get();
		Log.printAndLog(this, "currentMode() service result : " + result);
		return result;
	}

	/**
	 * @see interfaces.BatteryImplementationI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		boolean result = this.hasPlan.get();
		Log.printAndLog(this, "hasPlan() service result : " + result);
		return result;
	}

	/**
	 * @see interfaces.BatteryImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		LocalTime result = this.lastStartTime.get();
		Log.printAndLog(this, "startTime() service result : " + result);
		return result;
	}

	/**
	 * @see interfaces.BatteryImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		Duration result = this.durationLastPlanned.get();
		Log.printAndLog(this, "Duration() service result : " + result);
		return result;
	}

	/**
	 * @see interfaces.BatteryImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		LocalTime result = this.deadlineTime.get();
		Log.printAndLog(this, "deadLine() service result : " + result);
		return result;
	}

	/**
	 * @see interfaces.BatteryImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		boolean succeed = false;
		succeed = this.lastStartTime.compareAndSet(this.lastStartTime.get(),
				this.lastStartTime.get().plusHours(d.toHours()));
		Log.printAndLog(this, "postpone(" + d + ") service result : " + succeed);
		return succeed;
	}

	/**
	 * @see interfaces.BatteryImplementationI#cancel()
	 */
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
		synchronized (this.lastStartTime)
		{
			succeed = true;
			succeed &= this.hasPlan.compareAndSet(false, true);
			succeed &= this.durationLastPlanned.compareAndSet(this.durationLastPlanned.get(), durationLastPlanned);
			succeed &= this.deadlineTime.compareAndSet(this.deadlineTime.get(), deadline);
		}
		return succeed;
	}

}
