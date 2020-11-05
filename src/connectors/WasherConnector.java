package connectors;

import java.util.Date;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.WasherCI;
import interfaces.WasherImplementationI;

public class WasherConnector extends AbstractConnector implements WasherCI {
	/**
	 * @see WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return ((WasherCI) this.offering).isTurnedOn();
	}

	/**
	 * @see WasherImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((WasherCI) this.offering).turnOn();
	}

	/**
	 * @see WasherImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((WasherCI) this.offering).turnOff();
	}

	/**
	 * @see WasherImplementationI#getProgramTemperature()
	 */
	@Override
	public int getProgramTemperature() throws Exception {
		return ((WasherCI) this.offering).getProgramTemperature();
	}

	/**
	 * @see interfaces.WasherImplementationI#setProgramTemperature(int)
	 */
	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		((WasherCI) this.offering).setProgramTemperature(temperature);
	}

	/**
	 * @see interfaces.WasherImplementationI#setProgramTemperature(int)
	 */
	@Override
	public int getProgramDuration() throws Exception {
		return ((WasherCI) this.offering).getProgramDuration();
	}

	/**
	 * @see interfaces.WasherImplementationI#setProgramDuration(int)
	 */
	@Override
	public void setProgramDuration(int duration) throws Exception {
		((WasherCI) this.offering).setProgramDuration(duration);
	}

	/**
	 * @see interfaces.WasherImplementationI#setDelay(Date)
	 */
	@Override
	public void setDelay(Date date) throws Exception {
		((WasherCI) this.offering).setDelay(date);
	}

	/**
	 * @see interfaces.WasherImplementationI#getDelay()
	 */
	@Override
	public Date getDelay() throws Exception {
		return ((WasherCI) this.offering).getDelay();
	}
}
