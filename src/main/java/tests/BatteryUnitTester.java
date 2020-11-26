package main.java.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.connectors.BatteryConnector;
import main.java.connectors.ControlBatteryConnector;
import main.java.interfaces.BatteryCI;
import main.java.ports.BatteryOutboundPort;
import main.java.utils.Log;

import java.time.Duration;
import java.time.LocalTime;

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

	public void testUpMode() {
		Log.printAndLog(this, "testUpMode()");
		try {
			int cur_value = this.bop.currentMode();
			this.bop.upMode();
			assertEquals(this.bop.currentMode(), (cur_value + 1) % 3);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the getBatteryCharge method
	 */
	public void testGetBatteryCharge() {
		Log.printAndLog(this, "test getBatteryCharge()");
		try {
			assertEquals(0, this.bop.getBatteryCharge());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testDownMode() {
		Log.printAndLog(this, "testDownMode()");
		try{
			int cur_value = this.bop.currentMode();
			this.bop.downMode();
			assertEquals(this.bop.currentMode(), Math.floorMod((cur_value -1), 3));
		} catch(Exception e){
			System.err.println("Error occured in test down mode");
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testSetMode() {
		Log.printAndLog(this, "testSetMode()");
		try{
			int exp_value = (this.bop.currentMode() + 1)%3;
			this.bop.setMode(exp_value);
			assertEquals(exp_value, this.bop.currentMode());
		} catch(Exception e){
			System.err.println("Error occured in test down mode");
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testPlanifyTest() {
		Log.printAndLog(this, "testPlanifyTest()");
		try{
			Duration d = Duration.ofHours(1);
			LocalTime deadline = LocalTime.now().plusHours(3);
			this.bop.planifyEvent(d, deadline);
			assertTrue(this.bop.hasPlan());

		} catch(Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testCancel() {
		Log.printAndLog(this, "testCancel()");
		try{
			Duration d = Duration.ofHours(1);
			LocalTime deadline = LocalTime.now().plusHours(3);
			this.bop.planifyEvent(d, deadline);
			assertTrue(this.bop.hasPlan());
			this.bop.cancel();
			assertTrue(!this.bop.hasPlan());
			assertEquals(this.bop.deadline(), null);
			assertEquals(this.bop.startTime(), null);
			assertEquals(this.bop.duration(), null);

		} catch(Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testPostpone() {
		Log.printAndLog(this, "testPostpone");
		try{
			Duration d = Duration.ofHours(1);
			LocalTime deadline = LocalTime.now().plusHours(3);
			Duration postponeDuration = Duration.ofHours(4);
			this.bop.planifyEvent(d, deadline);
			this.bop.postpone(postponeDuration);
		} catch(Exception e){
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testDuration() {
		Log.printAndLog(this, "testDuration");
		try{
			Duration d = Duration.ofHours(1);
			LocalTime deadline = LocalTime.now().plusHours(3);
			this.bop.planifyEvent(d,deadline);
			assertEquals(d,this.bop.duration());
		}catch(Exception e){
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testDeadline() {
		Log.printAndLog(this, "testDeadline");
		try{
			Duration d = Duration.ofHours(1);
			LocalTime deadline = LocalTime.now().plusHours(3);
			this.bop.planifyEvent(d, deadline);
			assertEquals(deadline, this.bop.deadline());
		} catch(Exception e){
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}
	/**
	 * Run all the tests
	 */
	protected void runAllTests() {
		//this.testGetBatteryCharge();
		this.testUpMode();
		this.testDownMode();
		this.testSetMode();
		this.testPlanifyTest();
		this.testCancel();
		this.testDuration();
		this.testDeadline();
		//this.testPostpone();
		Log.printAndLog(this, "all tests passed");
	}

}
