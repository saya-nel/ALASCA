package ports;

import java.time.Duration;
import java.time.LocalTime;

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
	 * Constructor of FanOutboundPort
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
		return ((BatteryCI) this.getConnector()).getBatteryCharge();
	}

	/**
	 * @see interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean upMode() {
		try {
			return ((BatteryCI) this.getConnector()).upMode();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.BatteryImplementationI#downMode()
	 */
	@Override
	public boolean downMode() {
		try {
			return ((BatteryCI) this.getConnector()).downMode();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.BatteryImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) {
		try {
			return ((BatteryCI) this.getConnector()).setMode(modeIndex);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.BatteryImplementationI#currentMode()
	 */
	@Override
	public int currentMode() {
		try {
			return ((BatteryCI) this.getConnector()).currentMode();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * @see interfaces.BatteryImplementationI#hasPlan()
	 */
	@Override
	public boolean hasPlan() {
		try {
			return ((BatteryCI) this.getConnector()).hasPlan();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.BatteryImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() {
		try {
			return ((BatteryCI) this.getConnector()).startTime();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see interfaces.BatteryImplementationI#duration()
	 */
	@Override
	public Duration duration() {
		try {
			return ((BatteryCI) this.getConnector()).duration();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see interfaces.BatteryImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() {
		try {
			return ((BatteryCI) this.getConnector()).deadline();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see interfaces.BatteryImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) {
		try {
			return ((BatteryCI) this.getConnector()).postpone(d);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.BatteryImplementationI#cancel()
	 */
	@Override
	public boolean cancel() {
		try {
			return ((BatteryCI) this.getConnector()).cancel();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
