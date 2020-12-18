package main.java.simulation.battery;
/**
 *
 * @author Bello Memmi
 *
 * Model of User battery
 */

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import main.java.simulation.battery.events.AbstractBatteryEvent;

public class BatteryUser_MILModel extends AtomicModel {

    /** time interval between event outputs */
    protected static final double STEP = 20.0;
    /** the current event being output */
    protected AbstractBatteryEvent currentEvent;


}
