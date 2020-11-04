package connectors;

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
	 * @see WasherImplementationI#turnOnWasher()
	 */
	@Override
	public void turnOnWasher() throws Exception {
		((WasherCI) this.offering).turnOnWasher();
	}

	/**
	 * @see WasherImplementationI#turnOffWasher()
	 */
	@Override
	public void turnOffWasher() throws Exception {
		((WasherCI) this.offering).turnOffWasher();
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
}
