package main.java.components.washer.interfaces;

import java.time.Duration;
import java.time.LocalTime;

import main.java.components.washer.Washer;

/**
 * 
 * The interface <code>WasherImplementationI</code> defines the service that
 * must be implemented by the {@link Washer} component.
 * 
 * @author Bello Memmi
 *
 */
public interface WasherImplementationI {

	/**
	 * @return true if the washer is currently working, false else
	 * @throws Exception
	 */
	public boolean isTurnedOn() throws Exception;

	/**
	 * Change the temperature of the program
	 * 
	 * @param temperature the new temperature of the program
	 * @throws Exception
	 */
	public void setProgramTemperature(int temperature) throws Exception;

	/**
	 * Return the temperature of the program
	 * 
	 * @return the temperature of the program
	 * @throws Exception
	 */
	public int getProgramTemperature() throws Exception;

	/**
	 * Add an event
	 * 
	 * @param startTime time to start washing
	 * @param endTime   time to end washing
	 * @return true if success
	 * @throws Exception
	 */
	public boolean planifyEvent(LocalTime startTime, LocalTime endTime) throws Exception;

	/*
	 * 
	 * STANDARD
	 * 
	 */

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#on()
	 */
	public boolean turnOn() throws Exception;

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#off()
	 */
	public boolean turnOff() throws Exception;

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

}
