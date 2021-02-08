package main.java.components.fan.interfaces;

import main.java.components.fan.Fan;
import main.java.components.fan.utils.FanLevel;

/**
 * 
 * The interface <code>FanImplementationI</code> defines the service that must
 * be implemented by the {@link Fan} component.
 * 
 * @author Bello Memmi
 *
 */
public interface FanImplementationI {

	// -------------------------------------------------------------------------
	// Component services signatures
	// -------------------------------------------------------------------------

	/**
	 * Turn on the fan
	 * 
	 * @throws Exception
	 */
	public void turnOn() throws Exception;

	/**
	 * Turn off the fan
	 * 
	 * @throws Exception
	 */
	public void turnOff() throws Exception;

	/**
	 * Adjust the power of the fan
	 * 
	 * @param level desired power
	 * @throws Exception
	 */
	public void adjustPower(FanLevel level) throws Exception;

	/**
	 * Says if the fan is switch on or off
	 * 
	 * @return true if on, false else
	 * @throws Exception
	 */
	public boolean isTurnedOn() throws Exception;

	/**
	 * Return the current level of the fan
	 * 
	 * @return the fan current level
	 * @throws Exception
	 */
	public FanLevel getFanLevel() throws Exception;

}
