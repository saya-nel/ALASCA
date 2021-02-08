package main.java.components.washer.sil;

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
import main.java.components.washer.sil.events.SetEco;
import main.java.components.washer.sil.events.SetPerformance;
import main.java.components.washer.sil.events.SetStd;
import main.java.components.washer.sil.events.TurnOff;
import main.java.components.washer.sil.events.TurnOn;
import main.java.deployment.RunSILSimulation;

/**
 * The class <code>WasherRTAtomicSimulatorPlugin</code> extends the real time
 * atomic model plug-in to add the necessary methods for the washer component.
 * 
 * @author Bello Memmi
 *
 */
public class WasherRTAtomicSimulatorPlugin extends RTAtomicSimulatorPlugin {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	protected boolean isUnitTest;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public WasherRTAtomicSimulatorPlugin() {
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
		simParams.put(WasherUserSILModel.WASHER_REFERENCE_NAME, this.getOwner());
		super.setSimulationRunParameters(simParams);
	}

	/**
	 * initialise the local simulation architecture for the washer component.
	 *
	 * @param isUnitTest true if the component is under unit test.
	 * @throws Exception
	 */
	public void initialiseSimulationArchitecture(boolean isUnitTest) throws Exception {
		this.isUnitTest = isUnitTest;

		Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();
		Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

		Set<String> submodels = new HashSet<String>();
		submodels.add(WasherStateSILModel.URI);
		submodels.add(WasherUserSILModel.URI);
		if (isUnitTest) {
			submodels.add(WasherElectricalSILModel.URI);
		}

		atomicModelDescriptors.put(WasherStateSILModel.URI,
				RTAtomicModelDescriptor.create(WasherStateSILModel.class, WasherStateSILModel.URI, TimeUnit.SECONDS,
						null, SimulationEngineCreationMode.ATOMIC_RT_ENGINE, RunSILSimulation.ACC_FACTOR));
		atomicModelDescriptors.put(WasherUserSILModel.URI,
				RTAtomicModelDescriptor.create(WasherUserSILModel.class, WasherUserSILModel.URI, TimeUnit.SECONDS, null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE, RunSILSimulation.ACC_FACTOR));
		if (isUnitTest) {
			atomicModelDescriptors.put(WasherElectricalSILModel.URI,
					RTAtomicHIOA_Descriptor.create(WasherElectricalSILModel.class, WasherElectricalSILModel.URI,
							TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
							RunSILSimulation.ACC_FACTOR));
		}

		Map<Class<? extends EventI>, ReexportedEvent> reexported = null;
		Map<EventSource, EventSink[]> connections = null;
		if (isUnitTest) {
			connections = new HashMap<EventSource, EventSink[]>();
			EventSource source = new EventSource(WasherStateSILModel.URI, TurnOn.class);
			EventSink[] sinks = new EventSink[] { new EventSink(WasherElectricalSILModel.URI, TurnOn.class) };
			connections.put(source, sinks);
			source = new EventSource(WasherStateSILModel.URI, TurnOff.class);
			sinks = new EventSink[] { new EventSink(WasherElectricalSILModel.URI, TurnOff.class) };
			connections.put(source, sinks);
			source = new EventSource(WasherStateSILModel.URI, SetEco.class);
			sinks = new EventSink[] { new EventSink(WasherElectricalSILModel.URI, SetEco.class) };
			connections.put(source, sinks);
			source = new EventSource(WasherStateSILModel.URI, SetStd.class);
			sinks = new EventSink[] { new EventSink(WasherElectricalSILModel.URI, SetStd.class) };
			connections.put(source, sinks);
			source = new EventSource(WasherStateSILModel.URI, SetPerformance.class);
			sinks = new EventSink[] { new EventSink(WasherElectricalSILModel.URI, SetPerformance.class) };
			connections.put(source, sinks);
		} else {
			reexported = new HashMap<Class<? extends EventI>, ReexportedEvent>();
			reexported.put(TurnOn.class, new ReexportedEvent(WasherStateSILModel.URI, TurnOn.class));
			reexported.put(TurnOff.class, new ReexportedEvent(WasherStateSILModel.URI, TurnOff.class));
			reexported.put(SetEco.class, new ReexportedEvent(WasherStateSILModel.URI, SetEco.class));
			reexported.put(SetStd.class, new ReexportedEvent(WasherStateSILModel.URI, SetStd.class));
			reexported.put(SetPerformance.class, new ReexportedEvent(WasherStateSILModel.URI, SetPerformance.class));
		}

		coupledModelDescriptors.put(WasherSILCoupledModel.URI,
				new RTCoupledModelDescriptor(WasherSILCoupledModel.class, WasherSILCoupledModel.URI, submodels, null,
						reexported, connections, null, SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));

		this.setSimulationArchitecture(new RTArchitecture(RunSILSimulation.SIM_ARCHITECTURE_URI,
				WasherSILCoupledModel.URI, atomicModelDescriptors, coupledModelDescriptors, TimeUnit.SECONDS,
				RunSILSimulation.ACC_FACTOR));
	}

}
