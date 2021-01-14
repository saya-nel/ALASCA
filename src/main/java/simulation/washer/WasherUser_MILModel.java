package main.java.simulation.washer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.simulation.utils.SimProgram;
import main.java.simulation.washer.events.AbstractWasherEvent;
import main.java.simulation.washer.events.PlanifyProgram;
import main.java.simulation.washer.events.SetEco;
import main.java.simulation.washer.events.SetPerformance;
import main.java.simulation.washer.events.SetStd;
import main.java.simulation.washer.events.TurnOff;
import main.java.simulation.washer.events.TurnOn;

/**
 *
 * The class <code>WasherUser_MILModel</code> defines a very simple user model
 * for the Washer
 *
 * <p>
 * This model is meant to illustrate how to program user MIL models, sending
 * events to other models to simulate
 * </p>
 * <p>
 * Here, we simple ouptput events at a regularly rate and in a predefined cycle
 * to test all of the different modes in the Washer
 * </p>
 * 
 * @author Bello Memmi
 */
@ModelExternalEvents(exported = { TurnOn.class, TurnOff.class, SetPerformance.class, SetStd.class, SetEco.class,
		PlanifyProgram.class })
public class WasherUser_MILModel extends AtomicModel {

	private static final long serialVersionUID = 1L;

	/** time interval between event outputs. */
	protected static final double STEP = 20.0;

	/** the current event being output. */
	protected AbstractWasherEvent currentEvent;

	/** time interval between event outputs. */
	protected Duration time2next;

	public WasherUser_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	protected AbstractWasherEvent getCurrentEventAndSetNext(Time t) {
		if (this.currentEvent == null) {
			this.currentEvent = new TurnOn(t);
		} else {
			@SuppressWarnings("unchecked")
			Class<AbstractWasherEvent> c = (Class<AbstractWasherEvent>) this.currentEvent.getClass();
			if (c.equals(TurnOn.class)) {
				this.currentEvent = new SetEco(t);
			} else if (c.equals(SetEco.class)) {
				this.currentEvent = new SetStd(t);
			} else if (c.equals(SetStd.class)) {
				this.currentEvent = new SetPerformance(t);
			} else if (c.equals(SetPerformance.class)) {
				this.currentEvent = new PlanifyProgram(t,
						new SimProgram(this.getCurrentStateTime(), new Duration(2, this.getSimulatedTimeUnit())));
			} else if (c.equals(SetPerformance.class)) {
				this.currentEvent = new TurnOff(t);
			}

		}
		return this.currentEvent;
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

	@Override
	public ArrayList<EventI> output() {
		ArrayList<EventI> ret = new ArrayList<EventI>();
		ret.add(this.getCurrentEventAndSetNext(this.getTimeOfNextEvent()));
		return ret;
	}

	@Override
	public Duration timeAdvance() {
		//return Duration.INFINITY;
		return this.time2next;
	}

}
