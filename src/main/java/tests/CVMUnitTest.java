package main.java.tests;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import main.java.components.Controller;
import main.java.components.Washer;

/**
 * CVM for running components testers
 * 
 * @author Bello Memmi
 *
 */
public class CVMUnitTest extends AbstractCVM {

	// componentURIs
	protected final static String BATTERY_URI = "battery-URI";
	protected final static String FAN_URI = "fan-URI";
	protected final static String SOLARPANELS_URI = "solarpanels-URI";
	protected final static String WASHER_URI = "washer-URI";
	protected final static String CONTROLLER_URI = "controller-URI";
	protected final static String PETROLGENERATOR_URI = "petrolgenerator-URI";
	protected final static String FRIDGE_URI = "fridge-URI";
	// ports URIs
	protected final static String BATTERY_INBOUND_PORT_URI = "bip-URI";
	protected final static String FAN_INBOUND_PORT_URI = "fip-URI";
	protected final static String SOLARPANELS_INBOUND_PORT_URI = "spip-URI";
	protected final static String WASHER_INBOUND_PORT_URI = "wip-URI";
	protected final static String CONTROLLER_INBOUND_PORT_URI = "cip-URI";
	protected final static String PETROLGENERATOR_INBOUND_PORT_URI = "pgip-URI";
	protected final static String FRIDGE_INBOUND_PORT_URI = "fridgeip-URI";

	public CVMUnitTest() throws Exception {
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {

		// Battery
		// AbstractComponent.createComponent(Battery.class.getCanonicalName(),
		// new Object[] { BATTERY_URI,true, "serial_number_battery",
		// BATTERY_INBOUND_PORT_URI, CONTROLLER_INBOUND_PORT_URI, (float) 10000 });
		// AbstractComponent.createComponent(BatteryUnitTester.class.getCanonicalName(),
		// new Object[] { BATTERY_INBOUND_PORT_URI });
		/*
		 * // Fan AbstractComponent.createComponent(Fan.class.getCanonicalName(), new
		 * Object[] { FAN_URI, FAN_INBOUND_PORT_URI });
		 * AbstractComponent.createComponent(FanUnitTester.class.getCanonicalName(), new
		 * Object[] { FAN_INBOUND_PORT_URI });
		 * 
		 * // SolarPanels
		 * AbstractComponent.createComponent(SolarPanels.class.getCanonicalName(), new
		 * Object[] { SOLARPANELS_URI, SOLARPANELS_INBOUND_PORT_URI });
		 * AbstractComponent.createComponent(SolarPanelsUnitTester.class.
		 * getCanonicalName(), new Object[] { SOLARPANELS_INBOUND_PORT_URI });
		 * 
		 * // Washer AbstractComponent.createComponent(Washer.class.getCanonicalName(),
		 * new Object[] { WASHER_URI, WASHER_INBOUND_PORT_URI });
		 * AbstractComponent.createComponent(WasherUnitTester.class.getCanonicalName(),
		 * new Object[] { WASHER_INBOUND_PORT_URI });
		 * 
		 * // Petrol Generator
		 * AbstractComponent.createComponent(PetrolGenerator.class.getCanonicalName(),
		 * new Object[] { PETROLGENERATOR_URI, PETROLGENERATOR_INBOUND_PORT_URI, (float)
		 * 1000 }); AbstractComponent.createComponent(PetrolGeneratorUnitTester.class.
		 * getCanonicalName(), new Object[] { PETROLGENERATOR_INBOUND_PORT_URI });
		 */
		// Fridge
		/*
		 * AbstractComponent.createComponent(Fridge.class.getCanonicalName(), new
		 * Object[] { FRIDGE_URI, FRIDGE_INBOUND_PORT_URI});
		 * AbstractComponent.createComponent(FridgeUnitTester.class.getCanonicalName(),
		 * new Object[] { FRIDGE_INBOUND_PORT_URI});
		 * 
		 */
		// Washer
		AbstractComponent.createComponent(Washer.class.getCanonicalName(), new Object[] { WASHER_URI,
				"WASHER_SERIAL_NUMBER", WASHER_INBOUND_PORT_URI, CONTROLLER_INBOUND_PORT_URI });
//		// Controller
		AbstractComponent.createComponent(Controller.class.getCanonicalName(),
				new Object[] { CONTROLLER_URI, true, new String[] { CONTROLLER_INBOUND_PORT_URI }, new String[] {} });
//
//		//TODO Resoudre problème sur ControllerUnitTest Creation composant pose probleme
		// AbstractComponent.createComponent(ControllerUnitTest.class.getCanonicalName(),
		// new Object[] { CONTROLLER_INBOUND_PORT_URI});
		super.deploy();
	}

	public static void main(String[] args) {
		try {
			CVMUnitTest cvm = new CVMUnitTest();
			cvm.startStandardLifeCycle(1000L);
			Thread.sleep(700L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
// -----------------------------------------------------------------------------
