package main.java.simulation.battery;

import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.simulation.fan.events.AbstractFanEvent;
import main.java.utils.BatteryState;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@ModelExternalEvents(imported = {})
public class BatteryElectricity_MILModel extends AtomicHIOA {
    public static final double RECHARGING_MODE_CONSUMPTION = 30;
    public static final double DRAINING_MODE_CONSUMPTION = 40;
    public static final double SLEEPING_MODE_CONSUMPTION = 0;
    public static final double TENSION = 220;

    @ExportedVariable(type = Double.class)
    protected final Value<Double> currentIntensity = new Value<~>(this, 0.0, 0);

    protected BatteryState currentState = BatteryState.SLEEPING;
    protected boolean consumptionHasChanged = false;

    public BatteryElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
        throws Exception {
        super(uri, simulatedTimeUnit, simulationEngine);
    }

    public void setMode(BatteryState state) {
        this.currentState = state;
    }

    public BatteryState getMode(){
        return this.currentState;
    }



    public void toggleConsumptionHasChanged() {
        this.consumptionHasChanged = (this.consumptionHasChanged) ? false: true;
    }
    // -------------------------------------------------------------------------
    // DEVS simulation protocol
    // -------------------------------------------------------------------------

    @Override
    protected void InitialiseVariables(Time startTime) {
        this.currentIntensity.v = 0.0;
        super.initialiseVariables(startTime);
    }
    @Override
    public void initialiseState() {
        this.currentState = BatteryState.SLEEPING;
    }

    @Override
    public ArrayList<EventI> output() {
        return null;
    }

    @Override
    public Duration timeAdvance() {
        if (this.consumptionHasChanged) {
            this.toggleConsumptionHasChanged();
            return new Duration(0.0, this.getSimulatedTimeUnit());
        }
        else {
            return Duration.INFINITY;
        }
    }
    /**
     * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
     */
    @Override
    public void userDefinedInternalTransition(Duration elapsedTime) {
        super.userDefinedInternalTransition(elapsedTime);
        if (this.isOn) {
            switch (this.currentState) {
                case BatteryState.DRAINING:
                    this.currentIntensity.v = RECHARGING_MODE_CONSUMPTION / TENSION;
                    break;

            }
        } else {
            this.currentIntensity.v = 0.;
        }
        this.currentIntensity.time = this.getCurrentStateTime();
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
     */
    @Override
    public void userDefinedExternalTransition(Duration elapsedTime) {
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        assert currentEvents != null && currentEvents.size() == 1;
        Event ce = (Event) currentEvents.get(0);
        assert ce instanceof AbstractFanEvent;
        ce.executeOn(this);
        super.userDefinedExternalTransition(elapsedTime);
    }
}
