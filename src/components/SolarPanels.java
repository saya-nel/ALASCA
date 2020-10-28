package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import interfaces.SolarPanelsCI;
import interfaces.SolarPanelsImplementationI;
import ports.SolarPanelsInboundPort;

/**
 * Class representing the SolarPanels component
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { SolarPanelsCI.class })
public class SolarPanels extends AbstractComponent implements SolarPanelsImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	/**
	 * Inboud port of the heater
	 */
	protected SolarPanelsInboundPort spip;

	/**
	 * Constructor of the solar panels
	 * 
	 * @param uri of the SolarPanels component
	 */
	public SolarPanels(String uri, String spipURI) throws Exception {
		super(uri, 1, 0);
		myUri = uri;
		this.spip = new SolarPanelsInboundPort(spipURI, this);
		this.spip.publishPort();
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.spip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}
	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	@Override
	public float getCurrentEnergyProduction() throws Exception {
		return 0;
	}
}
