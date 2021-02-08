package main.java.components.electricMeter;

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.components.electricMeter.interfaces.ElectricMeterCI;
import main.java.components.electricMeter.interfaces.ElectricMeterImplementationI;
import main.java.components.electricMeter.ports.ElectricMeterInboundPort;
import main.java.components.electricMeter.sil.ElectricMeterSILCoupledModel;
import main.java.components.electricMeter.sil.ElectricMeterSILModel;

/**
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { ElectricMeterCI.class })
public class ElectricMeter extends AbstractCyPhyComponent implements ElectricMeterImplementationI {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	public static final String REFLECTION_INBOUND_PORT_URI = "EMRIB-URI";
	/** true if the component is executed for unit testing. */
	protected boolean isUnitTesting;
	/** the atomic simulator plug-in of the component. */
	protected ElectricMeterRTAtomicSimulatorPlugin simulatorPlugin;
	/** URI of the executor service used to perform the simulation. */
	protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

	protected ElectricMeterInboundPort eip;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create the electric meter component.
	 *
	 * @param isUnitTesting true if the component is executed in unit testing.
	 */
	protected ElectricMeter(String eipURI, boolean isUnitTesting) throws Exception {
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);

		this.isUnitTesting = isUnitTesting;

		this.eip = new ElectricMeterInboundPort(eipURI, this);
		this.eip.publishPort();

		this.tracer.get().setTitle("Electric meter component");
		this.tracer.get().setRelativePosition(2, 0);
		this.toggleTracing();
	}

	// -------------------------------------------------------------------------
	// Component life-cycle.
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();

		try {
			// create the scheduled executor service that will run the
			// simulation tasks
			this.createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
			// create and initialise the atomic simulator plug-in that will
			// hold and execute the SIL simulation models
			this.simulatorPlugin = new ElectricMeterRTAtomicSimulatorPlugin();
			this.simulatorPlugin.setPluginURI(ElectricMeterSILCoupledModel.URI);
			this.simulatorPlugin.setSimulationExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI);
			this.simulatorPlugin.initialiseSimulationArchitecture();
			this.installPlugin(this.simulatorPlugin);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.eip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	@Override
	public double getProduction() throws Exception {
		double currentProduction = (double) this.simulatorPlugin.getModelStateValue(ElectricMeterSILModel.URI,
				ElectricMeterRTAtomicSimulatorPlugin.PRODUCTION_VARIABLE_NAME);
		return currentProduction;
	}

	@Override
	public double getIntensity() throws Exception {
		double currentIntensity = (double) this.simulatorPlugin.getModelStateValue(ElectricMeterSILModel.URI,
				ElectricMeterRTAtomicSimulatorPlugin.INTENSITY_VARIABLE_NAME);
		return currentIntensity;
	}

}
// -----------------------------------------------------------------------------
