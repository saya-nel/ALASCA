package main.java.components.fridge.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * The class <code>FridgeActuatorCI</code> declares the actuation services for a
 * fridge component.
 *
 * @author Bello Memmi
 */
public interface FridgeActuatorCI extends OfferedCI, RequiredCI {
	/**
	 * put the fridge in passive mode when it is in the right temp
	 * 
	 * @throws Exception
	 */
	public void startPassive() throws Exception;

	/**
	 * put the fridge in active mode when the temps are critical
	 * 
	 * @throws Exception
	 */
	public void stopPassive() throws Exception;
}
