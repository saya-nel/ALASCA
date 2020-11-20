package interfaces;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

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
	

	public boolean turnOn() throws Exception;

	public boolean turnOff() throws Exception;
	
	public boolean upMode() throws Exception;

	public boolean downMode() throws Exception;

	public boolean setMode(int modeIndex) throws Exception;

	public int currentMode() throws Exception;
	
	/*
	 *
	 * PLANNING
	 * 
	 */
	

	public boolean hasPlan() throws Exception;

	public LocalTime startTime() throws Exception;

	public Duration duration() throws Exception;

	public LocalTime deadline() throws Exception;

	public boolean postpone(Duration d) throws Exception;

	public boolean cancel() throws Exception;

}
