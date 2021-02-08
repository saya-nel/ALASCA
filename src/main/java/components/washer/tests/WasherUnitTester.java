package main.java.components.washer.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

	public void testIsTurnedOn() {
		Log.printAndLog(this, "testOn()");
		try {
			this.wop.turnOn();
			Thread.sleep(1000); // wait for the washer to turn on
			assertTrue(this.wop.isTurnedOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testTurnOn() {
		Log.printAndLog(this, "testOn()");
		try {
			this.wop.turnOn();
			assertTrue(this.wop.isTurnedOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testTurnOff() {
		Log.printAndLog(this, "testOff()");
		try {
			this.wop.turnOff();
			assertTrue(!this.wop.isTurnedOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testSetProgramTemperature() {
		Log.printAndLog(this, "testSetProgramTemperature()");
		try {
			this.wop.setProgramTemperature(30);
			assertEquals(30, this.wop.getProgramTemperature());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testGetProgramTemperature() {
		Log.printAndLog(this, "testGetProgramTemperature()");
		try {
			this.wop.setProgramTemperature(40);
			assertEquals(40, this.wop.getProgramTemperature());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testUpMode() {
		Log.printAndLog(this, "testUpMode()");
		try {
			this.wop.setMode(0);
			this.wop.upMode();
			assertEquals(this.wop.currentMode(), 1);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testDownMode() {
		Log.printAndLog(this, "testDownMode()");
		try {
			this.wop.setMode(1);
			this.wop.downMode();
			assertEquals(this.wop.currentMode(), 0);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testSetMode() {
		Log.printAndLog(this, "testSetMode()");
		try {
			this.wop.setMode(1);
			assertEquals(1, this.wop.currentMode());
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
			this.wop.planifyEvent(starTime, endTime);
			assertTrue(this.wop.hasPlan());
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
			this.wop.planifyEvent(starTime, endTime);
			assertEquals(starTime, this.wop.startTime());
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
			this.wop.planifyEvent(starTime, endTime);
			assertEquals(Duration.between(starTime, endTime), this.wop.duration());
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
			this.wop.planifyEvent(starTime, endTime);
			assertEquals(endTime, this.wop.deadline());
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
			this.wop.planifyEvent(starTime, endTime);
			this.wop.postpone(Duration.ofHours(1));
			assertEquals(starTime.plusHours(1), this.wop.startTime());
			assertEquals(endTime.plusHours(1), this.wop.deadline());
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
			this.wop.planifyEvent(starTime, endTime);
			assertTrue(this.wop.hasPlan());
			this.wop.cancel();
			assertFalse(this.wop.hasPlan());
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
			this.wop.planifyEvent(starTime, endTime);
			assertTrue(this.wop.hasPlan());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Run all the tests
	 */
	private void runAllTests() {
		this.testIsTurnedOn();
		this.testTurnOn();
		this.testTurnOff();
		this.testGetProgramTemperature();
		this.testSetProgramTemperature();
		this.testUpMode();
		this.testDownMode();
		this.testSetMode();
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
