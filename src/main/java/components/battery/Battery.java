package main.java.components.battery;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.components.battery.sil.BatteryElectricalSILModel;
import main.java.components.battery.sil.BatteryRTAtomicSimulatorPlugin;
import main.java.components.battery.sil.BatteryStateSILModel;
import main.java.components.battery.sil.events.SetDraining;
import main.java.components.battery.sil.events.SetRecharging;
import main.java.components.battery.sil.events.SetSleeping;
import main.java.deployment.RunSILSimulation;
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
public class Battery extends AbstractCyPhyComponent implements BatteryImplementationI {

	public enum Operations {
		SetDraining, SetRecharching, SetSleeping
	}

	/**
	 * URI of the reflection inbound port of this component; works for singleton.
	 */
	public static final String REFLECTION_INBOUND_PORT_URI = "battery-ibp-uri";

	/** true if the component is executed in a SIL simulation mode. */
	protected boolean isSILSimulated;
	/** true if the component is under unit test. */
	protected boolean isUnitTest;

	protected BatteryRTAtomicSimulatorPlugin simulatorPlugin;
	protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

	/**
	 * Serial number for registering on controller
	 */
	protected String serialNumber;

	/**
	 * Actual charge of battery in ah
	 */
	protected float batteryCharge;

	// ah
	protected float maximumPowerLevel; // ah

	/**
	 * Actual battery mode 0 for RECHARGING 1 for DRAINING 2 for SLEEPING
	 */
	protected BatteryState operatingMode;

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
	protected Battery(String serialNumber, String bipURI, String cip_URI, boolean isSILSimulated, boolean isUnitTest)
			throws Exception {
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);
		// infos
		this.serialNumber = serialNumber;
		this.cip_uri = cip_URI;
		this.isSILSimulated = isSILSimulated;
		this.isUnitTest = isUnitTest;
		// power
		this.maximumPowerLevel = 189;
		this.batteryCharge = this.maximumPowerLevel;
		// modes
		this.operatingMode = BatteryState.SLEEPING;
		// planning
		this.hasPlan = new AtomicBoolean(false);
		this.deadlineTime = new AtomicReference<>(null);
		this.durationLastPlanned = new AtomicReference<>(null);
		this.lastStartTime = new AtomicReference<>(null);
		this.postponeDur = new AtomicReference<>(null);
		// ports
		this.bip = new BatteryInboundPort(bipURI, this);
		this.bip.publishPort();
		this.cop = new ControllerOutboundPort(this);
		this.cop.localPublishPort();
		// tracer
		this.tracer.get().setTitle("Battery component");
		this.tracer.get().setRelativePosition(0, 2);
		this.toggleTracing();
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();
		if (this.isSILSimulated) {
			try {
				// create the scheduled executor service that will run the
				// simulation tasks
				this.createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
				// create and initialise the atomic simulator plug-in that will
				// hold and execute the SIL simulation models
				this.simulatorPlugin = new BatteryRTAtomicSimulatorPlugin();
				this.simulatorPlugin.setPluginURI(BatteryStateSILModel.URI);
				this.simulatorPlugin.setSimulationExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI);
				this.simulatorPlugin.initialiseSimulationArchitecture(this.isUnitTest);
				this.installPlugin(this.simulatorPlugin);
			} catch (Exception e) {
				throw new ComponentStartException(e);
			}
		}
//		try {
//			if (cip_uri.length() > 0)
//				this.doPortConnection(this.cop.getPortURI(), this.cip_uri,
//						ControllerConnector.class.getCanonicalName());
//		} catch (Exception e) {
//			throw new ComponentStartException(e);
//		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		if (this.isSILSimulated && this.isUnitTest) {
			this.simulatorPlugin.setSimulationRunParameters(new HashMap<String, Object>());
			this.simulatorPlugin.startRTSimulation(System.currentTimeMillis() + 100, 0.0, 10.1);
		}

		class DecreaseEnergy extends TimerTask {
			@Override
			public void run() {
				if (operatingMode == BatteryState.DRAINING) {
					batteryCharge -= (BatteryElectricalSILModel.DRAINING_MODE_PRODUCTION
							/ BatteryElectricalSILModel.TENSION) / 3600;
					if (batteryCharge <= 0) {
						batteryCharge = 0;
						try {
							setMode(1); // put the battery on sleeping mode
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					logMessage("current battery charge : " + batteryCharge);
				} else if (operatingMode == BatteryState.RECHARGING) {
					batteryCharge += (BatteryElectricalSILModel.RECHARGING_MODE_CONSUMPTION
							/ BatteryElectricalSILModel.TENSION) / 3600;
					if (batteryCharge >= maximumPowerLevel) {
						batteryCharge = maximumPowerLevel;
						try {
							setMode(1); // put the battery on sleeping mode
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					logMessage("current battery charge : " + batteryCharge);
				}
			}
		}

		// wait the start of simulation and run Decrease petrol each simulated second
		Thread.sleep(RunSILSimulation.DELAY_TO_START_SIMULATION);
		Timer t = new Timer();
		t.schedule(new DecreaseEnergy(), 0, (long) (1000 / RunSILSimulation.ACC_FACTOR));

//		byte[] encoded = Files.readAllBytes(Paths.get("src/main/java/adapter/battery-control.xml"));
//		String xmlFile = new String(encoded, "UTF-8");
//		boolean isRegister = this.cop.register(this.serialNumber, bip.getPortURI(), xmlFile);
//		if (!isRegister)
//			throw new Exception("Battery can't register to controller");

	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void finalise() throws Exception {
		if (cop.connected())
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

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see main.java.interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		float result = 0;
		Log.printAndLog(this, "getBatteryCharge() service result : " + result);
		return result;
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		boolean succeed = false;
		switch (operatingMode) {
		case DRAINING:
			operatingMode = BatteryState.RECHARGING;
			simulateOperation(Operations.SetRecharching);
			succeed = true;
			break;
		case RECHARGING:
			succeed = false;
			break;
		case SLEEPING:
			succeed = true;
			operatingMode = BatteryState.RECHARGING;
			simulateOperation(Operations.SetRecharching);
			break;
		default:
			break;
		}
		this.logMessage("upMode() service result : " + succeed + ", current mode : " + operatingMode);
		return succeed;
	}

	/**
	 * @see interfaces.BatteryImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		boolean succeed = false;
		switch (operatingMode) {
		case DRAINING:
			succeed = false;
			break;
		case RECHARGING:
			succeed = true;
			operatingMode = BatteryState.SLEEPING;
			simulateOperation(Operations.SetSleeping);
			break;
		case SLEEPING:
			succeed = true;
			operatingMode = BatteryState.DRAINING;
			simulateOperation(Operations.SetDraining);
			break;
		default:
			break;
		}
		this.logMessage("downMode() service result : " + succeed + ", current mode : " + operatingMode);
		return succeed;
	}

	/**
	 * @see interfaces.BatteryImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		boolean succeed = false;
		switch (modeIndex) {
		case 0:
			if (operatingMode != BatteryState.DRAINING) {
				succeed = true;
				operatingMode = BatteryState.DRAINING;
				simulateOperation(Operations.SetDraining);
			}
			break;
		case 1:
			if (operatingMode != BatteryState.SLEEPING) {
				succeed = true;
				operatingMode = BatteryState.SLEEPING;
				simulateOperation(Operations.SetSleeping);
			}
			break;
		case 2:
			if (operatingMode != BatteryState.RECHARGING) {
				succeed = true;
				operatingMode = BatteryState.RECHARGING;
				simulateOperation(Operations.SetRecharching);
			}
			break;
		default:
			break;
		}
		this.logMessage("setMode(" + modeIndex + ") service result : " + succeed + ", current mode : " + operatingMode);
		return succeed;

	}

	/**
	 * @see interfaces.BatteryImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		int result = this.operatingMode.ordinal();
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
	 * @see main.java.interfaces.BatteryImplementationI#cancel()
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

	/**
	 * @see main.java.interfaces.BatteryImplementationI#planifyEvent(Duration,
	 *      LocalTime)
	 */
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

	protected void simulateOperation(Operations op) throws Exception {
		switch (op) {
		case SetDraining:
			this.simulatorPlugin.triggerExternalEvent(BatteryStateSILModel.URI, t -> new SetDraining(t));
			break;
		case SetRecharching:
			this.simulatorPlugin.triggerExternalEvent(BatteryStateSILModel.URI, t -> new SetRecharging(t));
			break;
		case SetSleeping:
			this.simulatorPlugin.triggerExternalEvent(BatteryStateSILModel.URI, t -> new SetSleeping(t));
		default:
			break;
		}
	}

}
