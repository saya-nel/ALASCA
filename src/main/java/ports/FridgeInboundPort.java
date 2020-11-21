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
			try {
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
	public boolean upMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).upMode());
	}

	/**
	 * @see FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).downMode());
	}

	/**
	 * @see FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).setMode(modeIndex));
	}

	/**
	 * @see FridgeImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).currentMode());
	}

	/**
	 * @see FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).suspended());
	}

	/**
	 * @see FridgeImplementationI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).suspend());
	}

	/**
	 * @see FridgeImplementationI#resume()
	 */
	@Override
	public boolean resume() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).resume());
	}

	/**
	 * @see FridgeImplementationI#emergency()
	 */
	@Override
	public double emergency() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).emergency());
	}
}
