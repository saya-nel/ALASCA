package fr.sorbonne_u.components.cyphy.hem2020e3.equipments.meter;

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

import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTCoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.cyphy.hem2020e3.RunSILSimulation;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.Activate;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.BoilerElectricalSILModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.BoilerWaterSILModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.DoNotHeat;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.Heat;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.Passivate;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.HairDryerElectricalSILModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.HairDryerUserSILModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SetHigh;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SetLow;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SwitchOff;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SwitchOn;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.meter.sil.ElectricMeterSILCoupledModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.meter.sil.ElectricMeterSILModel;

// -----------------------------------------------------------------------------
/**
 * The class <code>ElectricMeterRTAtomicSimulatorPlugin</code> defines the
 * atomic simulator plug-in for the electric meter.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2020-12-18</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			ElectricMeterRTAtomicSimulatorPlugin
extends		RTAtomicSimulatorPlugin
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long	serialVersionUID = 1L;
	/** name of the intensity variable used in the protocol allowing
	 *  the owner component to access a value in the simulation models. 	*/
	public static final String	INTENSITY_VARIABLE_NAME = "intensity"; 

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create the plug-in.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
	public				ElectricMeterRTAtomicSimulatorPlugin()
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
	 * pre	true		// no preconditions.
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
		// initialise the simulation parameters giving the references to the
		// owner component before passing the parameters to the simulation
		// models
		simParams.put(ElectricMeterSILModel.ELECTRIC_METER_REFERENCE_NAME,
				  this.getOwner());
		// for the next two, a different name is used to be able to use the
		// models in two contexts: integration tests and unit through SIL
		// simulations 
		simParams.put(HairDryerUserSILModel.HAIR_DRYER_REFERENCE_NAME,
				      this.getOwner());
		simParams.put(BoilerWaterSILModel.BOILER_REFERENCE_NAME,
					  this.getOwner());

		// this will pass the parameters to the simulation models that will then
		// be able to get their own parameters.
		super.setSimulationRunParameters(simParams);
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin#getModelStateValue(java.lang.String, java.lang.String)
	 */
	@Override
	public Object		getModelStateValue(
		String modelURI,
		String name
		) throws Exception
	{
		assert	modelURI != null && name != null;

		// Get a Java reference on the object representing the corresponding
		// simulation model.
		ModelDescriptionI m = this.simulator.getDescendentModel(modelURI);
		// The only model in this example that provides access to some value
		// is the ElectricMeterSILModel.
		assert	m instanceof ElectricMeterSILModel;
		// The only variable that can be accessed is the intensity.
		assert	name.equals(INTENSITY_VARIABLE_NAME);

		return ((ElectricMeterSILModel)m).getIntensity();
	}

	/**
	 * initialise the local simulation architecture for the electric meter
	 * component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @throws Exception	<i>to do</i>.
	 */
	public void			initialiseSimulationArchitecture() throws Exception
	{
		Map<String,AbstractAtomicModelDescriptor>
									atomicModelDescriptors = new HashMap<>();
		Map<String,CoupledModelDescriptor>
									coupledModelDescriptors = new HashMap<>();

		Set<String> submodels = new HashSet<String>();
		submodels.add(ElectricMeterSILModel.URI);
		submodels.add(HairDryerElectricalSILModel.URI);
		submodels.add(BoilerElectricalSILModel.URI);

		atomicModelDescriptors.put(
				ElectricMeterSILModel.URI,
				RTAtomicHIOA_Descriptor.create(
						ElectricMeterSILModel.class,
						ElectricMeterSILModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));
		atomicModelDescriptors.put(
				HairDryerElectricalSILModel.URI,
				RTAtomicHIOA_Descriptor.create(
						HairDryerElectricalSILModel.class,
						HairDryerElectricalSILModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));
		atomicModelDescriptors.put(
				BoilerElectricalSILModel.URI,
				RTAtomicHIOA_Descriptor.create(
						BoilerElectricalSILModel.class,
						BoilerElectricalSILModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));

		Map<Class<? extends EventI>,EventSink[]> imported =
							new HashMap<Class<? extends EventI>,EventSink[]>();
		imported.put(
				SwitchOn.class,
				new EventSink[] {new EventSink(HairDryerElectricalSILModel.URI,
											   SwitchOn.class)});
		imported.put(
				SwitchOff.class,
				new EventSink[] {new EventSink(HairDryerElectricalSILModel.URI,
											   SwitchOff.class)});
		imported.put(
				SetHigh.class,
				new EventSink[] {new EventSink(HairDryerElectricalSILModel.URI,
											   SetHigh.class)});
		imported.put(
				SetLow.class,
				new EventSink[] {new EventSink(HairDryerElectricalSILModel.URI,
											   SetLow.class)});
		imported.put(
				Heat.class,
				new EventSink[] {new EventSink(BoilerElectricalSILModel.URI,
											   Heat.class)});
		imported.put(
				DoNotHeat.class,
				new EventSink[] {new EventSink(BoilerElectricalSILModel.URI,
											   DoNotHeat.class)});
		imported.put(
				Passivate.class,
				new EventSink[] {new EventSink(BoilerElectricalSILModel.URI,
											   Passivate.class)});
		imported.put(
				Activate.class,
				new EventSink[] {new EventSink(BoilerElectricalSILModel.URI,
											   Activate.class)});

		Map<VariableSource,VariableSink[]> bindings =
								new HashMap<VariableSource,VariableSink[]>();
		VariableSource source =
				new VariableSource("currentIntensity", Double.class,
								   HairDryerElectricalSILModel.URI);
		VariableSink[] sinks =
				new VariableSink[] {
						new VariableSink("hairDryerIntensity", Double.class,
										 ElectricMeterSILModel.URI)
				};
		bindings.put(source, sinks);
		source = new VariableSource("currentIntensity", Double.class,
									BoilerElectricalSILModel.URI);
		sinks = new VariableSink[] {
					new VariableSink("boilerIntensity", Double.class,
									 ElectricMeterSILModel.URI)
				};
		bindings.put(source, sinks);

		coupledModelDescriptors.put(
				ElectricMeterSILCoupledModel.URI,
				new RTCoupledHIOA_Descriptor(
						ElectricMeterSILCoupledModel.class,
						ElectricMeterSILCoupledModel.URI,
						submodels,
						imported,
						null, // reeexported
						null, // connections
						null, // factory
						SimulationEngineCreationMode.COORDINATION_RT_ENGINE, // 
						null, // imported vars
						null, // reexported vars
						bindings,
						RunSILSimulation.ACC_FACTOR));

		this.setSimulationArchitecture(
				new RTArchitecture(
						RunSILSimulation.SIM_ARCHITECTURE_URI,
						ElectricMeterSILCoupledModel.URI,
						atomicModelDescriptors,
						coupledModelDescriptors,
						TimeUnit.SECONDS,
						RunSILSimulation.ACC_FACTOR));
	}
}
// -----------------------------------------------------------------------------
