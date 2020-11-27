package main.java.ports;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.interfaces.WasherCI;

/**
 *
 * Outbound port for the washer component interface
 *
 * @author Bello Memmi
 *
 */
public class WasherOutboundPort extends AbstractOutboundPort implements WasherCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the WasherOutboundPort
	 * 
	 * @param owner owner component
	 * @throws Exception
	 */
	public WasherOutboundPort(ComponentI owner) throws Exception {
		super(WasherCI.class, owner);
	}

	/**
	 * Constructor of the WasherOutboundPort
	 * 
	 * @param inbound_uri reflexion uri of the port
	 * @param owner       owner component
	 * @throws Exception
	 */
	public WasherOutboundPort(String inbound_uri, ComponentI owner) throws Exception {
		super(inbound_uri, WasherCI.class, owner);
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return ((WasherCI) this.getConnector()).isTurnedOn();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#setProgramTemperature(int)
	 */
	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		((WasherCI) this.getConnector()).setProgramTemperature(temperature);
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#getProgramTemperature()
	 */
	@Override
	public int getProgramTemperature() throws Exception {
		return ((WasherCI) this.getConnector()).getProgramTemperature();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#setProgramDuration(int)
	 */
	@Override
	public void setProgramDuration(int duration) throws Exception {
		((WasherCI) this.getConnector()).setProgramDuration(duration);
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#getProgramDuration()
	 */
	@Override
	public int getProgramDuration() throws Exception {
		return ((WasherCI) this.getConnector()).getProgramDuration();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#turnOn()
	 */
	@Override
	public boolean turnOn() throws Exception {
		return ((WasherCI) this.getConnector()).turnOn();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#turnOff()
	 */
	@Override
	public boolean turnOff() throws Exception {
		return ((WasherCI) this.getConnector()).turnOff();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((WasherCI) this.getConnector()).upMode();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((WasherCI) this.getConnector()).downMode();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((WasherCI) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((WasherCI) this.getConnector()).currentMode();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return ((WasherCI) this.getConnector()).hasPlan();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return ((WasherCI) this.getConnector()).startTime();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return ((WasherCI) this.getConnector()).duration();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return ((WasherCI) this.getConnector()).deadline();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((WasherCI) this.getConnector()).postpone(d);
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return ((WasherCI) this.getConnector()).cancel();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#planifyEvent(Duration,
	 *      LocalTime)
	 */
	@Override
	public boolean planifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception {
		return ((WasherCI) this.getConnector()).planifyEvent(durationLastPlanned, deadline);
	}
}
