package main.java.components.fridge.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.components.fridge.connectors.FridgeConnector;
import main.java.components.fridge.interfaces.FridgeCI;
import main.java.components.fridge.ports.FridgeOutboundPort;
import main.java.utils.Log;

/**
 * The class <code>FridgeUnitTester</code> implements a component performing
 * unit tests for the class <code>Fridge</code> as a BCM component.
 * 
 * @author Bello Memmi
 *
 */
@RequiredInterfaces(required = { FridgeCI.class })
public class FridgeUnitTester extends AbstractComponent {
	/**
	 * Fridge outbound port for FridgeUnitTester
	 */
	protected FridgeOutboundPort fop;

	/**
	 * Fridge inbound port to connect to URI
	 */
	protected String fipURI;

	/**
	 * FridgeUnitTester constructor
	 * 
	 * @param fipURI fridge inbound port uri
	 * @throws Exception
	 */
	protected FridgeUnitTester(String fipURI) throws Exception {
		super(1, 0);
		this.initialise(fipURI);
	}

	/**
	 *
	 * @param fipURI inbound port's uri
	 * @throws Exception
	 */
	protected void initialise(String fipURI) throws Exception {
		this.fipURI = fipURI;
		this.fop = new FridgeOutboundPort(this);
		this.fop.publishPort();
		this.tracer.get().setTitle("Fridge tester component");
		this.tracer.get().setRelativePosition(0, 0);
		this.toggleTracing();
	}
	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();
		try {
			this.doPortConnection(this.fop.getPortURI(), this.fipURI, FridgeConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void execute() throws Exception {
		super.execute();
		this.runAllTests();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void finalise() throws Exception {
		this.doPortDisconnection(this.fop.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.fop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException();
		}
		super.shutdown();
	}
	// -------------------------------------------------------------------------
	// TESTS
	// -------------------------------------------------------------------------

	/**
	 * Test the upMode method
	 */
	public void testUpMode() {
		Log.printAndLog(this, "testUpMode()");
		try {
			this.fop.setMode(0);
			this.fop.upMode();
			assertEquals(this.fop.currentMode(), 1);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the downMode method
	 */
	public void testDownMode() {
		Log.printAndLog(this, "testDownMode()");
		try {
			this.fop.setMode(1);
			this.fop.downMode();
			assertEquals(this.fop.currentMode(), 0);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the setMode method
	 */
	public void testSetMode() {
		Log.printAndLog(this, "testSetMode()");
		try {
			this.fop.setMode(1);
			assertEquals(1, this.fop.currentMode());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the currentMode method
	 */
	public void testCurrentMode() {
		Log.printAndLog(this, "testGetMode()");
		try {
			this.fop.setMode(0);
			assertEquals(0, this.fop.currentMode());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 *
	 * TEST SUSPENSIBLE METHODS
	 *
	 */

	/**
	 * Test the suspended method
	 */
	public void testSuspended() {
		Log.printAndLog(this, "testSuspended()");
		try {
			this.fop.suspend();
			assertTrue(this.fop.suspended());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the suspend method
	 */
	public void testSuspend() {
		Log.printAndLog(this, "testSuspend()");
		try {
			this.fop.suspend();
			assertTrue(this.fop.suspended());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the resume method
	 */
	public void testResume() {
		Log.printAndLog(this, "testResume()");
		try {
			this.fop.resume();
			assertTrue(!this.fop.suspended());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the emergency method
	 */
	public void testEmergency() {
		Log.printAndLog(this, "testEmergency()");
		try {
			this.fop.suspend();
			this.fop.emergency();
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/**
	 * Run all the tests
	 */
	protected void runAllTests() {
		this.testUpMode();
		this.testDownMode();
		this.testSetMode();
		this.testCurrentMode();
		this.testSuspend();
		this.testResume();
		this.testEmergency();
		Log.printAndLog(this, "all test passed");
	}
}
