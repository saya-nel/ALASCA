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
import main.java.components.fridge.sil.FridgeSILCoupledModel;
import main.java.components.fridge.sil.FridgeTemperatureSILModel;
import main.java.components.fridge.sil.events.Activate;
import main.java.components.fridge.sil.events.Passivate;
import main.java.components.fridge.utils.FridgeMode;
import main.java.utils.Log;

@OfferedInterfaces(offered = { FridgeCI.class, FridgeSensorCI.class, FridgeActuatorCI.class })
@RequiredInterfaces(required = { ControllerCI.class })
public class Fridge extends AbstractCyPhyComponent
		implements FridgeImplementationI, FridgeReactiveControlImplementationI {

	protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

	/** plug-in for the real atomic simulator of the fridge */
	protected FridgeRTAtomicSimulatorPlugin simulatorPlugin;

	public static final String SENSOR_INBOUND_PORT_URI = "FRIDGE-SENSOR-IBP-URI";
	protected FridgeSensorInboundPort sensorIBP;

	public static final String ACTUATOR_INBOUND_PORT_URI = "FRIDGE-ACTUATOR-IBP-URI";
	protected FridgeActuatorInboundPort actuatorIBP;

	/** true if the component is executed in a SIL simulation mode. */
	protected boolean isSILSimulated;

	public static final String CONTROL_INBOUND_PORT_URI = "FRIDGE_CONTROL_IBP_URI";
	protected FridgeInboundPort controlIBP;

	/**
	 * Component URI
	 */
	protected String myUri;

	/**
	 * Serial number for registering on controller
	 */
	protected String serialNumber;

	/**
	 * Requested Temperature for the fridge
	 */
	protected float requestedTemperature;

	/**
	 * current temperature inside of fridge pas sûr de l'utilité intégré à la
	 * simulation
	 */
	protected int currentTemperature;
	/**
	 * Inbound port of the fridge component
	 */
	protected FridgeInboundPort fip;

	/**
	 * Outbound port of the controller from which the fridge will register
	 */
	protected ControllerOutboundPort cop;

	/**
	 * inbound port of controller to connect fridge with
	 */
	public static final String REFLECTION_INBOUND_PORT_URI = Fridge.class.getSimpleName();

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

	public final static double MINIMAL_FRIDGE_TEMP = -4;

	public final static double STANDARD_FREEZE_TEMP = -20;
	public final static double ECO_FREEZE_TEMP = -10;

	public double currentTempDerivative = 0.0;
	/**
	 * the tolerance on the target refregirant temperature to get a control with
	 * hysteresis
	 */
	public final static double CRITICAL_TEMPERATURE = 9;// this.requestedTemperature + 5;

	public final static double EXTERNAL_TEMPERATURE = 25;

	public final static double FREEZE_TRANSFER_CONSTANT = 1000;

	public final static double TRANSFER_OUTSIDE_CONSTANT = 100;

	protected Fridge(boolean isSILSimulated) throws Exception {
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);
		this.passive = new AtomicBoolean(false);
		this.lastSuspensionTime = new AtomicReference<>();
		this.initialise(isSILSimulated);
		/**
		 * this.serialNumber = serialNumber; this.passive = new AtomicBoolean(false);
		 * this.lastSuspensionTime = new AtomicReference<>(); this.mode = new
		 * AtomicReference<>(FridgeMode.NORMAL); this.cop = new
		 * ControllerOutboundPort(this); this.cop.localPublishPort(); this.cip_URI =
		 * cip_URI; this.initialise(fipURI); if (toogleTracing) {
		 * this.tracer.get().setTitle("Fridge component");
		 * this.tracer.get().setRelativePosition(1, 2); this.toggleTracing(); }
		 */
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	public void initialise(boolean isSILSimulated) throws Exception {
//		assert fridgeInboundPortURI != null : new PreconditionException("fridgeInboundPortURI != null");
//		assert !fridgeInboundPortURI.isEmpty() : new PreconditionException("!fridgeInboundPortURI.isEmpty()");
//		this.currentTemperature = 20;
//		this.requestedTemperature = 10;
//		this.fip = new FridgeInboundPort(fridgeInboundPortURI, this);
//		this.fip.publishPort();
		this.isSILSimulated = isSILSimulated;
		this.controlIBP = new FridgeInboundPort(CONTROL_INBOUND_PORT_URI, this);
		this.controlIBP.publishPort();
		this.sensorIBP = new FridgeSensorInboundPort(this);
		this.sensorIBP.publishPort();
		this.actuatorIBP = new FridgeActuatorInboundPort(this);
		this.actuatorIBP.publishPort();
		this.tracer.get().setTitle("Fridge component");
		this.tracer.get().setRelativePosition(1, 0);
		this.toggleTracing();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void finalise() throws Exception {
		if (cop.connected())
			this.cop.doDisconnection();
		if (controlIBP.connected())
			controlIBP.doDisconnection();
		if (sensorIBP.connected())
			sensorIBP.doDisconnection();
		if (actuatorIBP.connected())
			actuatorIBP.doDisconnection();
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.cop.unpublishPort();
			// this.fip.unpublishPort();
			controlIBP.unpublishPort();
			sensorIBP.unpublishPort();
			actuatorIBP.unpublishPort();
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
		if (this.isSILSimulated) {
			try {
				// create the scheduled executor service that will run the
				// simulation tasks
				this.createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
				// create and initialise the atomic simulator plug-in that will
				// hold and execute the SIL simulation models
				this.simulatorPlugin = new FridgeRTAtomicSimulatorPlugin();
				this.simulatorPlugin.setPluginURI(FridgeSILCoupledModel.URI);
				this.simulatorPlugin.setSimulationExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI);
				this.simulatorPlugin.initialiseSimulationArchitecture();

				this.installPlugin(this.simulatorPlugin);
			} catch (Exception e) {
				throw new ComponentStartException(e);
			}
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void execute() throws Exception {
		byte[] encoded = Files.readAllBytes(Paths.get("src/main/java/adapter/fridge-control.xml"));
		String xmlFile = new String(encoded, "UTF-8");
		boolean isRegister = this.cop.register(this.serialNumber, this.fip.getPortURI(), xmlFile);
		if (!isRegister)
			throw new Exception("Fridge can't register to controller");
	}
	// ----------------------------------------------------------------------------
	// Component services implementation
	// ----------------------------------------------------------------------------

	public FridgeMode getMode() {
		return mode.get();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		Log.printAndLog(this, "getRequestedTemperature() service result : " + requestedTemperature);
		return requestedTemperature;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#setRequestedTemperature(float)
	 */
	@Override
	public void setRequestedTemperature(float temp) throws Exception {
		Log.printAndLog(this, "setRequestedTemperature(" + temp + ") service called, new temp = " + temp);
		this.requestedTemperature = temp;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#getCurrentTemperature()
	 */
	@Override
	public float getCurrentTemperature() throws Exception {
		Log.printAndLog(this, "getCurrentTemperature() service result : " + this.currentTemperature);
		return this.currentTemperature;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		boolean succeed = false;
		if (this.mode.get() == FridgeMode.NORMAL)
			succeed = false;
		else
			succeed = this.mode.compareAndSet(this.mode.get(),
					FridgeMode.values()[(this.mode.get().ordinal() + 1) % 2]);
		Log.printAndLog(this, "upMode() service result : " + true);
		return succeed;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		boolean succeed = false;
		if (this.mode.get() == FridgeMode.ECO)
			succeed = false;
		else
			succeed = this.mode.compareAndSet(this.mode.get(), FridgeMode.values()[this.mode.get().ordinal() - 1]);
		Log.printAndLog(this, "downmode() service result : " + true);
		return succeed;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		boolean succeed = false;
		try {
			succeed = this.mode.compareAndSet(this.mode.get(), FridgeMode.values()[modeIndex]);
		} catch (Exception e) {
			e.printStackTrace();
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
		Log.printAndLog(this, "currentMode() service result : " + res);
		return res;
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		boolean res = this.passive.get();
		Log.printAndLog(this, "suspended() service result : " + res);
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
						FridgeRTAtomicSimulatorPlugin.CURRENT_TEMPERATURE_FRIDGE_NAME);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("Current content temperature not implemented yet");
		}
	}
}
