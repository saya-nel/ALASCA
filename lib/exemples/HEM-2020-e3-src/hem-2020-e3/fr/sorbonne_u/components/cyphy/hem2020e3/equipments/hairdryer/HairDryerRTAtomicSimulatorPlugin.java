package fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a
// new implementation of the DEVS simulation standard for Java.
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
import fr.sorbonne_u.components.cyphy.hem2020e3.RunSILSimulation;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.HairDryerElectricalSILModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.HairDryerSILCoupledModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.HairDryerStateSILModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.HairDryerUserSILModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SetHigh;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SetLow;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SwitchOff;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SwitchOn;
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

// -----------------------------------------------------------------------------
/**
 * The class <code>HairDryerRTAtomicSimulatorPlugin</code> extends the real
 * time atomic model plug-in to add the necessary methods for the hair dryer
 * component.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2020-12-17</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			HairDryerRTAtomicSimulatorPlugin
extends		RTAtomicSimulatorPlugin
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	protected boolean	isUnitTest;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				HairDryerRTAtomicSimulatorPlugin()
	{
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * .
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code !simParams.containsKey(HairDryerUserSILModel.HAIR_DRYER_REFERENCE_NAME)}
	 * post	true		// no postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void			setSimulationRunParameters(
		Map<String, Object> simParams
		) throws Exception
	{
		// initialise the simulation parameter giving the reference to the
		// owner component before passing the parameters to the simulation
		// models
		simParams.put(HairDryerUserSILModel.HAIR_DRYER_REFERENCE_NAME,
					  this.getOwner());

		// this will pass the parameters to the simulation models that will
		// then be able to get their own parameters.
		super.setSimulationRunParameters(simParams);
	}

	/**
	 * initialise the local simulation architecture for the hair dryer
	 * component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param isUnitTest	true if the component is under unit test.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			initialiseSimulationArchitecture(
		boolean isUnitTest
		) throws Exception
	{
		this.isUnitTest = isUnitTest;

		Map<String,AbstractAtomicModelDescriptor>
									atomicModelDescriptors = new HashMap<>();
		Map<String,CoupledModelDescriptor>
									coupledModelDescriptors = new HashMap<>();

		Set<String> submodels = new HashSet<String>();
		submodels.add(HairDryerStateSILModel.URI);
		submodels.add(HairDryerUserSILModel.URI);
		if (isUnitTest) {
			submodels.add(HairDryerElectricalSILModel.URI);
		}

		atomicModelDescriptors.put(
				HairDryerStateSILModel.URI,
				RTAtomicModelDescriptor.create(
						HairDryerStateSILModel.class,
						HairDryerStateSILModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));
		atomicModelDescriptors.put(
				HairDryerUserSILModel.URI,
				RTAtomicModelDescriptor.create(
						HairDryerUserSILModel.class,
						HairDryerUserSILModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));
		if (isUnitTest) {
			atomicModelDescriptors.put(
					HairDryerElectricalSILModel.URI,
					RTAtomicHIOA_Descriptor.create(
							HairDryerElectricalSILModel.class,
							HairDryerElectricalSILModel.URI,
							TimeUnit.SECONDS,
							null,
							SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
							RunSILSimulation.ACC_FACTOR));
		}

		Map<Class<? extends EventI>,ReexportedEvent> reexported = null;
		Map<EventSource, EventSink[]> connections = null;
		if (isUnitTest) {
			connections = new HashMap<EventSource, EventSink[]>();
			EventSource source =
					new EventSource(HairDryerStateSILModel.URI, SwitchOn.class);
			EventSink[] sinks =
					new EventSink[] {
							new EventSink(HairDryerElectricalSILModel.URI,
										  SwitchOn.class)
					};
			connections.put(source, sinks);
			source =
					new EventSource(HairDryerStateSILModel.URI, SwitchOff.class);
			sinks = new EventSink[] {
							new EventSink(HairDryerElectricalSILModel.URI,
										  SwitchOff.class)
					};
			connections.put(source, sinks);
			source =
					new EventSource(HairDryerStateSILModel.URI, SetLow.class);
			sinks = new EventSink[] {
							new EventSink(HairDryerElectricalSILModel.URI,
										  SetLow.class)
					};
			connections.put(source, sinks);
			source =
					new EventSource(HairDryerStateSILModel.URI, SetHigh.class);
			sinks = new EventSink[] {
							new EventSink(HairDryerElectricalSILModel.URI,
										  SetHigh.class)
					};
			connections.put(source, sinks);
		} else {
			reexported =
					new HashMap<Class<? extends EventI>,ReexportedEvent>();
			reexported.put(SwitchOn.class,
						   new ReexportedEvent(HairDryerStateSILModel.URI,
								   			   SwitchOn.class));
			reexported.put(SwitchOff.class,
						   new ReexportedEvent(HairDryerStateSILModel.URI,
								   			   SwitchOff.class));
			reexported.put(SetLow.class,
						   new ReexportedEvent(HairDryerStateSILModel.URI,
								   			   SetLow.class));
			reexported.put(SetHigh.class,
						   new ReexportedEvent(HairDryerStateSILModel.URI,
								   			   SetHigh.class));
		}

		coupledModelDescriptors.put(
				HairDryerSILCoupledModel.URI,
				new RTCoupledModelDescriptor(
						HairDryerSILCoupledModel.class,
						HairDryerSILCoupledModel.URI,
						submodels,
						null,
						reexported,
						connections,
						null,
						SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));

		this.setSimulationArchitecture(
				new RTArchitecture(
						RunSILSimulation.SIM_ARCHITECTURE_URI,
						HairDryerSILCoupledModel.URI,
						atomicModelDescriptors,
						coupledModelDescriptors,
						TimeUnit.SECONDS,
						RunSILSimulation.ACC_FACTOR));
	}
}
// -----------------------------------------------------------------------------
