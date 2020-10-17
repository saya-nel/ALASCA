package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.BatteryCI;
import interfaces.HeaterCI;

/**
 * 
 * Heater inbound port for Heater component interface
 * 
 * @author Bello Memmy
 *
 */
public class HeaterInboundPort extends AbstractInboundPort implements HeaterCI {

	private static final long serialVersionUID = 1L;

	public HeaterInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BatteryCI.class, owner);
		// TODO Auto-generated constructor stub
	}
}
