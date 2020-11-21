package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.FridgeCI;
import interfaces.SuspensionEquipmentControlCI;

/**
 * Connector between the Controller and the Fridge, TODO : this class should be
 * auto-generated in the future.
 * 
 * @author Bello Memmi
 *
 */
public class ControlFridgeConnector extends AbstractConnector implements SuspensionEquipmentControlCI {

	/**
	 * @see interfaces.FridgeImplementationI#getRequestedTemperature()
	 */
	public float getRequestedTemperature() throws Exception {
		return ((FridgeCI) this.offering).getRequestedTemperature();
	}

	/**
	 * @see interfaces.FridgeImplementationI#setRequestedTemperature(float)
	 */
	public void setRequestedTemperature(float temp) throws Exception {
		((FridgeCI) this.offering).setRequestedTemperature(temp);
	}

	/**
	 * @see interfaces.FridgeImplementationI#getCurrentTemperature()
	 */
	public float getCurrentTemperature() throws Exception {
		return ((FridgeCI) this.offering).getCurrentTemperature();
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#on()
	 */
	@Override
	public boolean on() throws Exception {
		return false;
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#off()
	 */
	@Override
	public boolean off() throws Exception {
		return false;
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((FridgeCI) this.offering).upMode();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((FridgeCI) this.offering).downMode();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((FridgeCI) this.offering).setMode(modeIndex);
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((FridgeCI) this.offering).currentMode();
	}

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		return ((FridgeCI) this.offering).upMode();
	}

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception {
		return ((FridgeCI) this.offering).suspend();
	}

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#resume()
	 */
	@Override
	public boolean resume() throws Exception {
		return ((FridgeCI) this.offering).resume();
	}

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#emergency()
	 */
	@Override
	public double emergency() throws Exception {
		return ((FridgeCI) this.offering).emergency();
	}

}
