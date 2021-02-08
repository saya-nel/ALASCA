package main.java.components.solarPanels.interfaces;

import main.java.components.solarPanels.SolarPanels;

/**
 * 
 * The interface <code>SolarPanelsImplementationI</code> defines the service
 * that must be implemented by the {@link SolarPanels} component.
 * 
 * @author Bello Memmi
 *
 */
public interface SolarPanelsImplementationI {

	// -------------------------------------------------------------------------
	// Component services signatures
	// -------------------------------------------------------------------------

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
