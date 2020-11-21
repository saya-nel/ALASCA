package ports;

import java.util.Map;

import components.Controller;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ControllerCI;

public class ControllerInboundPort extends AbstractInboundPort implements ControllerCI {

	private static final long serialVersionUID = 1L;

	protected final int executorIndex;

	public ControllerInboundPort(String uri, int executorIndex, ComponentI owner) throws Exception {
		super(uri, ControllerCI.class, owner);
		this.executorIndex = executorIndex;
	}

	@Override
	public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception {
		return this.getOwner().handleRequestSync(executorIndex,
				owner -> ((Controller) owner).register(serial_number, inboundPortURI, XMLFile));
	}

	@Override
	public Map<String, String> getRegisteredDevices() throws Exception {
		return this.getOwner().handleRequestSync(executorIndex, owner -> ((Controller) owner).getRegisteredDevices());
	}
}
