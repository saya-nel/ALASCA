package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import interfaces.HeaterCI;
import interfaces.HeaterImplementationI;

/**
 * Class representing the Heater component
 * 
 * @author Bello Memmy
 *
 */
@OfferedInterfaces(offered = { HeaterCI.class })
public class Heater extends AbstractComponent implements HeaterImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	public Heater(String uri) {
		super(uri, 1, 0);
		myUri = uri;
	}
}
