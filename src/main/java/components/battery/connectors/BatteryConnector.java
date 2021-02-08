package main.java.components.battery.connectors;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.components.battery.interfaces.BatteryCI;

/**
 * The class <code>BatteryConnector</code> implements a connector for the
 * component interface <code>BatteryCI</code>.
 *
 * @author Bello Memmi
 *
 */
public class BatteryConnector extends AbstractConnector implements BatteryCI {

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		return ((BatteryCI) super.offering).getBatteryCharge();
	}

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((BatteryCI) this.offering).upMode();
	}

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((BatteryCI) this.offering).downMode();
	}

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((BatteryCI) this.offering).setMode(modeIndex);
	}

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((BatteryCI) this.offering).currentMode();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return ((BatteryCI) this.offering).hasPlan();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return ((BatteryCI) this.offering).startTime();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return ((BatteryCI) this.offering).duration();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return ((BatteryCI) this.offering).deadline();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((BatteryCI) this.offering).postpone(d);
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return ((BatteryCI) this.offering).cancel();
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#planifyEvent(Duration,
	 *      LocalTime)
	 */
	@Override
	public boolean planifyEvent(LocalTime startTime, LocalTime endTime) throws Exception {
		return ((BatteryCI) this.offering).planifyEvent(startTime, endTime);
	}

}
