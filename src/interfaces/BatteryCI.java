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
	 * @see interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception;

	/**
	 * @see interfaces.BatteryImplementationI#getBatteryState()
	 */
	@Override
	public BatteryState getBatteryState() throws Exception;

	/**
	 * @see interfaces.BatteryImplementationI#setBatteryState(BatteryState)
	 */
	@Override
	public void setBatteryState(BatteryState state) throws Exception;
}
