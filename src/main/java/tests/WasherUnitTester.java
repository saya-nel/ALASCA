package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import connectors.ControlWasherConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.WasherCI;
import ports.ControlWasherOutboundPort;
import utils.Log;

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
	protected ControlWasherOutboundPort wop;

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
		this.wop = new ControlWasherOutboundPort(this);
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
			this.doPortConnection(this.wop.getPortURI(), wipURI, ControlWasherConnector.class.getCanonicalName());
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

	/**
	 * Test the isTurnedOn method
	 */
	private void testIsTurnedOn() {
		Log.printAndLog(this, "test isTurnedOn()");
		try {
			assertFalse(wop.isTurnedOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the turnOn method
	 */
	private void testTurnOn() {
		Log.printAndLog(this, "test turnOnWasher()");
		try {
			wop.turnOn();
			assertTrue(wop.isTurnedOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the turnOff method
	 */
	private void testTurnOff() {
		Log.printAndLog(this, "test turnOff()");
		try {
			wop.turnOff();
			assertFalse(wop.isTurnedOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the getProgramTemperature method
	 */
	private void testGetProgramTemperature() {
		Log.printAndLog(this, "test getRequestedTemperature()");
		try {
			assertEquals(30, wop.getProgramTemperature());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the setProgramTemperature method
	 */
	private void testSetProgramTemperature() {
		Log.printAndLog(this, "test setProgramTemperature()");
		try {
			wop.setProgramTemperature(50);
			assertEquals(50, wop.getProgramTemperature());
			wop.setProgramTemperature(30);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the setProgramDuration method
	 */
	private void testSetProgramDuration() {
		Log.printAndLog(this, "test setProgramDuration()");
		try {
			wop.setProgramDuration(20);
			assertEquals(20, wop.getProgramDuration());
			wop.setProgramDuration(60);
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the getProgramDuration method
	 */
	private void testGetProgramDuration() {
		Log.printAndLog(this, "test getProgramDuration()");
		try {
			assertEquals(60, wop.getProgramDuration());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}


	/**
	 * Run all the tests
	 */
	private void runAllTests() {
		testIsTurnedOn();
		testTurnOn();
		testTurnOff();
		testGetProgramTemperature();
		testSetProgramTemperature();
		testSetProgramDuration();
		testGetProgramDuration();

		Log.printAndLog(this, "all tests passed");
	}
}
