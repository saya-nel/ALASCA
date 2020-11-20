package connectors;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.PlanningEquipmentControlCI;
import interfaces.WasherCI;

//TODO : revoir gestion des exeptions 

/**
 * Connector between the Controller and the Washer, TODO : this class should be
 * auto-generated in the future.
 * 
 * @author Bello Memmi
 *
 */
public class ControlWasherConnector extends AbstractConnector implements PlanningEquipmentControlCI {

	/**
	 * @see interfaces.StandardEquipmentControlCI#on()
	 */
	@Override
	public boolean on() {
		try {
			return ((WasherCI) this.offering).turnOn();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#off()
	 */
	@Override
	public boolean off() {
		try {
			return ((WasherCI) this.offering).turnOff();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() {
		try {
			return ((WasherCI) this.offering).upMode();
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
			return ((WasherCI) this.offering).downMode();
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
			return ((WasherCI) this.offering).setMode(modeIndex);
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
			return ((WasherCI) this.offering).currentMode();
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
			return ((WasherCI) this.offering).hasPlan();
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
			return ((WasherCI) this.offering).startTime();
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
			return ((WasherCI) this.offering).duration();
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
			return ((WasherCI) this.offering).deadline();
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
			return ((WasherCI) this.offering).postpone(d);
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
			return ((WasherCI) this.offering).cancel();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
