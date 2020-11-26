package main.java.ports;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.connectors.BatteryConnector;
import main.java.connectors.ControlBatteryConnector;
import main.java.interfaces.BatteryCI;

/**
 * Outbound port of Battery component
 * 
 * @author Bello Memmi
 *
 */
public class ControlBatteryOutboundPort extends AbstractOutboundPort implements BatteryCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of ControlBatteryOutboundPort
	 * 
	 * @param owner owner component
	 * @throws Exception
	 */
	public ControlBatteryOutboundPort(ComponentI owner) throws Exception {
		super(BatteryCI.class, owner);
	}

	public ControlBatteryOutboundPort(String inbound_uri, ComponentI owner) throws Exception {
		super(inbound_uri, BatteryCI.class, owner);
	}

	/**
	 * @see interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		return ((BatteryConnector) this.getConnector()).getBatteryCharge();
	}

	/**
	 * @see interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((BatteryConnector) this.getConnector()).upMode();
	}

	/**
	 * @see interfaces.BatteryImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((BatteryConnector) this.getConnector()).downMode();
	}

	/**
	 * @see interfaces.BatteryImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((BatteryConnector) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see interfaces.BatteryImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((BatteryConnector) this.getConnector()).currentMode();
	}

	/**
	 * @see interfaces.BatteryImplementationI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return ((BatteryConnector) this.getConnector()).hasPlan();
	}

	/**
	 * @see interfaces.BatteryImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return ((BatteryConnector) this.getConnector()).startTime();
	}

	/**
	 * @see interfaces.BatteryImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return ((BatteryConnector) this.getConnector()).duration();
	}

	/**
	 * @see interfaces.BatteryImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return ((BatteryConnector) this.getConnector()).deadline();
	}

	/**
	 * @see interfaces.BatteryImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((BatteryConnector) this.getConnector()).postpone(d);
	}

	/**
	 * @see interfaces.BatteryImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return ((BatteryConnector) this.getConnector()).cancel();
	}

	@Override
	public boolean planifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception {
		return ((BatteryConnector) this.getConnector()).planifyEvent(durationLastPlanned, deadline);
	}

}
