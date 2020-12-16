package main.java.simulation.fan.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.simulation.fan.FanElectricity_MILModel;
import main.java.utils.FanLevel;

public class SetLow extends AbstractFanEvent {

	private static final long serialVersionUID = 1L;

	public SetLow(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "SetLow(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.es.events.ES_Event#hasPriorityOver(fr.sorbonne_u.devs_simulation.models.events.EventI)
	 */
	@Override
	public boolean hasPriorityOver(EventI e) {
		if (e instanceof TurnOff) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model) {
		assert model instanceof FanElectricity_MILModel;

		FanElectricity_MILModel m = (FanElectricity_MILModel) model;
		if (m.isOn() && m.getLevel() != FanLevel.LOW) {
			m.setLevel(FanLevel.LOW);
			m.toggleConsumptionHasChanged();
		}
	}

}
