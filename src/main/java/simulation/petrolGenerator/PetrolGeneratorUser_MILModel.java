package main.java.simulation.petrolGenerator;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.simulation.petrolGenerator.events.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@ModelExternalEvents(exported = {TurnOn.class, TurnOff.class, GetMaxLevel.class, GetPetrolLevel.class, IsTurnedOn.class, AddPetrol.class})
public class PetrolGeneratorUser_MILModel extends AtomicModel {
    /** time interval between event outputs. */
    protected static final double STEP = 10.0;
    /** the current event being output. */
    protected AbstractPetrolGeneratorEvent currentEvent;
    /** time interval between event outputs. */
    protected Duration time2next;

    public PetrolGeneratorUser_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
    }
    protected AbstractPetrolGeneratorEvent getCurrentEventAndSetNext(Time t) {
        if (this.currentEvent == null) {
            this.currentEvent = new TurnOn(t);
        } else {
            @SuppressWarnings("unchecked")
            Class<AbstractPetrolGeneratorEvent> c = (Class<AbstractPetrolGeneratorEvent>) this.currentEvent.getClass();
            if (c.equals(TurnOn.class)) {
                this.currentEvent = new TurnOn(t);
            } else if (c.equals(TurnOff.class)) {
                this.currentEvent = new TurnOff(t);
            } else if (c.equals(AddPetrol.class)) {
                this.currentEvent = new AddPetrol(t);
            } else if (c.equals(GetMaxLevel.class)) {
                this.currentEvent = new GetPetrolLevel(t);
            } else if (c.equals(GetPetrolLevel.class)) {
                this.currentEvent = new GetPetrolLevel(t);
            } else if (c.equals(IsTurnedOn.class)) {
                this.currentEvent = new IsTurnedOn(t);
            }
        }
        return this.currentEvent;
    }

    // -------------------------------------------------------------------------
    // DEVS simulation protocol
    // -------------------------------------------------------------------------

    /**
     * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
     */
    @Override
    public void initialiseState(Time initialTime) {
        this.time2next = new Duration(STEP, this.getSimulatedTimeUnit());
        this.currentEvent = null;
        super.initialiseState(initialTime);
    }

    @Override
    public ArrayList<EventI> output() {
        ArrayList<EventI> ret = new ArrayList<EventI>();
        ret.add(this.getCurrentEventAndSetNext(this.getTimeOfNextEvent()));
        return ret;
    }

    @Override
    public Duration timeAdvance() {
        return this.time2next;
    }

}
