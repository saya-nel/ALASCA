package main.java.components.washer.ports;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.washer.Washer;
import main.java.components.washer.interfaces.WasherCI;

/**
 * 
 * The class <code>WasherInboundPort</code> implements an inbound port for the
 * component interface <code>WasherCI</code>.
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
	 * @see main.java.components.washer.interfaces.WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).isTurnedOn());
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#setProgramTemperature(int)
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
	 * @see main.java.components.washer.interfaces.WasherImplementationI#getProgramTemperature()
	 */
	@Override
	public int getProgramTemperature() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).getProgramTemperature());
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#turnOn()
	 */
	@Override
	public boolean turnOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).turnOn());
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#turnOff()
	 */
	@Override
	public boolean turnOff() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).turnOff());
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).upMode());
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).downMode());
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).setMode(modeIndex));
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).currentMode());
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#hashPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).hasPlan());
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).startTime());
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).duration());
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).deadline());
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).postpone(d));
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).cancel());
	}

	/**
	 * @see main.java.components.washer.interfaces.WasherImplementationI#planifyEvent(Duration,
	 *      LocalTime)
	 */
	@Override
	public boolean planifyEvent(LocalTime startTime, LocalTime endTime) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).planifyEvent(startTime, endTime));
	}
}
