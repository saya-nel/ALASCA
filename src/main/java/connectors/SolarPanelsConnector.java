package main.java.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.interfaces.SolarPanelsCI;

/**
 * Connector for the SolarPanelsCI component interface
 * 
 * @author Bello Memmi
 *
 */
public class SolarPanelsConnector extends AbstractConnector implements SolarPanelsCI {

	/**
	 * @see interfaces.SolarPanelsImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((SolarPanelsCI) this.offering).turnOn();
	}

	/**
	 * @see interfaces.SolarPanelsImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((SolarPanelsCI) this.offering).turnOff();
	}

	@Override
	public boolean isTurnedOn() throws Exception {
		return ((SolarPanelsCI) this.offering).isTurnedOn();
	}
}
