package main.java.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.interfaces.WasherCI;

import java.time.Duration;
import java.time.LocalTime;

public class WasherConnector extends AbstractConnector implements WasherCI {


    @Override
    public boolean isTurnedOn() throws Exception {
        return ((WasherCI) this.offering).isTurnedOn();
    }

    @Override
    public void setProgramTemperature(int temperature) throws Exception {

    }

    @Override
    public int getProgramTemperature() throws Exception {
        return ((WasherCI) this.offering).getProgramTemperature();
    }

    @Override
    public void setProgramDuration(int duration) throws Exception {

    }

    @Override
    public int getProgramDuration() throws Exception {
        return ((WasherCI) this.offering).getProgramDuration();
    }

    @Override
    public boolean turnOn() throws Exception {
        return ((WasherCI) this.offering).turnOn();
    }

    @Override
    public boolean turnOff() throws Exception {
        return ((WasherCI) this.offering).turnOff();
    }

    /**
     * @see interfaces.StandardEquipmentControlCI#upMode()
     */
    @Override
    public boolean upMode() throws Exception {
        return ((WasherCI) this.offering).upMode();
    }

    /**
     * @see interfaces.StandardEquipmentControlCI#downMode()
     */
    @Override
    public boolean downMode() throws Exception {
        return ((WasherCI) this.offering).downMode();
    }

    /**
     * @see interfaces.StandardEquipmentControlCI#setMode(int)
     */
    @Override
    public boolean setMode(int modeIndex) throws Exception {
        return ((WasherCI) this.offering).setMode(modeIndex);
    }

    /**
     * @see interfaces.StandardEquipmentControlCI#currentMode()
     */
    @Override
    public int currentMode() throws Exception {
        return ((WasherCI) this.offering).currentMode();
    }

    /**
     * @see interfaces.PlanningEquipmentControlCI#hasPlan()
     */
    @Override
    public boolean hasPlan() throws Exception {
        return ((WasherCI) this.offering).hasPlan();
    }

    /**
     * @see interfaces.PlanningEquipmentControlCI#startTime()
     */
    @Override
    public LocalTime startTime() throws Exception {
        return ((WasherCI) this.offering).startTime();
    }

    /**
     * @see interfaces.PlanningEquipmentControlCI#duration()
     */
    @Override
    public Duration duration() throws Exception {
        return ((WasherCI) this.offering).duration();
    }

    /**
     * @see interfaces.PlanningEquipmentControlCI#deadline()
     */
    @Override
    public LocalTime deadline() throws Exception {
        return ((WasherCI) this.offering).deadline();
    }

    /**
     * @see interfaces.PlanningEquipmentControlCI#postpone(Duration)
     */
    @Override
    public boolean postpone(Duration d) throws Exception {
        return ((WasherCI) this.offering).postpone(d);
    }

    /**
     * @see interfaces.PlanningEquipmentControlCI#cancel()
     */
    @Override
    public boolean cancel() throws Exception {
        return ((WasherCI) this.offering).cancel();
    }

    @Override
    public boolean planifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception {
        return ((WasherCI) this.offering).planifyEvent(durationLastPlanned, deadline);
    }
}