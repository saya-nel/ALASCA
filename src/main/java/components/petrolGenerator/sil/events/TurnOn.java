package main.java.components.petrolGenerator.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.petrolGenerator.sil.PetrolGeneratorElectricalSILModel;

/**
 * The class <code>TurnOn</code> defines the event of the petrol generator being
 * turned on
 *
 * @author Bello Memmi
 */
public class TurnOn extends AbstractPetrolGeneratorEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * create a TurnOn event.
	 *
	 *
	 * @param timeOfOccurrence time of occurrence of the event.
	 */
	public TurnOn(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "TurnOn petrol generator(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
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
		assert model instanceof PetrolGeneratorElectricalSILModel;

		PetrolGeneratorElectricalSILModel m = (PetrolGeneratorElectricalSILModel) model;
		m.turnOn();
		m.toggleConsumptionHasChanged();
	}
}
