package main.java.components.washer.tests;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import main.java.components.washer.Washer;

/**
 * The class <code>CVMUnitTest</code> performs unit tests on the {@link Washer}
 * component.
 * 
 * @author Bello Memmi
 *
 */
public class CVMUnitTest extends AbstractCVM {
	/**
	 * URI of the washer inbound port offering the interface <code>WasherCI</code>.
	 */
	protected final static String WASHER_INBOUND_PORT_URI = "wdip-URI";
	/**
	 * if true, the unit test is driven by the SIL simulation, otherwise it is
	 * driven by the component <code>WasherUnitTester</code>.
	 */
	protected final static boolean SIL_UNIT_TEST = false;

	public CVMUnitTest() throws Exception {

	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {
		AbstractComponent.createComponent(Washer.class.getCanonicalName(),
				new Object[] { "serial", WASHER_INBOUND_PORT_URI, "", SIL_UNIT_TEST, true });
		if (!SIL_UNIT_TEST) {
			AbstractComponent.createComponent(WasherUnitTester.class.getCanonicalName(),
					new Object[] { WASHER_INBOUND_PORT_URI });
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