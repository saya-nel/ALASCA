package main.java.components.fan.sil;

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
import main.java.components.fan.sil.events.SetHigh;
import main.java.components.fan.sil.events.SetLow;
import main.java.components.fan.sil.events.SetMid;
import main.java.components.fan.sil.events.TurnOff;
import main.java.components.fan.sil.events.TurnOn;
import main.java.deployment.RunSILSimulation;

/**
 * The class <code>FanRTAtomicSimulatorPlugin</code> extends the real time
 * atomic model plug-in to add the necessary methods for the fan component.
 * 
 * @author Bello Memmi
 *
 */
public class FanRTAtomicSimulatorPlugin extends RTAtomicSimulatorPlugin {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	protected boolean isUnitTest;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public FanRTAtomicSimulatorPlugin() {
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		simParams.put(FanUserSILModel.FAN_REFERENCE_NAME, this.getOwner());
		super.setSimulationRunParameters(simParams);
	}

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

		Set<String> submodels = new HashSet<String>();
		submodels.add(FanStateSILModel.URI);
		submodels.add(FanUserSILModel.URI);
		if (isUnitTest) {
			submodels.add(FanElectricalSILModel.URI);
		}

		atomicModelDescriptors.put(FanStateSILModel.URI,
				RTAtomicModelDescriptor.create(FanStateSILModel.class, FanStateSILModel.URI, TimeUnit.SECONDS, null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE, RunSILSimulation.ACC_FACTOR));
		atomicModelDescriptors.put(FanUserSILModel.URI,
				RTAtomicModelDescriptor.create(FanUserSILModel.class, FanUserSILModel.URI, TimeUnit.SECONDS, null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE, RunSILSimulation.ACC_FACTOR));
		if (isUnitTest) {
			atomicModelDescriptors.put(FanElectricalSILModel.URI,
					RTAtomicHIOA_Descriptor.create(FanElectricalSILModel.class, FanElectricalSILModel.URI,
							TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
							RunSILSimulation.ACC_FACTOR));
		}

		Map<Class<? extends EventI>, ReexportedEvent> reexported = null;
		Map<EventSource, EventSink[]> connections = null;
		if (isUnitTest) {
			connections = new HashMap<EventSource, EventSink[]>();
			EventSource source = new EventSource(FanStateSILModel.URI, TurnOn.class);
			EventSink[] sinks = new EventSink[] { new EventSink(FanElectricalSILModel.URI, TurnOn.class) };
			connections.put(source, sinks);
			source = new EventSource(FanStateSILModel.URI, TurnOff.class);
			sinks = new EventSink[] { new EventSink(FanElectricalSILModel.URI, TurnOff.class) };
			connections.put(source, sinks);
			source = new EventSource(FanStateSILModel.URI, SetLow.class);
			sinks = new EventSink[] { new EventSink(FanElectricalSILModel.URI, SetLow.class) };
			connections.put(source, sinks);
			source = new EventSource(FanStateSILModel.URI, SetMid.class);
			sinks = new EventSink[] { new EventSink(FanElectricalSILModel.URI, SetMid.class) };
			connections.put(source, sinks);
			source = new EventSource(FanStateSILModel.URI, SetHigh.class);
			sinks = new EventSink[] { new EventSink(FanElectricalSILModel.URI, SetHigh.class) };
			connections.put(source, sinks);
		} else {
			reexported = new HashMap<Class<? extends EventI>, ReexportedEvent>();
			reexported.put(TurnOn.class, new ReexportedEvent(FanStateSILModel.URI, TurnOn.class));
			reexported.put(TurnOff.class, new ReexportedEvent(FanStateSILModel.URI, TurnOff.class));
			reexported.put(SetLow.class, new ReexportedEvent(FanStateSILModel.URI, SetLow.class));
			reexported.put(SetMid.class, new ReexportedEvent(FanStateSILModel.URI, SetMid.class));
			reexported.put(SetHigh.class, new ReexportedEvent(FanStateSILModel.URI, SetHigh.class));
		}

		coupledModelDescriptors.put(FanSILCoupledModel.URI,
				new RTCoupledModelDescriptor(FanSILCoupledModel.class, FanSILCoupledModel.URI, submodels, null,
						reexported, connections, null, SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));

		this.setSimulationArchitecture(new RTArchitecture(RunSILSimulation.SIM_ARCHITECTURE_URI, FanSILCoupledModel.URI,
				atomicModelDescriptors, coupledModelDescriptors, TimeUnit.SECONDS, RunSILSimulation.ACC_FACTOR));
	}

}
