package interfaces;

/**
 *
 * Washer services interfaces
 *
 * @author Bello Memmi
 *
 */
public interface WasherImplementationI {

    /**
     * @return                  true if the washer is currently working
     * @throws Exception
     */
    public boolean getStateWasher() throws Exception;

    /**
     *  Change the state of the washer and the operating _temperature
     * @param operating_temperature             of the wash
     * @throws Exception
     */
    public void turnOnWasher(int operating_temperature) throws Exception;

    /**
     *  Change the state of the washer and turn it off
     * @throws Exception
     */
    public void turnOffWasher() throws Exception;

    /**
     *
     * @return              washer's operating temperature of water
     * @throws Exception
     */
    public int getOperatingTemperature() throws Exception;


}
