package interfaces;

/**
 * 
 * Heater services interface
 * 
 * @author Bello Memmi
 *
 */
public interface HeaterImplementationI {

	/**
	 * Return the requested temperature
	 * 
	 * @return the requested temperature
	 * @throws Exception
	 */
	float getRequestedTemperature() throws Exception;

	/**
	 * Change the requested temperature
	 * 
	 * @param requestedTemperature the new requested temperature
	 * @throws Exception
	 */
	void setRequestedTemperature(float requestedTemperature) throws Exception;

	/**
	 * Turn on the heater
	 * 
	 * @throws Exception
	 */
	void turnOn() throws Exception;

	/**
	 * Turn off the heater
	 * 
	 * @throws Exception
	 */
	void turnOff() throws Exception;

	/**
	 * Return if the heater is turned on or not
	 * 
	 * @return true if the heater is turned on, false else
	 * @throws Exception
	 */
	boolean isHeaterOn() throws Exception;

}
