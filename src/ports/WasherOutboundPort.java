package ports;

import components.Washer;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.WasherCI;
import interfaces.WasherImplementationI;

/**
 *
 * Outbound port of Washer component
 *
 * @author Bello Memmi
 *
 */
public class WasherOutboundPort extends AbstractOutboundPort implements WasherCI {
    public WasherOutboundPort(ComponentI owner) throws Exception {
        super(WasherCI.class, owner);
    }

    /**
     * @see WasherImplementationI#getStateWasher()
     */
    @Override
    public boolean getStateWasher() throws Exception {
        return ((WasherCI) this.getConnector()).getStateWasher();
    }

    /**
     * @see WasherImplementationI#turnOnWasher(int)
     */
    @Override
    public void turnOnWasher(int operating_temperature) throws Exception {
        ((WasherCI) this.getConnector()).turnOnWasher(operating_temperature);
    }

    /**
     * @see WasherImplementationI#turnOffWasher()
     */
    @Override
    public void turnOffWasher() throws Exception {
        ((WasherCI) this.getConnector()).turnOffWasher();
    }

    /**
     * @see WasherImplementationI#getOperatingTemperature()
     */
    @Override
    public int getOperatingTemperature() throws Exception {
        return ((WasherCI) this.getConnector()).getOperatingTemperature();
    }
}
