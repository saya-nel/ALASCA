package ports;

import components.Heater;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.BatteryCI;
import interfaces.HeaterCI;

/**
 * 
 * Heater inbound port for Heater component interface
 * 
 * @author Bello Memmi
 *
 */
public class HeaterInboundPort extends AbstractInboundPort implements HeaterCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor of the heater inbound port
	 * 
	 * @param uri   of the heater inbound port
	 * @param owner owner component
	 * @throws Exception
	 */
	public HeaterInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BatteryCI.class, owner);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @see HeaterCI
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Heater) owner).getRequestedTemperature());
	}

	/**
	 * @see HeaterCI
	 */
	@Override
	public void turnOn() throws Exception {
		this.getOwner().runTask(owner -> {
					try {
						((Heater) owner).turnOn();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		);
	}
	/**
	 * @see HeaterCI
	 */
	@Override
	public void turnOff() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Heater) owner).turnOff();
			} catch (Exception e) {
				e.printStackTrace();
			}
				}
		);
	}
	/**
	 * @see HeaterCI
	 */
	@Override
	public boolean heaterIsOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Heater) owner).heaterIsOn());
	}

}
