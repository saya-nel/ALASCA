package fr.sorbonne_u.components.cyphy.hem2020e2.equipments.panel;

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
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ImportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

// -----------------------------------------------------------------------------
/**
 * The class <code>PanelElectricity_MILModel</code> defines the core model for
 * the electric panel.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The electric panel is there to sum up the consumptions of the different
 * appliances to get the global electricity consumption and to provide a
 * service to get the current global electricity consumption.
 * </p>
 * <p>
 * This model is the embryonic electric panel MIL model that currently only
 * imports the variable giving the electricity consumption of the hair dryer.
 * A fully developed model would import similarly the electricity consumption
 * of every appliance and sum them up to get the global electricity consumption.
 * </p>
 * <p>
 * The model, being MIL, provides a way to get the value of the global
 * electricity consumption through a simple event-exchanging protocol.
 * Upon receiving a <code>ConsumptionLevelRequest</code>, the model will
 * force an immediate internal transition that will in turn trigger the
 * output of a <code>ConsumptionLevel</code> event with the current global
 * electricity consumption as its informative content (or payload).
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-11-12</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
// -----------------------------------------------------------------------------
@ModelExternalEvents(imported = {ConsumptionLevelRequest.class},
					 exported = {ConsumptionLevel.class})
// -----------------------------------------------------------------------------
public class			PanelElectricity_MILModel
extends		AtomicHIOA
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long		serialVersionUID = 1L;
	/** URI for an instance model; works as long as only one instance is
	 *  created.															*/
	public static final String		URI =
										PanelElectricity_MILModel.class.
																	getName();
	/** time interval between electricity consumption levels are computed
	 *  from the consumption of all connected and switched on appliances,
	 *  expressed in the simulation time unit of the model.					*/
	protected static final double	STEP_LENGTH = 1.0;
	/** standard interval between electricity consumption level computations
	 *  as simulation duration, including the time unit.					*/
	protected final Duration		standardStep;

	/** current intensity in amperes; intensity is power/tension.			*/
	@ExportedVariable(type = Double.class)
	protected final Value<Double>	currentIntensity =
											new Value<Double>(this, 0.0, 0);
	/** current intensity of the hair dryer in amperes; intensity is
	 *  power/tension.														*/
	@ImportedVariable(type = Double.class)
	protected Value<Double>			hairDryerIntensity;
	/** current intensity of the boiler in amperes; intensity is
	 *  power/tension.														*/
	@ImportedVariable(type = Double.class)
	protected Value<Double>			boilerIntensity;

	/** time interval until the next global electricity consumption
	 *  computation.														*/
	protected Duration				nextStep;
	/** true if a request for the current value of the global electricity
	 *  consumption (through a <code>ConsumptionLevelRequest</code>
	 *  event, false otherwise.												*/
	protected boolean				requestReceived = false;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a new panel model instance.
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
	public				PanelElectricity_MILModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine);
		this.standardStep = new Duration(STEP_LENGTH, simulatedTimeUnit);
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void		initialiseVariables(Time startTime)
	{
		// initial intensity, before the first computation
		this.currentIntensity.v = 0.0;
		super.initialiseVariables(startTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			initialiseState(Time initialTime)
	{
		// initially, no request has been received yet.
		this.requestReceived = false;
		// the first time interval until the first computation.
		this.nextStep = this.standardStep;
		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI> output()
	{
		if (this.requestReceived) {
			// when a request has been received, output a ConsumptionLevel
			// event with the current global electricity consumption.
			ArrayList<EventI> ret = new ArrayList<EventI>();
			ret.add(new ConsumptionLevel(this.getTimeOfNextEvent(),
										 this.currentIntensity.v));
			return ret;
		} else {
			// otherwise, no output event.
			return null;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration		timeAdvance()
	{
		if (this.requestReceived) {
			// trigger an immediate internal transition.
			return Duration.zero(this.getSimulatedTimeUnit());
		} else {
			// wait until the next planned computation.
			return this.nextStep;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			userDefinedInternalTransition(Duration elapsedTime)
	{
		super.userDefinedInternalTransition(elapsedTime);

		if (!this.requestReceived) {
			// no request received, hence this is a computation step
			// compute the new global electricity consumption
			this.currentIntensity.v =
							this.hairDryerIntensity.v + this.boilerIntensity.v;
			this.currentIntensity.time = this.getCurrentStateTime();
			// the next planned computation 
			this.nextStep = this.standardStep;
		} else {
			// a request has been received before the next computation
			assert	elapsedTime.lessThanOrEqual(this.standardStep);
			// the event has already been output, simply replan the next
			// computation at its previously planned time.
			this.nextStep = this.standardStep.subtract(elapsedTime);
			this.requestReceived = false;
		}
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
		// The received event can only be a ConsumptionLevelRequest, as this
		// is the only imported event by this model.		
		assert	currentEvents.get(0) instanceof ConsumptionLevelRequest;

		ConsumptionLevelRequest ce =
							(ConsumptionLevelRequest)currentEvents.get(0);
		System.out.println("Panel receiving the external event " +
							ce.getClass().getSimpleName() + "(" +
							ce.getTimeOfOccurrence().getSimulatedTime() + ")");

		// this will trigger an immediate internal transition and the ouput
		// of the ConsumptionLevel event.
		this.requestReceived = true;

		super.userDefinedExternalTransition(elapsedTime);
	}
}
// -----------------------------------------------------------------------------
