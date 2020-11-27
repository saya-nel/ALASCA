package main.java.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.connectors.FridgeConnector;
import main.java.interfaces.FridgeCI;
import main.java.ports.FridgeOutboundPort;
import main.java.utils.Log;

/**
 * Tester for the Fridge component
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
	 * Test the getRequestedTemperature method
	 */
	public void testGetRequestedTemperature() {
		Log.printAndLog(this, "test getRequestedTemperature()");
		try {
			assertEquals(10, this.fop.getRequestedTemperature());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the setRequestedTemperature method
	 */
	public void testSetRequestedTemperature() {
		Log.printAndLog(this, "test setRequestedTemperatuer()");
		try {
			fop.setRequestedTemperature(12);
			assertEquals(12, this.fop.getRequestedTemperature());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the getCurrentTemperature method
	 */
	public void testGetCurrentTemperature() {
		Log.printAndLog(this, "test getCurrentTemperature()");
		try {
			assertEquals(20, this.fop.getCurrentTemperature());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 *
	 * TEST STANDARDS EQUIPMENTS METHODS
	 *
	 */
	public void testUpMode() {
		Log.printAndLog(this, "testUpMode()");
		try {
			int cur_value = this.fop.currentMode();
			this.fop.upMode();
			assertEquals(this.fop.currentMode(), (cur_value + 1) % 3);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}


	public void testDownMode() {
		Log.printAndLog(this, "testDownMode()");
		try {
			int cur_value = this.fop.currentMode();
			this.fop.downMode();
			assertEquals(this.fop.currentMode(), Math.floorMod((cur_value - 1), 3));
		} catch (Exception e) {
			System.err.println("Error occured in test down mode");
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testSetMode() {
		Log.printAndLog(this, "testSetMode()");
		try {
			int exp_value = (this.fop.currentMode() + 1) % 3;
			this.fop.setMode(exp_value);
			assertEquals(exp_value, this.fop.currentMode());
		} catch (Exception e) {
			System.err.println("Error occured in test down mode");
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}
	/**
	 *
	 * TEST SUSPENSIBLE METHODS
	 *
	 */
	public void testSuspend() {
		Log.printAndLog(this, "testSuspend()");
		try {
			this.fop.suspend();
			assertTrue(this.fop.suspended());
		} catch (Exception e) {
			System.err.println("Error occured in test down mode");
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testResume() {
		Log.printAndLog(this, "testResume()");
		try {
			this.fop.resume();
			assertTrue(!this.fop.suspended());
		} catch (Exception e) {
			System.err.println("Error occured in test down mode");
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

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
		this.testGetCurrentTemperature();
		this.testGetRequestedTemperature();
		this.testSetRequestedTemperature();
		this.testUpMode();
		this.testDownMode();
		this.testSetMode();
		this.testSuspend();//ordre important
		this.testResume();
		this.testEmergency();
		Log.printAndLog(this, "all test passed");
	}
}
