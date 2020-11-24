package main.java.deployment;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import main.java.components.Battery;
import main.java.components.Controller;

/**
 * 
 * @author Bello Memmi
 *
 */
public class CVM extends AbstractCVM {

	// single-JVM execution

	// Components URIs and serials

	/**
	 * Battery component URI
	 */
	public final static String batteryURI = "battery";
	public final static String batterySerial = "battery1";

	/**
	 * Fan component URI
	 */
	public final static String fanURI = "fan";

	/**
	 * Heater component URI
	 */
	public final static String heaterURI = "heater";

	/**
	 * SolarPanels component URI
	 */
	public final static String solarPanelsURI = "solarPanels";

	/**
	 * Controller component URI
	 */
	public final static String controllerURI = "controller";
	// Ports URIs

	/**
	 * Battery Inbound port URI
	 */
	public final static String batteryBIP_uri = "batteryBIP";

	/**
	 * Fan Inbound port URI
	 */
	public final static String fanFIP_uri = "fanFIP";

	/**
	 * SolarPanels Inbound port URI
	 */
	public final static String solarPanelsSPIP_uri = "solarPanelsSPIP";

	/**
	 * Controller Inbound port URI
	 */
	public final static String controllerCIP_uri = "controllerCIP";

	/**
	 * CVM constructor
	 */
	public CVM() throws Exception {
		super();
	}

	/**
	 * Create and deploy components
	 * 
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {
		// create Battery component
		AbstractComponent.createComponent(Battery.class.getCanonicalName(),
				new Object[] { batteryURI, true, batterySerial, batteryBIP_uri, controllerCIP_uri, (float) 2000 });
		// create Controller component
		AbstractComponent.createComponent(Controller.class.getCanonicalName(),
				new Object[] { controllerURI, true, new String[] { controllerCIP_uri, }, new String[] {} });
		super.deploy();
	}

	/**
	 * Program entry
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CVM c = new CVM();
			c.startStandardLifeCycle(10000L);
			Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
