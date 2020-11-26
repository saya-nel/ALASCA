package main.java.ports;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.Washer;
import main.java.interfaces.WasherCI;

/**
 * Class representing the inbound port of the component Washer
 *
 * @author Bello Memmi
 *
 */
public class WasherInboundPort extends AbstractInboundPort implements WasherCI {

	private static final long serialVersionUID = 1L;

	public WasherInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, WasherCI.class, owner);
	}

	/**
	 * @see interfaces.WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).isTurnedOn());
	}

	/**
	 * @see interfaces.WasherImplementationI#setProgramTemperature(int)
	 */
	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Washer) owner).setProgramTemperature(temperature);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see interfaces.WasherImplementationI#getProgramTemperature()
	 */
	@Override
	public int getProgramTemperature() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).getProgramTemperature());
	}

	/**
	 * @see interfaces.WasherImplementationI#setProgramDuration(int)
	 */
	@Override
	public void setProgramDuration(int duration) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Washer) owner).setProgramDuration(duration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see interfaces.WasherImplementationI#getProgramDuration()
	 */
	@Override
	public int getProgramDuration() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).getProgramDuration());
	}

	/**
	 * @see interfaces.WasherImplementationI#turnOn()
	 */
	@Override
	public boolean turnOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).turnOn());
	}

	/**
	 * @see interfaces.WasherImplementationI#turnOff()
	 */
	@Override
	public boolean turnOff() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).turnOff());
	}

	/**
	 * @see interfaces.WasherImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).upMode());
	}

	/**
	 * @see interfaces.WasherImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).downMode());
	}

	/**
	 * @see interfaces.WasherImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).setMode(modeIndex));
	}

	/**
	 * @see interfaces.WasherImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).currentMode());
	}

	/**
	 * @see interfaces.WasherImplementationI#hashPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).hasPlan());
	}

	/**
	 * @see interfaces.WasherImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).startTime());
	}

	/**
	 * @see interfaces.WasherImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).duration());
	}

	/**
	 * @see interfaces.WasherImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).deadline());
	}

	/**
	 * @see interfaces.WasherImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).postpone(d));
	}

	/**
	 * @see interfaces.WasherImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).cancel());
	}

	@Override
	public boolean planifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).planifyEvent(durationLastPlanned, deadline));
	}
}
