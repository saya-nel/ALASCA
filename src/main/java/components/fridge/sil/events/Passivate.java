package main.java.components.fridge.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.fridge.sil.models.FridgeElectricity_SILModel;

/**
 * The class <code>Passivate</code> represents the action of suspending the fridge
 * as an event in the SIL simulation.
 *
 * @author Bello Memmi
 */
public class Passivate
extends Event {
    public				Passivate(Time timeOfOccurrence)
    {
        super(timeOfOccurrence, null);
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
     */
    @Override
    public void			executeOn(AtomicModel model)
    {
        assert	model instanceof FridgeElectricity_SILModel;
        ((FridgeElectricity_SILModel)model).suspend();
    }
}
