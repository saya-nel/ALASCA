package main.java.ports;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.battery.Battery;
import main.java.interfaces.BatteryCI;

/**
 * 
 * Battery inbound port for Battery component interface
 * 
 * @author Bello Memmi
 *
 */
public class BatteryInboundPort extends AbstractInboundPort implements BatteryCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the BatteryInboundPort
	 * 
	 * @param uri   reflexion uri of the port
	 * @param owner owner component
	 * @throws Exception
	 */
	public BatteryInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BatteryCI.class, owner);
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).getBatteryCharge());
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).upMode());
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).downMode());
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).setMode(modeIndex));
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).currentMode());
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).hasPlan());
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).startTime());
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).duration());
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).deadline());
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).postpone(d));
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).cancel());
	}

	/**
	 * @see main.java.interfaces.BatteryImplementationI#planifyEvent(Duration,
	 *      LocalTime)
	 */
	@Override
	public boolean planifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception {
		return this.getOwner()
				.handleRequestSync(owner -> ((Battery) owner).planifyEvent(durationLastPlanned, deadline));
	}

}
