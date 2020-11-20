package connectors;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.PlanningEquipmentControlCI;

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
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#off()
	 */
	@Override
	public boolean off() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#hasPlan()
	 */
	@Override
	public boolean hasPlan() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#startTime()
	 */
	@Override
	public LocalTime startTime() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#duration()
	 */
	@Override
	public Duration duration() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#deadline()
	 */
	@Override
	public LocalTime deadline() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#cancel()
	 */
	@Override
	public boolean cancel() {
		// TODO Auto-generated method stub
		return false;
	}

}
