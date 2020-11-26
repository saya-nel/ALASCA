package main.java.interfaces;

import java.time.Duration;
import java.time.LocalDate;
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
	public boolean upMode() throws Exception;

	/**
	 * @see interfaces.StandardEquipmentControlCI#downMode()
	 */
	public boolean downMode() throws Exception;

	/**
	 * @see interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	public boolean setMode(int modeIndex) throws Exception;

	/**
	 * @see interfaces.StandardEquipmentControlCI#currentMode()
	 */
	public int currentMode() throws Exception;

	/*
	 * 
	 * PLANNING
	 * 
	 */

	/**
	 * @see interfaces.PlanningEquipmentControlCI#hasPlan()
	 */
	public boolean hasPlan() throws Exception;

	/**
	 * @see interfaces.PlanningEquipmentControlCI#startTime()
	 */
	public LocalTime startTime() throws Exception;

	/**
	 * @see interfaces.PlanningEquipmentControlCI#duration()
	 */
	public Duration duration() throws Exception;

	/**
	 * @see interfaces.PlanningEquipmentControlCI#deadline()
	 */
	public LocalTime deadline() throws Exception;

	/**
	 * @see interfaces.PlanningEquipmentControlCI#postpone(Duration)
	 */
	public boolean postpone(Duration d) throws Exception;

	/**
	 * @see interfaces.PlanningEquipmentControlCI#cancel()
	 */
	public boolean cancel() throws Exception;

	/**
	 * planify event
	 * @param durationLastPlanned			duration of the event
	 * @param deadline						deadline of the event
	 * @return								true if the plan has been successfully added
	 */
	public boolean planifyEvent(Duration durationLastPlanned,LocalTime deadline) throws Exception;

}
