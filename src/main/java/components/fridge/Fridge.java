package main.java.components.fridge;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.components.controller.connectors.ControllerConnector;
import main.java.components.controller.interfaces.ControllerCI;
import main.java.components.controller.ports.ControllerOutboundPort;
import main.java.components.fridge.interfaces.FridgeActuatorCI;
import main.java.components.fridge.interfaces.FridgeCI;
import main.java.components.fridge.interfaces.FridgeImplementationI;
import main.java.components.fridge.interfaces.FridgeReactiveControlImplementationI;
import main.java.components.fridge.interfaces.FridgeSensorCI;
import main.java.components.fridge.ports.FridgeActuatorInboundPort;
import main.java.components.fridge.ports.FridgeInboundPort;
import main.java.components.fridge.ports.FridgeSensorInboundPort;
import main.java.components.fridge.sil.FridgeRTAtomicSimulatorPlugin;
import main.java.components.fridge.sil.FridgeTemperatureSILModel;
import main.java.components.fridge.sil.events.Activate;
import main.java.components.fridge.sil.events.Passivate;
import main.java.components.fridge.sil.events.SetEco;
import main.java.components.fridge.sil.events.SetNormal;
import main.java.components.fridge.utils.FridgeMode;
import main.java.utils.Log;

/**
 * The class <code>Fridge</code> implements a fridge component A fridge can be
 * on two mode : eco or normal A fridge can be suspended, but will restart when
 * the critical temperature is reach
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { FridgeCI.class, FridgeSensorCI.class, FridgeActuatorCI.class })
@RequiredInterfaces(required = { ControllerCI.class })
public class Fridge extends AbstractCyPhyComponent
		implements FridgeImplementationI, FridgeReactiveControlImplementationI {

	/**
	 * inbound port of controller to connect fridge with
	 */
	public static final String REFLECTION_INBOUND_PORT_URI = Fridge.class.getSimpleName();

	/**
	 * true if the component is executed in a SIL simulation mode.
	 */
	protected boolean isSILSimulated;
	protected boolean isUnitTest;
	/**
	 * URI of the executor service used for real time simulation.
	 */
	protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

	/**
	 * plug-in for the real atomic simulator of the fridge
	 */
	protected FridgeRTAtomicSimulatorPlugin simulatorPlugin;

	public static final String SENSOR_INBOUND_PORT_URI = "FRIDGE-SENSOR-IBP-URI";
	protected FridgeSensorInboundPort sensorIBP;

	public static final String ACTUATOR_INBOUND_PORT_URI = "FRIDGE-ACTUATOR-IBP-URI";
	protected FridgeActuatorInboundPort actuatorIBP;

	public static final String FIP_URI = "fridgeip-URI";
	protected FridgeInboundPort fip;

	/**
	 * Serial number for registering on controller
	 */
	protected String serialNumber;

	/**
	 * Outbound port of the controller from which the fridge will register
	 */
	protected ControllerOutboundPort cop;

	/**
	 * Uri of the controller to connect to
	 */
	protected String cip_URI;

	/** maximum time during which the fridge can be suspended **/
	protected static long MAX_SUSPENSION = Duration.ofHours(12).toMillis();

	/** last time the fridge has been suspended while it is still suspended. **/
	protected final AtomicReference<LocalTime> lastSuspensionTime;

	/** true if the fridge is passive **/
	protected final AtomicBoolean passive;

	/**
	 * Current mode of the Fridge
	 */
	protected AtomicReference<FridgeMode> mode;

	/**
	 * Freeze temp in standard mode
	 */
	public final static double STANDARD_FREEZE_TEMP = -20;
	/**
	 * Freeze temp in eco mode
	 */
	public final static double ECO_FREEZE_TEMP = -10;

	/**
	 * temperature in the room
	 */
	public final static double EXTERNAL_TEMPERATURE = 20;

	/**
	 * fridge transfer constant in the differential equation.
	 */
	public final static double FREEZE_TRANSFER_CONSTANT = 1000;

	/**
	 * insulation fridge transfer constant in the differential equation.
	 */
	public final static double TRANSFER_OUTSIDE_CONSTANT = 10000;

	/**
	 * create a fridge component
	 * 
	 * @param serialNumber   serial number for register to the controller
	 * @param cip_URI        uri of the controller inbound port
	 * @param isSILSimulated true if the component is simulated
	 * @param isUnitTest     true if the component is under unit tests
	 * @throws Exception
	 */
	protected Fridge(String serialNumber, String cip_URI, boolean isSILSimulated, boolean isUnitTest) throws Exception {
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);
		// attributs
		this.serialNumber = serialNumber;
		this.isSILSimulated = isSILSimulated;
		this.isUnitTest = isUnitTest;
		this.passive = new AtomicBoolean(false);
		this.lastSuspensionTime = new AtomicReference<>();
		this.mode = new AtomicReference<>(FridgeMode.NORMAL);
		// ports
		this.fip = new FridgeInboundPort(FIP_URI, this);
		this.fip.publishPort();
		this.sensorIBP = new FridgeSensorInboundPort(this);
		this.sensorIBP.publishPort();
		this.actuatorIBP = new FridgeActuatorInboundPort(this);
		this.actuatorIBP.publishPort();
		this.cip_URI = cip_URI;
		this.cop = new ControllerOutboundPort(this);
		this.cop.localPublishPort();
		// tracer
		this.tracer.get().setTitle("Fridge component");
		this.tracer.get().setRelativePosition(1, 2);
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
				this.simulatorPlugin = new FridgeRTAtomicSimulatorPlugin();
				this.simulatorPlugin.setPluginURI(FridgeTemperatureSILModel.URI);
				this.simulatorPlugin.setSimulationExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI);
				this.simulatorPlugin.initialiseSimulationArchitecture(isUnitTest);
				this.installPlugin(this.simulatorPlugin);
			} catch (Exception e) {
				throw new ComponentStartException(e);
			}
		}
		try {
			if (cip_URI.length() > 0)
				this.doPortConnection(this.cop.getPortURI(), this.cip_URI,
						ControllerConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
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
			this.fip.unpublishPort();
			sensorIBP.unpublishPort();
			actuatorIBP.unpublishPort();
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
		if (cip_URI.length() > 0) {
			byte[] encoded = Files.readAllBytes(Paths.get("src/main/java/adapter/fridge-control.xml"));
			String xmlFile = new String(encoded, "UTF-8");
			boolean isRegister = this.cop.register(this.serialNumber, this.fip.getPortURI(), xmlFile);
			if (!isRegister)
				throw new Exception("Fridge can't register to controller");
		}
	}
	// ----------------------------------------------------------------------------
	// Component services implementation
	// ----------------------------------------------------------------------------

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		boolean succeed = setMode(mode.get().ordinal() + 1);
		Log.printAndLog(this, "upMode() service result : " + succeed);
		return succeed;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		boolean succeed = setMode(mode.get().ordinal() - 1);
		Log.printAndLog(this, "downmode() service result : " + succeed);
		return succeed;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		boolean succeed = false;
		if (modeIndex >= 0 && modeIndex < FridgeMode.values().length && modeIndex != this.mode.get().ordinal()) {
			succeed = true;
			this.mode.set(FridgeMode.values()[modeIndex]);
		}
		if (isSILSimulated && succeed) {
			if (this.mode.get() == FridgeMode.NORMAL) {
				this.simulatorPlugin.triggerExternalEvent(FridgeTemperatureSILModel.URI, t -> new SetNormal(t));
			} else {
				this.simulatorPlugin.triggerExternalEvent(FridgeTemperatureSILModel.URI, t -> new SetEco(t));
			}
		}
		Log.printAndLog(this, "setMode(" + modeIndex + ") service result : " + succeed);
		return succeed;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		int res = this.mode.get().ordinal();
		// Log.printAndLog(this, "currentMode() service result : " + res);
		return res;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		boolean res = this.passive.get();
		// Log.printAndLog(this, "suspended() service result : " + res);
		return res;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception {
		boolean succeed = false;
		synchronized (this.passive) {
			succeed = this.passive.compareAndSet(false, true);
			if (succeed) {
				this.lastSuspensionTime.set(LocalTime.now());
				if (isSILSimulated)
					this.simulatorPlugin.triggerExternalEvent(FridgeTemperatureSILModel.URI, t -> new Passivate(t));
			}
		}
		Log.printAndLog(this, "suspend() service result : " + succeed);
		return succeed;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#resume()
	 */
	@Override
	public boolean resume() throws Exception {
		boolean succeed;
		synchronized (this.passive) {
			succeed = this.passive.compareAndSet(true, false);
			if (succeed) {
				this.lastSuspensionTime.set(null);
				if (isSILSimulated)
					this.simulatorPlugin.triggerExternalEvent(FridgeTemperatureSILModel.URI, t -> new Activate(t));
			}
		}
		Log.printAndLog(this, "resume() service result : " + succeed);
		return succeed;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#emergency()
	 */
	@Override
	public double emergency() throws Exception {
		synchronized (this.passive) {
			double res = 0d;
			if (!this.passive.get()) {
				res = 0.0;
			} else {
				Duration d = Duration.between(this.lastSuspensionTime.get(), LocalTime.now());
				long inMillis = d.toMillis();
				if (inMillis > MAX_SUSPENSION) {
					res = 1.0;
				} else {
					res = ((double) inMillis) / ((double) MAX_SUSPENSION);
				}
			}
			Log.printAndLog(this, "emergency() service result : " + res);
			return res;
		}
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------
	/**
	 * @see FridgeReactiveControlImplementationI#isPassive()
	 */
	@Override
	public boolean isPassive() {
		return this.passive.get();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeReactiveControlImplementationI#passiveSwitch(boolean)
	 */
	@Override
	public void passiveSwitch(boolean passive) {
		this.passive.set(passive);
		if (this.isSILSimulated) {
			try {
				if (passive) {
					this.simulatorPlugin.triggerExternalEvent(FridgeTemperatureSILModel.URI, t -> new Passivate(t));
				} else {
					this.simulatorPlugin.triggerExternalEvent(FridgeTemperatureSILModel.URI, t -> new Activate(t));
				}

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * @see FridgeReactiveControlImplementationI#contentTemperatureSensor()
	 */
	@Override
	public double contentTemperatureSensor() {
		if (this.isSILSimulated) {
			try {
				return (double) this.simulatorPlugin.getModelStateValue(FridgeTemperatureSILModel.URI,
						FridgeRTAtomicSimulatorPlugin.FRIDGE_TEMPERATURE_VARIABLE_NAME);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("Current content temperature not implemented yet");
		}
	}
}
