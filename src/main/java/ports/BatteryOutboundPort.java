package main.java.ports;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.interfaces.BatteryCI;

/**
 * Outbound port of Battery component for battery component interface
 * 
 * @author Bello Memmi
 *
 */
public class BatteryOutboundPort extends AbstractOutboundPort implements BatteryCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of ControlBatteryOutboundPort
	 * 
	 * @param owner owner component
	 * @throws Exception
	 */
	public BatteryOutboundPort(ComponentI owner) throws Exception {
		super(BatteryCI.class, owner);
	}

	/**
	 * Constructor of the ControlBatteryOutboundPort
	 * 
	 * @param inbound_uri reflexion uri of the port
	 * @param owner       owner component
	 * @throws Exception
	 */
	public BatteryOutboundPort(String inbound_uri, ComponentI owner) throws Exception {
		super(inbound_uri, BatteryCI.class, owner);
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		return ((BatteryCI) this.getConnector()).getBatteryCharge();
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((BatteryCI) this.getConnector()).upMode();
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((BatteryCI) this.getConnector()).downMode();
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((BatteryCI) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((BatteryCI) this.getConnector()).currentMode();
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return ((BatteryCI) this.getConnector()).hasPlan();
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return ((BatteryCI) this.getConnector()).startTime();
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return ((BatteryCI) this.getConnector()).duration();
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return ((BatteryCI) this.getConnector()).deadline();
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((BatteryCI) this.getConnector()).postpone(d);
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return ((BatteryCI) this.getConnector()).cancel();
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#planifyEvent(Duration,
	 *      LocalTime)
	 */
	@Override
	public boolean planifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception {
		return ((BatteryCI) this.getConnector()).planifyEvent(durationLastPlanned, deadline);
	}

}
