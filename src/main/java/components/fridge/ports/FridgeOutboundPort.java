package main.java.components.fridge.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.components.fridge.interfaces.FridgeCI;

/**
 * Outbound port of Fridge component interface
 *
 * @author Bello Memmi
 *
 */
public class FridgeOutboundPort extends AbstractOutboundPort implements FridgeCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of Fridge inbound port
	 * 
	 * @param owner owner of the component
	 * @throws Exception
	 */
	public FridgeOutboundPort(ComponentI owner) throws Exception {
		super(FridgeCI.class, owner);
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((FridgeCI) this.getConnector()).upMode();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((FridgeCI) this.getConnector()).downMode();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((FridgeCI) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((FridgeCI) this.getConnector()).currentMode();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		return ((FridgeCI) this.getConnector()).suspended();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception {
		return ((FridgeCI) this.getConnector()).suspend();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#resume()
	 */
	@Override
	public boolean resume() throws Exception {
		return ((FridgeCI) this.getConnector()).resume();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#emergency()
	 */
	@Override
	public double emergency() throws Exception {
		return ((FridgeCI) this.getConnector()).emergency();
	}
}
