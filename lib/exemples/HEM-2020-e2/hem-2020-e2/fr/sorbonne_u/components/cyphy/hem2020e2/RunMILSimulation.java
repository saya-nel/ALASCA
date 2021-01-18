package fr.sorbonne_u.components.cyphy.hem2020e2;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide an example
// for the extension of the BCM component model that aims to define a components
// tailored for cyber-physical control systems (CPCS) for Java.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability. 
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or 
// data to be ensured and,  more generally, to use and operate it in the 
// same conditions as regards security. 
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.HEM_MILCoupledModel;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.boiler.BoilerMILModel;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.hairdryer.HairDryerElectricity_MILModel;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.hairdryer.HairDryerUser_MILModel;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.hairdryer.SetLow;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.hairdryer.SetHigh;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.hairdryer.SwitchOn;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.hairdryer.SwitchOff;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.hem.HEM_MILModel;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.panel.ConsumptionLevel;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.panel.ConsumptionLevelRequest;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.panel.PanelElectricity_MILModel;
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

// -----------------------------------------------------------------------------
/**
 * The class <code>RunMILSimulation</code> is the main class used to run
 * simulations on the example models of the household energy management
 * project.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-11-13</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			RunMILSimulation
{
	public static void main(String[] args)
	{
		try {
			// map that will contain the atomic model descriptors to construct
			// the simulation architecture
			Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors =
																new HashMap<>();

			// the hair dyer model simulating its electricity consumption, an
			// atomic HIOA model hence we use an AtomicHIOA_Descriptor
			atomicModelDescriptors.put(
					HairDryerElectricity_MILModel.URI,
					AtomicHIOA_Descriptor.create(
							HairDryerElectricity_MILModel.class,
							HairDryerElectricity_MILModel.URI,
							TimeUnit.SECONDS,
							null,
							SimulationEngineCreationMode.ATOMIC_ENGINE));
			// the hair dryer user model which emits events simulating the
			// actions of a user, a plain atomic model hence we use an
			// AtomicModelDescriptor
			atomicModelDescriptors.put(
					HairDryerUser_MILModel.URI,
					AtomicModelDescriptor.create(
							HairDryerUser_MILModel.class,
							HairDryerUser_MILModel.URI,
							TimeUnit.SECONDS,
							null,
							SimulationEngineCreationMode.ATOMIC_ENGINE));
			// the boiler model simulating its electricity consumption, an
			// atomic HIOA model hence we use an AtomicHIOA_Descriptor
			atomicModelDescriptors.put(
					BoilerMILModel.URI,
					AtomicHIOA_Descriptor.create(
							BoilerMILModel.class,
							BoilerMILModel.URI,
							TimeUnit.SECONDS,
							null,
							SimulationEngineCreationMode.ATOMIC_ENGINE));
			// the electric panel model simulating its electricity consumption,
			// an atomic HIOA model hence we use an AtomicHIOA_Descriptor
			atomicModelDescriptors.put(
					PanelElectricity_MILModel.URI,
					AtomicHIOA_Descriptor.create(
							PanelElectricity_MILModel.class,
							PanelElectricity_MILModel.URI,
							TimeUnit.SECONDS,
							null,
							SimulationEngineCreationMode.ATOMIC_ENGINE));
			// a prototype model for the household energy manager showing a
			// way to connect it to the electric panel model
			atomicModelDescriptors.put(
					HEM_MILModel.URI,
					AtomicModelDescriptor.create(
							HEM_MILModel.class,
							HEM_MILModel.URI,
							TimeUnit.SECONDS,
							null,
							SimulationEngineCreationMode.ATOMIC_ENGINE));

			// map that will contain the coupled model descriptors to construct
			// the simulation architecture
			Map<String,CoupledModelDescriptor> coupledModelDescriptors =
																new HashMap<>();

			// the set of submodels of the coupled model, given by their URIs
			Set<String> submodels = new HashSet<String>();
			submodels.add(HairDryerElectricity_MILModel.URI);
			submodels.add(HairDryerUser_MILModel.URI);
			submodels.add(BoilerMILModel.URI);
			submodels.add(PanelElectricity_MILModel.URI);
			submodels.add(HEM_MILModel.URI);

			// event exchanging connections between exporting and importing
			// models
			Map<EventSource,EventSink[]> connections =
										new HashMap<EventSource,EventSink[]>();
			connections.put(
					new EventSource(HairDryerUser_MILModel.URI, SwitchOn.class),
					new EventSink[] {
							new EventSink(HairDryerElectricity_MILModel.URI,
										  SwitchOn.class)
					});
			connections.put(
					new EventSource(HairDryerUser_MILModel.URI,
									SwitchOff.class),
					new EventSink[] {
							new EventSink(HairDryerElectricity_MILModel.URI,
										  SwitchOff.class)
					});
			connections.put(
					new EventSource(HairDryerUser_MILModel.URI, SetLow.class),
					new EventSink[] {
							new EventSink(HairDryerElectricity_MILModel.URI,
										  SetLow.class)
					});
			connections.put(
					new EventSource(HairDryerUser_MILModel.URI, SetHigh.class),
					new EventSink[] {
							new EventSink(HairDryerElectricity_MILModel.URI,
										  SetHigh.class)
					});
			connections.put(
					new EventSource(PanelElectricity_MILModel.URI,
									ConsumptionLevel.class),
					new EventSink[] {
							new EventSink(HEM_MILModel.URI,
									ConsumptionLevel.class)
					});
			connections.put(
					new EventSource(HEM_MILModel.URI,
									ConsumptionLevelRequest.class),
					new EventSink[] {
							new EventSink(PanelElectricity_MILModel.URI,
										  ConsumptionLevelRequest.class)
					});

			// variable sharing bindings between exporting and importing
			// HIOA models
			Map<VariableSource,VariableSink[]> bindings =
								new HashMap<VariableSource,VariableSink[]>();

			VariableSource source =
					new VariableSource("currentIntensity", Double.class,
									   HairDryerElectricity_MILModel.URI);
			VariableSink[] sinks =
					new VariableSink[] {
							new VariableSink("hairDryerIntensity", Double.class,
											 PanelElectricity_MILModel.URI)
					};
			bindings.put(source, sinks);
			source = new VariableSource("currentIntensity", Double.class,
										BoilerMILModel.URI);
			sinks =
					new VariableSink[] {
							new VariableSink("boilerIntensity", Double.class,
											 PanelElectricity_MILModel.URI)
					};
			bindings.put(source, sinks);

			// creation of the coupled model descriptor
			coupledModelDescriptors.put(
					HEM_MILCoupledModel.URI,
					new CoupledHIOA_Descriptor(
							HEM_MILCoupledModel.class,
							HEM_MILCoupledModel.URI,
							submodels,
							null,
							null,
							connections,
							null,
							SimulationEngineCreationMode.COORDINATION_ENGINE,
							null,
							null,
							bindings));

			// creation of the simulation architecture from the
			// models, with the coupled model as the root model
			ArchitectureI architecture =
					new Architecture(
							HEM_MILCoupledModel.URI,
							atomicModelDescriptors,
							coupledModelDescriptors,
							TimeUnit.SECONDS);

			// simulation run, after creating the simulation models from the
			// architecture
			SimulationEngine se = architecture.constructSimulator();
			SimulationEngine.SIMULATION_STEP_SLEEP_TIME = 0L;
			se.doStandAloneSimulation(0.0, 5000.0);
			// Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
// -----------------------------------------------------------------------------
