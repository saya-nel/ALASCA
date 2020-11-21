package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * 
 * SolarPanels component interface
 * 
 * @author Bello Memmi
 *
 */
public interface SolarPanelsCI extends SolarPanelsImplementationI, OfferedCI, RequiredCI {

	/**
	 * @see interfaces.SolarPanelsImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception;

	/**
	 * @see interfaces.SolarPanelsImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception;

	/**
	 * @see interfaces.SolarPanelsImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception;
}
