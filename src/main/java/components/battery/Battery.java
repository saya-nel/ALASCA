package main.java.components.battery;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.components.battery.interfaces.BatteryCI;
import main.java.components.battery.interfaces.BatteryImplementationI;
import main.java.components.battery.ports.BatteryInboundPort;
import main.java.components.battery.sil.BatteryElectricalSILModel;
import main.java.components.battery.sil.BatteryRTAtomicSimulatorPlugin;
import main.java.components.battery.sil.BatteryStateSILModel;
import main.java.components.battery.sil.events.SetDraining;
import main.java.components.battery.sil.events.SetRecharging;
import main.java.components.battery.sil.events.SetSleeping;
import main.java.components.battery.utils.BatteryState;
import main.java.components.controller.connectors.ControllerConnector;
import main.java.components.controller.interfaces.ControllerCI;
import main.java.components.controller.ports.ControllerOutboundPort;
import main.java.deployment.RunSILSimulation;
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
	protected final float maximumPowerLevel = 189; // ah

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
	 * Start time of the planed program
	 */
	protected AtomicReference<LocalTime> startTime;

	/**
	 * End time of the planed program
	 */
	protected AtomicReference<LocalTime> endTime;

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
		this.batteryCharge = 5;
		// modes
		this.operatingMode = BatteryState.SLEEPING;
		// planning
		this.startTime = new AtomicReference<>(null);
		this.endTime = new AtomicReference<>(null);
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
		try {
			if (cip_uri.length() > 0)
				this.doPortConnection(this.cop.getPortURI(), this.cip_uri,
						ControllerConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
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

		Battery me = this;

		class DecreaseEnergy extends TimerTask {
			@Override
			public void run() {

				try {
					// if the battery have a planified program that need to start, we start it
					if (startTime.get() != null && startTime().isAfter(LocalTime.now())) {
						setMode(BatteryState.RECHARGING.ordinal());
					}
					// if the battery have a planified program that is finish, we cancel it
					else if (startTime.get() != null && deadline().isBefore(LocalTime.now())) {
						me.cancel();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

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

		if (cip_uri.length() > 0) {
			byte[] encoded = Files.readAllBytes(Paths.get("src/main/java/adapter/battery-control.xml"));
			String xmlFile = new String(encoded, "UTF-8");
			boolean isRegister = this.cop.register(this.serialNumber, bip.getPortURI(), xmlFile);
			if (!isRegister)
				throw new Exception("Battery can't register to controller");
		}

		if (this.isSILSimulated) {
			// wait the start of simulation and run Decrease petrol each simulated second
			Thread.sleep(RunSILSimulation.DELAY_TO_START_SIMULATION);
			Timer t = new Timer();
			t.schedule(new DecreaseEnergy(), 0, (long) (1000 / RunSILSimulation.ACC_FACTOR));
		}

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
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		float result = this.batteryCharge;
		Log.printAndLog(this, "getBatteryCharge() service result : " + result);
		return result;
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		boolean succeed = setMode(operatingMode.ordinal() + 1);
		Log.printAndLog(this, "upMode() service result : " + succeed + ", current mode : " + operatingMode);
		return succeed;
	}

	/**
	 * @see interfaces.BatteryImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		boolean succeed = setMode(operatingMode.ordinal() - 1);
		Log.printAndLog(this, "downMode() service result : " + succeed + ", current mode : " + operatingMode);
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
			if (operatingMode != BatteryState.DRAINING && batteryCharge > 0) {
				succeed = true;
				operatingMode = BatteryState.DRAINING;
				if (isSILSimulated)
					simulateOperation(Operations.SetDraining);
			}
			break;
		case 1:
			if (operatingMode != BatteryState.SLEEPING) {
				succeed = true;
				operatingMode = BatteryState.SLEEPING;
				if (isSILSimulated)
					simulateOperation(Operations.SetSleeping);
			}
			break;
		case 2:
			if (operatingMode != BatteryState.RECHARGING && batteryCharge < maximumPowerLevel) {
				succeed = true;
				operatingMode = BatteryState.RECHARGING;
				if (isSILSimulated)
					simulateOperation(Operations.SetRecharching);
			}
			break;
		default:
			break;
		}
		Log.printAndLog(this,
				"setMode(" + modeIndex + ") service result : " + succeed + ", current mode : " + operatingMode);
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
		boolean result = this.startTime.get() != null;
		Log.printAndLog(this, "hasPlan() service result : " + result);
		return result;
	}

	/**
	 * @see interfaces.BatteryImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		LocalTime result = this.startTime.get();
		Log.printAndLog(this, "startTime() service result : " + result);
		return result;
	}

	/**
	 * @see interfaces.BatteryImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		Duration result = null;
		if (this.startTime.get() != null && this.endTime.get() != null) {
			result = Duration.between(this.startTime.get(), this.endTime.get());
		}
		Log.printAndLog(this, "Duration() service result : " + result);
		return result;
	}

	/**
	 * @see interfaces.BatteryImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		LocalTime result = this.endTime.get();
		Log.printAndLog(this, "deadLine() service result : " + result);
		return result;
	}

	/**
	 * @see interfaces.BatteryImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		boolean succeed = false;
		if (this.startTime.get() != null && this.endTime.get() != null) {
			this.startTime.set((LocalTime) d.addTo(this.startTime.get()));
			this.endTime.set((LocalTime) d.addTo(this.endTime.get()));
			succeed = true;
		}
		Log.printAndLog(this, "postpone(" + d + ") service result : " + succeed);
		return succeed;
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		boolean succeed = false;
		// if the program isn't running, we can cancel it
		if (this.startTime.get() != null && this.startTime.get().isAfter(LocalTime.now())) {
			synchronized (this.startTime) {
				this.startTime.set(null);
				this.endTime.set(null);
				succeed = true;
			}
		}
		Log.printAndLog(this, "cancel() service result : " + succeed);
		return succeed;
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#planifyEvent(Duration,
	 *      LocalTime)
	 */
	@Override
	public boolean planifyEvent(LocalTime startTime, LocalTime endTime) {
		boolean succeed = false;
		if (startTime.isAfter(LocalTime.now()) && endTime.isAfter(startTime)) {
			synchronized (this.startTime) {
				this.startTime.set(startTime);
				this.endTime.set(endTime);
				succeed = true;
			}
		}
		Log.printAndLog(this, "planifyEvent(" + startTime + ", " + endTime + ") service result : " + succeed
				+ ". startTime : " + this.startTime.get() + ", endTime : " + this.endTime.get());
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
