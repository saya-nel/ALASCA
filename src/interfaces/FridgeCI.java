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
    public float getRequestedTemperature() throws Exception;

    /**
	 * @see interfaces.FridgeImplementationI#setRequestedTemperature(float)
	 */
    public void setRequestedTemperature(float temp) throws Exception;

    /**
	 * @see interfaces.FridgeImplementationI#getCurrentTemperature()
	 */
    public float getCurrentTemperature() throws Exception;
    
    /**
     * @see interfaces.StandardEquipmentControlCI#upMode()
     */
	public boolean upMode();

	/**
     * @see interfaces.StandardEquipmentControlCI#downMode()
     */
	public boolean downMode();

	/**
     * @see interfaces.StandardEquipmentControlCI#setMode(int)
     */
	public boolean setMode(int modeIndex);

	/**
     * @see interfaces.StandardEquipmentControlCI#currentMode()
     */
	public int currentMode();
    
    /**
     * @see interfaces.SuspensionEquipmentControlCI#suspended()
     */
	public boolean suspended();

	/**
     * @see interfaces.SuspensionEquipmentControlCI#suspend()
     */
	public boolean suspend();

	/**
     * @see interfaces.SuspensionEquipmentControlCI#resume()
     */
	public boolean resume();

	/**
     * @see interfaces.SuspensionEquipmentControlCI#emergency()
     */
	public double emergency();

}
