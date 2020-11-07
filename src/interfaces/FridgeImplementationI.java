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

    /**
     * Switch off the fridge component
     * @throws Exception
     */
    public void switchOff() throws Exception;

    /**
     * Switch on the fridge component
     * @throws Exception
     */
    public void switchOn() throws Exception;

    /**
     *
     * @return              true if fridge is on
     * @throws Exception
     */
    public boolean getState() throws Exception;

}
