package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;

/**
 * 
 * Heater component interface
 * 
 * @author Bello Memmi
 *
 */
public interface HeaterCI extends HeaterImplementationI, OfferedCI {
    /**
     *
     * @return                  requested temperature
     * @throws Exception
     */
    public float getRequestedTemperature() throws Exception;


    /**
     * enables heater system
     * @throws Exception
     */
    public void turnOn() throws Exception;

    /**
     * disables heater system
     * @throws Exception
     */
    public void turnOff() throws Exception;

    /**
     *
     * @return                  true if the heater is enabled false otherwise
     * @throws Exception
     */
    public boolean heaterIsOn() throws Exception;


}
