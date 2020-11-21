package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.FridgeCI;
import interfaces.FridgeImplementationI;

/**
 * Outbound port of Fridge component
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
	 * @see FridgeImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		return ((FridgeCI) this.getConnector()).getRequestedTemperature();
	}

	/**
	 * @see FridgeImplementationI#setRequestedTemperature(float)
	 */
	@Override
	public void setRequestedTemperature(float temp) throws Exception {
		((FridgeCI) this.getConnector()).setRequestedTemperature(temp);
	}

	/**
	 * @see FridgeImplementationI#getCurrentTemperature()
	 */
	@Override
	public float getCurrentTemperature() throws Exception {
		return ((FridgeCI) this.getConnector()).getCurrentTemperature();
	}

	/**
	 * @see FridgeImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((FridgeCI) this.getConnector()).upMode();
	}

	/**
	 * @see FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((FridgeCI) this.getConnector()).downMode();
	}

	/**
	 * @see FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((FridgeCI) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see FridgeImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((FridgeCI) this.getConnector()).currentMode();
	}

	/**
	 * @see FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		return ((FridgeCI) this.getConnector()).suspended();
	}

	/**
	 * @see FridgeImplementationI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception {
		return ((FridgeCI) this.getConnector()).suspend();
	}

	/**
	 * @see FridgeImplementationI#resume()
	 */
	@Override
	public boolean resume() throws Exception {
		return ((FridgeCI) this.getConnector()).resume();
	}

	/**
	 * @see FridgeImplementationI#emergency()
	 */
	@Override
	public double emergency() throws Exception {
		return ((FridgeCI) this.getConnector()).emergency();
	}
}
