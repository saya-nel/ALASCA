package main.java.simulation.battery.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public class BatteryPlan extends AbstractBatteryEvent{
    public BatteryPlan(Time timeOfOccurrence) {
        super(timeOfOccurrence, null);
    }
    /**
     * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
     */
    @Override
    public String eventAsString() {
        return "BatteryPlanificatin(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
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
//		TODO
    }
}
