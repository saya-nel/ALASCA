package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.HeaterCI;

/**
 * Connector for the HeaterCI component interface
 * 
 * @author Bello Memmi
 *
 */
public class HeaterConnector extends AbstractConnector implements HeaterCI {

	/**
	 * @see interfaces.HeaterImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		return ((HeaterCI) this.offering).getRequestedTemperature();
	}

	/**
	 * @see interfaces.HeaterImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((HeaterCI) this.offering).turnOn();
	}

	/**
	 * @see interfaces.HeaterImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((HeaterCI) this.offering).turnOff();
	}

	/**
	 * @see interfaces.HeaterImplementationI#isHeaterOn()
	 */
	@Override
	public boolean isHeaterOn() throws Exception {
		return ((HeaterCI) this.offering).isHeaterOn();
	}
}
