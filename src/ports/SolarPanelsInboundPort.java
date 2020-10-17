package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.BatteryCI;
import interfaces.SolarPanelsCI;

/**
 * 
 * SolarPanels inbound port for SolarPanels component interface
 * 
 * @author Bello Memmy
 *
 */
public class SolarPanelsInboundPort extends AbstractInboundPort implements SolarPanelsCI {

	private static final long serialVersionUID = 1L;

	public SolarPanelsInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BatteryCI.class, owner);
		// TODO Auto-generated constructor stub
	}
}
