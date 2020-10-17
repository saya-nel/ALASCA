package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.BatteryCI;
import utils.BatteryState;

/**
 * Connector for the BatteryCI component interface
 * 
 * @author Bello Memmi
 *
 */
public class BatteryConnector extends AbstractConnector implements BatteryCI {

    @Override
    public float readBatteryCharge() throws Exception {
        return ((BatteryCI) this.offering).readBatteryCharge();
    }

    @Override
    public BatteryState getBatteryState() throws Exception {
        return ((BatteryCI) this.offering).getBatteryState();
    }

    @Override
    public void setBatteryState(BatteryState state) throws Exception {
        ((BatteryCI) this.offering).setBatteryState(state);
    }
}
