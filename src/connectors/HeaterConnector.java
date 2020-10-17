package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.HeaterCI;

/**
 * Connector for the HeaterCI component interface
 * 
 * @author Bello Memmi
 *
 */
public class HeaterConnector extends AbstractConnector implements HeaterCI {

    /**
     * @see HeaterCI
     */
    @Override
    public float getRequestedTemperature() throws Exception {
        return ((HeaterCI) this.offering).getRequestedTemperature() ;
    }

    /**
     * @see HeaterCI
     */
    @Override
    public void turnOn() throws Exception {
        ((HeaterCI) this.offering).turnOn();
    }
    /**
     * @see HeaterCI
     */
    @Override
    public void turnOff() throws Exception {
        ((HeaterCI) this.offering).turnOff();
    }
    /**
     * @see HeaterCI
     */
    @Override
    public boolean heaterIsOn() throws Exception {
        return ((HeaterCI) this.offering).heaterIsOn();
    }
}
