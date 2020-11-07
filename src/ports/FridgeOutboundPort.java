package ports;


/**
 * Outbound port of Fridge component
 *
 * @author Bello Memmi
 *
 */

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.FridgeCI;
import interfaces.FridgeImplementationI;


public class FridgeOutboundPort extends AbstractOutboundPort implements FridgeCI {
    /**
     * Constructor of Fridge inbound port
     * @param owner         owner of the component
     * @throws Exception
     */
    public FridgeOutboundPort(ComponentI owner) throws Exception {
        super(FridgeCI.class, owner);
    }

    /**
     * @see FridgeImplementationI#getRequestedTemperature() 
     */
    @Override
    public float getRequestedTemperature() throws Exception {
        return((FridgeCI) this.getConnector()).getRequestedTemperature();
    }

    /**
     * @see FridgeImplementationI#setRequestedTemperature(float)
     */
    @Override
    public void setRequestedTemperature(float temp) throws Exception {
        ((FridgeCI) this.getConnector()).setRequestedTemperature(temp);
    }

    /**
     * @see FridgeImplementationI#getCurrentTemperature()
     */
    @Override
    public float getCurrentTemperature() throws Exception {
        return ((FridgeCI) this.getConnector()).getCurrentTemperature();
    }

    /**
     * @see FridgeImplementationI#switchOff()
     */
    @Override
    public void switchOff() throws Exception {
        ((FridgeCI) this.getConnector()).switchOff();
    }

    /**
     * @see FridgeImplementationI#switchOn()
     */
    @Override
    public void switchOn() throws Exception {
        ((FridgeCI) this.getConnector()).switchOn();
    }

    /**
     * @see FridgeImplementationI#getState()
     */
    @Override
    public boolean getState() throws Exception {
        return ((FridgeCI) this.getConnector()).getState();
    }
}
