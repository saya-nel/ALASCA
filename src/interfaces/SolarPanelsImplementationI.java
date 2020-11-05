package interfaces;

/**
 * 
 * SolarPanels services interface
 * 
 * @author Bello Memmi
 *
 */
public interface SolarPanelsImplementationI {

	/**
	 * Turn the solar panels on
	 * 
	 * @throws Exception
	 */
	public void turnOn() throws Exception;

	/**
	 * Turn the solar panels off
	 * 
	 * @throws Exception
	 */
	public void turnOff() throws Exception;

	/**
	 * Return true if the solar panels are turned on, false else
	 * 
	 * @return true if the solar panels are turned on, false else
	 * @throws Exception
	 */
	public boolean isTurnedOn() throws Exception;

}
