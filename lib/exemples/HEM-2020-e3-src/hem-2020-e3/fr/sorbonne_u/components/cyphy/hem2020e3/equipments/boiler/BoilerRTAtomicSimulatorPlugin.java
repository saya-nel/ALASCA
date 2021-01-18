package fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler;

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
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.cyphy.hem2020e3.RunSILSimulation;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.BoilerWaterSILModel;

// -----------------------------------------------------------------------------
/**
 * The class <code>BoilerRTAtomicSimulatorPlugin</code> implements the
 * atomic simulator plug-in for the boiler component.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-12-30</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			BoilerRTAtomicSimulatorPlugin
extends		RTAtomicSimulatorPlugin
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	/** name of the variable to be used in the protocol to access data in
	 *  a simulation model from its owner component.						*/
	public static final String	WATER_TEMPERATURE_VARIABLE_NAME = "waterTemp";

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				BoilerRTAtomicSimulatorPlugin()
	{
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * initialise the local simulation architecture for the boiler.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @throws Exception	<i>to do</I>.
	 */
	public void			initialiseSimulationArchitecture() throws Exception
	{
		Map<String,AbstractAtomicModelDescriptor>
									atomicModelDescriptors = new HashMap<>();
		atomicModelDescriptors.put(
				BoilerWaterSILModel.URI,
				RTAtomicHIOA_Descriptor.create(
						BoilerWaterSILModel.class,
						BoilerWaterSILModel.URI,
						TimeUnit.SECONDS,
						null,
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
						RunSILSimulation.ACC_FACTOR));

		this.setSimulationArchitecture(
				new RTArchitecture(
						RunSILSimulation.SIM_ARCHITECTURE_URI,
						BoilerWaterSILModel.URI,
						atomicModelDescriptors,
						new HashMap<>(),
						TimeUnit.SECONDS,
						RunSILSimulation.ACC_FACTOR));
	}

	// -------------------------------------------------------------------------
	// Simulation protocol methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void			setSimulationRunParameters(
		Map<String, Object> simParams
		) throws Exception
	{
		assert	!simParams.containsKey(
								BoilerWaterSILModel.BOILER_REFERENCE_NAME);
		simParams.put(BoilerWaterSILModel.BOILER_REFERENCE_NAME,
					  this.getOwner());
		super.setSimulationRunParameters(simParams);
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin#getModelStateValue(java.lang.String, java.lang.String)
	 */
	@Override
	public Object		getModelStateValue(String modelURI, String name)
	throws Exception
	{
		assert	modelURI != null && name != null;
		assert	modelURI.equals(BoilerWaterSILModel.URI);
		assert	name.equals(WATER_TEMPERATURE_VARIABLE_NAME);

		// Get a Java reference on the object representing the corresponding
		// simulation model.
		ModelDescriptionI m = this.simulator.getDescendentModel(modelURI);
		assert	m instanceof BoilerWaterSILModel;
		return ((BoilerWaterSILModel)m).getWaterTemperature();
	}
}
// -----------------------------------------------------------------------------
