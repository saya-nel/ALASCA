package interfaces;

/**
 *
 * Washer services interfaces
 *
 * @author Bello Memmi
 *
 */
public interface WasherImplementationI {

	/**
	 * @return true if the washer is currently working, false else
	 * @throws Exception
	 */
	public boolean isTurnedOn() throws Exception;

	/**
	 * Turn the washer on
	 * 
	 * @throws Exception
	 */
	public void turnOn() throws Exception;

	/**
	 * Turn the washer off
	 * 
	 * @throws Exception
	 */
	public void turnOff() throws Exception;

	/**
	 * Change the temperature of the program
	 * 
	 * @param temperature the new temperature of the program
	 * @throws Exception
	 */
	public void setProgramTemperature(int temperature) throws Exception;

	/**
	 * Return the temperature of the program
	 * 
	 * @return the temperature of the program
	 * @throws Exception
	 */
	public int getProgramTemperature() throws Exception;

	/**
	 * Change the duration of the program
	 * 
	 * @param duration duration of the program
	 * @throws Exception
	 */
	public void setProgramDuration(int duration) throws Exception;

	/**
	 * Return the duration of the program
	 * 
	 * @return duration of the program
	 * @throws Exception
	 */
	public int getProgramDuration() throws Exception;

}
