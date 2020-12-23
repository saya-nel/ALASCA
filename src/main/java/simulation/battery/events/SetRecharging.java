package main.java.simulation.battery.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.simulation.battery.BatteryElectricity_MILModel;
import main.java.utils.BatteryState;

public class SetRecharging extends AbstractBatteryEvent {

	private static final long serialVersionUID = 1L;

	public SetRecharging(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "SetRecharging(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.es.events.ES_Event#hasPriorityOver(fr.sorbonne_u.devs_simulation.models.events.EventI)
	 */
	@Override
	public boolean hasPriorityOver(EventI e) {
		return false;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model) {
		assert model instanceof BatteryElectricity_MILModel;

		BatteryElectricity_MILModel m = (BatteryElectricity_MILModel) model;
		if (m.getState() != BatteryState.RECHARGING) {
			m.setState(BatteryState.RECHARGING);
			m.toggleConsumptionHasChanged();
		}
	}

}
