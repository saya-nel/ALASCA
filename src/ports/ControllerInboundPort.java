package ports;

import java.util.Map;

import components.Controller;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ControllerCI;

public class ControllerInboundPort extends AbstractInboundPort implements ControllerCI {
	public ControllerInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ControllerCI.class, owner);
	}

	@Override
	public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Controller) owner).register(serial_number, inboundPortURI, XMLFile));
	}

	@Override
	public Map<String, String> getRegisteredDevices() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Controller) owner).getRegisteredDevices());
	}
}
