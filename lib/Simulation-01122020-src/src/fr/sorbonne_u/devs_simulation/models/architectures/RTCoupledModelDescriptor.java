package fr.sorbonne_u.devs_simulation.models.architectures;

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

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.StandardRTCoupledModelFactory;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;
import fr.sorbonne_u.devs_simulation.models.interfaces.CoupledModelFactoryI;
import fr.sorbonne_u.devs_simulation.models.interfaces.CoupledModelI;
import fr.sorbonne_u.devs_simulation.models.interfaces.RTModelFactoryI;

// -----------------------------------------------------------------------------
/**
 * The class <code>RTCoupledModelDescriptor</code> defines real time coupled
 * models in model architectures.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	{@code CoupledModelDescriptor.checkInternalConsistency(this)}
 * invariant	{@code accelerationFactor > 0.0}
 * </pre>
 * 
 * <p>Created on : 2020-11-24</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			RTCoupledModelDescriptor
extends		CoupledModelDescriptor
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long 	serialVersionUID = 1L;
	/** the standard acceleration factor.									*/
	public static final double	STD_ACCELERATION_FACTOR = 1.0;
	/** the acceleration factor used to control the pace of the simulation
	 *  when converting the simulated time to real time.					*/
	protected double			accelerationFactor;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a new coupled model creation descriptor.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || cmFactory != null}
	 * pre	{@code modelURI != null}
	 * pre	{@code submodelURIs != null && submodelURIs.size() > 1}
	 * post	{@code CoupledModelDescriptor.checkInternalConsistency(this)}
	 * </pre>
	 *
	 * @param modelClass			class defining the coupled model.
	 * @param modelURI				URI of the coupled model to be created.
	 * @param submodelURIs			URIs of the submodels of the coupled model.
	 * @param imported				map from imported event types to the submodels importing them.
	 * @param reexported			map from event types exported by submodels to exported event types exported by the coupled model.
	 * @param connections			connections between event types exported by submodels to event types imported by other submodels.
	 * @param cmFactory				coupled model factory allowing to create the coupled model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.	
	 */
	public				RTCoupledModelDescriptor(
		Class<? extends CoupledModelI> modelClass,
		String modelURI,
		Set<String> submodelURIs,
		Map<Class<? extends EventI>,EventSink[]> imported,
		Map<Class<? extends EventI>,ReexportedEvent> reexported,
		Map<EventSource, EventSink[]> connections,
		CoupledModelFactoryI cmFactory,
		SimulationEngineCreationMode engineCreationMode
		)
	{
		
		this(modelClass, modelURI, submodelURIs,
			 imported, reexported, connections,
			 cmFactory, engineCreationMode,
			 STD_ACCELERATION_FACTOR);
	}

	/**
	 * create a new coupled model creation descriptor.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || cmFactory != null}
	 * pre	{@code modelURI != null}
	 * pre	{@code submodelURIs != null && submodelURIs.size() > 1}
	 * pre	{@code accelerationFactor > 0.0}
	 * post	{@code CoupledModelDescriptor.checkInternalConsistency(this)}
	 * </pre>
	 *
	 * @param modelClass			class defining the coupled model.
	 * @param modelURI				URI of the coupled model to be created.
	 * @param submodelURIs			URIs of the submodels of the coupled model.
	 * @param imported				map from imported event types to the submodels importing them.
	 * @param reexported			map from event types exported by submodels to exported event types exported by the coupled model.
	 * @param connections			connections between event types exported by submodels to event types imported by other submodels.
	 * @param cmFactory				coupled model factory allowing to create the coupled model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.	
	 * @param accelerationFactor	for real time engines, the acceleration factor between the simulcation clock and the real time.
	 */
	public				RTCoupledModelDescriptor(
		Class<? extends CoupledModelI> modelClass,
		String modelURI,
		Set<String> submodelURIs,
		Map<Class<? extends EventI>,EventSink[]> imported,
		Map<Class<? extends EventI>,ReexportedEvent> reexported,
		Map<EventSource, EventSink[]> connections,
		CoupledModelFactoryI cmFactory,
		SimulationEngineCreationMode engineCreationMode,
		double accelerationFactor
		)
	{
		super(modelClass, modelURI, submodelURIs,
			  imported, reexported, connections,
			  cmFactory, engineCreationMode);

		assert	accelerationFactor > 0.0;

		this.accelerationFactor = accelerationFactor;
	}

	/**
	 * create a new coupled model creation descriptor.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || cmFactory != null}
	 * pre	{@code modelURI != null}
	 * pre	{@code submodelURIs != null && submodelURIs.size() > 1}
	 * pre	{@code mc != null}
	 * post	{@code CoupledModelDescriptor.checkInternalConsistency(this)}
	 * </pre>
	 *
	 * @param modelClass			class defining the coupled model.
	 * @param modelURI				URI of the coupled model to be created.
	 * @param submodelURIs			URIs of the submodels of the coupled model.
	 * @param imported				map from imported event types to the submodels importing them.
	 * @param reexported			map from event types exported by submodels to exported event types exported by the coupled model.
	 * @param connections			connections between event types exported by submodels to event types imported by other submodels.
	 * @param cmFactory				coupled model factory allowing to create the coupled model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.
	 * @param mc					model composer to be used.
	 */
	public				RTCoupledModelDescriptor(
		Class<? extends CoupledModelI> modelClass,
		String modelURI,
		Set<String> submodelURIs,
		Map<Class<? extends EventI>,EventSink[]> imported,
		Map<Class<? extends EventI>,ReexportedEvent> reexported,
		Map<EventSource, EventSink[]> connections,
		CoupledModelFactoryI cmFactory,
		SimulationEngineCreationMode engineCreationMode,
		ModelComposer mc
		)
	{
		this(modelClass, modelURI, submodelURIs,
			 imported, reexported, connections,
			 cmFactory, engineCreationMode, mc,
			 STD_ACCELERATION_FACTOR);
	}

	/**
	 * create a new coupled model creation descriptor.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || cmFactory != null}
	 * pre	{@code modelURI != null}
	 * pre	{@code submodelURIs != null && submodelURIs.size() > 1}
	 * pre	{@code mc != null}
	 * pre	{@code accelerationFactor > 0.0}
	 * post	{@code CoupledModelDescriptor.checkInternalConsistency(this)}
	 * </pre>
	 *
	 * @param modelClass			class defining the coupled model.
	 * @param modelURI				URI of the coupled model to be created.
	 * @param submodelURIs			URIs of the submodels of the coupled model.
	 * @param imported				map from imported event types to the submodels importing them.
	 * @param reexported			map from event types exported by submodels to exported event types exported by the coupled model.
	 * @param connections			connections between event types exported by submodels to event types imported by other submodels.
	 * @param cmFactory				coupled model factory allowing to create the coupled model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.
	 * @param mc					model composer to be used.
	 * @param accelerationFactor	for real time engines, the acceleration factor between the simulcation clock and the real time.
	 */
	public				RTCoupledModelDescriptor(
		Class<? extends CoupledModelI> modelClass,
		String modelURI,
		Set<String> submodelURIs,
		Map<Class<? extends EventI>,
		EventSink[]> imported,
		Map<Class<? extends EventI>,
		ReexportedEvent> reexported,
		Map<EventSource, EventSink[]> connections,
		CoupledModelFactoryI cmFactory,
		SimulationEngineCreationMode engineCreationMode,
		ModelComposer mc,
		double accelerationFactor
		)
	{
		super(modelClass, modelURI, submodelURIs,
			  imported, reexported, connections,
			  cmFactory, engineCreationMode, mc);

		assert	accelerationFactor > 0.0;

		this.accelerationFactor = accelerationFactor;
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor#createCoupledModel(fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI[])
	 */
	@Override
	public ModelDescriptionI	createCoupledModel(ModelDescriptionI[] models)
	throws Exception
	{
		if (this.engineCreationMode ==
						SimulationEngineCreationMode.ATOMIC_RT_ENGINE ||
			this.engineCreationMode ==
						SimulationEngineCreationMode.COORDINATION_RT_ENGINE)
		{
			HashSet<String> hs = new HashSet<String>();
			for (int i = 0 ; i < models.length ; i++) {
				hs.add(models[i].getURI());
			}
			assert	this.submodelURIs.containsAll(hs) &&
											hs.containsAll(this.submodelURIs);

			CoupledModelI m = null;
			CoupledModelFactoryI cmFactory = null;
			if (this.cmFactory == null) {
				cmFactory = new StandardRTCoupledModelFactory(this.modelClass);
				((RTModelFactoryI)cmFactory).
								setAccelerationFactor(this.accelerationFactor);
			} else {
				cmFactory = this.cmFactory;
			}
			if (this.engineCreationMode ==
							SimulationEngineCreationMode.ATOMIC_RT_ENGINE) {
				m = this.mc.composeAsAtomicModel(
						models,
						this.modelURI,
						cmFactory.createAtomicSimulationEngine(),
						cmFactory,
						this.imported,
						this.reexported,
						this.connections);
				return m.getSimulationEngine();
			} else {
				assert	this.engineCreationMode ==
							SimulationEngineCreationMode.COORDINATION_RT_ENGINE;
				m = this.mc.compose(models,
						this.modelURI,
						cmFactory.createCoordinationEngine(),
						cmFactory,
						this.imported,
						this.reexported,
						this.connections);
				return m.getSimulationEngine();
			}
		} else {
			return super.createCoupledModel(models);
		}
	}
}
// -----------------------------------------------------------------------------
