package main.java.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.interfaces.FridgeCI;

/**
 * Connector for the FridgeCI component interface
 * 
 * @author Bello Memmi
 *
 */
public class FridgeConnector extends AbstractConnector implements FridgeCI {

	/**
	 * @see main.java.interfaces.FridgeImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		return ((FridgeCI) this.offering).getRequestedTemperature();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#setRequestedTemperature(float)
	 */
	@Override
	public void setRequestedTemperature(float temp) throws Exception {
		((FridgeCI) this.offering).setRequestedTemperature(temp);
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#getCurrentTemperature()
	 */
	@Override
	public float getCurrentTemperature() throws Exception {
		return ((FridgeCI) this.offering).getCurrentTemperature();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((FridgeCI) this.offering).upMode();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((FridgeCI) this.offering).downMode();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((FridgeCI) this.offering).setMode(modeIndex);
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((FridgeCI) this.offering).currentMode();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		return ((FridgeCI) this.offering).suspended();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception {
		return ((FridgeCI) this.offering).suspend();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#resume()
	 */
	@Override
	public boolean resume() throws Exception {
		return ((FridgeCI) this.offering).resume();
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#emergency()
	 */
	@Override
	public double emergency() throws Exception {
		return ((FridgeCI) this.offering).emergency();
	}

}
