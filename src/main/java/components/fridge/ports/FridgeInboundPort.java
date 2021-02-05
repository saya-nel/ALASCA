package main.java.components.fridge.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.fridge.Fridge;
import main.java.components.fridge.interfaces.FridgeCI;

/**
 * Fridge inbound port for the fridge component interface
 * 
 * @author Bello Memmi
 *
 */
public class FridgeInboundPort extends AbstractInboundPort implements FridgeCI {

	private static final long serialVersionUID = 1L;

	/**
	 * FridgeInboundPort constructor
	 * 
	 * @param uri   reflexion uri of the port
	 * @param owner owner component
	 * @throws Exception
	 */
	public FridgeInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, FridgeCI.class, owner);
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#getCurrentTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).getRequestedTemperature());
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#setRequestedTemperature(float)
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
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#getCurrentTemperature()
	 */
	@Override
	public float getCurrentTemperature() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).getCurrentTemperature());
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).upMode());
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).downMode());
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).setMode(modeIndex));
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).currentMode());
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).suspended());
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).suspend());
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#resume()
	 */
	@Override
	public boolean resume() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).resume());
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#emergency()
	 */
	@Override
	public double emergency() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).emergency());
	}
}
