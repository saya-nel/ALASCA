package main.java.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.Controller;
import main.java.interfaces.ControllerCI;

/**
 * Controller inbound port for the controller component interface
 * 
 * @author Bello Memmi
 *
 */
public class ControllerInboundPort extends AbstractInboundPort implements ControllerCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Index of the thread pool for running services
	 */
	protected final int executorIndex;

	/**
	 * Constructor of the ControllerInboundPort
	 * 
	 * @param uri           reflexion uri of the port
	 * @param executorIndex index of the thread pool for running services
	 * @param owner         owner component
	 * @throws Exception
	 */
	public ControllerInboundPort(String uri, int executorIndex, ComponentI owner) throws Exception {
		super(uri, ControllerCI.class, owner);
		this.executorIndex = executorIndex;
	}

	/**
	 * @see main.java.interfaces.ControllerImplementationI#register(String, String,
	 *      String)
	 */
	@Override
	public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception {
		return this.getOwner().handleRequestSync(executorIndex,
				owner -> ((Controller) owner).register(serial_number, inboundPortURI, XMLFile));
	}

}
