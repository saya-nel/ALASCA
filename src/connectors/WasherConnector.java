package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.WasherCI;
import interfaces.WasherImplementationI;

public class WasherConnector extends AbstractConnector implements WasherCI {
    /**
     * @see WasherImplementationI#getStateWasher()
     */
    @Override
    public boolean getStateWasher() throws Exception {
        return ((WasherCI) this.offering).getStateWasher();
    }

    /**
     * @see WasherImplementationI#turnOnWasher(int)
     */
    @Override
    public void turnOnWasher(int operating_temperature) throws Exception {
        ((WasherCI) this.offering).turnOnWasher(operating_temperature);
    }

    /**
     * @see WasherImplementationI#turnOffWasher()
     */
    @Override
    public void turnOffWasher() throws Exception {
        ((WasherCI) this.offering).turnOffWasher();
    }

    /**
     * @see WasherImplementationI#getOperatingTemperature()
     */
    @Override
    public int getOperatingTemperature() throws Exception {
        return ((WasherCI) this.offering).getOperatingTemperature();
    }
}
