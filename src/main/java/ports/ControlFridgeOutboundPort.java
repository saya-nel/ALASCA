package main.java.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.connectors.ControlFridgeConnector;
import main.java.interfaces.FridgeCI;
import main.java.interfaces.FridgeImplementationI;

/**
 * Outbound port of Fridge component
 *
 * @author Bello Memmi
 *
 */
public class ControlFridgeOutboundPort extends AbstractOutboundPort implements FridgeCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of Fridge inbound port
	 * 
	 * @param owner owner of the component
	 * @throws Exception
	 */
	public ControlFridgeOutboundPort(ComponentI owner) throws Exception {
		super(FridgeCI.class, owner);
	}

	/**
	 * @see FridgeImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		return ((ControlFridgeConnector) this.getConnector()).getRequestedTemperature();
	}

	/**
	 * @see FridgeImplementationI#setRequestedTemperature(float)
	 */
	@Override
	public void setRequestedTemperature(float temp) throws Exception {
		((ControlFridgeConnector) this.getConnector()).setRequestedTemperature(temp);
	}

	/**
	 * @see FridgeImplementationI#getCurrentTemperature()
	 */
	@Override
	public float getCurrentTemperature() throws Exception {
		return ((ControlFridgeConnector) this.getConnector()).getCurrentTemperature();
	}

	/**
	 * @see FridgeImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((ControlFridgeConnector) this.getConnector()).upMode();
	}

	/**
	 * @see FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((ControlFridgeConnector) this.getConnector()).downMode();
	}

	/**
	 * @see FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((ControlFridgeConnector) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see FridgeImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((ControlFridgeConnector) this.getConnector()).currentMode();
	}

	/**
	 * @see FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		return ((ControlFridgeConnector) this.getConnector()).suspended();
	}

	/**
	 * @see FridgeImplementationI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception {
		return ((ControlFridgeConnector) this.getConnector()).suspend();
	}

	/**
	 * @see FridgeImplementationI#resume()
	 */
	@Override
	public boolean resume() throws Exception {
		return ((ControlFridgeConnector) this.getConnector()).resume();
	}

	/**
	 * @see FridgeImplementationI#emergency()
	 */
	@Override
	public double emergency() throws Exception {
		return ((ControlFridgeConnector) this.getConnector()).emergency();
	}
}
