package main.java.components.fridge.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * The component interface <code>FridgeSensorCI</code> declares the method
 * proposed as sensor in a fridge.
 *
 * @author Bello Memmi
 */
public interface FridgeSensorCI extends OfferedCI, RequiredCI {
	/**
	 * return the temperature in the fridge.
	 *
	 * Should return a better representation of a sensor data (with time, etc.).
	 *
	 */
	public double getContentTemperatureInCelsius() throws Exception;
}
