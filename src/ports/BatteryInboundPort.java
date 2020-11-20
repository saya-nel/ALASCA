package ports;

import java.time.Duration;
import java.time.LocalTime;

import components.Battery;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.BatteryCI;

/**
 * 
 * Battery inbound port for Battery component interface
 * 
 * @author Bello Memmi
 *
 */
public class BatteryInboundPort extends AbstractInboundPort implements BatteryCI {

	private static final long serialVersionUID = 1L;

	public BatteryInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BatteryCI.class, owner);
	}

	/**
	 * @see interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).getBatteryCharge());
	}

	/**
	 * @see interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean upMode() {
		try {
			return this.getOwner().handleRequestSync(owner -> ((Battery) owner).upMode());
		} catch (AssertionError | Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean downMode() {
		try {
			return this.getOwner().handleRequestSync(owner -> ((Battery) owner).downMode());
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Battery) owner).setMode(modeIndex));
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Battery) owner).currentMode());
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Battery) owner).hasPlan());
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Battery) owner).startTime());
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Battery) owner).duration());
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Battery) owner).deadline());
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Battery) owner).postpone(d));
		} catch (AssertionError | Exception e) {
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
			return this.getOwner().handleRequestSync(owner -> ((Battery) owner).cancel());
		} catch (AssertionError | Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
