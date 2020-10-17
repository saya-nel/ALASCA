package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
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
}
