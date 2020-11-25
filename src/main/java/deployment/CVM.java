package main.java.deployment;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import main.java.components.*;

/**
 * 
 * @author Bello Memmi
 *
 */
public class CVM extends AbstractCVM {

	// single-JVM execution

	// Components URIs and serials

	/**
	 * Battery component URI & serial
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
	public final static String petrolGeneratorURI = "petrolGenerator";

	/**
	 * SolarPanels component URI
	 */
	public final static String solarPanelsURI = "solarPanels";

	/**
	 * Fridge component URI & serial
	 */
	public final static String fridgeUri = "fridge";
	public final static String fridgeSerial = "fridge1";

	/**
	 * Washer component URI
	 */
	public final static String washerUri = "washer";
	public final static String washerSerial = "washer1";

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
	 * PetrolGenerator Inbound port URI
	 */
	public final static String petrolGeneratorPGIP_uri = "petrolGeneratorPGIP";

	/**
	 * SolarPanels Inbound port URI
	 */
	public final static String solarPanelsSPIP_uri = "solarPanelsSPIP";

	/**
	 * Fridge Inbound port URI
	 */
	public final static String fridgeFIP_uri = "fridgeFIP";

	/**
	 * Washer Inbound port URI
	 */
	public final static String washerWIP_uri = "washerWIP";

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

		// create components that arn't connected to controller :

		// create Fan component
		AbstractComponent.createComponent(Fan.class.getCanonicalName(), new Object[] { fanURI, fanFIP_uri });

		// create PetrolGenerator component
		AbstractComponent.createComponent(PetrolGenerator.class.getCanonicalName(),
				new Object[] { petrolGeneratorURI, petrolGeneratorPGIP_uri, (float) 2000 });

		// create SolarPanels component
		AbstractComponent.createComponent(SolarPanels.class.getCanonicalName(),
				new Object[] { solarPanelsURI, solarPanelsSPIP_uri });

		// create the controller and components that are connected to controller :

		// create Controller component
		AbstractComponent.createComponent(Controller.class.getCanonicalName(),
				new Object[] { controllerURI, true, controllerCIP_uri });

		// create Battery component
		//AbstractComponent.createComponent(Battery.class.getCanonicalName(),
		//		new Object[] { batteryURI, true, batterySerial, batteryBIP_uri, controllerCIP_uri, (float) 2000 });

		// create Fridge component
//		AbstractComponent.createComponent(Fridge.class.getCanonicalName(),
//				new Object[] { fridgeUri, true, fridgeSerial, fridgeFIP_uri, controllerCIP_uri });

		// create Washer component
		AbstractComponent.createComponent(Washer.class.getCanonicalName(),
				new Object[] { washerUri, true, washerSerial, washerWIP_uri, controllerCIP_uri });

		// create electricPanel component

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
			Thread.sleep(3000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
