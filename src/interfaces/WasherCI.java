package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 *
 * Washer component interface
 *
 * @author Bello Memmi
 *
 */
public interface WasherCI extends WasherImplementationI, RequiredCI, OfferedCI {
    /**
     * @see WasherImplementationI#getStateWasher()
     */
    @Override
    public boolean getStateWasher() throws Exception;

    /**
     * @see WasherImplementationI#turnOnWasher(int)
     */
    @Override
    public void turnOnWasher(int operating_temperature) throws Exception;

    /**
     * @see WasherImplementationI#turnOffWasher()
     */
    @Override
    public void turnOffWasher() throws Exception;

    /**
     * @see WasherImplementationI#getOperatingTemperature()
     */
    @Override
    public int getOperatingTemperature() throws Exception;


}
