package main.java.components.fridge.interfaces;

/**
 * The interface <code>FridgeImplementationI</code> defines the operations to be
 * implemented to control the fridge and its operation mode.
 *
 *
 * @author Bello Memmi
 *
 */
public interface FridgeImplementationI {

	/*
	 * 
	 * STANDARD METHODS
	 * 
	 */

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#upMode()
	 */
	public boolean upMode() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#downMode()
	 */
	public boolean downMode() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	public boolean setMode(int modeIndex) throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#currentMode()
	 */
	public int currentMode() throws Exception;

	/*
	 * 
	 * SUSPENSION
	 * 
	 */

	/**
	 * @see main.java.components.controller.interfaces.SuspensionEquipmentControlCI#suspended()
	 */
	public boolean suspended() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.SuspensionEquipmentControlCI#suspend()
	 */
	public boolean suspend() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.SuspensionEquipmentControlCI#resume()
	 */
	public boolean resume() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.SuspensionEquipmentControlCI#emergency()
	 */
	public double emergency() throws Exception;
}
