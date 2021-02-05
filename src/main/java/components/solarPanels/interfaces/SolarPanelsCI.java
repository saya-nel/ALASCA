package main.java.components.solarPanels.interfaces;

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
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception;

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception;

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception;
}
