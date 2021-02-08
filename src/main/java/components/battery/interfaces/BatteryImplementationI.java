package main.java.components.battery.interfaces;

import java.time.Duration;
import java.time.LocalTime;

import main.java.components.battery.Battery;

/**
 * 
 * The interface <code>BatteryImplementationI</code> defines the service that
 * must be implemented by the {@link Battery} component.
 * 
 * @author Bello Memmi
 *
 */
public interface BatteryImplementationI {

	// -------------------------------------------------------------------------
	// Component services signatures
	// -------------------------------------------------------------------------

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
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#upMode()
	 */
	public boolean upMode() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#downMode()
	 */
	public boolean downMode() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	public boolean setMode(int modeIndex) throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#currentMode()
	 */
	public int currentMode() throws Exception;

	/*
	 * 
	 * PLANNING
	 * 
	 */

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#hasPlan()
	 */
	public boolean hasPlan() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#startTime()
	 */
	public LocalTime startTime() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#duration()
	 */
	public Duration duration() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#deadline()
	 */
	public LocalTime deadline() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#postpone(Duration)
	 */
	public boolean postpone(Duration d) throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#cancel()
	 */
	public boolean cancel() throws Exception;

	/**
	 * planify a battery charge
	 * 
	 * @param starTime time to start the charge
	 * @param endTime  time to end the charge
	 * @return true if the plan has been successfully added
	 */
	public boolean planifyEvent(LocalTime starTime, LocalTime endTime) throws Exception;

}
