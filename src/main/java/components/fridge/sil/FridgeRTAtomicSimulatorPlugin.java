package main.java.components.fridge.sil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import main.java.deployment.RunSILSimulation;

/**
 * The class <code>FridgeRTAtomicSimulatorPlugin</code> implements the atomic
 * simulator plug-in for the fridge component.
 * 
 * @author Bello Memmi
 */
public class FridgeRTAtomicSimulatorPlugin extends RTAtomicSimulatorPlugin {

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	/**
	 * name of the variable to be used in the protocol to access data in a
	 * simulation model from its owner component.
	 */
	public static final String FRIDGE_TEMPERATURE_VARIABLE_NAME = "contentTemperature";

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	public FridgeRTAtomicSimulatorPlugin() {
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * initialise the local simulation architecture for the hair dryer component.
	 *
	 * @throws Exception <i>to do</i>.
	 */
	public void initialiseSimulationArchitecture() throws Exception {

		Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

		atomicModelDescriptors.put(FridgeTemperatureSILModel.URI,
				RTAtomicHIOA_Descriptor.create(FridgeTemperatureSILModel.class, FridgeTemperatureSILModel.URI,
						TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));

		this.setSimulationArchitecture(
				new RTArchitecture(RunSILSimulation.SIM_ARCHITECTURE_URI, FridgeTemperatureSILModel.URI,
						atomicModelDescriptors, new HashMap<>(), TimeUnit.SECONDS, RunSILSimulation.ACC_FACTOR));
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		simParams.put(FridgeTemperatureSILModel.FRIDGE_REFERENCE_NAME, this.getOwner());

		super.setSimulationRunParameters(simParams);
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin#getModelStateValue(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Object getModelStateValue(String modelURI, String name) throws Exception {
		assert modelURI != null && name != null;
		assert modelURI.equals(FridgeTemperatureSILModel.URI);
		assert name.equals(FRIDGE_TEMPERATURE_VARIABLE_NAME);

		// Get a Java reference on the object representing the corresponding
		// simulation model.
		ModelDescriptionI m = this.simulator.getDescendentModel(modelURI);
		assert m instanceof FridgeTemperatureSILModel;
		return ((FridgeTemperatureSILModel) m).getContentTemperature();
	}
}
