package connectors;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.BatteryCI;
import interfaces.PlanningEquipmentControlCI;

// TODO : revoir gestion des exeptions 

/**
 * Connector between the Controller and the Battery, TODO : this class should be
 * auto-generated in the future.
 * 
 * @author Bello Memmi
 *
 */
public class ControlBatteryConnector extends AbstractConnector implements PlanningEquipmentControlCI {

	/**
	 * @see interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	public float getBatteryCharge() throws Exception {
		return ((BatteryCI) this.offering).getBatteryCharge();
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#on()
	 */
	@Override
	public boolean on() {
		return false;
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#off()
	 */
	@Override
	public boolean off() {
		return false;
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() {
		try {
			return ((BatteryCI) this.offering).upMode();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() {
		try {
			return ((BatteryCI) this.offering).downMode();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) {
		try {
			return ((BatteryCI) this.offering).setMode(modeIndex);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() {
		try {
			return ((BatteryCI) this.offering).currentMode();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#hasPlan()
	 */
	@Override
	public boolean hasPlan() {
		try {
			return ((BatteryCI) this.offering).hasPlan();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#startTime()
	 */
	@Override
	public LocalTime startTime() {
		try {
			return ((BatteryCI) this.offering).startTime();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#duration()
	 */
	@Override
	public Duration duration() {
		try {
			return ((BatteryCI) this.offering).duration();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#deadline()
	 */
	@Override
	public LocalTime deadline() {
		try {
			return ((BatteryCI) this.offering).deadline();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) {
		try {
			return ((BatteryCI) this.offering).postpone(d);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#cancel()
	 */
	@Override
	public boolean cancel() {
		try {
			return ((BatteryCI) this.offering).cancel();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
