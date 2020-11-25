package fr.sorbonne_u.components.cyphy.hem2020e2.equipments.hairdryer;

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
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

// -----------------------------------------------------------------------------
/**
 * The class <code>HairDryerElectricity_MILModel</code> defines a MIL model
 * of the electricity consumption of a hair dryer.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The hair dryer can be switched on and off, and when switched on, it can be
 * eitehr in a low mode, with lower electricity consumption, or a high mode,
 * with a higher electricity consumption.
 * </p>
 * <p>
 * The electricity consumption is represented as a double variable that has to
 * be exported towards the electric panel MIL model in order to be summed up
 * to get the global electricity consumption.
 * </p>
 * <p>
 * To model the user actions, four events are defined to be imported and the
 * external transitions upon the reception of these events force the hair
 * dryer in the corresponding mode.
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
@ModelExternalEvents(imported = {SwitchOn.class,
								 SwitchOff.class,
								 SetLow.class,
								 SetHigh.class})
// -----------------------------------------------------------------------------
public class			HairDryerElectricity_MILModel
extends		AtomicHIOA
{
	// -------------------------------------------------------------------------
	// Inner classes and types
	// -------------------------------------------------------------------------

	/**
	 * The enumeration <code>State</code> describes the discrete states or
	 * modes of the hair dryer.
	 *
	 * <p><strong>Description</strong></p>
	 * 
	 * The hair dryer can be <code>OFF</code> or on, and then it is either in
	 * <code>LOW</code> mode (less hot and less consuming) or in
	 * <code>HIGH</code> mode (hotter and more consuming).
	 * 
	 * <p>Created on : 2019-10-10</p>
	 * 
	 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
	 */
	public static enum State {
		OFF,
		/** low mode is less hot and less consuming.						*/
		LOW,			
		/** high mode is hotter and more consuming.							*/
		HIGH
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long	serialVersionUID = 1L;

	/** URI for an instance model; works as long as only one instance is
	 *  created.															*/
	public static final String	URI =
									HairDryerElectricity_MILModel.class.
																	getName();

	/** energy consumption (in Watts) of the hair dryer in LOW mode.		*/
	public static final double		LOW_MODE_CONSUMPTION = 660.0; // Watts
	/** energy consumption (in Watts) of the hair dryer in HIGH mode.		*/
	public static final double		HIGH_MODE_CONSUMPTION = 1100.0; // Watts
	/** nominal tension (in Volts) of the hair dryer.						*/
	public static final double		TENSION = 220.0; // Volts

	/** current intensity in amperes; intensity is power/tension.			*/
	@ExportedVariable(type = Double.class)
	protected final Value<Double>	currentIntensity =
											new Value<Double>(this, 0.0, 0);
	/** current state (OFF, LOW, HIGH) of the hair dryer.					*/
	protected State					currentState = State.OFF;
	/** true when the electricity consumption of the dryer has changed
	 *  after executing an external event; the external event changes the
	 *  value of <code>currentState</code> and then an internal transition
	 *  will be triggered by putting through in this variable which will
	 *  update the variable <code>currentIntensity</code>.					*/
	protected boolean				consumptionHasChanged = false;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a hair dryer MIL model instance.
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
	 * @throws Exception		<i>to do</i>.
	 */
	public				HairDryerElectricity_MILModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * set the state of the hair dryer.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	s != null
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param s		the new state.
	 */
	public void			setState(State s)
	{
		this.currentState = s;
	}

	/**
	 * return the state of the hair dryer.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	{@code ret != null}
	 * </pre>
	 *
	 * @return	the state of the hair dryer.
	 */
	public State		getState()
	{
		return this.currentState;
	}

	/**
	 * toggle the value of the state of the model telling whether the
	 * electricity consumption level has just changed or not; when it changes
	 * after receiving an external event, an immediate internal transition
	 * is triggered to update the level of electricity consumption.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
	public void			toggleConsumptionHasChanged()
	{
		if (this.consumptionHasChanged) {
			this.consumptionHasChanged = false;
		} else {
			this.consumptionHasChanged = true;
		}
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
		// initially, the hair dryer is off, so its consumption is zero.
		this.currentIntensity.v = 0.0;

		super.initialiseVariables(startTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#initialiseState()
	 */
	@Override
	public void			initialiseState()
	{
		// initially the hair dryer is off and its electricity consumption is
		// not about to change.
		this.currentState = State.OFF;
		this.consumptionHasChanged = false;

		super.initialiseState();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI>	output()
	{
		return null;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration		timeAdvance()
	{
		// to trigger an internal transition after an external transition, the
		// variable consumptionHasChanged is set to true, hence when it is true
		// return a zero delay otherwise return an infinite delay (no internal
		// transition expected)
		if (this.consumptionHasChanged) {
			// after triggering the internal transition, toggle the boolean
			// to prepare for the next internal transition.
			this.toggleConsumptionHasChanged();
			return new Duration(0.0, this.getSimulatedTimeUnit());
		} else {
			return Duration.INFINITY;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			userDefinedInternalTransition(Duration elapsedTime)
	{
		super.userDefinedInternalTransition(elapsedTime);

		// set the current electricity consumption from the current state
		switch (this.currentState)
		{
			case OFF : this.currentIntensity.v = 0.0; break;
			case LOW :
				this.currentIntensity.v = LOW_MODE_CONSUMPTION/TENSION;
				break;
			case HIGH :
				this.currentIntensity.v = HIGH_MODE_CONSUMPTION/TENSION;
		}
		this.currentIntensity.time = this.getCurrentStateTime();
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
		// and for the hair dryer model, there will be exactly one by
		// construction.
		assert	currentEvents != null && currentEvents.size() == 1;

		Event ce = (Event) currentEvents.get(0);
		System.out.println("Hair dryer executing the external event " +
							ce.getClass().getSimpleName() + "(" +
							ce.getTimeOfOccurrence().getSimulatedTime() + ")");
		assert	ce instanceof AbstractHairDryerEvent;
		// events have a method execute on to perform their effect on this
		// model
		ce.executeOn(this);

		super.userDefinedExternalTransition(elapsedTime);
	}
}
// -----------------------------------------------------------------------------
