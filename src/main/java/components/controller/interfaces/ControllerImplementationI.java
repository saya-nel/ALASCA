package main.java.components.controller.interfaces;

import main.java.components.controller.Controller;

/**
 * 
 * The interface <code>ControllerImplementationI</code> defines the service that
 * must be implemented by the {@link Controller} component.
 * 
 * @author Bello Memmi
 *
 */
public interface ControllerImplementationI {
	/**
	 *
	 * @param serial_number  of the device that has to register in Controller
	 * @param inboundPortURI of the device that has to register in Controller
	 * @param XMLFile        describing offered control and how to connect required
	 *                       interfaces and offered interfaces
	 * 
	 * @return true if the device has been registrated, false else
	 * @throws Exception
	 */
	public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception;

}
