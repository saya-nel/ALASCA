package deployment;

import components.Battery;
import components.Fan;
import components.Heater;
import components.SolarPanels;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

/**
 * 
 * @author Bello Memmi
 *
 */
public class CVM extends AbstractCVM {

	// single-JVM execution

	// Components URIs

	/**
	 * Battery component URI
	 */
	public final static String batteryURI = "battery";

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
	 * Heater Inbound port URI
	 */
	public final static String heaterHIP_uri = "heaterHIP";

	/**
	 * SolarPanels Inbound port URI
	 */
	public final static String solarPanelsSPIP_uri = "solarPanelsSPIP";

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
				new Object[] { batteryURI, batteryBIP_uri });
		// create Fan component
		AbstractComponent.createComponent(Fan.class.getCanonicalName(), new Object[] { fanURI, fanFIP_uri });
		// create Heater component
		AbstractComponent.createComponent(Heater.class.getCanonicalName(), new Object[] { heaterURI, heaterHIP_uri });
		// create SolarPanels component
		AbstractComponent.createComponent(SolarPanels.class.getCanonicalName(),
				new Object[] { solarPanelsURI, solarPanelsSPIP_uri });

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
