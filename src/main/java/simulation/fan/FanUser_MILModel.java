package main.java.simulation.fan;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.simulation.fan.events.AbstractFanEvent;
import main.java.simulation.fan.events.SetHigh;
import main.java.simulation.fan.events.SetLow;
import main.java.simulation.fan.events.SetMid;
import main.java.simulation.fan.events.TurnOff;
import main.java.simulation.fan.events.TurnOn;

@ModelExternalEvents(exported = {TurnOn.class,
								TurnOff.class,
								SetHigh.class,
								SetMid.class,
								SetLow.class})
public class FanUser_MILModel extends AtomicModel {

	private static final long serialVersionUID = 1L;

	/** time interval between event outputs. */
	protected static final double STEP = 10.0;
	/** the current event being output. */
	protected AbstractFanEvent currentEvent;
	/** time interval between event outputs. */
	protected Duration time2next;

	public FanUser_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	protected AbstractFanEvent getCurrentEventAndSetNext(Time t) {
		if (this.currentEvent == null) {
			this.currentEvent = new TurnOn(t);
		} else {
			@SuppressWarnings("unchecked")
			Class<AbstractFanEvent> c = (Class<AbstractFanEvent>) this.currentEvent.getClass();
			if (c.equals(TurnOn.class)) {
				this.currentEvent = new SetLow(t);
			} else if (c.equals(SetLow.class)) {
				this.currentEvent = new SetMid(t);
			} else if (c.equals(SetMid.class)) {
				this.currentEvent = new SetHigh(t);
			} else if (c.equals(SetHigh.class)) {
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
