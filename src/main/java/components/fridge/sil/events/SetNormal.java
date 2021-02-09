package main.java.components.fridge.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.fridge.sil.FridgeElectricalSILModel;
import main.java.components.fridge.utils.FridgeMode;

/**
 * The class <code>SetNormal</code> defines the MIL event of the fridge being
 * set to medium mode.
 *
 * @author Bello Memmi
 */
public class SetNormal extends AbstractFridgeEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * create a SetNormal event.
	 *
	 * @param timeOfOccurrence time of occurrence of the event.
	 */
	public SetNormal(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "SetNormal(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.es.events.ES_Event#hasPriorityOver(EventI)
	 */
	@Override
	public boolean hasPriorityOver(EventI e) {
		return false;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model) {
		assert model instanceof FridgeElectricalSILModel;

		FridgeElectricalSILModel m = (FridgeElectricalSILModel) model;
		if (m.getMode() != FridgeMode.NORMAL) {
			m.setMode(FridgeMode.NORMAL);
		}
	}

}
