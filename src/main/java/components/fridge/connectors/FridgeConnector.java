package main.java.components.fridge.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.components.fridge.interfaces.FridgeCI;

/**
 * Connector for the FridgeCI component interface
 * 
 * @author Bello Memmi
 *
 */
public class FridgeConnector extends AbstractConnector implements FridgeCI {

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((FridgeCI) this.offering).upMode();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((FridgeCI) this.offering).downMode();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((FridgeCI) this.offering).setMode(modeIndex);
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((FridgeCI) this.offering).currentMode();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		return ((FridgeCI) this.offering).suspended();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception {
		return ((FridgeCI) this.offering).suspend();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#resume()
	 */
	@Override
	public boolean resume() throws Exception {
		return ((FridgeCI) this.offering).resume();
	}

	/**
	 * @see main.java.components.fridge.interfaces.FridgeImplementationI#emergency()
	 */
	@Override
	public double emergency() throws Exception {
		return ((FridgeCI) this.offering).emergency();
	}

}
