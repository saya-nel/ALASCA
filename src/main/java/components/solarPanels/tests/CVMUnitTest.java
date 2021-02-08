package main.java.components.solarPanels.tests;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import main.java.components.solarPanels.SolarPanels;
import main.java.components.solarPanels.interfaces.SolarPanelsCI;

/**
 * 
 * The class <code>CVMUnitTest</code> performs unit tests on the
 * {@link SolarPanels} component.
 * 
 * @author Bello Memmi
 *
 */
public class CVMUnitTest extends AbstractCVM {
	/**
	 * URI of the solar panels inbound port offering the interface
	 * {@link SolarPanelsCI}
	 */
	protected final static String SP_INBOUND_PORT_URI = "spip-URI";
	/**
	 * if true, the unit test is driven by the SIL simulation, otherwise it is
	 * driven by the component <code>SolarPanelsUnitTester</code>.
	 */
	protected final static boolean SIL_UNIT_TEST = false;

	public CVMUnitTest() throws Exception {

	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {
		AbstractComponent.createComponent(SolarPanels.class.getCanonicalName(),
				new Object[] { SP_INBOUND_PORT_URI, SIL_UNIT_TEST, true });
		if (!SIL_UNIT_TEST) {
			AbstractComponent.createComponent(SolarPanelsUnitTester.class.getCanonicalName(),
					new Object[] { SP_INBOUND_PORT_URI });
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