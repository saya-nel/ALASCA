package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import interfaces.SolarPanelsCI;
import interfaces.SolarPanelsImplementationI;

/**
 * Class representing the SolarPanels component
 * 
 * @author Bello Memmy
 *
 */
@OfferedInterfaces(offered = { SolarPanelsCI.class })
public class SolarPanels extends AbstractComponent implements SolarPanelsImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	public SolarPanels(String uri) {
		super(uri, 1, 0);
		myUri = uri;
	}
}
