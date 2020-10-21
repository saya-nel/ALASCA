package interfaces;

import utils.BatteryState;

/**
 * 
 * Battery services interface
 * 
 * @author Bello Memmi
 *
 */
public interface BatteryImplementationI {

	/**
	 * Return the actual battery charge
	 * 
	 * @return the actual battery charge
	 * @throws Exception
	 */
	float getBatteryCharge() throws Exception;

	/**
	 * return the actual battery state
	 * 
	 * @return the battery state
	 * @throws Exception
	 */
	BatteryState getBatteryState() throws Exception;

	/**
	 * update the battery state
	 * 
	 * @param state new state
	 * @throws Exception
	 */
	void setBatteryState(BatteryState state) throws Exception;

}
