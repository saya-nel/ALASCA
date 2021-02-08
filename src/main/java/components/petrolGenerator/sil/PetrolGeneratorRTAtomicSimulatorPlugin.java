package main.java.components.petrolGenerator.sil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTCoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;
import main.java.components.petrolGenerator.sil.events.TurnOff;
import main.java.components.petrolGenerator.sil.events.TurnOn;
import main.java.deployment.RunSILSimulation;

/**
 * The class <code>PetrolGeneratorRTAtomicSimulatorPlugin</code> extends the
 * real time atomic model plug-in to add the necessary methods for the petrol
 * generator component.
 * 
 * @author Bello Memmi
 *
 */
public class PetrolGeneratorRTAtomicSimulatorPlugin extends RTAtomicSimulatorPlugin {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	protected boolean isUnitTest;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public PetrolGeneratorRTAtomicSimulatorPlugin() {
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 *
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		simParams.put(PetrolGeneratorUserSILModel.PETROL_GENERATOR_REFERENCE_NAME, this.getOwner());
		super.setSimulationRunParameters(simParams);
	}

	/**
	 * initialise the local simulation architecture for the petrol generator
	 * component.
	 *
	 * @param isUnitTest true if the component is under unit test.
	 * @throws Exception
	 */
	public void initialiseSimulationArchitecture(boolean isUnitTest) throws Exception {
		this.isUnitTest = isUnitTest;

		Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();
		Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

		Set<String> submodels = new HashSet<String>();
		submodels.add(PetrolGeneratorStateSILModel.URI);
		submodels.add(PetrolGeneratorUserSILModel.URI);
		if (isUnitTest) {
			submodels.add(PetrolGeneratorElectricalSILModel.URI);
		}

		atomicModelDescriptors.put(PetrolGeneratorStateSILModel.URI,
				RTAtomicModelDescriptor.create(PetrolGeneratorStateSILModel.class, PetrolGeneratorStateSILModel.URI,
						TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));
		atomicModelDescriptors.put(PetrolGeneratorUserSILModel.URI,
				RTAtomicModelDescriptor.create(PetrolGeneratorUserSILModel.class, PetrolGeneratorUserSILModel.URI,
						TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));
		if (isUnitTest) {
			atomicModelDescriptors.put(PetrolGeneratorElectricalSILModel.URI,
					RTAtomicHIOA_Descriptor.create(PetrolGeneratorElectricalSILModel.class,
							PetrolGeneratorElectricalSILModel.URI, TimeUnit.SECONDS, null,
							SimulationEngineCreationMode.ATOMIC_RT_ENGINE, RunSILSimulation.ACC_FACTOR));
		}

		Map<Class<? extends EventI>, ReexportedEvent> reexported = null;
		Map<EventSource, EventSink[]> connections = null;
		if (isUnitTest) {
			connections = new HashMap<EventSource, EventSink[]>();
			EventSource source = new EventSource(PetrolGeneratorStateSILModel.URI, TurnOn.class);
			EventSink[] sinks = new EventSink[] { new EventSink(PetrolGeneratorElectricalSILModel.URI, TurnOn.class) };
			connections.put(source, sinks);
			source = new EventSource(PetrolGeneratorStateSILModel.URI, TurnOff.class);
			sinks = new EventSink[] { new EventSink(PetrolGeneratorElectricalSILModel.URI, TurnOff.class) };
			connections.put(source, sinks);
		} else {
			reexported = new HashMap<Class<? extends EventI>, ReexportedEvent>();
			reexported.put(TurnOn.class, new ReexportedEvent(PetrolGeneratorStateSILModel.URI, TurnOn.class));
			reexported.put(TurnOff.class, new ReexportedEvent(PetrolGeneratorStateSILModel.URI, TurnOff.class));
		}

		coupledModelDescriptors.put(PetrolGeneratorSILCoupledModel.URI,
				new RTCoupledModelDescriptor(PetrolGeneratorSILCoupledModel.class, PetrolGeneratorSILCoupledModel.URI,
						submodels, null, reexported, connections, null,
						SimulationEngineCreationMode.COORDINATION_RT_ENGINE, RunSILSimulation.ACC_FACTOR));

		this.setSimulationArchitecture(new RTArchitecture(RunSILSimulation.SIM_ARCHITECTURE_URI,
				PetrolGeneratorSILCoupledModel.URI, atomicModelDescriptors, coupledModelDescriptors, TimeUnit.SECONDS,
				RunSILSimulation.ACC_FACTOR));

	}
}
