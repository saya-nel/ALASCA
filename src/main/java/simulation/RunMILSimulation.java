package main.java.simulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.architectures.ArchitectureI;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.CoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.simulators.SimulationEngine;
import main.java.simulation.controller.Controller_MILModel;
import main.java.simulation.fan.FanElectricity_MILModel;
import main.java.simulation.fan.FanUser_MILModel;
import main.java.simulation.fan.events.SetHigh;
import main.java.simulation.fan.events.SetLow;
import main.java.simulation.fan.events.SetMid;
import main.java.simulation.fan.events.TurnOff;
import main.java.simulation.fan.events.TurnOn;
import main.java.simulation.panel.PanelElectricity_MILModel;
import main.java.simulation.panel.events.ConsumptionLevel;
import main.java.simulation.panel.events.ConsumptionLevelRequest;

public class RunMILSimulation {

	public static void main(String[] args) {
		try {

			String fanURI = "fanURI";
			String fanUserURI = "fanUserURI";

			String panelURI = "panelURI";

			String controllerURI = "controllerURI";
			String controllerCoupledURI = "controllerCoupledURI";

			// map that will contain the atomic model descriptors to construct
			// the simulation architecture
			Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

			// fan
			atomicModelDescriptors.put(fanURI, AtomicHIOA_Descriptor.create(FanElectricity_MILModel.class, fanURI,
					TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_ENGINE));

			atomicModelDescriptors.put(fanUserURI, AtomicModelDescriptor.create(FanUser_MILModel.class, fanUserURI,
					TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_ENGINE));

			// electric pannel
			atomicModelDescriptors.put(panelURI, AtomicHIOA_Descriptor.create(PanelElectricity_MILModel.class, panelURI,
					TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_ENGINE));
			// controller
			atomicModelDescriptors.put(controllerURI, AtomicModelDescriptor.create(Controller_MILModel.class,
					controllerURI, TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_ENGINE));

			// map that will contain the coupled model descriptors to construct
			// the simulation architecture
			Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

			// the set of submodels of the coupled model, given by their URIs
			Set<String> submodels = new HashSet<String>();
			submodels.add(fanURI);
			submodels.add(fanUserURI);
			submodels.add(panelURI);
			submodels.add(controllerURI);

			// event exchanging connections between exporting and importing
			// models
			Map<EventSource, EventSink[]> connections = new HashMap<EventSource, EventSink[]>();
			// sending by fanUser
			connections.put(new EventSource(fanUserURI, TurnOn.class),
					new EventSink[] { new EventSink(fanURI, TurnOn.class) });
			connections.put(new EventSource(fanUserURI, TurnOff.class),
					new EventSink[] { new EventSink(fanURI, TurnOff.class) });
			connections.put(new EventSource(fanUserURI, SetLow.class),
					new EventSink[] { new EventSink(fanURI, SetLow.class) });
			connections.put(new EventSource(fanUserURI, SetMid.class),
					new EventSink[] { new EventSink(fanURI, SetMid.class) });
			connections.put(new EventSource(fanUserURI, SetHigh.class),
					new EventSink[] { new EventSink(fanURI, SetHigh.class) });
			// sending by electric panel
			connections.put(new EventSource(panelURI, ConsumptionLevel.class),
					new EventSink[] { new EventSink(controllerURI, ConsumptionLevel.class) });
			// sending by controller
			connections.put(new EventSource(controllerURI, ConsumptionLevelRequest.class),
					new EventSink[] { new EventSink(panelURI, ConsumptionLevelRequest.class) });

			// variable sharing bindings between exporting and importing
			// HIOA models
			Map<VariableSource, VariableSink[]> bindings = new HashMap<VariableSource, VariableSink[]>();
			// fan
			VariableSource source = new VariableSource("currentIntensity", Double.class, fanURI);
			VariableSink[] sinks = new VariableSink[] { new VariableSink("fanIntensity", Double.class, panelURI) };
			bindings.put(source, sinks);
			// autres simus ici
//			source = new VariableSource("currentIntensity", Double.class, BoilerMILModel.URI);
//			sinks = new VariableSink[] {
//					new VariableSink("boilerIntensity", Double.class, PanelElectricity_MILModel.URI) };
//			bindings.put(source, sinks);

			// creation of the coupled model descriptor
			coupledModelDescriptors.put(controllerCoupledURI,
					new CoupledHIOA_Descriptor(Controller_MILCoupledModel.class, controllerCoupledURI, submodels, null,
							null, connections, null, SimulationEngineCreationMode.COORDINATION_ENGINE, null, null,
							bindings));

			// creation of the simulation architecture from the
			// models, with the coupled model as the root model
			ArchitectureI architecture = new Architecture(controllerCoupledURI, atomicModelDescriptors,
					coupledModelDescriptors, TimeUnit.SECONDS);

			// simulation run, after creating the simulation models from the
			// architecture
			SimulationEngine se = architecture.constructSimulator();
			SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;
			se.doStandAloneSimulation(0.0, 5000.0);
			// Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}