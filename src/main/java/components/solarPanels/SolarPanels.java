package main.java.components.solarPanels;

import java.util.HashMap;

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.components.controller.Controller;
import main.java.components.solarPanels.interfaces.SolarPanelsCI;
import main.java.components.solarPanels.interfaces.SolarPanelsImplementationI;
import main.java.components.solarPanels.ports.SolarPanelsInboundPort;
import main.java.components.solarPanels.sil.SolarPanelsRTAtomicSimulatorPlugin;
import main.java.components.solarPanels.sil.SolarPanelsStateSILModel;
import main.java.components.solarPanels.sil.events.TurnOff;
import main.java.components.solarPanels.sil.events.TurnOn;

/**
 * The class <code>SolarPanels</code> implements the solar panels component.
 *
 * When he is turned on, the solar panels produce energy, he is not linked to
 * the {@link Controller} so he never turn off, but the implementation allow it.
 *
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { SolarPanelsCI.class })
public class SolarPanels extends AbstractCyPhyComponent implements SolarPanelsImplementationI {

	public enum Operations {
		TURN_ON, TURN_OFF
	}

	/**
	 * URI of the reflection inbound port of this component; works for singleton.
	 */
	public static final String REFLECTION_INBOUND_PORT_URI = "SolarPanels-ibp-uri";

	/** true if the component is executed in a SIL simulation mode. */
	protected boolean isSILSimulated;
	/** true if the component is under unit test. */
	protected boolean isUnitTest;

	protected SolarPanelsRTAtomicSimulatorPlugin simulatorPlugin;
	protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

	/**
	 * Inboud port of the solar panels
	 */
	protected SolarPanelsInboundPort spip;

	/**
	 * True if the solar panels are turnedOn, false else
	 */
	protected boolean isTurnedOn;

	/**
	 * Constructor of the solar panels
	 * 
	 * @param spipURI uri of the inbound port
	 */
	protected SolarPanels(String spipURI, boolean isSILSimulated, boolean isUnitTest) throws Exception {
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);
		this.isSILSimulated = isSILSimulated;
		this.isUnitTest = isUnitTest;
		isTurnedOn = false;
		this.spip = new SolarPanelsInboundPort(spipURI, this);
		this.spip.publishPort();

		this.tracer.get().setTitle("SolarPanels component");
		this.tracer.get().setRelativePosition(0, 1);
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
				this.simulatorPlugin = new SolarPanelsRTAtomicSimulatorPlugin();
				this.simulatorPlugin.setPluginURI(SolarPanelsStateSILModel.URI);
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
			this.spip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		isTurnedOn = true;
		this.logMessage("SP turn on");

		if (this.isSILSimulated) {
			this.simulateOperation(Operations.TURN_ON);
		}
	}

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		isTurnedOn = false;
		this.logMessage("SP turn off");

		if (this.isSILSimulated) {
			this.simulateOperation(Operations.TURN_OFF);
		}
	}

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return isTurnedOn;
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
			this.simulatorPlugin.triggerExternalEvent(SolarPanelsStateSILModel.URI, t -> new TurnOn(t));
			break;
		case TURN_OFF:
			this.simulatorPlugin.triggerExternalEvent(SolarPanelsStateSILModel.URI, t -> new TurnOff(t));
			break;
		}
	}
}
