package main.java.simulation.battery.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.simulation.battery.BatteryElectricity_MILModel;

public class UpMode extends AbstractBatteryEvent{
    public UpMode(Time timeOfOccurrence) { super(timeOfOccurrence, null);}

    @Override
    public String eventAsString() {
        return "UpMode("+this.getTimeOfOccurrence().getSimulatedTime()+")";
    }

    @Override
    public boolean hasPriorityOver(EventI e) {
       return true;
    }

    @Override
    public void executeOn(AtomicModel model) {
        assert model instanceof BatteryElectricity_MILModel;

    }
}
