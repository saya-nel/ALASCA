package ports;

import components.Battery;
import components.Heater;
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
		// TODO Auto-generated constructor stub
	}

	@Override
	public float readBatteryCharge() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).readBatteryCharge());
	}

	@Override
	public BatteryState getBatteryState() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Battery) owner).getBatteryState());
	}

	@Override
	public void setBatteryState(BatteryState state) throws Exception {
		this.getOwner().runTask(owner -> {
			try{
				((Battery) owner).setBatteryState(state);
			} catch (Exception e){
				e.printStackTrace();
			}
				}
		);
	}
}
