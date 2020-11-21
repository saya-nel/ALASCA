package interfaces;

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
	 * @see interfaces.WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#setProgramTemperature(int)
	 */
	@Override
	public void setProgramTemperature(int temperature) throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#getProgramTemperature()
	 */
	@Override
	public int getProgramTemperature() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#setProgramDuration(int)
	 */
	@Override
	public void setProgramDuration(int duration) throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#getProgramDuration()
	 */
	@Override
	public int getProgramDuration() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#turnOn()
	 */
	@Override
	public boolean turnOn() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#turnOff()
	 */
	@Override
	public boolean turnOff() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception;
}
