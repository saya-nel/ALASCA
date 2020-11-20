package ports;
/**
 * Inbound port of Fridge component
 *
 * @author Bello Memmi
 *
 */

import components.Fridge;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.FridgeCI;
import interfaces.FridgeImplementationI;

public class FridgeInboundPort extends AbstractInboundPort implements FridgeCI {


	private static final long serialVersionUID = 1L;

	public FridgeInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, FridgeCI.class, owner);
    }

    /**
     * @see FridgeImplementationI#getCurrentTemperature()
     */
    @Override
    public float getRequestedTemperature() throws Exception {
        return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).getRequestedTemperature());
    }

    /**
     * @see FridgeImplementationI#setRequestedTemperature(float)
     */
    @Override
    public void setRequestedTemperature(float temp) throws Exception {
        this.getOwner().runTask(owner -> {
            try{
                ((Fridge) owner).setRequestedTemperature(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @see FridgeImplementationI#getCurrentTemperature()
     */
    @Override
    public float getCurrentTemperature() throws Exception {
        return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).getCurrentTemperature());
    }

    /**
     * @see FridgeImplementationI#upMode()
     */
	@Override
	public boolean upMode() {
		try {
			return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).upMode());
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).downMode());
		} catch (AssertionError | Exception e) {
			return false;
		}
	}

	/**
     * @see FridgeImplementationI#setMode(int)
     */
	@Override
	public boolean setMode(int modeIndex) {
		try {
			return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).setMode(modeIndex));
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).currentMode());
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).suspended());
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).suspend());
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).resume());
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).emergency());
		} catch (AssertionError | Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
