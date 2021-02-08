package main.java.components.battery.ports;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.components.battery.interfaces.BatteryCI;

/**
 * The class <code>BatteryOutboundPort</code> implements an outbound port for
 * the component interface <code>BatteryCI</code>.
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
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		return ((BatteryCI) this.getConnector()).getBatteryCharge();
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((BatteryCI) this.getConnector()).upMode();
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((BatteryCI) this.getConnector()).downMode();
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((BatteryCI) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((BatteryCI) this.getConnector()).currentMode();
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return ((BatteryCI) this.getConnector()).hasPlan();
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return ((BatteryCI) this.getConnector()).startTime();
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return ((BatteryCI) this.getConnector()).duration();
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return ((BatteryCI) this.getConnector()).deadline();
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((BatteryCI) this.getConnector()).postpone(d);
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return ((BatteryCI) this.getConnector()).cancel();
	}

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#planifyEvent(Duration,
	 *      LocalTime)
	 */
	@Override
	public boolean planifyEvent(LocalTime startTime, LocalTime endTime) throws Exception {
		return ((BatteryCI) this.getConnector()).planifyEvent(startTime, endTime);
	}

}
