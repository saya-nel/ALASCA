package main.java.components.controller.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.components.controller.interfaces.ControllerCI;

/**
 * The class <code>ControllerInboundPort</code> implements an inbound port for
 * the component interface <code>ControllerCI</code>.
 * 
 * @author Bello Memmi
 *
 */
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
	}

	/**
	 * Constructor of the controller outbound port
	 * 
	 * @param uri   reflexion uri of the port
	 * @param owner owner component
	 * @throws Exception
	 */
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

}
