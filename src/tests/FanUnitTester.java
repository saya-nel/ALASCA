package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import connectors.FanConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.FanCI;
import ports.FanOutboundPort;
import utils.FanLevel;
import utils.Log;

/**
 * Tester for the Fan component
 * 
 * @author Bello Memmi
 *
 */
@RequiredInterfaces(required = { FanCI.class })
public class FanUnitTester extends AbstractComponent {

	/**
	 * Fan outbound port for FanUnitTester
	 */
	protected FanOutboundPort fop;

	/**
	 * Fan inbound port to connect to URI
	 */
	protected String fipURI;

	/**
	 * FanUnitTester constructor
	 * 
	 * @param fipURI fan inbound port uri to connect to
	 * @throws Exception
	 */
	protected FanUnitTester(String fipURI) throws Exception {
		super(1, 0);
		this.initialise(fipURI);
	}

	/**
	 * initialise the component
	 * 
	 * @param fipURI fan inbound port uri to connect to
	 * @throws Exception
	 */
	protected void initialise(String fipURI) throws Exception {
		this.fipURI = fipURI;
		this.fop = new FanOutboundPort(this);
		this.fop.publishPort();

		this.tracer.get().setTitle("Fan tester component");
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
			this.doPortConnection(this.fop.getPortURI(), fipURI, FanConnector.class.getCanonicalName());
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
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// TESTS
	// -------------------------------------------------------------------------

	/**
	 * Test the turnOn method
	 */
	public void testTurnOn() {
		Log.printAndLog(this, "test turnOn()");
		try {
			fop.turnOn();
			assertTrue(fop.isTurnedOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the turnOff method
	 */
	public void testTurnOff() {
		Log.printAndLog(this, "test turnOff()");
		try {
			fop.turnOff();
			assertFalse(fop.isTurnedOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the adjustPower method
	 */
	public void testAdjustPower() {
		Log.printAndLog(this, "test adjustPower()");
		try {
			fop.adjustPower(FanLevel.HIGH);
			assertEquals(FanLevel.HIGH, fop.getFanLevel());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the isTurnedOn method
	 */
	public void testIsTurnedOn() {
		Log.printAndLog(this, "test isTurnedOn()");
		try {
			fop.turnOn();
			assertTrue(fop.isTurnedOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the getFanLevel method
	 */
	public void testGetFanLevel() {
		Log.printAndLog(this, "test isTurnedOn()");
		try {
			fop.adjustPower(FanLevel.MID);
			assertEquals(FanLevel.MID, fop.getFanLevel());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Run all the tests
	 */
	protected void runAllTests() {
		testTurnOn();
		testTurnOff();
		testAdjustPower();
		testIsTurnedOn();
		testGetFanLevel();
	}
}
