package main.java.connectors;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.interfaces.BatteryCI;

/**
 * Connector between two BatteryCI implementations
 *
 * @author Bello Memmi
 *
 */
public class BatteryConnector extends AbstractConnector implements BatteryCI {

	/**
	 * @see main.java.interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		return ((BatteryCI) super.offering).getBatteryCharge();
	}

	/**
	 * @see main.java.interfaces.StandardEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((BatteryCI) this.offering).upMode();
	}

	/**
	 * @see main.java.interfaces.StandardEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((BatteryCI) this.offering).downMode();
	}

	/**
	 * @see main.java.interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((BatteryCI) this.offering).setMode(modeIndex);
	}

	/**
	 * @see main.java.interfaces.StandardEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((BatteryCI) this.offering).currentMode();
	}

	/**
	 * @see main.java.interfaces.PlanningEquipmentControlCI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return ((BatteryCI) this.offering).hasPlan();
	}

	/**
	 * @see main.java.interfaces.PlanningEquipmentControlCI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return ((BatteryCI) this.offering).startTime();
	}

	/**
	 * @see main.java.interfaces.PlanningEquipmentControlCI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return ((BatteryCI) this.offering).duration();
	}

	/**
	 * @see main.java.interfaces.PlanningEquipmentControlCI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return ((BatteryCI) this.offering).deadline();
	}

	/**
	 * @see main.java.interfaces.PlanningEquipmentControlCI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((BatteryCI) this.offering).postpone(d);
	}

	/**
	 * @see main.java.interfaces.PlanningEquipmentControlCI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return ((BatteryCI) this.offering).cancel();
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#planifyEvent(Duration,
	 *      LocalTime)
	 */
	@Override
	public boolean planifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception {
		return ((BatteryCI) this.offering).planifyEvent(durationLastPlanned, deadline);
	}

}
