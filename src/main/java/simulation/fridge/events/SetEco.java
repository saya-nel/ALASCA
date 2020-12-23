package main.java.simulation.fridge.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.simulation.fridge.FridgeElectricity_MILModel;
import main.java.utils.FridgeMode;

public class SetEco extends AbstractFridgeEvent {

	private static final long serialVersionUID = 1L;

	public SetEco(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "SetEco(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
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
		assert model instanceof FridgeElectricity_MILModel;

		FridgeElectricity_MILModel m = (FridgeElectricity_MILModel) model;
		if (m.getMode() != FridgeMode.ECO) {
			m.setMode(FridgeMode.ECO);
			m.toggleConsumptionHasChanged();
		}
	}

}
