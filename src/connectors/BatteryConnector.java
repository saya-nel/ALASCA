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

	/**
	 * @see interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		return ((BatteryCI) this.offering).getBatteryCharge();
	}

	/**
	 * @see interfaces.BatteryImplementationI#getBatteryState()
	 */
	@Override
	public BatteryState getBatteryState() throws Exception {
		return ((BatteryCI) this.offering).getBatteryState();
	}

	/**
	 * @see interfaces.BatteryImplementationI#setBatteryState(BatteryState)
	 */
	@Override
	public void setBatteryState(BatteryState state) throws Exception {
		((BatteryCI) this.offering).setBatteryState(state);
	}
}
