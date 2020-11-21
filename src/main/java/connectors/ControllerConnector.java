package connectors;

import java.util.Map;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ControllerCI;
import interfaces.ControllerImplementationI;

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
	public boolean register(String serial_number,  String inboundPortURI, String XMLFile) throws Exception {
		return ((ControllerCI) this.offering).register(serial_number, inboundPortURI, XMLFile);
	}

	/**
	 * @see ControllerImplementationI#getRegisteredDevices()
	 */
	@Override
	public Map<String, String> getRegisteredDevices() throws Exception {
		return ((ControllerCI) this.offering).getRegisteredDevices();
	}
}
