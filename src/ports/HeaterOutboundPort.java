package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.HeaterCI;

/**
 * Outbound port for Heater component
 * 
 * @author Bello Memmi
 *
 */
public class HeaterOutboundPort extends AbstractOutboundPort implements HeaterCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of HeaterOutboundPort
	 * 
	 * @param owner owner component
	 * @throws Exception
	 */
	public HeaterOutboundPort(ComponentI owner) throws Exception {
		super(HeaterCI.class, owner);
	}

	/**
	 * @see interfaces.HeaterImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		return ((HeaterCI) this.getConnector()).getRequestedTemperature();
	}

	/**
	 * @see interfaces.HeaterImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((HeaterCI) this.getConnector()).turnOn();
	}

	/**
	 * @see interfaces.HeaterImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((HeaterCI) this.getConnector()).turnOff();
	}

	/**
	 * @see interfaces.HeaterImplementationI#isHeaterOn()
	 */
	@Override
	public boolean isHeaterOn() throws Exception {
		return ((HeaterCI) this.getConnector()).isHeaterOn();
	}

	/**
	 * @see interfaces.HeaterImplementationI#setRequestedTemperature(float)
	 */
	@Override
	public void setRequestedTemperature(float requestedTemperature) throws Exception {
		((HeaterCI) this.getConnector()).setRequestedTemperature(requestedTemperature);
	}

}
