package ports;

import components.Washer;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.WasherCI;

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
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner)).isTurnedOn();
	}

	/**
	 * @see interfaces.WasherImplementationI#turnOnWasher()
	 */
	@Override
	public void turnOnWasher() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Washer) owner).turnOnWasher();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see interfaces.WasherImplementationI#turnOffWasher()
	 */
	@Override
	public void turnOffWasher() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Washer) owner).turnOffWasher();
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
	 * @see interfaces.WasherImplementationI#getProgramDuration()
	 */
	@Override
	public int getProgramDuration() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).getProgramDuration());
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

}
