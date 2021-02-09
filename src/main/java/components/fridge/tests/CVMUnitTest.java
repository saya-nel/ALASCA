package main.java.components.fridge.tests;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import main.java.components.fridge.Fridge;
import main.java.components.fridge.FridgeReactiveController;

/**
 * The class <code>CVMUnitTest</code> performs unit tests on the {@link Fridge}
 * component.
 * 
 * @author Bello Memmi
 *
 */
public class CVMUnitTest extends AbstractCVM {
	/**
	 * if true, the unit test is driven by the SIL simulation, otherwise it is
	 * driven by the component
	 */
	protected final static boolean SIL_UNIT_TEST = false;

	public CVMUnitTest() throws Exception {

	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {
		AbstractComponent.createComponent(Fridge.class.getCanonicalName(),
				new Object[] { "serial", "", SIL_UNIT_TEST, true });
		if (SIL_UNIT_TEST) {
			AbstractComponent.createComponent(FridgeReactiveController.class.getCanonicalName(), new Object[] {});
		}
		if (!SIL_UNIT_TEST) {
			AbstractComponent.createComponent(FridgeUnitTester.class.getCanonicalName(),
					new Object[] { Fridge.FIP_URI });
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