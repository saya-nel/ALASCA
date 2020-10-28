package ports;
/**
 * Controller outbound port
 */

import components.Controller;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ControllerCI;

import java.util.Map;

public class ControllerOutboundPort extends AbstractOutboundPort implements ControllerCI {
    /**
     *  Constructor of the controller outbound port
     * @param uri           uri of the controller inbound port
     * @param owner         owner component
     * @throws Exception
     */
    public ControllerOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ControllerCI.class, owner);
        assert uri != null && owner != null;
    }

    /**
     * @see interfaces.ControllerImplementationI#register(String, String)
     */
    @Override
    public void register(String serial_number, String XMLFile) throws Exception {
        ((ControllerCI) this.connector).register(serial_number, XMLFile);
    }

    /**
     * @see interfaces.ControllerImplementationI#getRegisteredDevices()
     */
    @Override
    public Map<String, String> getRegisteredDevices() throws Exception {
        return ((ControllerCI) this.connector).getRegisteredDevices();
    }
}
