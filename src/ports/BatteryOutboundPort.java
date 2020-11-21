package ports;

import java.time.Duration;
import java.time.LocalTime;

import connectors.ControlBatteryConnector;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.BatteryCI;

/**
 * Outbound port of Battery component
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
	 * @see interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		return ((ControlBatteryConnector) this.getConnector()).getBatteryCharge();
	}

	/**
	 * @see interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((ControlBatteryConnector) this.getConnector()).upMode();
	}

	/**
	 * @see interfaces.BatteryImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((ControlBatteryConnector) this.getConnector()).downMode();
	}

	/**
	 * @see interfaces.BatteryImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((ControlBatteryConnector) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see interfaces.BatteryImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((ControlBatteryConnector) this.getConnector()).currentMode();
	}

	/**
	 * @see interfaces.BatteryImplementationI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return ((ControlBatteryConnector) this.getConnector()).hasPlan();
	}

	/**
	 * @see interfaces.BatteryImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return ((ControlBatteryConnector) this.getConnector()).startTime();
	}

	/**
	 * @see interfaces.BatteryImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return ((ControlBatteryConnector) this.getConnector()).duration();
	}

	/**
	 * @see interfaces.BatteryImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return ((ControlBatteryConnector) this.getConnector()).deadline();
	}

	/**
	 * @see interfaces.BatteryImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((ControlBatteryConnector) this.getConnector()).postpone(d);
	}

	/**
	 * @see interfaces.BatteryImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return ((ControlBatteryConnector) this.getConnector()).cancel();
	}

}
