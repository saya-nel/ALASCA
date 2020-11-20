package interfaces;

import java.time.Duration;
import java.time.LocalTime;

/**
 * 
 * Battery services interface
 * 
 * @author Bello Memmi
 *
 */
public interface BatteryImplementationI {

	/**
	 * Return the actual battery charge
	 * 
	 * @return the actual battery charge
	 * @throws Exception
	 */
	public float getBatteryCharge() throws Exception;

	/*
	 * 
	 * STANDARD
	 * 
	 */

	/**
	 * @see interfaces.StandardEquipmentControlCI#upMode()
	 */
	public boolean upMode();

	/**
	 * @see interfaces.StandardEquipmentControlCI#downMode()
	 */
	public boolean downMode();

	/**
	 * @see interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	public boolean setMode(int modeIndex);

	/**
	 * @see interfaces.StandardEquipmentControlCI#currentMode()
	 */
	public int currentMode();
	
	/*
	 * 
	 * PLANNING
	 * 
	 */
	
	/**
	 * @see interfaces.PlanningEquipmentControlCI#hasPlan()
	 */
	public boolean hasPlan();

	/**
	 * @see interfaces.PlanningEquipmentControlCI#startTime()
	 */
	public LocalTime startTime();

	/**
	 * @see interfaces.PlanningEquipmentControlCI#duration()
	 */
	public Duration duration();

	/**
	 * @see interfaces.PlanningEquipmentControlCI#deadline()
	 */
	public LocalTime deadline();

	/**
	 * @see interfaces.PlanningEquipmentControlCI#postpone(Duration)
	 */
	public boolean postpone(Duration d);

	/**
	 * @see interfaces.PlanningEquipmentControlCI#cancel()
	 */
	public boolean cancel();

}
