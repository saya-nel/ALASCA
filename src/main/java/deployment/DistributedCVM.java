package main.java.deployment;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import main.java.components.Battery;
import main.java.components.Controller;
import main.java.components.Fan;
import main.java.components.Fridge;
import main.java.components.PetrolGenerator;
import main.java.components.SolarPanels;
import main.java.components.Washer;

public class DistributedCVM extends AbstractDistributedCVM {

	// multi-JVM execution

	// JVM uris
	/**
	 * URI of jvm1
	 */
	protected static String JVM1_URI = "jvm1";
	/**
	 * URI of jvm2
	 */
	protected static String JVM2_URI = "jvm2";

	// Components URIs and serials

	/**
	 * Battery component URI & serial
	 */
	protected final static String batteryURI = "battery";
	protected final static String batterySerial = "battery1";

	/**
	 * Fan component URI
	 */
	protected final static String fanURI = "fan";

	/**
	 * Heater component URI
	 */
	protected final static String petrolGeneratorURI = "petrolGenerator";

	/**
	 * SolarPanels component URI
	 */
	protected final static String solarPanelsURI = "solarPanels";

	/**
	 * Fridge component URI & serial
	 */
	protected final static String fridgeUri = "fridge";
	protected final static String fridgeSerial = "fridge1";

	/**
	 * Washer component URI
	 */
	protected final static String washerUri = "washer";
	protected final static String washerSerial = "washer1";

	/**
	 * Controller component URI
	 */
	protected final static String controllerURI = "controller";

	// Ports URIs

	/**
	 * Battery Inbound port URI
	 */
	protected final static String batteryBIP_uri = "batteryBIP";

	/**
	 * Fan Inbound port URI
	 */
	protected final static String fanFIP_uri = "fanFIP";

	/**
	 * PetrolGenerator Inbound port URI
	 */
	protected final static String petrolGeneratorPGIP_uri = "petrolGeneratorPGIP";

	/**
	 * SolarPanels Inbound port URI
	 */
	protected final static String solarPanelsSPIP_uri = "solarPanelsSPIP";

	/**
	 * Fridge Inbound port URI
	 */
	protected final static String fridgeFIP_uri = "fridgeFIP";

	/**
	 * Washer Inbound port URI
	 */
	protected final static String washerWIP_uri = "washerWIP";

	/**
	 * Controller Inbound port URI
	 */
	protected final static String controllerCIP_uri = "controllerCIP";

	/**
	 * CVM constructor
	 */
	public DistributedCVM(String[] args, int xLayout, int yLayout) throws Exception {
		super(args, xLayout, yLayout);
	}

	/**
	 * Create and deploy components
	 * 
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {

		if (thisJVMURI.equals(JVM1_URI)) {

			// create Controller component
			AbstractComponent.createComponent(Controller.class.getCanonicalName(),
					new Object[] { controllerURI, true, controllerCIP_uri });

			// create Fan component
			AbstractComponent.createComponent(Fan.class.getCanonicalName(), new Object[] { fanURI, fanFIP_uri });

			// create PetrolGenerator component
			AbstractComponent.createComponent(PetrolGenerator.class.getCanonicalName(),
					new Object[] { petrolGeneratorURI, petrolGeneratorPGIP_uri, (float) 2000 });

			// create Battery component
			AbstractComponent.createComponent(Battery.class.getCanonicalName(),
					new Object[] { batteryURI, true, batterySerial, batteryBIP_uri, controllerCIP_uri, (float) 2000 });

		} else if (thisJVMURI.equals(JVM2_URI)) {

			// create SolarPanels component
			AbstractComponent.createComponent(SolarPanels.class.getCanonicalName(),
					new Object[] { solarPanelsURI, solarPanelsSPIP_uri });

			// create Washer component
			AbstractComponent.createComponent(Washer.class.getCanonicalName(),
					new Object[] { washerUri, true, washerSerial, washerWIP_uri, controllerCIP_uri });

			// create Fridge component
			AbstractComponent.createComponent(Fridge.class.getCanonicalName(),
					new Object[] { fridgeUri, true, fridgeSerial, fridgeFIP_uri, controllerCIP_uri });

		} else {
			System.out.println("Unknown jvm uri : " + thisJVMURI);
		}

		super.deploy();
	}

	/**
	 * Program entry
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DistributedCVM c = new DistributedCVM(args, 2, 5);
			c.startStandardLifeCycle(10000L);
			Thread.sleep(3000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
