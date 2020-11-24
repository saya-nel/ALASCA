package main.java.interfaces;

import java.util.Map;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ControllerCI extends ControllerImplementationI, OfferedCI, RequiredCI {

	/**
	 * @see interfaces.ControllerImplementationI#register(String, String)
	 */
	@Override
	boolean register(String serial_number, String InboundPortURI, String XMLFile) throws Exception;

	/**
	 * @see interfaces.ControllerImplementationI#getRegisteredDevices()
	 */
	@Override
	Map<String, String> getRegisteredDevices() throws Exception;
}
