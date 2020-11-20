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
	
    public float getRequestedTemperature() throws Exception {
    	return ((FridgeCI)this.offering).getRequestedTemperature();
    }

	/**
	 * @see interfaces.StandardEquipmentControlCI#on()
	 */
	@Override
	public boolean on() {
		return false;
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#off()
	 */
	@Override
	public boolean off() {
		return false;
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() {
		try {
			return ((FridgeCI)this.offering).upMode();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() {
		try {
			return ((FridgeCI)this.offering).downMode();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) {
		try {
			return ((FridgeCI)this.offering).setMode(modeIndex);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() {
		try {
			return ((FridgeCI)this.offering).currentMode();
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#suspended()
	 */
	@Override
	public boolean suspended() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#suspend()
	 */
	@Override
	public boolean suspend() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#resume()
	 */
	@Override
	public boolean resume() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#emergency()
	 */
	@Override
	public double emergency() {
		// TODO Auto-generated method stub
		return 0;
	}

}
