package ports;

import java.util.Date;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.WasherCI;
import interfaces.WasherImplementationI;

/**
 *
 * Outbound port of Washer component
 *
 * @author Bello Memmi
 *
 */
public class WasherOutboundPort extends AbstractOutboundPort implements WasherCI {

	private static final long serialVersionUID = 1L;

	public WasherOutboundPort(ComponentI owner) throws Exception {
		super(WasherCI.class, owner);
	}

	/**
	 * @see WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return ((WasherCI) this.getConnector()).isTurnedOn();
	}

	/**
	 * @see WasherImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((WasherCI) this.getConnector()).turnOn();
	}

	/**
	 * @see WasherImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((WasherCI) this.getConnector()).turnOff();
	}

	/**
	 * @see WasherImplementationI#getProgramTemperature()
	 */
	@Override
	public int getProgramTemperature() throws Exception {
		return ((WasherCI) this.getConnector()).getProgramTemperature();
	}

	/**
	 * @see interfaces.WasherImplementationI#setProgramTemperature(int)
	 */
	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		((WasherCI) this.getConnector()).setProgramTemperature(temperature);
	}

	/**
	 * @see interfaces.WasherImplementationI#getProgramDuration()
	 */
	@Override
	public int getProgramDuration() throws Exception {
		return ((WasherCI) this.getConnector()).getProgramDuration();
	}

	/**
	 * @see interfaces.WasherImplementationI#setProgramDuration(int)
	 */
	@Override
	public void setProgramDuration(int duration) throws Exception {
		((WasherCI) this.getConnector()).setProgramDuration(duration);
	}

	/**
	 * @see interfaces.WasherImplementationI#setDelay(Date)
	 */
	@Override
	public void setDelay(Date date) throws Exception {
		((WasherCI) this.getConnector()).setDelay(date);
	}

	@Override
	public Date getDelay() throws Exception {
		return ((WasherCI) this.getConnector()).getDelay();
	}
}
