package main.java.components.fan;

import java.util.HashMap;

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.components.fan.interfaces.FanCI;
import main.java.components.fan.interfaces.FanImplementationI;
import main.java.components.fan.ports.FanInboundPort;
import main.java.components.fan.sil.FanRTAtomicSimulatorPlugin;
import main.java.components.fan.sil.FanSILCoupledModel;
import main.java.components.fan.sil.FanStateSILModel;
import main.java.components.fan.sil.events.SetHigh;
import main.java.components.fan.sil.events.SetLow;
import main.java.components.fan.sil.events.SetMid;
import main.java.components.fan.sil.events.TurnOff;
import main.java.components.fan.sil.events.TurnOn;
import main.java.components.fan.utils.FanLevel;

/**
 * The class <code>Fan</code> implements the fan component.
 *
 * The Fan have 3 modes of consumption and can be turned on on all these mods
 * the consumption vary with the mode
 *
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { FanCI.class })
public class Fan extends AbstractCyPhyComponent implements FanImplementationI {

	public enum Operations {
		TURN_ON, TURN_OFF, SET_LOW, SET_MID, SET_HIGH
	}

	/**
	 * URI of the reflection inbound port of this component; works for singleton.
	 */
	public static final String REFLECTION_INBOUND_PORT_URI = "Fan-ibp-uri";

	/** true if the component is executed in a SIL simulation mode. */
	protected boolean isSILSimulated;
	/** true if the component is under unit test. */
	protected boolean isUnitTest;

	protected FanRTAtomicSimulatorPlugin simulatorPlugin;
	protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

	/**
	 * Actual level of the fan
	 */
	protected FanLevel currentLevel;

	/**
	 * Actual power state of the fan
	 */
	protected boolean isOn;

	/**
	 * Inbound port of the fan component
	 */
	protected FanInboundPort fip;

	/**
	 * Constructor of the fan
	 * 
	 * @param fipURI uri of the fan component inbound port
	 */
	protected Fan(String fipURI, boolean isSILSimulated, boolean isUnitTest) throws Exception {
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);
		this.initialise(fipURI, isSILSimulated, isUnitTest);
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * Initialise the fan component
	 *
	 * 
	 * @param fipURI uri of the fan component inbound port
	 * @throws Exception
	 */
	public void initialise(String fipURI, boolean isSILSimulated, boolean isUnitTest) throws Exception {
		this.isSILSimulated = isSILSimulated;
		this.isUnitTest = isUnitTest;

		this.isOn = false;
		this.currentLevel = FanLevel.MID;

		fip = new FanInboundPort(fipURI, this);
		fip.publishPort();

		this.tracer.get().setTitle("Fan component");
		this.tracer.get().setRelativePosition(0, 0);
		this.toggleTracing();
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
				this.simulatorPlugin = new FanRTAtomicSimulatorPlugin();
				this.simulatorPlugin.setPluginURI(FanSILCoupledModel.URI);
				this.simulatorPlugin.setSimulationExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI);
				this.simulatorPlugin.initialiseSimulationArchitecture(this.isUnitTest);
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
		super.execute();

		if (this.isSILSimulated && this.isUnitTest) {
			this.simulatorPlugin.setSimulationRunParameters(new HashMap<String, Object>());
			this.simulatorPlugin.startRTSimulation(System.currentTimeMillis() + 100, 0.0, 10.1);
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.fip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		this.isOn = true;
		this.logMessage("Fan turn on");

		if (this.isSILSimulated) {
			this.simulateOperation(Operations.TURN_ON);
		}
	}

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		this.isOn = false;
		this.logMessage("Fan turn off");

		if (this.isSILSimulated) {
			this.simulateOperation(Operations.TURN_OFF);
		}
	}

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#adjustPower(FanLevel)
	 */
	@Override
	public void adjustPower(FanLevel level) throws Exception {
		this.currentLevel = level;
		this.logMessage("new Fan level : " + level.name());

		if (this.isSILSimulated) {
			switch (level) {
			case LOW:
				this.simulateOperation(Operations.SET_LOW);
				break;
			case MID:
				this.simulateOperation(Operations.SET_MID);
				break;
			case HIGH:
				this.simulateOperation(Operations.SET_HIGH);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return isOn;
	}

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#getFanLevel()
	 */
	@Override
	public FanLevel getFanLevel() throws Exception {
		return currentLevel;
	}

	/**
	 * Send the event associated with the operation to the simulation
	 * 
	 * @param op operation
	 * @throws Exception
	 */
	protected void simulateOperation(Operations op) throws Exception {
		switch (op) {
		case TURN_ON:
			this.simulatorPlugin.triggerExternalEvent(FanStateSILModel.URI, t -> new TurnOn(t));
			break;
		case TURN_OFF:
			this.simulatorPlugin.triggerExternalEvent(FanStateSILModel.URI, t -> new TurnOff(t));
			break;
		case SET_HIGH:
			this.simulatorPlugin.triggerExternalEvent(FanStateSILModel.URI, t -> new SetHigh(t));
			break;
		case SET_MID:
			this.simulatorPlugin.triggerExternalEvent(FanStateSILModel.URI, t -> new SetMid(t));
			break;
		case SET_LOW:
			this.simulatorPlugin.triggerExternalEvent(FanStateSILModel.URI, t -> new SetLow(t));
		}
	}

}
