package main.java.simulation.petrolGenerator.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.simulation.petrolGenerator.PetrolGeneratorElectricity_MILModel;

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
	 * @see fr.sorbonne_u.devs_simulation.es.events.ES_Event#hasPriorityOver(fr.sorbonne_u.devs_simulation.models.events.EventI)
	 */
	@Override
	public boolean hasPriorityOver(EventI e) {
		return true;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model) {
		assert model instanceof PetrolGeneratorElectricity_MILModel;

		PetrolGeneratorElectricity_MILModel m = (PetrolGeneratorElectricity_MILModel) model;
		if (m.getCurrentPetrolLevel() <= m.getMaximumPetrolLevel()) {
			m.fillAll();
		}
	}

}
