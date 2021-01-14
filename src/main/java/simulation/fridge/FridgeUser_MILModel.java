package main.java.simulation.fridge;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.simulation.fridge.events.AbstractFridgeEvent;
import main.java.simulation.fridge.events.SetEco;
import main.java.simulation.fridge.events.SetNormal;

@ModelExternalEvents(exported = { SetEco.class, SetNormal.class })
public class FridgeUser_MILModel extends AtomicModel {

	private static final long serialVersionUID = 1L;

	/** time interval between event outputs. */
	protected static final double STEP = 10.0;
	/** the current event being output. */
	protected AbstractFridgeEvent currentEvent;
	/** time interval between event outputs. */
	protected Duration time2next;

	public FridgeUser_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	protected AbstractFridgeEvent getCurrentEventAndSetNext(Time t) {
		if (this.currentEvent == null) {
			this.currentEvent = new SetEco(t);
		} else {
			@SuppressWarnings("unchecked")
			Class<AbstractFridgeEvent> c = (Class<AbstractFridgeEvent>) this.currentEvent.getClass();
			if (c.equals(SetEco.class)) {
				this.currentEvent = new SetNormal(t);
			} else if (c.equals(SetNormal.class)) {
				this.currentEvent = new SetEco(t);
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
		return Duration.INFINITY;
		// return this.time2next;
	}

}
