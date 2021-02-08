package main.java.components.fan.tests;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import main.java.components.fan.Fan;

public class CVMUnitTest extends AbstractCVM {

	/**
	 * URI of the hair dryer inbound port offering the interface
	 * <code>HairDryerCI</code>.
	 */
	protected final static String FAN_INBOUND_PORT_URI = "fip-URI";
	/**
	 * if true, the unit test is driven by the SIL simulation, otherwise it is
	 * driven by the component <code>HairDryerUnitTester</code>.
	 */
	protected final static boolean SIL_UNIT_TEST = false;

	public CVMUnitTest() throws Exception {
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {
		AbstractComponent.createComponent(Fan.class.getCanonicalName(),
				new Object[] { FAN_INBOUND_PORT_URI, SIL_UNIT_TEST, true });
		if (!SIL_UNIT_TEST) {
			AbstractComponent.createComponent(FanUnitTester.class.getCanonicalName(),
					new Object[] { FAN_INBOUND_PORT_URI });
		}

		super.deploy();
	}

	public static void main(String[] args) {
		try {
			CVMUnitTest cvm = new CVMUnitTest();
			cvm.startStandardLifeCycle(SIL_UNIT_TEST ? 12000L : 2000L);
			Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
