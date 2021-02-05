package main.java.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.components.washer.connectors.WasherConnector;
import main.java.components.washer.interfaces.WasherCI;
import main.java.components.washer.ports.WasherOutboundPort;
import main.java.utils.Log;

/**
 * Tester for the Washer component
 * 
 * @author Bello Memmi
 *
 */
@RequiredInterfaces(required = { WasherCI.class })
public class WasherUnitTester extends AbstractComponent {

	/**
	 * Washer outbound port for WasherUnitTester
	 */
	protected WasherOutboundPort wop;

	/**
	 * Washer inbound port to connect to URI
	 */
	protected String wipURI;

	protected WasherUnitTester(String wipURI) throws Exception {
		super(1, 0);
		this.initialise(wipURI);
	}

	private void initialise(String wipURI) throws Exception {
		this.wipURI = wipURI;
		this.wop = new WasherOutboundPort(this);
		this.wop.publishPort();

		this.tracer.get().setTitle("Washer tester component");
		this.tracer.get().setRelativePosition(1, 0);
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
			this.doPortConnection(this.wop.getPortURI(), wipURI, WasherConnector.class.getCanonicalName());
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
		this.doPortDisconnection(this.wop.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.wop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// TESTS
	// -------------------------------------------------------------------------

	public void testOn() {
		Log.printAndLog(this, "testOn()");
		try {
			this.wop.turnOn();
			assertTrue(this.wop.isTurnedOn());
		} catch (Exception e)
		{
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");

	}

	public void testOff() {
		Log.printAndLog(this, "testOff()");
		try {
			this.wop.turnOff();
			assertTrue(!this.wop.isTurnedOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");

	}
	public void testUpMode() {
		Log.printAndLog(this, "testUpMode()");
		try {
			this.wop.turnOn();
			int cur_value = this.wop.currentMode();
			this.wop.upMode();
			assertEquals(this.wop.currentMode(), (cur_value + 1) % 3);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testDownMode() {
		Log.printAndLog(this, "testDownMode()");
		try {
			int cur_value = this.wop.currentMode();
			this.wop.downMode();
			assertEquals(this.wop.currentMode(), Math.floorMod((cur_value - 1), 3));
		} catch (Exception e) {
			System.err.println("Error occured in test down mode");
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testSetMode() {
		Log.printAndLog(this, "testSetMode()");
		try {
			int exp_value = (this.wop.currentMode() + 1) % 3;
			this.wop.setMode(exp_value);
			assertEquals(exp_value, this.wop.currentMode());
		} catch (Exception e) {
			System.err.println("Error occured in test down mode");
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testPlanifyTest() {
		Log.printAndLog(this, "testPlanifyTest()");
		try {
			Duration d = Duration.ofHours(1);
			LocalTime deadline = LocalTime.now().plusHours(3);
			this.wop.planifyEvent(d, deadline);
			assertTrue(this.wop.hasPlan());

		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testCancel() {
		Log.printAndLog(this, "testCancel()");
		try {
			Duration d = Duration.ofHours(1);
			LocalTime deadline = LocalTime.now().plusHours(3);
			this.wop.planifyEvent(d, deadline);
			assertTrue(this.wop.hasPlan());
			this.wop.cancel();
			assertTrue(!this.wop.hasPlan());
			assertEquals(this.wop.deadline(), null);
			assertEquals(this.wop.startTime(), null);
			assertEquals(this.wop.duration(), null);

		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testPostpone() {
		Log.printAndLog(this, "testPostpone");
		try {
			Duration d = Duration.ofHours(1);
			LocalTime deadline = LocalTime.now().plusHours(3);
			Duration postponeDuration = Duration.ofHours(4);
			this.wop.planifyEvent(d, deadline);
			this.wop.postpone(postponeDuration);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testDuration() {
		Log.printAndLog(this, "testDuration");
		try {
			Duration d = Duration.ofHours(1);
			LocalTime deadline = LocalTime.now().plusHours(3);
			this.wop.planifyEvent(d, deadline);
			assertEquals(d, this.wop.duration());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testDeadline() {
		Log.printAndLog(this, "testDeadline");
		try {
			Duration d = Duration.ofHours(1);
			LocalTime deadline = LocalTime.now().plusHours(3);
			this.wop.planifyEvent(d, deadline);
			assertEquals(deadline, this.wop.deadline());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Run all the tests
	 */
	private void runAllTests() {

		this.testUpMode();
		this.testDownMode();
		this.testSetMode();
		this.testPlanifyTest();
		this.testCancel();
		this.testDuration();
		this.testDeadline();

		Log.printAndLog(this, "all tests Washer passed");
	}
}
