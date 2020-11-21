package connectors;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.PlanningEquipmentControlCI;
import interfaces.WasherCI;

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
	public boolean on() throws Exception {
		return ((WasherCI) this.offering).turnOn();
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#off()
	 */
	@Override
	public boolean off() throws Exception {
		return ((WasherCI) this.offering).turnOff();
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((WasherCI) this.offering).upMode();
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((WasherCI) this.offering).downMode();
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((WasherCI) this.offering).setMode(modeIndex);
	}

	/**
	 * @see interfaces.StandardEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((WasherCI) this.offering).currentMode();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return ((WasherCI) this.offering).hasPlan();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return ((WasherCI) this.offering).startTime();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return ((WasherCI) this.offering).duration();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return ((WasherCI) this.offering).deadline();
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((WasherCI) this.offering).postpone(d);
	}

	/**
	 * @see interfaces.PlanningEquipmentControlCI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return ((WasherCI) this.offering).cancel();
	}

}
