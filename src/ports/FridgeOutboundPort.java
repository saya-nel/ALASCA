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
     * @param owner         owner of the component
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
        return((FridgeCI) this.getConnector()).getRequestedTemperature();
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
	public boolean upMode() {
		try {
			return ((FridgeCI) this.getConnector()).upMode();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
     * @see FridgeImplementationI#downMode()
     */
	@Override
	public boolean downMode() {
		try {
			return ((FridgeCI) this.getConnector()).downMode();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
     * @see FridgeImplementationI#setMode(int)
     */
	@Override
	public boolean setMode(int modeIndex) {
		try {
			return ((FridgeCI) this.getConnector()).setMode(modeIndex);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
     * @see FridgeImplementationI#currentMode()
     */
	@Override
	public int currentMode() {
		try {
			return ((FridgeCI) this.getConnector()).currentMode();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
     * @see FridgeImplementationI#suspended()
     */
	@Override
	public boolean suspended() {
		try {
			return ((FridgeCI) this.getConnector()).suspended();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
     * @see FridgeImplementationI#suspend()
     */
	@Override
	public boolean suspend() {
		try {
			return ((FridgeCI) this.getConnector()).suspend();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
     * @see FridgeImplementationI#resume()
     */
	@Override
	public boolean resume() {
		try {
			return ((FridgeCI) this.getConnector()).resume();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
     * @see FridgeImplementationI#emergency()
     */
	@Override
	public double emergency() {
		try {
			return ((FridgeCI) this.getConnector()).emergency();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
