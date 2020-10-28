package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ControllerCI;
import interfaces.ControllerImplementationI;

import java.util.Map;

/**
 * Connector for the ControllerCI component interface
 *
 * @author Bello Memmi
 */
public class ControllerConnector extends AbstractConnector implements ControllerCI {

    /**
     * @see interfaces.ControllerCI#register(String, String)
     */
    @Override
    public void register(String serial_number, String XMLFile) throws Exception {
         ((ControllerCI) this.offering).register(serial_number, XMLFile);
    }

    /**
     * @see ControllerImplementationI#getRegisteredDevices()
     */
    @Override
    public Map<String, String> getRegisteredDevices() throws Exception {
        return ((ControllerCI) this.offering).getRegisteredDevices();
    }
}
