package main.java.interfaces;

import java.time.Duration;
import java.time.LocalTime;

/**
 *
 * Washer services interfaces
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
	 * Change the duration of the program
	 * 
	 * @param duration duration of the program
	 * @throws Exception
	 */
	public void setProgramDuration(int duration) throws Exception;

	/**
	 * Return the duration of the program
	 * 
	 * @return duration of the program
	 * @throws Exception
	 */
	public int getProgramDuration() throws Exception;

	/*
	 * 
	 * STANDARD
	 * 
	 */

	/**
	 * @see interfaces.StandardEquipmentControlCI#on()
	 */
	public boolean turnOn() throws Exception;

	/**
	 * @see interfaces.StandardEquipmentControlCI#off()
	 */
	public boolean turnOff() throws Exception;

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
	 *
	 *
	 * Planify
	 *
	 *
	 */

	/**
	 * Add an event
	 * @param durationLastPlanned   		duration of program
	 * @param deadline						deadline of program
	 * @return								true if success
	 * @throws Exception
	 */
	public boolean planifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception ;
}
