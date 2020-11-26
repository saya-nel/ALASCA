package main.java.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.interfaces.BatteryCI;
import main.java.interfaces.PlanningEquipmentControlCI;

import java.time.Duration;
import java.time.LocalTime;

public class BatteryConnector extends AbstractConnector implements BatteryCI {

    /**
     * @see interfaces.BatteryImplementationI#getBatteryCharge()
     */
    public float getBatteryCharge() throws Exception {
        return ((BatteryCI) super.offering).getBatteryCharge();
    }


    /**
     * @see interfaces.StandardEquipmentControlCI#upMode()
     */
    @Override
    public boolean upMode() throws Exception {
        return ((BatteryCI) this.offering).upMode();
    }

    /**
     * @see interfaces.StandardEquipmentControlCI#downMode()
     */
    @Override
    public boolean downMode() throws Exception {
        return ((BatteryCI) this.offering).downMode();
    }

    /**
     * @see interfaces.StandardEquipmentControlCI#setMode(int)
     */
    @Override
    public boolean setMode(int modeIndex) throws Exception {
        return ((BatteryCI) this.offering).setMode(modeIndex);
    }

    /**
     * @see interfaces.StandardEquipmentControlCI#currentMode()
     */
    @Override
    public int currentMode() throws Exception {
        return ((BatteryCI) this.offering).currentMode();
    }

    /**
     * @see interfaces.PlanningEquipmentControlCI#hasPlan()
     */
    @Override
    public boolean hasPlan() throws Exception {
        return ((BatteryCI) this.offering).hasPlan();
    }

    /**
     * @see interfaces.PlanningEquipmentControlCI#startTime()
     */
    @Override
    public LocalTime startTime() throws Exception {
        return ((BatteryCI) this.offering).startTime();
    }

    /**
     * @see interfaces.PlanningEquipmentControlCI#duration()
     */
    @Override
    public Duration duration() throws Exception {
        return ((BatteryCI) this.offering).duration();
    }

    /**
     * @see interfaces.PlanningEquipmentControlCI#deadline()
     */
    @Override
    public LocalTime deadline() throws Exception {
        return ((BatteryCI) this.offering).deadline();
    }

    /**
     * @see interfaces.PlanningEquipmentControlCI#postpone(Duration)
     */
    @Override
    public boolean postpone(Duration d) throws Exception {
        return ((BatteryCI) this.offering).postpone(d);
    }

    /**
     * @see interfaces.PlanningEquipmentControlCI#cancel()
     */
    @Override
    public boolean cancel() throws Exception {
        return ((BatteryCI) this.offering).cancel();
    }

    @Override
    public boolean planifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception {
        return ((BatteryCI) this.offering).planifyEvent(durationLastPlanned, deadline);
    }

}
