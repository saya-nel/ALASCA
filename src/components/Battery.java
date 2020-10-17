package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import interfaces.BatteryCI;
import interfaces.BatteryImplementationI;

/**
 * Class representing the Battery component
 * 
 * @author Bello Memmy
 *
 */
@OfferedInterfaces(offered = { BatteryCI.class })
public class Battery extends AbstractComponent implements BatteryImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	public Battery(String uri) {
		super(uri, 1, 0);
		myUri = uri;
	}

}
