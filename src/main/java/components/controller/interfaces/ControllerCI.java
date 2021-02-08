package main.java.components.controller.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import main.java.components.controller.Controller;

/**
 * 
 * The component interface <code>ControllerCI</code> defines the services a
 * {@link Controller} component offers and that can be required from it.
 * 
 * @author Bello Memmi
 *
 */
public interface ControllerCI extends ControllerImplementationI, OfferedCI, RequiredCI {

	/**
	 * @see main.java.components.controller.interfaces.ControllerImplementationI#register(String,
	 *      String)
	 */
	@Override
	boolean register(String serial_number, String InboundPortURI, String XMLFile) throws Exception;

}
