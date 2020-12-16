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

import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.StandardRTAtomicModelFactory;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelFactoryI;
import fr.sorbonne_u.devs_simulation.models.interfaces.RTModelFactoryI;
import fr.sorbonne_u.devs_simulation.simulators.AtomicRTEngine;
import fr.sorbonne_u.devs_simulation.simulators.StandardRTScheduler;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSchedulingI;

// -----------------------------------------------------------------------------
/**
 * The class <code>RTAtomicModelDescriptor</code> describes real time atomic
 * models in DEVS simulation architectures.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	{@code AtomicModelDescriptor.checkInvariant(this)}
 * invariant	{@code engineCreationMode.isRealTime()}
 * invariant	{@code schedulerProvider != null}
 * invariant	{@code accelerationFactor > 0.0}
 * </pre>
 * 
 * <p>Created on : 2020-11-23</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			RTAtomicModelDescriptor
extends		AtomicModelDescriptor
{
	// -------------------------------------------------------------------------
	// Inner types, interfaces and classes
	// -------------------------------------------------------------------------

	/**
	 * The functional interface <code>RTSchedulerProviderFI</code> types a
	 * lambda-expression which execution returns a real time scheduler that
	 * is meant to be attached to a real time atomic model.
	 *
	 * <p><strong>Description</strong></p>
	 * 
	 * <p><strong>Invariant</strong></p>
	 * 
	 * <pre>
	 * invariant	true
	 * </pre>
	 * 
	 * <p>Created on : 2020-11-27</p>
	 * 
	 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
	 */
	@FunctionalInterface
	public static interface		RTSchedulerProviderFI
	{
		public RTSchedulingI	provide();
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long		serialVersionUID = 1L;
	/** the standard real time scheduler provider for a real time atomic
	 *  model.																*/
	public static final RTSchedulerProviderFI
									STD_SCHEDULER_PROVIDER =
												() -> new StandardRTScheduler();
	/** the real time scheduler provider attached to this model.			*/
	protected RTSchedulerProviderFI	schedulerProvider;
	/** the standard acceleration factor.									*/
	public static final double		STD_ACCELERATION_FACTOR = 1.0;
	/** the acceleration factor used to control the pace of the simulation
	 *  when converting the simulated time to real time.					*/
	protected double				accelerationFactor;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a new real time atomic model creation descriptor.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || amFactory != null}
	 * pre	{@code modelURI != null}
	 * pre	{@code importedEvents != null}
	 * pre	{@code exportedEvents != null}
	 * pre	{@code simulatedTimeUnit != null}
	 * pre	{@code engineCreationMode.isAtomic()}
	 * pre	{@code engineCreationMode.isRealTime()}
	 * pre	{@code schedulerProvider != null}
	 * pre	{@code accelerationFactor > 0.0}
	 * post	{@code RTAtomicModelDescriptor.checkInvariant(this)}
	 * </pre>
	 *
	 * @param modelClass			Java class defining the atomic model.
	 * @param modelURI				URI of the model to be created.
	 * @param importedEvents		events imported by the model.
	 * @param exportedEvents		events exported by the model.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param amFactory				atomic model factory creating the model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.
	 * @param schedulerProvider		lambda-expression which execution provides the real time scheduler to be set on the simulaiton engine of the model.
	 * @param accelerationFactor	for real time engines, the acceleration factor between the simulcation clock and the real time.
	 * @throws Exception			<i>to do</i>.
	 */
	public				RTAtomicModelDescriptor(
		Class<? extends AtomicModel> modelClass,
		String modelURI,
		Class<? extends EventI>[] importedEvents,
		Class<? extends EventI>[] exportedEvents,
		TimeUnit simulatedTimeUnit,
		AtomicModelFactoryI amFactory,
		SimulationEngineCreationMode engineCreationMode,
		RTSchedulerProviderFI schedulerProvider,
		double accelerationFactor
		) throws Exception
	{
		super(modelClass, modelURI, importedEvents, exportedEvents,
			  simulatedTimeUnit, amFactory, engineCreationMode);

		assert	schedulerProvider != null;
		assert	accelerationFactor > 0.0;

		this.schedulerProvider = schedulerProvider;
		this.accelerationFactor = accelerationFactor;
	}

	/**
	 * return true if the invariant holds on the given model descriptor.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code d != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param d	model descriptor to be checked.
	 * @return	true if the invariant holds on the given model descriptor.
	 */
	public static boolean	checkInvariant(RTAtomicModelDescriptor d)
	{
		assert	d != null;

		boolean invariant = AtomicModelDescriptor.checkInvariant(d);
		invariant &= d.engineCreationMode.isRealTime();
		invariant &= d.schedulerProvider != null;
		invariant &= d.accelerationFactor > 0.0;
		return invariant;
	}

	// -------------------------------------------------------------------------
	// Static methods
	// -------------------------------------------------------------------------

	/**
	 * create a real time atomic model descriptor from a given class, possibly
	 * using the given real time atomic model factory if one is provided.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || amFactory != null}
	 * pre	{@code modelURI != null}
	 * pre	{@code simulatedTimeUnit != null}
	 * pre	{@code engineCreationMode.isAtomic()}
	 * pre	{@code engineCreationMode.isRealTime()}
	 * post	{@code RTAtomicModelDescriptor.checkInvariant(ret)}
	 * </pre>
	 *
	 * @param modelClass			Java class defining the atomic model.
	 * @param modelURI				URI of the model to be created.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param amFactory				atomic model factory creating the model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.	
	 * @return						the new atomic model descriptor.
	 * @throws Exception			<i>to do</i>.
	 */
	public static RTAtomicModelDescriptor	create(
		Class<? extends AtomicModel> modelClass,
		String modelURI,
		TimeUnit simulatedTimeUnit,
		AtomicModelFactoryI amFactory,
		SimulationEngineCreationMode engineCreationMode
		) throws Exception
	{
		return RTAtomicModelDescriptor.create(
							modelClass, modelURI, simulatedTimeUnit,
							amFactory, engineCreationMode,
							STD_SCHEDULER_PROVIDER, STD_ACCELERATION_FACTOR);
	}

	/**
	 * create a real time atomic model descriptor from a given class, possibly
	 * using the given real time atomic model factory if one is provided.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || amFactory != null}
	 * pre	{@code modelURI != null}
	 * pre	{@code simulatedTimeUnit != null}
	 * pre	{@code engineCreationMode.isAtomic()}
	 * pre	{@code engineCreationMode.isRealTime()}
	 * pre	{@code schedulerProvider != null}
	 * post	{@code RTAtomicModelDescriptor.checkInvariant(ret)}
	 * </pre>
	 *
	 * @param modelClass			Java class defining the atomic model.
	 * @param modelURI				URI of the model to be created.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param amFactory				atomic model factory creating the model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.	
	 * @param schedulerProvider		lambda-expression which execution provides the real time scheduler to be set on the simulaiton engine of the model.
	 * @return						the new atomic model descriptor.
	 * @throws Exception			<i>to do</i>.
	 */
	public static RTAtomicModelDescriptor	create(
		Class<? extends AtomicModel> modelClass,
		String modelURI,
		TimeUnit simulatedTimeUnit,
		AtomicModelFactoryI amFactory,
		SimulationEngineCreationMode engineCreationMode,
		RTSchedulerProviderFI schedulerProvider
		) throws Exception
	{
		return RTAtomicModelDescriptor.create(
								modelClass, modelURI, simulatedTimeUnit,
								amFactory, engineCreationMode,
								schedulerProvider, STD_ACCELERATION_FACTOR);
	}

	/**
	 * create a real time atomic model descriptor from a given class, possibly
	 * using the given real time atomic model factory if one is provided.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || amFactory != null}
	 * pre	{@code modelURI != null}
	 * pre	{@code simulatedTimeUnit != null}
	 * pre	{@code engineCreationMode.isAtomic()}
	 * pre	{@code engineCreationMode.isRealTime()}
	 * pre	{@code accelerationFactor > 0.0}
	 * post	{@code RTAtomicModelDescriptor.checkInvariant(ret)}
	 * </pre>
	 *
	 * @param modelClass			Java class defining the atomic model.
	 * @param modelURI				URI of the model to be created.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param amFactory				atomic model factory creating the model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.	
	 * @param accelerationFactor	the acceleration factor between the simulation clock and the real time.
	 * @return						the new atomic model descriptor.
	 * @throws Exception			<i>to do</i>.
	 */
	public static RTAtomicModelDescriptor	create(
		Class<? extends AtomicModel> modelClass,
		String modelURI,
		TimeUnit simulatedTimeUnit,
		AtomicModelFactoryI amFactory,
		SimulationEngineCreationMode engineCreationMode,
		double accelerationFactor
		) throws Exception
	{
		return RTAtomicModelDescriptor.create(
								modelClass, modelURI, simulatedTimeUnit,
								amFactory, engineCreationMode,
								STD_SCHEDULER_PROVIDER, accelerationFactor);
	}

	/**
	 * create a real time atomic model descriptor from a given class, possibly
	 * using the given real time atomic model factory if one is provided.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || amFactory != null}
	 * pre	{@code modelURI != null}
	 * pre	{@code simulatedTimeUnit != null}
	 * pre	{@code engineCreationMode.isAtomic()}
	 * pre	{@code engineCreationMode.isRealTime()}
	 * pre	{@code schedulerProvider != null}
	 * pre	{@code accelerationFactor > 0.0}
	 * post	{@code RTAtomicModelDescriptor.checkInvariant(ret)}
	 * </pre>
	 *
	 * @param modelClass			Java class defining the atomic model.
	 * @param modelURI				URI of the model to be created.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param amFactory				atomic model factory creating the model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.	
	 * @param schedulerProvider		lambda-expression which execution provides the real time scheduler to be set on the simulaiton engine of the model.
	 * @param accelerationFactor	the acceleration factor between the simulation clock and the real time.
	 * @return						the new atomic model descriptor.
	 * @throws Exception			<i>to do</i>.
	 */
	public static RTAtomicModelDescriptor	create(
		Class<? extends AtomicModel> modelClass,
		String modelURI,
		TimeUnit simulatedTimeUnit,
		AtomicModelFactoryI amFactory,
		SimulationEngineCreationMode engineCreationMode,
		RTSchedulerProviderFI schedulerProvider,
		double accelerationFactor
		) throws Exception
	{
		@SuppressWarnings("unchecked")
		Class<? extends EventI>[] importedEvents =
				(Class<? extends EventI>[])
					modelClass.getMethod(
								"getExportedEventTypes",
								new Class<?>[] {Class.class}).
						invoke(null, new Object[] {modelClass});
		@SuppressWarnings("unchecked")
		Class<? extends EventI>[] exportedEvents =
				(Class<? extends EventI>[])
					modelClass.getMethod(
								"getExportedEventTypes",
								new Class<?>[] {Class.class}).
						invoke(null, new Object[] {modelClass});

		return new RTAtomicModelDescriptor(
					modelClass, modelURI, importedEvents,
					exportedEvents, simulatedTimeUnit, amFactory,
					engineCreationMode, schedulerProvider, accelerationFactor);
	}

	// -------------------------------------------------------------------------
	// Instance methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.architectures.AtomicModelDescriptor#createAtomicModel()
	 */
	@Override
	public ModelDescriptionI	createAtomicModel() throws Exception
	{
		if (this.engineCreationMode ==
								SimulationEngineCreationMode.ATOMIC_RT_ENGINE) {
			AtomicModelFactoryI amFactory = null;
			if (this.amFactory == null) {
				amFactory = new StandardRTAtomicModelFactory(this.modelClass);
				((RTModelFactoryI)amFactory).
							setAccelerationFactor(this.accelerationFactor);
			} else {
				amFactory = this.amFactory;
			}
			amFactory.setAtomicModelCreationParameters(
											this.modelURI,
											this.simulatedTimeUnit,
											amFactory.createAtomicEngine());
			AtomicRTEngine rte =
				(AtomicRTEngine) amFactory.createAtomicModel().
														getSimulationEngine();
			rte.setRTScheduler(this.schedulerProvider.provide());
			return rte;
		} else {
			return super.createAtomicModel();
		}
	}
}
// -----------------------------------------------------------------------------
