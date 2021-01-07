package main.java.simulation.petrolGenerator;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.simulation.petrolGenerator.events.AbstractPetrolGeneratorEvent;
import main.java.simulation.petrolGenerator.events.EmptyGenerator;
import main.java.simulation.petrolGenerator.events.FillAll;
import main.java.simulation.petrolGenerator.events.TurnOff;
import main.java.simulation.petrolGenerator.events.TurnOn;

/**
 * The users actions that interacts with the simulated petrol generator
 * 
 * @author Bello Memmi
 *
 */
@ModelExternalEvents(exported = { TurnOn.class, TurnOff.class, FillAll.class }, imported = { EmptyGenerator.class })
public class PetrolGeneratorUser_MILModel extends AtomicModel {

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** default time interval between event outputs. */
	protected static final double STEP = 1.0;

	/** the current event being output. */
	protected AbstractPetrolGeneratorEvent currentEvent;

	/** time interval between event outputs. */
	protected Duration time2next;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public PetrolGeneratorUser_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	public void receiveEmptyGenerator(EmptyGenerator event) {
		this.currentEvent = event;
	}

	/**
	 * set the current event to be output and return it as result; this method
	 * implements a simple simulation scenario just to test the model.
	 * 
	 * @param t time at which the next event must occur.
	 * @return the next event to be output.
	 */
	protected AbstractPetrolGeneratorEvent getCurrentEventAndSetNext(Time t) {
		// at the start, we turn the generator on
		if (this.currentEvent == null) {
			this.currentEvent = new TurnOn(t);
			// the next event will be a FillAll, from the moment when we can do it (when we
			// receive EmptyGenerator, we wait 10 more second before doing it
			this.time2next = new Duration(10., this.getSimulatedTimeUnit());
		} else {
			@SuppressWarnings("unchecked")
			Class<AbstractPetrolGeneratorEvent> c = (Class<AbstractPetrolGeneratorEvent>) this.currentEvent.getClass();
			if (c.equals(EmptyGenerator.class)) {
				this.currentEvent = new FillAll(t);
				// next event will be turnOn, we wait 1 second before doing it
				this.time2next = new Duration(1., this.getSimulatedTimeUnit());
			} else if (c.equals(FillAll.class)) {
				this.currentEvent = new TurnOn(t);
				// next event will be FillAll, we wait 1 second before doing it
				this.time2next = new Duration(10., this.getSimulatedTimeUnit());
			} else if (c.equals(TurnOn.class)) {
				// nothing to do, we wait the EmptyGenerator event
				return null;
			}
		}
		return currentEvent;
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void initialiseState(Time initialTime) {
		this.time2next = new Duration(STEP, this.getSimulatedTimeUnit());
		this.currentEvent = null;
		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI> output() {
		ArrayList<EventI> ret = new ArrayList<EventI>();
		AbstractPetrolGeneratorEvent nextEvent = this.getCurrentEventAndSetNext(this.getTimeOfNextEvent());
		if (nextEvent == null)
			return null;
		ret.add(nextEvent);
		return ret;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration timeAdvance() {
		return this.time2next;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		assert currentEvents != null && currentEvents.size() == 1;
		Event ce = (Event) currentEvents.get(0);
		System.out.println("PetrolGeneratorUser executing the external event " + ce.getClass().getSimpleName() + "("
				+ ce.getTimeOfOccurrence().getSimulatedTime() + ")");
		assert ce instanceof AbstractPetrolGeneratorEvent;
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
	}

}
