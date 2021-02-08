package main.java.components.solarPanels.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.components.solarPanels.interfaces.SolarPanelsCI;

/**
 * The class <code>SolarPanelsConnector</code> implements a connector for the
 * component interface <code>SolarPanelsCI</code>.
 * 
 * @author Bello Memmi
 *
 */
public class SolarPanelsConnector extends AbstractConnector implements SolarPanelsCI {

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((SolarPanelsCI) this.offering).turnOn();
	}

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((SolarPanelsCI) this.offering).turnOff();
	}

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return ((SolarPanelsCI) this.offering).isTurnedOn();
	}
}
