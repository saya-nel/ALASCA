package main.java.components.solarPanels.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import main.java.components.solarPanels.SolarPanels;

/**
 * 
 * The component interface <code>SolarPanelsCI</code> defines the services a
 * {@link SolarPanels} component offers and that can be required from it.
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
