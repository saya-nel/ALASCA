package ports;

import components.Battery;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.BatteryCI;
import utils.BatteryState;

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
	 * @see interfaces.BatteryImplementationI#getBatteryState()
	 */
	@Override
	public BatteryState getBatteryState() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).getBatteryState());
	}

	/**
	 * @see interfaces.BatteryImplementationI#getMaximumEnergy()
	 */
	@Override
	public float getMaximumEnergy() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).getMaximumEnergy());
	}

	/**
	 * @see interfaces.BatteryImplementationI#setBatteryState(BatteryState)
	 */
	@Override
	public void setBatteryState(BatteryState state) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Battery) owner).setBatteryState(state);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see interfaces.BatteryImplementationI#takeEnergy(float)
	 */
	@Override
	public float takeEnergy(float toTake) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).takeEnergy(toTake));
	}

	/**
	 * @see interfaces.BatteryImplementationI#addEnergy(float)
	 */
	@Override
	public void addEnergy(float toAdd) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Battery) owner).addEnergy(toAdd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
