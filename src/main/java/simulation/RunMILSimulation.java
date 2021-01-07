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
import main.java.simulation.battery.BatteryElectricity_MILModel;
import main.java.simulation.controller.Controller_MILModel;
import main.java.simulation.fan.FanElectricity_MILModel;
import main.java.simulation.fan.FanUser_MILModel;
import main.java.simulation.fan.events.SetHigh;
import main.java.simulation.fan.events.SetLow;
import main.java.simulation.fan.events.SetMid;
import main.java.simulation.fan.events.TurnOff;
import main.java.simulation.fan.events.TurnOn;
import main.java.simulation.fridge.FridgeElectricity_MILModel;
import main.java.simulation.panel.PanelElectricity_MILModel;
import main.java.simulation.panel.events.ConsumptionLevel;
import main.java.simulation.panel.events.ConsumptionLevelRequest;
import main.java.simulation.panel.events.ProductionLevel;
import main.java.simulation.panel.events.ProductionLevelRequest;
import main.java.simulation.petrolGenerator.PetrolGeneratorElectricity_MILModel;
import main.java.simulation.petrolGenerator.PetrolGeneratorUser_MILModel;
import main.java.simulation.petrolGenerator.events.EmptyGenerator;
import main.java.simulation.petrolGenerator.events.FillAll;
import main.java.simulation.solarPanels.SolarPanelsElectricity_MILModel;
import main.java.simulation.washer.WasherElectricity_MILModel;

public class RunMILSimulation {

	public static void main(String[] args) {
		try {

			String fanURI = "fanURI";
			String fanUserURI = "fanUserURI";

			String batteryURI = "batteryURI";

			String fridgeURI = "fridgeURI";

			String petrolGeneratorURI = "petrolGeneratorURI";
			String petrolGeneratorUserURI = "petrolGeneratorUserURI";

			String solarPanelsURI = "solarPanelsURI";

			String washerURI = "washerURI";

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

			// battery
			atomicModelDescriptors.put(batteryURI, AtomicHIOA_Descriptor.create(BatteryElectricity_MILModel.class,
					batteryURI, TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_ENGINE));

			// fridge
			atomicModelDescriptors.put(fridgeURI, AtomicHIOA_Descriptor.create(FridgeElectricity_MILModel.class,
					fridgeURI, TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_ENGINE));

			// petrolGenerator
			atomicModelDescriptors.put(petrolGeneratorURI,
					AtomicHIOA_Descriptor.create(PetrolGeneratorElectricity_MILModel.class, petrolGeneratorURI,
							TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_ENGINE));
			atomicModelDescriptors.put(petrolGeneratorUserURI,
					AtomicModelDescriptor.create(PetrolGeneratorUser_MILModel.class, petrolGeneratorUserURI,
							TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_ENGINE));

			// solarPanels
			atomicModelDescriptors.put(solarPanelsURI,
					AtomicHIOA_Descriptor.create(SolarPanelsElectricity_MILModel.class, solarPanelsURI,
							TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_ENGINE));

			// washer
			atomicModelDescriptors.put(washerURI, AtomicHIOA_Descriptor.create(WasherElectricity_MILModel.class,
					washerURI, TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_ENGINE));

			// electric panel
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
			// fan
			submodels.add(fanURI);
			submodels.add(fanUserURI);
			// battery
			submodels.add(batteryURI);
			// fridge
			submodels.add(fridgeURI);
			// petrolGenerator
			submodels.add(petrolGeneratorURI);
			submodels.add(petrolGeneratorUserURI);
			// solarPanels
			submodels.add(solarPanelsURI);
			// washer
			submodels.add(washerURI);
			// electric panel
			submodels.add(panelURI);
			// controller
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
			// sending by petrolGenerator and petrolGeneratorUser
			connections.put(new EventSource(petrolGeneratorURI, EmptyGenerator.class),
					new EventSink[] { new EventSink(petrolGeneratorUserURI, EmptyGenerator.class) });
			connections.put(
					new EventSource(petrolGeneratorUserURI, main.java.simulation.petrolGenerator.events.TurnOn.class),
					new EventSink[] { new EventSink(petrolGeneratorURI,
							main.java.simulation.petrolGenerator.events.TurnOn.class) });
			connections.put(new EventSource(petrolGeneratorUserURI, FillAll.class),
					new EventSink[] { new EventSink(petrolGeneratorURI, FillAll.class) });

			// sending by electric panel
			connections.put(new EventSource(panelURI, ConsumptionLevel.class),
					new EventSink[] { new EventSink(controllerURI, ConsumptionLevel.class) });
			connections.put(new EventSource(panelURI, ProductionLevel.class),
					new EventSink[] { new EventSink(controllerURI, ProductionLevel.class) });
			// sending by controller
			connections.put(new EventSource(controllerURI, ConsumptionLevelRequest.class),
					new EventSink[] { new EventSink(panelURI, ConsumptionLevelRequest.class) });
			connections.put(new EventSource(controllerURI, ProductionLevelRequest.class),
					new EventSink[] { new EventSink(panelURI, ProductionLevelRequest.class) });

			// variable sharing bindings between exporting and importing
			// HIOA models
			Map<VariableSource, VariableSink[]> bindings = new HashMap<VariableSource, VariableSink[]>();
			// fan
			VariableSource source = new VariableSource("currentIntensity", Double.class, fanURI);
			VariableSink[] sinks = new VariableSink[] { new VariableSink("fanIntensity", Double.class, panelURI) };
			bindings.put(source, sinks);
			// battery
			source = new VariableSource("currentIntensity", Double.class, batteryURI);
			sinks = new VariableSink[] { new VariableSink("batteryIntensity", Double.class, panelURI) };
			bindings.put(source, sinks);
			source = new VariableSource("currentProduction", Double.class, batteryURI);
			sinks = new VariableSink[] { new VariableSink("batteryProduction", Double.class, panelURI) };
			bindings.put(source, sinks);
			// fridge
			source = new VariableSource("currentIntensity", Double.class, fridgeURI);
			sinks = new VariableSink[] { new VariableSink("fridgeIntensity", Double.class, panelURI) };
			bindings.put(source, sinks);
			// petrolGenerator
			source = new VariableSource("currentProduction", Double.class, petrolGeneratorURI);
			sinks = new VariableSink[] { new VariableSink("petrolGeneratorProduction", Double.class, panelURI) };
			bindings.put(source, sinks);
			// solarPanels
			source = new VariableSource("currentProduction", Double.class, solarPanelsURI);
			sinks = new VariableSink[] { new VariableSink("solarPanelsProduction", Double.class, panelURI) };
			bindings.put(source, sinks);
			// washer
			source = new VariableSource("currentIntensity", Double.class, washerURI);
			sinks = new VariableSink[] { new VariableSink("washerIntensity", Double.class, panelURI) };
			bindings.put(source, sinks);

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
			SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 80L;
			se.doStandAloneSimulation(0.0, 5000.0);
//			Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}