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
	public float getBatteryCharge() throws Exception;

	/**
	 * return the actual battery state
	 * 
	 * @return the battery state
	 * @throws Exception
	 */
	public BatteryState getBatteryState() throws Exception;

	/**
	 * return the maximum energy of the battery
	 */
	public float getMaximumEnergy() throws Exception;

	/**
	 * update the battery state
	 * 
	 * @param state new state
	 * @throws Exception
	 */
	public void setBatteryState(BatteryState state) throws Exception;

	// TODO ELEVER

	/**
	 * 
	 * @param toTake energy to take from Battery
	 * @return taken energy
	 * @throws Exception
	 */
	public float takeEnergy(float toTake) throws Exception;

	/**
	 * 
	 * @param toTake energy to add to Battery
	 * @throws Exception
	 */
	public void addEnergy(float toAdd) throws Exception;

}
