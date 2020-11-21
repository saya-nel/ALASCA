package interfaces;

/**
 *
 * @author Bello Memmi
 *
 */

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface FridgeCI extends FridgeImplementationI, OfferedCI, RequiredCI {
	/**
	 * @see interfaces.FridgeImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception;

	/**
	 * @see interfaces.FridgeImplementationI#setRequestedTemperature(float)
	 */
	@Override
	public void setRequestedTemperature(float temp) throws Exception;

	/**
	 * @see interfaces.FridgeImplementationI#getCurrentTemperature()
	 */
	@Override
	public float getCurrentTemperature() throws Exception;

	/**
	 * @see interfaces.StandardEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception;

	/**
	 * @see interfaces.StandardEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception;

	/**
	 * @see interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception;

	/**
	 * @see interfaces.StandardEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception;

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception;

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception;

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#resume()
	 */
	@Override
	public boolean resume() throws Exception;

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#emergency()
	 */
	@Override
	public double emergency() throws Exception;

}
