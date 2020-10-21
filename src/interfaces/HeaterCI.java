package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * 
 * Heater component interface
 * 
 * @author Bello Memmi
 *
 */
public interface HeaterCI extends HeaterImplementationI, OfferedCI, RequiredCI {

	/**
	 * @see interfaces.HeaterImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception;

	/**
	 * @see interfaces.HeaterImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception;

	/**
	 * @see interfaces.HeaterImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception;

	/**
	 * @see interfaces.HeaterImplementationI#isHeaterOn()
	 */
	@Override
	public boolean isHeaterOn() throws Exception;

	/**
	 * @see interfaces.HeaterImplementationI#setRequestedTemperature(float)
	 */
	@Override
	public void setRequestedTemperature(float requestedTemperature) throws Exception;

}
