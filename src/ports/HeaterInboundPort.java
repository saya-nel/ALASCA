package ports;

import components.Heater;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.HeaterCI;

/**
 * 
 * Heater inbound port for Heater component interface
 * 
 * @author Bello Memmi
 *
 */
public class HeaterInboundPort extends AbstractInboundPort implements HeaterCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor of the heater inbound port
	 * 
	 * @param uri   of the heater inbound port
	 * @param owner owner component
	 * @throws Exception
	 */
	public HeaterInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, HeaterInboundPort.class, owner);
	}

	/**
	 * @see interfaces.HeaterImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Heater) owner).getRequestedTemperature());
	}

	/**
	 * @see interfaces.HeaterImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Heater) owner).turnOn();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see interfaces.HeaterImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Heater) owner).turnOff();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see interfaces.HeaterImplementationI#isHeaterOn()
	 */
	@Override
	public boolean isHeaterOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Heater) owner).isHeaterOn());
	}

	/**
	 * @see interfaces.HeaterImplementationI#setRequestedTemperature(float)
	 */
	@Override
	public void setRequestedTemperature(float requestedTemperature) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Heater) owner).setRequestedTemperature(requestedTemperature);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
