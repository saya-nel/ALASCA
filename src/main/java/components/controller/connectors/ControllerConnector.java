package main.java.components.controller.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.components.controller.interfaces.ControllerCI;

/**
 * The class <code>ControllerConnector</code> implements a connector for the
 * component interface <code>ControllerCI</code>.
 * 
 * @author Bello Memmi
 */
public class ControllerConnector extends AbstractConnector implements ControllerCI {

	/**
	 * @see main.java.components.controller.interfaces.ControllerCI#register(String,
	 *      String)
	 */
	@Override
	public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception {
		return ((ControllerCI) this.offering).register(serial_number, inboundPortURI, XMLFile);
	}

}
