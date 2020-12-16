package fr.sorbonne_u.devs_simulation.hioa.architectures;

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
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.StandardRTAtomicHIOA_Factory;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.StaticVariableDescriptor;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor.RTSchedulerProviderFI;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelFactoryI;
import fr.sorbonne_u.devs_simulation.models.interfaces.RTModelFactoryI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.RTAtomicSimulatorI;

// -----------------------------------------------------------------------------
/**
 * The class <code>RTAtomicHIOA_Descriptor</code> describes real time atomic
 * HIOA in DEVS simulation architectures.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	{@code RTAtomicHIOA_Descriptor.checkInvariant(this)}
 * invariant	{@code engineCreationMode.isRealTime()}
 * invariant	{@code schedulerProvider != null}
 * invariant	{@code accelerationFactor > 0.0}
 * </pre>
 * 
 * <p>Created on : 2020-11-27</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			RTAtomicHIOA_Descriptor
extends		AtomicHIOA_Descriptor
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long		serialVersionUID = 1L;
	/** the real time scheduler provider attached to this model.			*/
	protected RTSchedulerProviderFI	schedulerProvider;
	/** the acceleration factor used to control the pace of the simulation
	 *  when converting the simulated time to real time.					*/
	protected double				accelerationFactor;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a real time atomic HIOA model descriptor.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || amFactory != null}
	 * pre	{@code modelClass == null || AtomicHIOA.class.isAssignableFrom(modelClass)}
	 * pre	{@code modelURI != null}
	 * pre	{@code importedEvents != null}
	 * pre	{@code exportedEvents != null}
	 * pre	{@code simulatedTimeUnit != null}
	 * pre	{@code engineCreationMode.isAtomic()}
	 * pre	{@code engineCreationMode.isRealTime()}
	 * pre	{@code importedVariables != null}
	 * pre	{@code exportedVariables != null}
	 * pre	{@code schedulerProvider != null}
	 * pre	{@code accelerationFactor > 0.0}
	 * post	{@code RTAtomicHIOA_Descriptor.checkInvariant(this)}
	 * </pre>
	 *
	 * @param modelClass			class defining the model.
	 * @param modelURI				URI of the model to be created.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param importedEvents		array of imported event types.
	 * @param exportedEvents		array of exported event types.
	 * @param amFactory				atomic model factory creating the model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.	
	 * @param importedVariables		variables imported by this HIOA.
	 * @param exportedVariables		variables exported by this HIOA.
	 * @param schedulerProvider		lambda-expression which execution provides the real time scheduler to be set on the simulaiton engine of the model.
	 * @param accelerationFactor	for real time engines, the acceleration factor between the simulcation clock and the real time.
	 * @throws Exception			<i>to do</i>.
	 */
	public				RTAtomicHIOA_Descriptor(
		Class<? extends AtomicModel> modelClass,
		String modelURI,
		Class<? extends EventI>[] importedEvents,
		Class<? extends EventI>[] exportedEvents,
		TimeUnit simulatedTimeUnit,
		AtomicModelFactoryI amFactory,
		SimulationEngineCreationMode engineCreationMode,
		StaticVariableDescriptor[] importedVariables,
		StaticVariableDescriptor[] exportedVariables,
		RTSchedulerProviderFI schedulerProvider,
		double accelerationFactor
		) throws Exception
	{
		super(modelClass, modelURI, importedEvents, exportedEvents,
			  simulatedTimeUnit, amFactory, engineCreationMode,
			  importedVariables, exportedVariables);

		assert	engineCreationMode.isRealTime();
		assert	schedulerProvider != null;
		assert	accelerationFactor > 0.0;

		this.schedulerProvider = schedulerProvider;
		this.accelerationFactor = accelerationFactor;

		assert	RTAtomicHIOA_Descriptor.checkInvariant(this);
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
	public static boolean	checkInvariant(RTAtomicHIOA_Descriptor d)
	{
		assert	d != null;

		boolean invariant = AtomicHIOA_Descriptor.checkInvariant(d);
		invariant &= d.engineCreationMode.isRealTime();
		invariant &= d.schedulerProvider != null;
		invariant &= d.accelerationFactor > 0.0;
		return invariant;
	}
	// -------------------------------------------------------------------------
	// Static methods
	// -------------------------------------------------------------------------

	/**
	 * create an atomic HIOA descriptor extracting the sets of imported and
	 * exported variables as well as the imported and exported events from
	 * the model classes.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || amFactory != null}
	 * pre	{@code modelClass == null || AtomicHIOA.class.isAssignableFrom(modelClass)}
	 * pre	{@code modelURI != null}
	 * pre	{@code simulatedTimeUnit != null}
	 * pre	{@code engineCreationMode.isAtomic()}
	 * pre	{@code engineCreationMode.isRealTime()}
	 * post	{@code RTAtomicHIOA_Descriptor.checkInvariant(ret)}
	 * </pre>
	 *
	 * @param modelClass			class defining the model.
	 * @param modelURI				URI of the model to be created.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param amFactory				atomic model factory creating the model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.	
	 * @return						the model descriptor.
	 * @throws Exception			<i>to do</i>.
	 */
	@SuppressWarnings("unchecked")
	public static RTAtomicHIOA_Descriptor	create(
		Class<? extends AtomicModel> modelClass,
		String modelURI,
		TimeUnit simulatedTimeUnit,
		AtomicModelFactoryI amFactory,
		SimulationEngineCreationMode engineCreationMode
		) throws Exception
	{
		assert	modelClass == null ||
								AtomicHIOA.class.isAssignableFrom(modelClass);
		return RTAtomicHIOA_Descriptor.create(
							(Class<? extends AtomicHIOA>) modelClass,
							modelURI, simulatedTimeUnit,
							amFactory, engineCreationMode,
							RTAtomicModelDescriptor.STD_SCHEDULER_PROVIDER,
							RTAtomicModelDescriptor.STD_ACCELERATION_FACTOR);
	}

	/**
	 * create an atomic HIOA descriptor extracting the sets of imported and
	 * exported variables as well as the imported and exported events from
	 * the model classes.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || amFactory != null}
	 * pre	{@code modelClass == null || AtomicHIOA.class.isAssignableFrom(modelClass)}
	 * pre	{@code modelURI != null}
	 * pre	{@code simulatedTimeUnit != null}
	 * pre	{@code engineCreationMode.isAtomic()}
	 * pre	{@code engineCreationMode.isRealTime()}
	 * pre	{@code schedulerProvider != null}
	 * post	{@code RTAtomicHIOA_Descriptor.checkInvariant(ret)}
	 * </pre>
	 *
	 * @param modelClass			class defining the model.
	 * @param modelURI				URI of the model to be created.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param amFactory				atomic model factory creating the model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.	
	 * @param schedulerProvider		lambda-expression which execution provides the real time scheduler to be set on the simulaiton engine of the model.
	 * @return						the model descriptor.
	 * @throws Exception			<i>to do</i>.
	 */
	public static RTAtomicHIOA_Descriptor	create(
		Class<? extends AtomicHIOA> modelClass,
		String modelURI,
		TimeUnit simulatedTimeUnit,
		AtomicModelFactoryI amFactory,
		SimulationEngineCreationMode engineCreationMode,
		RTSchedulerProviderFI schedulerProvider
		) throws Exception
	{
		return RTAtomicHIOA_Descriptor.create(
							modelClass,
							modelURI, simulatedTimeUnit,
							amFactory, engineCreationMode,
							schedulerProvider,
							RTAtomicModelDescriptor.STD_ACCELERATION_FACTOR);
	}

	/**
	 * create an atomic HIOA descriptor extracting the sets of imported and
	 * exported variables as well as the imported and exported events from
	 * the model classes.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelClass != null || amFactory != null}
	 * pre	{@code modelClass == null || AtomicHIOA.class.isAssignableFrom(modelClass)}
	 * pre	{@code modelURI != null}
	 * pre	{@code simulatedTimeUnit != null}
	 * pre	{@code engineCreationMode.isAtomic()}
	 * pre	{@code engineCreationMode.isRealTime()}
	 * pre	{@code accelerationFactor > 0.0}
	 * post	{@code RTAtomicHIOA_Descriptor.checkInvariant(ret)}
	 * </pre>
	 *
	 * @param modelClass			class defining the model.
	 * @param modelURI				URI of the model to be created.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param amFactory				atomic model factory creating the model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.	
	 * @param accelerationFactor	for real time engines, the acceleration factor between the simulcation clock and the real time.
	 * @return						the model descriptor.
	 * @throws Exception			<i>to do</i>.
	 */
	public static RTAtomicHIOA_Descriptor	create(
		Class<? extends AtomicHIOA> modelClass,
		String modelURI,
		TimeUnit simulatedTimeUnit,
		AtomicModelFactoryI amFactory,
		SimulationEngineCreationMode engineCreationMode,
		double accelerationFactor
		) throws Exception
	{
		return RTAtomicHIOA_Descriptor.create(
							modelClass,
							modelURI, simulatedTimeUnit,
							amFactory, engineCreationMode,
							RTAtomicModelDescriptor.STD_SCHEDULER_PROVIDER,
							accelerationFactor);
	}

	/**
	 * create an atomic HIOA descriptor extracting the sets of imported and
	 * exported variables as well as the imported and exported events from
	 * the model classes.
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
	 * post	{@code RTAtomicHIOA_Descriptor.checkInvariant(ret)}
	 * </pre>
	 *
	 * @param modelClass			class defining the model.
	 * @param modelURI				URI of the model to be created.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param amFactory				atomic model factory creating the model or null if none.
	 * @param engineCreationMode	creation mode for the simulation engine.	
	 * @param schedulerProvider		lambda-expression which execution provides the real time scheduler to be set on the simulaiton engine of the model.
	 * @param accelerationFactor	for real time engines, the acceleration factor between the simulcation clock and the real time.
	 * @return						the model descriptor.
	 * @throws Exception			<i>to do</i>.
	 */
	public static RTAtomicHIOA_Descriptor	create(
		Class<? extends AtomicHIOA> modelClass,
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

		StaticVariableDescriptor[] importedVariables =
			(StaticVariableDescriptor[])
				modelClass.getMethod("getStaticImportedVars",
									 new Class<?>[] {Class.class}).
								invoke(null, new Object[] {modelClass});

		StaticVariableDescriptor[] exportedVariables =
			(StaticVariableDescriptor[])
				modelClass.getMethod("getStaticExportedVars",
									 new Class<?>[] {Class.class}).
								invoke(null, new Object[] {modelClass});

		return new RTAtomicHIOA_Descriptor(
					modelClass, modelURI, importedEvents,
					exportedEvents, simulatedTimeUnit, amFactory,
					engineCreationMode, importedVariables, exportedVariables,
					schedulerProvider, accelerationFactor);
	}

	// -------------------------------------------------------------------------
	// Instance methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.architectures.AtomicHIOA_Descriptor#createAtomicModel()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ModelDescriptionI	createAtomicModel() throws Exception
	{
		if (this.engineCreationMode ==
								SimulationEngineCreationMode.ATOMIC_RT_ENGINE) {
			AtomicHIOA hioa = null;
			AtomicModelFactoryI amFactory = null;
			if (this.amFactory == null) {
				amFactory =
					new StandardRTAtomicHIOA_Factory(
							(Class<? extends AtomicHIOA>) this.modelClass);
				((RTModelFactoryI)amFactory).
							setAccelerationFactor(this.accelerationFactor);
			} else {
				amFactory = this.amFactory;
			}
			amFactory.setAtomicModelCreationParameters(
											this.modelURI,
											this.simulatedTimeUnit,
											amFactory.createAtomicEngine());
			hioa = (AtomicHIOA) amFactory.createAtomicModel();
			hioa.staticInitialiseVariables();
			RTAtomicSimulatorI rte =
							(RTAtomicSimulatorI) hioa.getSimulationEngine();
			rte.setRTScheduler(this.schedulerProvider.provide());
			return rte;
		} else {
			return super.createAtomicModel();
		}
	}
}
// -----------------------------------------------------------------------------
