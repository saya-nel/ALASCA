package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.SolarPanelsCI;

/**
 * Outbound port for SolarPanels component
 * 
 * @author Bello Memmi
 *
 */
public class SolarPanelsOutboundPort extends AbstractOutboundPort implements SolarPanelsCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of SolarPanelsOubtboundPort
	 * 
	 * @param uri   uri of the port
	 * @param owner owner component
	 * @throws Exception
	 */
	public SolarPanelsOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, SolarPanelsCI.class, owner);
	}

	/**
	 * @see interfaces.SolarPanelsImplementationI#getCurrentEnergyProduction()
	 */
	@Override
	public float getCurrentEnergyProduction() throws Exception {
		return ((SolarPanelsCI) this.getConnector()).getCurrentEnergyProduction();
	}

}
