package main.java.components.fridge.interfaces;

/**
 *
 * @author Bello Memmi
 *
 */

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface FridgeCI extends FridgeImplementationI, OfferedCI, RequiredCI {

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.SuspensionEquipmentControlCI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.SuspensionEquipmentControlCI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.SuspensionEquipmentControlCI#resume()
	 */
	@Override
	public boolean resume() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.SuspensionEquipmentControlCI#emergency()
	 */
	@Override
	public double emergency() throws Exception;

}
