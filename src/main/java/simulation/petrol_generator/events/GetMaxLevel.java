package main.java.simulation.petrol_generator.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.simulation.fan.events.TurnOff;
import main.java.simulation.petrol_generator.PetrolGeneratorElectricity_MILModel;

public class GetMaxLevel extends AbstractPetrolGeneratorEvent{
    public GetMaxLevel(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
     */
    @Override
    public String eventAsString() {
        return "GetMaxPetrolLevel(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
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
        assert model instanceof PetrolGeneratorElectricity_MILModel;

        PetrolGeneratorElectricity_MILModel m = (PetrolGeneratorElectricity_MILModel) model;
        m.getMaximumPetrolLevel();
        m.toggleConsumptionHasChanged();

    }
}
