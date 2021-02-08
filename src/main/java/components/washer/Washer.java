package main.java.components.washer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;
import main.java.components.controller.connectors.ControllerConnector;
import main.java.components.controller.interfaces.ControllerCI;
import main.java.components.controller.ports.ControllerOutboundPort;
import main.java.components.washer.interfaces.WasherCI;
import main.java.components.washer.interfaces.WasherImplementationI;
import main.java.components.washer.ports.WasherInboundPort;
import main.java.components.washer.sil.WasherRTAtomicSimulatorPlugin;
import main.java.components.washer.sil.WasherSILCoupledModel;
import main.java.components.washer.sil.WasherStateSILModel;
import main.java.components.washer.sil.events.SetEco;
import main.java.components.washer.sil.events.SetPerformance;
import main.java.components.washer.sil.events.SetStd;
import main.java.components.washer.sil.events.TurnOff;
import main.java.components.washer.sil.events.TurnOn;
import main.java.components.washer.utils.WasherModes;
import main.java.utils.Log;

@OfferedInterfaces(offered = { WasherCI.class })
@RequiredInterfaces(required = { ControllerCI.class })
public class Washer extends AbstractCyPhyComponent implements WasherImplementationI {

	public enum Operations {
		SET_ECO, SET_STD, SET_PERFORMANCE, TURN_ON, TURN_OFF
	}

	/**
	 * URI of the reflection inbound port of this component; works for singleton.
	 */
	public static final String REFLECTION_INBOUND_PORT_URI = "Washer-ibp-uri";

	/** true if the component is executed in a SIL simulation mode. */
	protected boolean isSILSimulated;
	/** true if the component is under unit test. */
	protected boolean isUnitTest;

	protected WasherRTAtomicSimulatorPlugin simulatorPlugin;
	protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

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
	protected WasherModes mode;

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
	 * Start time of the planed program
	 */
	protected AtomicReference<LocalTime> startTime;

	/**
	 * End time of the planed program
	 */
	protected AtomicReference<LocalTime> endTime;

	/**
	 *
	 * @param reflectionPortURI
	 * @param serialNumber      serial number of Washer component
	 * @param wipURI            inbound port's URI of Washer
	 * @param cip_URI           inbound port's URI of controller for registering
	 * @throws Exception
	 */

	protected Washer(String serialNumber, String wipURI, String cip_URI, boolean isSILSimulated, boolean isUnitTest)
			throws Exception {
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);
		this.serialNumber = serialNumber;
		this.cip_uri = cip_URI;
		this.isSILSimulated = isSILSimulated;
		this.isUnitTest = isUnitTest;
		initialise(wipURI);

		this.tracer.get().setTitle("Washer component");
		this.tracer.get().setRelativePosition(1, 0);
		this.toggleTracing();

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
		this.mode = WasherModes.ECO;
		this.isOn = new AtomicBoolean(false);
		this.programTemperature = new AtomicInteger(30);
		this.startTime = new AtomicReference<>(null);
		this.endTime = new AtomicReference<>(null);
		this.wip = new WasherInboundPort(washerInboundPortURI, this);
		this.wip.publishPort();
		this.cop = new ControllerOutboundPort(this);
		this.cop.localPublishPort();
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
				this.simulatorPlugin = new WasherRTAtomicSimulatorPlugin();
				this.simulatorPlugin.setPluginURI(WasherSILCoupledModel.URI);
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

		byte[] encoded = Files.readAllBytes(Paths.get("src/main/java/adapter/washer-control.xml"));
		String xmlFile = new String(encoded, "UTF-8");
		boolean isRegister = this.cop.register(this.serialNumber, wip.getPortURI(), xmlFile);
		if (!isRegister)
			throw new Exception("Washer can't register to controller");
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
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void finalise() throws Exception {
		if (cop.connected())
			this.cop.doDisconnection();
		super.finalise();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		boolean res = this.isOn.get();
		Log.printAndLog(this, "isTurnedOn() service result : " + res);
		return res;
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#setProgramTemperature(int)
	 */
	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		if (temperature > 60)
			temperature = 60;
		else if (temperature < 30)
			temperature = 30;
		this.programTemperature.set(temperature);
		Log.printAndLog(this, "setProgramTemperature(" + temperature + ") service called");
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#getProgramDuration()
	 */
	@Override
	public int getProgramTemperature() throws Exception {
		int res = this.programTemperature.get();
		Log.printAndLog(this, "getProgramTemperature() service result : " + res);
		return res;
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#turnOn()
	 */
	@Override
	public boolean turnOn() throws Exception {
		boolean succeed = false;
		succeed = this.isOn.compareAndSet(false, true);
		Log.printAndLog(this, "turnOn() service result : " + succeed);

		if (this.isSILSimulated) {
			this.simulateOperation(Operations.TURN_ON);
		}

		return succeed;
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#turnOff()
	 */
	@Override
	public boolean turnOff() throws Exception {
		boolean succeed = false;
		succeed = this.isOn.compareAndSet(true, false);
		Log.printAndLog(this, "turnOff() service result : " + succeed);

		if (this.isSILSimulated) {
			this.simulateOperation(Operations.TURN_OFF);
		}

		return succeed;
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		boolean succeed = false;
		switch (mode) {
		case ECO:
			mode = WasherModes.STD;
			if (this.isSILSimulated)
				simulateOperation(Operations.SET_STD);
			succeed = true;
			break;
		case STD:
			mode = WasherModes.PERFORMANCE;
			if (this.isSILSimulated)
				simulateOperation(Operations.SET_PERFORMANCE);
			succeed = true;
			break;
		case PERFORMANCE:
			succeed = false;
			break;
		default:
			break;
		}
		Log.printAndLog(this, "upMode() service result : " + succeed + ", current mode : " + mode);
		return succeed;
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		boolean succeed = false;
		switch (mode) {
		case ECO:
			succeed = false;
			break;
		case STD:
			mode = WasherModes.ECO;
			if (this.isSILSimulated)
				simulateOperation(Operations.SET_ECO);
			succeed = true;
			break;
		case PERFORMANCE:
			mode = WasherModes.STD;
			if (this.isSILSimulated)
				simulateOperation(Operations.SET_STD);
			succeed = true;
			break;
		default:
			break;
		}
		Log.printAndLog(this, "downMode() service result : " + succeed + ", current mode : " + mode);
		return succeed;
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		boolean succeed = false;
		switch (modeIndex) {
		case 0:
			if (mode != WasherModes.ECO) {
				succeed = true;
				mode = WasherModes.ECO;
				if (this.isSILSimulated)
					simulateOperation(Operations.SET_ECO);
			}
			break;
		case 1:
			if (mode != WasherModes.STD) {
				succeed = true;
				mode = WasherModes.STD;
				if (this.isSILSimulated)
					simulateOperation(Operations.SET_STD);
			}
			break;
		case 2:
			if (mode != WasherModes.PERFORMANCE) {
				succeed = true;
				mode = WasherModes.PERFORMANCE;
				if (this.isSILSimulated)
					simulateOperation(Operations.SET_PERFORMANCE);
			}
			break;
		default:
			break;
		}
		Log.printAndLog(this, "setMode(" + modeIndex + ") service result : " + succeed + ", current mode : " + mode);
		return succeed;
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		int result = this.mode.ordinal();
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
		case TURN_ON:
			this.simulatorPlugin.triggerExternalEvent(WasherStateSILModel.URI, t -> new TurnOn(t));
			break;
		case TURN_OFF:
			this.simulatorPlugin.triggerExternalEvent(WasherStateSILModel.URI, t -> new TurnOff(t));
			break;
		case SET_ECO:
			this.simulatorPlugin.triggerExternalEvent(WasherStateSILModel.URI, t -> new SetEco(t));
			break;
		case SET_STD:
			this.simulatorPlugin.triggerExternalEvent(WasherStateSILModel.URI, t -> new SetStd(t));
			break;
		case SET_PERFORMANCE:
			this.simulatorPlugin.triggerExternalEvent(WasherStateSILModel.URI, t -> new SetPerformance(t));
		}
	}

}
