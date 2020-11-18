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

    /**
     *
     * @return                  true if the fridge is currently active (not suspended)
     * @throws Exception
     */
    public boolean active() throws Exception;

    /**
     * pass the fridge in the active mode
     * @return                  true if the fridge has been successfully activated, false otherwise.
     * @throws Exception
     */
    public boolean activate() throws Exception;

    /**
     * pass the fridge in the passive mode
     * @return                  true if the fridge has been successfully passivated, false otherwise.
     * @throws Exception
     */
    public boolean passivate() throws Exception;

    /**
     * return the degree of emergency to reactivate the boiler after passivating it.
     * @return              the degree of emergency to reactivate the fridge after passivating it.
     * @throws Exception
     */
    public double degreeOfEmergency() throws Exception;
}
