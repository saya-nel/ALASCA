package main.java.simulation.washer.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.simulation.washer.WasherElectricity_MILModel;
import main.java.utils.WasherModes;

public class PlanifyProgram extends AbstractWasherEvent{
    public PlanifyProgram(Time timeOfOccurrence) {
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
        assert model instanceof WasherElectricity_MILModel;
        WasherElectricity_MILModel m = (WasherElectricity_MILModel) model;
        /** The user automatically planify a program that begins in 20 seconds and during 2000 secondes */
        m.planifyEvent(this.getTimeOfOccurrence().add(new Duration(20, this.getTimeOfOccurrence().getTimeUnit())), new Duration(2000, this.getTimeOfOccurrence().getTimeUnit()));
        m.toggleConsumptionHasChanged();

    }
}
