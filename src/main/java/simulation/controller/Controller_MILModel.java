package main.java.simulation.controller;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.simulation.panel.events.ConsumptionLevel;
import main.java.simulation.panel.events.ConsumptionLevelRequest;

@ModelExternalEvents(exported = { ConsumptionLevelRequest.class }, imported = { ConsumptionLevel.class })
public class Controller_MILModel extends AtomicModel {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	/**
	 * time interval between electricity consumption levels are requested from the
	 * electric panel expressed in the simulation time unit of the model.
	 */
	protected static final double STEP_LENGTH = 1.0;
	/**
	 * standard interval between electricity consumption level requests as
	 * simulation duration, including the time unit.
	 */
	protected final Duration standardStep;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public Controller_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.standardStep = new Duration(STEP_LENGTH, simulatedTimeUnit);
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI> output() {
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
	public Duration timeAdvance() {
		// For this simple example model, consumption levels are requested at
		// a fixed rate.
		return this.standardStep;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		// get the vector of current external events
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		// when this method is called, there is at least one external event,
		// and for the panel model, there will be exactly one by
		// construction which is a consumption level request.
		assert currentEvents != null && currentEvents.size() == 1;
		// The received event can only be a ConsumptionLevel, as this is the
		// only imported event by this model.
		assert currentEvents.get(0) instanceof ConsumptionLevel;

		ConsumptionLevel ce = (ConsumptionLevel) currentEvents.get(0);
		System.out.println("HEM receiving the external event " + ce.getClass().getSimpleName() + "("
				+ ce.getTimeOfOccurrence().getSimulatedTime() + ", " + ce.getConsumptionLevel() + ")");

		// TODO : ici d'autre cas devront être gérés, quand on reçoit un evenement de
		// consomation, on doit regarder le niveau de consommation par rapport au niveau
		// d'energie dispo et interagir avec les appareils controlable si necessaire (ex
		// : changer le mode d'un appareil en eco si pas assez d'energie, ou l'arreter),
		// on fait cela en envoyer un evenement a l'appareil concerné

		super.userDefinedExternalTransition(elapsedTime);
	}
}
