package main.java.ports;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.washer.Washer;
import main.java.interfaces.WasherCI;

/**
 * inbound port for the washer component interface
 *
 * @author Bello Memmi
 *
 */
public class WasherInboundPort extends AbstractInboundPort implements WasherCI {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor of the WasherInboundPort
	 * 
	 * @param uri   reflexion uri of the port
	 * @param owner owner component
	 * @throws Exception
	 */
	public WasherInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, WasherCI.class, owner);
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).isTurnedOn());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#setProgramTemperature(int)
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
	 * @see main.java.interfaces.WasherImplementationI#getProgramTemperature()
	 */
	@Override
	public int getProgramTemperature() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).getProgramTemperature());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#setProgramDuration(int)
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
	 * @see main.java.interfaces.WasherImplementationI#getProgramDuration()
	 */
	@Override
	public int getProgramDuration() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).getProgramDuration());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#turnOn()
	 */
	@Override
	public boolean turnOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).turnOn());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#turnOff()
	 */
	@Override
	public boolean turnOff() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).turnOff());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).upMode());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).downMode());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).setMode(modeIndex));
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).currentMode());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#hashPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).hasPlan());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).startTime());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).duration());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).deadline());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).postpone(d));
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).cancel());
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#planifyEvent(Duration,
	 *      LocalTime)
	 */
	@Override
	public boolean planifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).planifyEvent(durationLastPlanned, deadline));
	}
}
