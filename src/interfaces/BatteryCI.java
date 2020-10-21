package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import utils.BatteryState;

/**
 * 
 * Battery component interface
 * 
 * @author Bello Memmi
 *
 */
public interface BatteryCI extends BatteryImplementationI, OfferedCI, RequiredCI {

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
	 * @see interfaces.BatteryImplementationI#getMaximumEnergy()
	 */
	@Override
	public float getMaximumEnergy() throws Exception;

	/**
	 * @see interfaces.BatteryImplementationI#setBatteryState(BatteryState)
	 */
	@Override
	public void setBatteryState(BatteryState state) throws Exception;

	/**
	 * @see interfaces.BatteryImplementationI#takeEnergy(float)
	 */
	@Override
	public float takeEnergy(float toTake) throws Exception;

	/**
	 * @see interfaces.BatteryImplementationI#addEnergy(float)
	 */
	@Override
	public void addEnergy(float toAdd) throws Exception;

}
