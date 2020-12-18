package main.java.simulation.battery.events;

/**
 *
 * @author Bello Memmi
 *
 * The class <code>PlanifyEvent</code> defines the MIL event of the battery planificating task
 */

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.simulation.battery.BatteryElectricity_MILModel;

public class PlanifyEvent extends AbstractBatteryEvent{
    public PlanifyEvent(Time timeOfOccurrence) { super(timeOfOccurrence, null);}

    /**
     * @see Event#eventAsString()
     */
    @Override
    public String eventAsString() {
        return "PlanifyEvent(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.es.events.ES_Event#hasPriorityOver(EventI)
     */
    @Override
    public boolean hasPriorityOver(EventI e) {
        return true;
    }

    public void executeOn(AtomicModel model) {
        assert model instanceof BatteryElectricity_MILModel;
        BatteryElectricity_MILModel m = (BatteryElectricity_MILModel) model;
        if (m.ge)

    }
    
}
