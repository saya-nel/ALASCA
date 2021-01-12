package main.java.simulation.controller;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.simulation.battery.events.EmptyPlan;
import main.java.simulation.panel.events.ConsumptionLevel;
import main.java.simulation.panel.events.ConsumptionLevelRequest;
import main.java.simulation.panel.events.ProductionLevel;
import main.java.simulation.panel.events.ProductionLevelRequest;
import main.java.simulation.utils.FileLogger;

@ModelExternalEvents(exported = { ConsumptionLevelRequest.class, ProductionLevelRequest.class }, imported = {
		ConsumptionLevel.class, ProductionLevel.class, /** ajout d'une tache gérée par le controller simulé*/ EmptyPlan.class })
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
		this.setLogger(new FileLogger("controller.log"));
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
		// when this method is called, there is at least one external event
		assert currentEvents != null && currentEvents.size() >= 1;
		// The received event can only be a ConsumptionLevel, as this is the
		// only imported event by this model.

		double consumptionLevel = 0.;
		double productionLevel = 0.;

		for (EventI event : currentEvents) {
			assert (event instanceof ConsumptionLevel || event instanceof ProductionLevel);
			if (event instanceof ConsumptionLevel) {
				ConsumptionLevel ce = (ConsumptionLevel) event;
				/*this.logger.logMessage("", "Controller receiving the external event " + ce.getClass().getSimpleName()
						+ "(" + ce.getTimeOfOccurrence().getSimulatedTime() + ", " + ce.getConsumptionLevel() + ")");*/
				consumptionLevel = ce.getConsumptionLevel();
			} else if (event instanceof ProductionLevel) {
				ProductionLevel pe = (ProductionLevel) event;
				/*this.logger.logMessage("",
						this.getCurrentStateTime() + " Controller receiving the external event "
								+ pe.getClass().getSimpleName() + "(" + pe.getTimeOfOccurrence().getSimulatedTime()
								+ ", " + pe.getProductionLevel() + ")");*/
				productionLevel = pe.getProductionLevel();
			} else if (event instanceof  EmptyPlan) {
				EmptyPlan ep = (EmptyPlan) event;
				this.logger.logMessage("", "Controller receiving the external event" +
						"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$4 " + ep.getClass().getSimpleName()
						+"(" + ep.getTimeOfOccurrence().getSimulatedTime() + ", "+ ")");

			}

		}

		// TODO : ici on utilise consumptionLevel / productionLevel et en fonction de
		// leurs valeurs on envoie des evenements aux composants simulés, on pourait par
		// exemple demander a un composant de s'arreter, ou de passer en eco, etc.

		super.userDefinedExternalTransition(elapsedTime);
	}
}
