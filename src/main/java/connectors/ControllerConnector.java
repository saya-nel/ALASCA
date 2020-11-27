package main.java.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.interfaces.ControllerCI;

/**
 * Connector for the ControllerCI component interface
 *
 * @author Bello Memmi
 */
public class ControllerConnector extends AbstractConnector implements ControllerCI {

	/**
	 * @see main.java.interfaces.ControllerCI#register(String, String)
	 */
	@Override
	public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception {
		return ((ControllerCI) this.offering).register(serial_number, inboundPortURI, XMLFile);
	}

}
