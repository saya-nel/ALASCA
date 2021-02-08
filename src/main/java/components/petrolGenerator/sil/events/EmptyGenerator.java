package main.java.components.petrolGenerator.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.petrolGenerator.sil.PetrolGeneratorUserSILModel;

/**
 * The class <code>EmptyGenerator</code> defines the event of the petrol
 * generator being on empty, it is send to the user
 *
 * @author Bello Memmi
 */
public class EmptyGenerator extends AbstractPetrolGeneratorEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * create a EmptyGenerator event.
	 *
	 *
	 * @param timeOfOccurrence time of occurrence of the event.
	 */
	public EmptyGenerator(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "EmptyGenerator(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
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
		assert model instanceof PetrolGeneratorUserSILModel;

		PetrolGeneratorUserSILModel m = (PetrolGeneratorUserSILModel) model;
		m.receiveEmptyGenerator(this);
	}

}
