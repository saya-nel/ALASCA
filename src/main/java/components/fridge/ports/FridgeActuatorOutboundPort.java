package main.java.components.fridge.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.components.fridge.interfaces.FridgeActuatorCI;

/**
 * The class <code>FridgeActuatorOutboundPort</code> implements an outbound port
 * for the <code>FridgeActuatorCI</code> component interface.
 * 
 * @author Bello Memmi
 *
 */
public class FridgeActuatorOutboundPort extends AbstractOutboundPort implements FridgeActuatorCI {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 * 
	 * @param owner owner component
	 * @throws Exception
	 */
	public FridgeActuatorOutboundPort(ComponentI owner) throws Exception {
		super(FridgeActuatorCI.class, owner);
	}

	/**
	 * @see FridgeActuatorCI#startPassive()
	 */
	@Override
	public void startPassive() throws Exception {
		((FridgeActuatorCI) this.getConnector()).startPassive();
	}

	/**
	 * @see FridgeActuatorCI#stopPassive()
	 */
	@Override
	public void stopPassive() throws Exception {
		((FridgeActuatorCI) this.getConnector()).stopPassive();
	}
}
