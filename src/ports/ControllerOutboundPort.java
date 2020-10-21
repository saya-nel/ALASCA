package ports;


import components.Controller;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ControllerCI;

public class ControllerOutboundPort extends AbstractOutboundPort implements ControllerCI {
    public ControllerOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ControllerCI.class, owner);
        assert uri != null && owner != null;
    }
    @Override
    public void register(String serial_number, String XMLFile) throws Exception {
        ((ControllerCI) this.connector).register(serial_number, XMLFile);
    }
}
