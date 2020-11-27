package main.java.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.interfaces.FridgeCI;

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
	 * @see main.java.interfaces.FridgeImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		return ((FridgeCI) this.getConnector()).getRequestedTemperature();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#setRequestedTemperature(float)
	 */
	@Override
	public void setRequestedTemperature(float temp) throws Exception {
		((FridgeCI) this.getConnector()).setRequestedTemperature(temp);
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#getCurrentTemperature()
	 */
	@Override
	public float getCurrentTemperature() throws Exception {
		return ((FridgeCI) this.getConnector()).getCurrentTemperature();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((FridgeCI) this.getConnector()).upMode();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((FridgeCI) this.getConnector()).downMode();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((FridgeCI) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((FridgeCI) this.getConnector()).currentMode();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		return ((FridgeCI) this.getConnector()).suspended();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception {
		return ((FridgeCI) this.getConnector()).suspend();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#resume()
	 */
	@Override
	public boolean resume() throws Exception {
		return ((FridgeCI) this.getConnector()).resume();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#emergency()
	 */
	@Override
	public double emergency() throws Exception {
		return ((FridgeCI) this.getConnector()).emergency();
	}
}
