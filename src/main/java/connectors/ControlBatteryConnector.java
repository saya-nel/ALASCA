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
		return ((BatteryCI) super.offering).getBatteryCharge();
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#on()
	 */
	@Override
	public boolean on() throws Exception {
		return false;
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#off()
	 */
	@Override
	public boolean off() throws Exception {
		return false;
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((BatteryCI) this.offering).upMode();
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((BatteryCI) this.offering).downMode();
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((BatteryCI) this.offering).setMode(modeIndex);
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((BatteryCI) this.offering).currentMode();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return ((BatteryCI) this.offering).hasPlan();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return ((BatteryCI) this.offering).startTime();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return ((BatteryCI) this.offering).duration();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return ((BatteryCI) this.offering).deadline();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((BatteryCI) this.offering).postpone(d);
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return ((BatteryCI) this.offering).cancel();
	}

}
