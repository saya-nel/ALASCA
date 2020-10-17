package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import utils.BatteryState;

/**
 * 
 * Battery component interface
 * 
 * @author Bello Memmi
 *
 */
public interface BatteryCI extends BatteryImplementationI, OfferedCI {
    /**
     *
     * @return              battery charge (mA/h)
     * @throws Exception
     */
    public float readBatteryCharge() throws Exception;

    /**
     *
     * @return              State of battery
     * @throws Exception
     */
    public BatteryState getBatteryState() throws Exception;

    /**
     *
     * @param state         of battery DRAINING RECHARGING or SLEEPING
     * @throws Exception
     */
    public void setBatteryState(BatteryState state) throws Exception;
}
