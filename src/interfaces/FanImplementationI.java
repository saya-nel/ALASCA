package interfaces;

import utils.FanLevel;

/**
 * 
 * Fan services interface
 * 
 * @author Bello Memmy
 *
 */
public interface FanImplementationI {

	/**
	 * Turn on the fan
	 * 
	 * @throws Exception
	 */
	void turnOn() throws Exception;

	/**
	 * Turn off the fan
	 * 
	 * @throws Exception
	 */
	void turnOff() throws Exception;

	/**
	 * Adjust the power of the fan
	 * 
	 * @param level desired power
	 * @throws Exception
	 */
	void adjustPower(FanLevel level) throws Exception;

	/**
	 * Says if the fan is switch on or off
	 * 
	 * @return true if on, false else
	 * @throws Exception
	 */
	boolean isTurnedOn() throws Exception;

	/**
	 * Return the current level of the fan
	 * 
	 * @return the fan current level
	 * @throws Exception
	 */
	FanLevel getFanLevel() throws Exception;

}
