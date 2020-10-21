package ports;

import components.SolarPanels;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.BatteryCI;
import interfaces.SolarPanelsCI;

/**
 * 
 * SolarPanels inbound port for SolarPanels component interface
 * 
 * @author Bello Memmi
 *
 */
public class SolarPanelsInboundPort extends AbstractInboundPort implements SolarPanelsCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor of the solar panels inbound port
	 * 
	 * @param uri   of the solar panels inbound port
	 * @param owner owner component
	 * @throws Exception
	 */
	public SolarPanelsInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, BatteryCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public float getCurrentEnergyProduction() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((SolarPanels) owner).getCurrentEnergyProduction());
	}
}
