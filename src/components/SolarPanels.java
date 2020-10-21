package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import interfaces.SolarPanelsCI;
import interfaces.SolarPanelsImplementationI;

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
	 * Constructor of the solar panels
	 * 
	 * @param uri of the SolarPanels component
	 */
	public SolarPanels(String uri) {
		super(uri, 1, 0);
		myUri = uri;
	}


	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
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
