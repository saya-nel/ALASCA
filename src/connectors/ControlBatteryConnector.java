package connectors;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.BatteryCI;
import interfaces.PlanningEquipmentControlCI;

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
		return ((BatteryCI) this.offering).upMode();
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() {
		return ((BatteryCI) this.offering).downMode();
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) {
		return ((BatteryCI) this.offering).setMode(modeIndex);
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() {
		return ((BatteryCI) this.offering).currentMode();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#hasPlan()
	 */
	@Override
	public boolean hasPlan() {
		return ((BatteryCI) this.offering).hasPlan();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#startTime()
	 */
	@Override
	public LocalTime startTime() {
		return ((BatteryCI) this.offering).startTime();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#duration()
	 */
	@Override
	public Duration duration() {
		return ((BatteryCI) this.offering).duration();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#deadline()
	 */
	@Override
	public LocalTime deadline() {
		return ((BatteryCI) this.offering).deadline();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) {
		return ((BatteryCI) this.offering).postpone(d);
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#cancel()
	 */
	@Override
	public boolean cancel() {
		return ((BatteryCI) this.offering).cancel();
	}

}
