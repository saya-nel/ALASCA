package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.FridgeCI;

/**
 * Connector for the FridgeCI component interface
 *
 * @author Bello Memmi
 *
 */
public class FridgeConnector extends AbstractConnector implements FridgeCI {

    @Override
    public float getRequestedTemperature() throws Exception {
        return ((FridgeCI) this.offering).getRequestedTemperature();
    }

    @Override
    public void setRequestedTemperature(float temp) throws Exception {
        ((FridgeCI) this.offering).setRequestedTemperature(temp);
    }

    @Override
    public float getCurrentTemperature() throws Exception {
        return ((FridgeCI) this.offering).getCurrentTemperature();
    }

    @Override
    public void switchOff() throws Exception {
        ((FridgeCI) this.offering).switchOff();
    }

    @Override
    public void switchOn() throws Exception {
        ((FridgeCI) this.offering).switchOn();
    }

    @Override
    public boolean getState() throws Exception {
        return ((FridgeCI) this.offering).getState();
    }
}
