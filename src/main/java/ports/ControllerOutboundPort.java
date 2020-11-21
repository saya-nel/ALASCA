package ports;

/**
 * Controller outbound port
 */

import java.util.Map;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ControllerCI;

public class ControllerOutboundPort extends AbstractOutboundPort implements ControllerCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the controller outbound port
	 *
	 * @param owner owner component
	 * @throws Exception
	 */
	public ControllerOutboundPort(ComponentI owner) throws Exception {
		super(ControllerCI.class, owner);
		// super(uri, ControllerCI.class, owner);
		assert uri != null && owner != null;
	}

	public ControllerOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ControllerCI.class, owner);
		assert uri != null && owner != null;
	}

	/**
	 * @see interfaces.ControllerImplementationI#register(String, String)
	 */
	@Override
	public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception {
		return ((ControllerCI) this.getConnector()).register(serial_number, inboundPortURI, XMLFile);
	}

	/**
	 * @see interfaces.ControllerImplementationI#getRegisteredDevices()
	 */
	@Override
	public Map<String, String> getRegisteredDevices() throws Exception {
		return ((ControllerCI) this.getConnector()).getRegisteredDevices();
	}
}
