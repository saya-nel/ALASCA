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
    	return ((FridgeCI)this.offering).getRequestedTemperature();
    }
    
    /**
     * @see interfaces.FridgeImplementationI#setRequestedTemperature(float)
     */
    public void setRequestedTemperature(float temp) throws Exception {
    	((FridgeCI)this.offering).setRequestedTemperature(temp);
    }

    /**
     * @see interfaces.FridgeImplementationI#getCurrentTemperature()
     */
    public float getCurrentTemperature() throws Exception {
    	return ((FridgeCI)this.offering).getCurrentTemperature();
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
		try {
			return ((FridgeCI)this.offering).upMode();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#suspend()
	 */
	@Override
	public boolean suspend() {
		try {
			return ((FridgeCI)this.offering).suspend();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#resume()
	 */
	@Override
	public boolean resume() {
		try {
			return ((FridgeCI)this.offering).resume();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see interfaces.SuspensionEquipmentControlCI#emergency()
	 */
	@Override
	public double emergency() {
		try {
			return ((FridgeCI)this.offering).emergency();
		} catch (Exception e) {
			return -1;
		}
	}

}
