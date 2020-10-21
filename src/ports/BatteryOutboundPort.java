package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.BatteryCI;
import utils.BatteryState;

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
	 * @param uri   uri of the port
	 * @param owner owner component
	 * @throws Exception
	 */
	public BatteryOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BatteryCI.class, owner);
	}

	/**
	 * @see interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		return ((BatteryCI) this.getConnector()).getBatteryCharge();
	}

	/**
	 * @see interfaces.BatteryImplementationI#getBatteryState()
	 */
	@Override
	public BatteryState getBatteryState() throws Exception {
		return ((BatteryCI) this.getConnector()).getBatteryState();
	}

	/**
	 * @see interfaces.BatteryImplementationI#setBatteryState(BatteryState)
	 */
	@Override
	public void setBatteryState(BatteryState state) throws Exception {
		((BatteryCI) this.getConnector()).setBatteryState(state);
	}

	/**
	 * @see interfaces.BatteryImplementationI#takeEnergy(float)
	 */
	@Override
	public float takeEnergy(float toTake) throws Exception {
		return ((BatteryCI) this.getConnector()).takeEnergy(toTake);
	}

}
