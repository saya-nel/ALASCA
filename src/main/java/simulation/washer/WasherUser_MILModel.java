package main.java.simulation.washer;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.simulation.washer.events.AbstractWasherEvent;
import main.java.simulation.washer.events.SetEco;
import main.java.simulation.washer.events.SetPerformance;
import main.java.simulation.washer.events.SetStd;
import main.java.simulation.washer.events.TurnOff;
import main.java.simulation.washer.events.TurnOn;

@ModelExternalEvents(exported = { TurnOn.class, TurnOff.class, SetPerformance.class, SetStd.class, SetEco.class })
public class WasherUser_MILModel extends AtomicModel {

	private static final long serialVersionUID = 1L;

	/** time interval between event outputs. */
	protected static final double STEP = 10.0;
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
		return this.time2next;
	}

}
