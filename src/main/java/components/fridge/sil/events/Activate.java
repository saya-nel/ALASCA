package main.java.components.fridge.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.fridge.sil.FridgeElectricalSILModel;

/**
 * The class <code>Activate</code> represents the action of activating the
 * fridge as an event in the SIL simulation.
 *
 * @author Bello Memmi
 */
public class Activate extends Event {

	private static final long serialVersionUID = 1L;

	/**
	 * create an Activate event.
	 *
	 * @param timeOfOccurrence time of occurrence of the event.
	 */
	public Activate(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model) {
		assert model instanceof FridgeElectricalSILModel;
		((FridgeElectricalSILModel) model).resume();
	}
}
