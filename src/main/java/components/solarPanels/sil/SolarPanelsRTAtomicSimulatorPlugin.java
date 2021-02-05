package main.java.components.solarPanels.sil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor;
import main.java.deployment.RunSILSimulation;

public class SolarPanelsRTAtomicSimulatorPlugin extends RTAtomicSimulatorPlugin {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	protected boolean isUnitTest;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public SolarPanelsRTAtomicSimulatorPlugin() {
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * initialise the local simulation architecture for the fan component.
	 *
	 * @param isUnitTest true if the component is under unit test.
	 * @throws Exception <i>to do</i>.
	 */
	public void initialiseSimulationArchitecture(boolean isUnitTest) throws Exception {
		this.isUnitTest = isUnitTest;

		Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();
		Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

//		Set<String> submodels = new HashSet<String>();
//		submodels.add(SolarPanelsStateSILModel.URI);
//		if (isUnitTest) {
//			submodels.add(SolarPanelsElectricalSILModel.URI);
//		}

		atomicModelDescriptors.put(SolarPanelsStateSILModel.URI,
				RTAtomicModelDescriptor.create(SolarPanelsStateSILModel.class, SolarPanelsStateSILModel.URI,
						TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));

//		if (isUnitTest) {
//			atomicModelDescriptors.put(SolarPanelsElectricalSILModel.URI,
//					RTAtomicHIOA_Descriptor.create(SolarPanelsElectricalSILModel.class,
//							SolarPanelsElectricalSILModel.URI, TimeUnit.SECONDS, null,
//							SimulationEngineCreationMode.ATOMIC_RT_ENGINE, RunSILSimulation.ACC_FACTOR));
//		}

//		Map<Class<? extends EventI>, ReexportedEvent> reexported = null;
//		Map<EventSource, EventSink[]> connections = null;
//		if (isUnitTest) {
//			connections = new HashMap<EventSource, EventSink[]>();
//			EventSource source = new EventSource(SolarPanelsStateSILModel.URI, TurnOn.class);
//			EventSink[] sinks = new EventSink[] { new EventSink(SolarPanelsElectricalSILModel.URI, TurnOn.class) };
//			connections.put(source, sinks);
//			source = new EventSource(SolarPanelsStateSILModel.URI, TurnOff.class);
//			sinks = new EventSink[] { new EventSink(SolarPanelsElectricalSILModel.URI, TurnOff.class) };
//			connections.put(source, sinks);
//		} else {
//			reexported = new HashMap<Class<? extends EventI>, ReexportedEvent>();
//			reexported.put(TurnOn.class, new ReexportedEvent(SolarPanelsStateSILModel.URI, TurnOn.class));
//			reexported.put(TurnOff.class, new ReexportedEvent(SolarPanelsStateSILModel.URI, TurnOff.class));
//		}

//		coupledModelDescriptors.put(SolarPanelsSILCoupledModel.URI,
//				new RTCoupledModelDescriptor(SolarPanelsSILCoupledModel.class, SolarPanelsSILCoupledModel.URI,
//						submodels, null, reexported, connections, null,
//						SimulationEngineCreationMode.COORDINATION_RT_ENGINE, RunSILSimulation.ACC_FACTOR));

		this.setSimulationArchitecture(new RTArchitecture(RunSILSimulation.SIM_ARCHITECTURE_URI,
				SolarPanelsStateSILModel.URI, atomicModelDescriptors, coupledModelDescriptors, TimeUnit.SECONDS,
				RunSILSimulation.ACC_FACTOR));
	}

}
