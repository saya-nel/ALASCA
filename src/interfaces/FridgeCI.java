package interfaces;
/**
 *
 * @author Bello Memmi
 *
 */

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface FridgeCI extends FridgeImplementationI, RequiredCI, OfferedCI {

    /**
     * @see FridgeImplementationI#getRequestedTemperature()
     */
    @Override
    public float getRequestedTemperature() throws Exception;
    /**
     * @see FridgeImplementationI#setRequestedTemperature(float)
     */
    @Override
    public void setRequestedTemperature(float temp) throws Exception;

    /**
     * @see FridgeImplementationI#getCurrentTemperature()
     */
    @Override
    public float getCurrentTemperature() throws Exception;

    /**
     * @see FridgeImplementationI#switchOff()
     */
    @Override
    public void switchOff() throws Exception;

    /**
     * @see FridgeImplementationI#switchOn()
     */
    @Override
    public void switchOn() throws Exception;

    /**
     * @see FridgeImplementationI#getState()
     */
    @Override
    public boolean getState() throws Exception;

}
