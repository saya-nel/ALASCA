package ports;

import java.time.Duration;
import java.time.LocalTime;

import connectors.ControlWasherConnector;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.WasherCI;

/**
 *
 * Outbound port of Washer component
 *
 * @author Bello Memmi
 *
 */
public class ControlWasherOutboundPort extends AbstractOutboundPort implements WasherCI {

	private static final long serialVersionUID = 1L;

	public ControlWasherOutboundPort(ComponentI owner) throws Exception {
		super(WasherCI.class, owner);
	}

	/**
	 * @see interfaces.WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).isTurnedOn();
	}

	/**
	 * @see interfaces.WasherImplementationI#setProgramTemperature(int)
	 */
	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		((ControlWasherConnector) this.getConnector()).setProgramTemperature(temperature);
	}

	/**
	 * @see interfaces.WasherImplementationI#getProgramTemperature()
	 */
	@Override
	public int getProgramTemperature() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).getProgramTemperature();
	}

	/**
	 * @see interfaces.WasherImplementationI#setProgramDuration(int)
	 */
	@Override
	public void setProgramDuration(int duration) throws Exception {
		((ControlWasherConnector) this.getConnector()).setProgramDuration(duration);
	}

	/**
	 * @see interfaces.WasherImplementationI#getProgramDuration()
	 */
	@Override
	public int getProgramDuration() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).getProgramDuration();
	}

	/**
	 * @see interfaces.WasherImplementationI#turnOn()
	 */
	@Override
	public boolean turnOn() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).on();
	}

	/**
	 * @see interfaces.WasherImplementationI#turnOff()
	 */
	@Override
	public boolean turnOff() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).off();
	}

	/**
	 * @see interfaces.WasherImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).upMode();
	}

	/**
	 * @see interfaces.WasherImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).downMode();
	}

	/**
	 * @see interfaces.WasherImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((ControlWasherConnector) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see interfaces.WasherImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).currentMode();
	}

	/**
	 * @see interfaces.WasherImplementationI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).hasPlan();
	}

	/**
	 * @see interfaces.WasherImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).startTime();
	}

	/**
	 * @see interfaces.WasherImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).duration();
	}

	/**
	 * @see interfaces.WasherImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).deadline();
	}

	/**
	 * @see interfaces.WasherImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((ControlWasherConnector) this.getConnector()).postpone(d);
	}

	/**
	 * @see interfaces.WasherImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return ((ControlWasherConnector) this.getConnector()).cancel();
	}
}
