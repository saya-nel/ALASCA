package ports;

import components.Controller;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ControllerCI;

import java.util.Map;

public class ControllerInboundPort extends AbstractInboundPort implements ControllerCI {
    public ControllerInboundPort(String uri, ComponentI owner) throws Exception
    {
        super(uri, ControllerCI.class, owner);
    }

    @Override
    public void register(String serial_number, String XMLFile) throws Exception {
        this.getOwner().runTask(owner -> {
            try {
                ((Controller) owner).register(serial_number, XMLFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Map<String, String> getRegisteredDevices() throws Exception {
        return this.getOwner().handleRequestSync(owner -> ((Controller) owner).getRegisteredDevices());
    }
}
