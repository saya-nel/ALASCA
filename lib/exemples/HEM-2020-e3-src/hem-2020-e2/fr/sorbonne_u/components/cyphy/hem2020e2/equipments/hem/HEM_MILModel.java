package fr.sorbonne_u.components.cyphy.hem2020e2.equipments.hem;

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

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.meter.ConsumptionLevel;
import fr.sorbonne_u.components.cyphy.hem2020e2.equipments.meter.ConsumptionLevelRequest;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;

// -----------------------------------------------------------------------------
/**
 * The class <code>HEM_MILModel</code> defines a simplified MIL model for the
 * household energy manager.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * This version of a MIL model for the household energy manager (HEM) simply
 * requests repeatedly the current electricity consumption from the electric
 * panel and display it on the terminal. This illustrates one functionality to
 * be used in the HEM and a relatively simple way to activate it. This is just
 * meant to be an starting example for the project.
 * </p>
 * <p>
 * The model, being MIL, provides a way to get the value of the global
 * electricity consumption through a simple event-exchanging protocol.
 * After the output of a <code>ConsumptionLevelRequest</code>, the model will
 * expect to receive a <code>ConsumptionLevel</code> event with the current
 * global electricity consumption as its informative content (or payload)
 * from the electric panel MIL model.
 * </p>
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
// -----------------------------------------------------------------------------
@ModelExternalEvents(exported = {ConsumptionLevelRequest.class},
					 imported = {ConsumptionLevel.class})
// -----------------------------------------------------------------------------
public class			HEM_MILModel
extends		AtomicModel
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long 		serialVersionUID = 1L;
	/** URI for an instance model; works as long as only one instance is
	 *  created.															*/
	public static final String		URI = HEM_MILModel.class.getSimpleName();
	/** time interval between electricity consumption levels are requested
	 *  from the electric panel expressed in the simulation time unit of
	 *  the model.															*/
	protected static final double	STEP_LENGTH = 1.0;
	/** standard interval between electricity consumption level requests
	 *  as simulation duration, including the time unit.					*/
	protected final Duration		standardStep;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a new house energy manager model instance.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param uri				URI of the model.
	 * @param simulatedTimeUnit	time unit used for the simulation time.
	 * @param simulationEngine	simulation engine to which the model is attached.
	 * @throws Exception		<i>to do.</i>
	 */
	public				HEM_MILModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine);
		this.standardStep = new Duration(STEP_LENGTH, simulatedTimeUnit);
		this.setLogger(new StandardLogger());
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			initialiseState(Time initialTime)
	{
		this.toggleDebugMode();
		this.logMessage("simulation begins for " + this.uri + ".\n");

		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI>	output()
	{
		// Each time the method is called (just before internal transitions)
		// a ConsumptionLevelRequest is output towards the electric panel.
		ArrayList<EventI> ret = new ArrayList<EventI>();
		ret.add(new ConsumptionLevelRequest(this.getTimeOfNextEvent()));
		return ret;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration		timeAdvance()
	{
		// For this simple example model, consumption levels are requested at
		// a fixed rate.
		return this.standardStep;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			userDefinedExternalTransition(Duration elapsedTime)
	{
		// get the vector of current external events
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		// when this method is called, there is at least one external event,
		// and for the panel model, there will be exactly one by
		// construction which is a consumption level request.
		assert	currentEvents != null && currentEvents.size() == 1;
		// The received event can only be a ConsumptionLevel, as this is the
		// only imported event by this model.
		assert	currentEvents.get(0) instanceof ConsumptionLevel;

		ConsumptionLevel ce = (ConsumptionLevel)currentEvents.get(0);
		this.logMessage("HEM receiving the external event " +
						ce.getClass().getSimpleName() + "(" +
						ce.getTimeOfOccurrence().getSimulatedTime() + ", " +
						ce.getConsumptionLevel() + ")\n");

		super.userDefinedExternalTransition(elapsedTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			endSimulation(Time endTime) throws Exception
	{
		this.logMessage("simulation ends for " + this.uri + ".\n");
		super.endSimulation(endTime);
	}
}
// -----------------------------------------------------------------------------
