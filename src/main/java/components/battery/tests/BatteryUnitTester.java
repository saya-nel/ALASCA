package main.java.components.battery.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.components.battery.connectors.BatteryConnector;
import main.java.components.battery.interfaces.BatteryCI;
import main.java.components.battery.ports.BatteryOutboundPort;
import main.java.utils.Log;

/**
 * Tester for the Battery component
 * 
 * @author Bello Memmi
 *
 */
@RequiredInterfaces(required = { BatteryCI.class })
public class BatteryUnitTester extends AbstractComponent {

	/**
	 * Battery outbound port for BatteryUnitTester
	 */
	protected BatteryOutboundPort bop;

	/**
	 * Battery inbound port to connect to URI
	 */
	protected String bipURI;

	/**
	 * Inbound port of the controller
	 */
	protected String cipURI;

	/**
	 * BatteryUnitTester constructor
	 * 
	 * @param bipURI battery inbound port uri to connect to
	 * @throws Exception
	 */
	protected BatteryUnitTester(String bipURI) throws Exception {
		super(1, 0);
		this.initialise(bipURI);
	}

	/**
	 * initialise the component
	 * 
	 * @param bipURI battery inbound port uri to connect to
	 * @throws Exception
	 */
	protected void initialise(String bipURI) throws Exception {
		this.bipURI = bipURI;
		this.bop = new BatteryOutboundPort(this);
		this.bop.publishPort();

		this.tracer.get().setTitle("Battery tester component");
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
			this.doPortConnection(this.bop.getPortURI(), bipURI, BatteryConnector.class.getCanonicalName());
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
		this.doPortDisconnection(this.bop.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.bop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// TESTS
	// -------------------------------------------------------------------------

	public void testGetBatteryCharge() {
		Log.printAndLog(this, "testGetBatteryCharge()");
		try {
			assertEquals(this.bop.getBatteryCharge(), 5);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testUpMode() {
		Log.printAndLog(this, "testUpMode()");
		try {
			this.bop.setMode(0);
			this.bop.upMode();
			assertEquals(this.bop.currentMode(), 1);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testDownMode() {
		Log.printAndLog(this, "testDownMode()");
		try {
			this.bop.setMode(1);
			this.bop.downMode();
			assertEquals(this.bop.currentMode(), 0);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testSetMode() {
		Log.printAndLog(this, "testSetMode()");
		try {
			this.bop.setMode(1);
			assertEquals(1, this.bop.currentMode());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testCurrentMode() {
		Log.printAndLog(this, "testCurrentMode()");
		try {
			this.bop.setMode(0);
			assertEquals(0, this.bop.currentMode());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testHasPlan() {
		Log.printAndLog(this, "testHasPlan()");
		try {
			LocalTime starTime = LocalTime.now().plusHours(1);
			LocalTime endTime = LocalTime.now().plusHours(2);
			this.bop.planifyEvent(starTime, endTime);
			assertTrue(this.bop.hasPlan());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testStartTime() {
		Log.printAndLog(this, "testStartTime()");
		try {
			LocalTime starTime = LocalTime.now().plusHours(1);
			LocalTime endTime = LocalTime.now().plusHours(2);
			this.bop.planifyEvent(starTime, endTime);
			assertEquals(starTime, this.bop.startTime());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testDuration() {
		Log.printAndLog(this, "testDuration");
		try {
			LocalTime starTime = LocalTime.now().plusHours(1);
			LocalTime endTime = LocalTime.now().plusHours(2);
			this.bop.planifyEvent(starTime, endTime);
			assertEquals(Duration.between(starTime, endTime), this.bop.duration());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testDeadline() {
		Log.printAndLog(this, "testDeadline");
		try {
			LocalTime starTime = LocalTime.now().plusHours(1);
			LocalTime endTime = LocalTime.now().plusHours(2);
			this.bop.planifyEvent(starTime, endTime);
			assertEquals(endTime, this.bop.deadline());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testPostpone() {
		Log.printAndLog(this, "testPostpone");
		try {
			LocalTime starTime = LocalTime.now().plusHours(1);
			LocalTime endTime = LocalTime.now().plusHours(2);
			this.bop.planifyEvent(starTime, endTime);
			this.bop.postpone(Duration.ofHours(1));
			assertEquals(starTime.plusHours(1), this.bop.startTime());
			assertEquals(endTime.plusHours(1), this.bop.deadline());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testCancel() {
		Log.printAndLog(this, "testCancel()");
		try {
			LocalTime starTime = LocalTime.now().plusHours(1);
			LocalTime endTime = LocalTime.now().plusHours(2);
			this.bop.planifyEvent(starTime, endTime);
			assertTrue(this.bop.hasPlan());
			this.bop.cancel();
			assertFalse(this.bop.hasPlan());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testPlanifyEvent() {
		Log.printAndLog(this, "testPlanifyEvent()");
		try {
			LocalTime starTime = LocalTime.now().plusHours(1);
			LocalTime endTime = LocalTime.now().plusHours(2);
			this.bop.planifyEvent(starTime, endTime);
			assertTrue(this.bop.hasPlan());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Run all the tests
	 */
	protected void runAllTests() {
		this.testGetBatteryCharge();
		this.testUpMode();
		this.testDownMode();
		this.testSetMode();
		this.testCurrentMode();
		this.testHasPlan();
		this.testStartTime();
		this.testDuration();
		this.testDeadline();
		this.testPostpone();
		this.testCancel();
		this.testPlanifyEvent();
		Log.printAndLog(this, "all tests passed");
	}

}
