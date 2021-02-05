package main.java.components.washer.interfaces;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 *
 * Washer component interface
 *
 * @author Bello Memmi
 *
 */
public interface WasherCI extends WasherImplementationI, RequiredCI, OfferedCI {

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#setProgramTemperature(int)
	 */
	@Override
	public void setProgramTemperature(int temperature) throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#getProgramTemperature()
	 */
	@Override
	public int getProgramTemperature() throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#setProgramDuration(int)
	 */
	@Override
	public void setProgramDuration(int duration) throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#getProgramDuration()
	 */
	@Override
	public int getProgramDuration() throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#turnOn()
	 */
	@Override
	public boolean turnOn() throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#turnOff()
	 */
	@Override
	public boolean turnOff() throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception;

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception;
}
