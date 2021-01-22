package main.java.components.petrolGenerator.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.petrolGenerator.sil.PetrolGeneratorElectricitySILModel;

public class FillAll extends AbstractPetrolGeneratorEvent {

	private static final long serialVersionUID = 1L;

	public FillAll(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "FillAll petrol generator(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
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
		assert model instanceof PetrolGeneratorElectricitySILModel;

		PetrolGeneratorElectricitySILModel m = (PetrolGeneratorElectricitySILModel) model;
		if (m.getCurrentPetrolLevel() <= m.getMaximumPetrolLevel()) {
			m.fillAll();
		}
	}

}
