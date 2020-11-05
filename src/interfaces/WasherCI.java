package interfaces;

import java.util.Date;

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
	 * @see interfaces.WasherImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#getProgramTemperature()
	 */
	@Override
	public int getProgramTemperature() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#setProgramTemperature(int)
	 */
	@Override
	public void setProgramTemperature(int temperature) throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#getProgramDuration()
	 */
	@Override
	public int getProgramDuration() throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#setProgramDuration(int)
	 */
	@Override
	public void setProgramDuration(int duration) throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#setDelay(Date)
	 */
	@Override
	public void setDelay(Date date) throws Exception;

	/**
	 * @see interfaces.WasherImplementationI#getDelay()
	 */
	@Override
	public Date getDelay() throws Exception;

}
