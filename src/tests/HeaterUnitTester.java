package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import connectors.HeaterConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.HeaterCI;
import ports.HeaterOutboundPort;
import utils.Log;

/**
 * Tester for the Heater component
 * 
 * @author Bello Memmi
 *
 */
@RequiredInterfaces(required = { HeaterCI.class })
public class HeaterUnitTester extends AbstractComponent {

	/**
	 * Heater outbound port for HeaterUnitTester
	 */
	protected HeaterOutboundPort hop;

	/**
	 * Heater inbound port to connect to URI
	 */
	protected String hipURI;

	/**
	 * HeaterUnitTester constructor
	 * 
	 * @param hipURI heater inbound port uri to connect to
	 * @throws Exception
	 */
	protected HeaterUnitTester(String hipURI) throws Exception {
		super(1, 0);
		this.initialise(hipURI);
	}

	/**
	 * initialise the component
	 * 
	 * @param hipURI heater inbound port uri to connect to
	 * @throws Exception
	 */
	protected void initialise(String hipURI) throws Exception {
		this.hipURI = hipURI;
		this.hop = new HeaterOutboundPort(this);
		this.hop.publishPort();

		this.tracer.get().setTitle("Heater tester component");
		this.tracer.get().setRelativePosition(1, 2);
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
			this.doPortConnection(this.hop.getPortURI(), hipURI, HeaterConnector.class.getCanonicalName());
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
		this.doPortDisconnection(this.hop.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.hop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
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
			assertEquals(20, hop.getRequestedTemperature());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the turnOn method
	 */
	public void testTurnOn() {
		Log.printAndLog(this, "test turnOff()");
		try {
			hop.turnOn();
			assertTrue(hop.isHeaterOn());
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
			hop.turnOff();
			assertFalse(hop.isHeaterOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the isHeaterTurnedOn method
	 */
	public void testIsHeaterOn() {
		Log.printAndLog(this, "test adjustPower()");
		try {
			assertFalse(hop.isHeaterOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the setRequestedTemperature method
	 */
	public void testSetRequestedTemperature() {
		Log.printAndLog(this, "test isTurnedOn()");
		try {
			hop.setRequestedTemperature(30);
			assertEquals(30, hop.getRequestedTemperature());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Run all the tests
	 */
	protected void runAllTests() {
		testGetRequestedTemperature();
		testTurnOn();
		testTurnOff();
		testIsHeaterOn();
		testSetRequestedTemperature();
	}

}
