package interfaces;

/**
 * Methods of fridge component
 *
 * @author Bello Memmi
 *
 */
public interface FridgeImplementationI {
	
    /**
     * @return              requested temperature of fridge
     * @throws Exception
     */
    public float getRequestedTemperature() throws Exception;

    /**
     * set the temperature requested
     * @param temp          temperature of the fridge aimed
     * @throws Exception
     */
    public void setRequestedTemperature(float temp) throws Exception;

    /**
     *
     * @return              current temperature inside the fridge
     * @throws Exception
     */
    public float getCurrentTemperature() throws Exception;
    
    /*
     * 
     * STANDARD METHODS
     * 
     */
    
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

    /*
     * 
     *  SUSPENSION
     * 
     */
    
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
