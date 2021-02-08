package main.java.components.petrolGenerator.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.components.petrolGenerator.connectors.PetrolGeneratorConnector;
import main.java.components.petrolGenerator.interfaces.PetrolGeneratorCI;
import main.java.components.petrolGenerator.ports.PetrolGeneratorOutboundPort;
import main.java.utils.Log;

/**
 * 
 * Test class for PetrolGenerator component
 * 
 * @author Bello Memmi
 */
@RequiredInterfaces(required = { PetrolGeneratorCI.class })
public class PetrolGeneratorUnitTester extends AbstractComponent {

	/**
	 * Petrol generator outbound port
	 */
	protected PetrolGeneratorOutboundPort pgop;

	/**
	 * Petrol generator inbound port to connect to URI
	 */
	protected String pgipURI;

	/**
	 * PetrolGeneratorUnitTester constructor
	 * 
	 * @param bipURI battery inbound port uri to connect to
	 * @throws Exception
	 */
	protected PetrolGeneratorUnitTester(String pgipURI) throws Exception {
		super(1, 0);
		this.initialise(pgipURI);
	}

	/**
	 * initialise the component
	 * 
	 * @param pgipURI petrol generator inbound port uri to connect to
	 * @throws Exception
	 */
	protected void initialise(String pgipURI) throws Exception {
		this.pgipURI = pgipURI;
		this.pgop = new PetrolGeneratorOutboundPort(this);
		this.pgop.publishPort();

		this.tracer.get().setTitle("PetrolGenerator tester component");
		this.tracer.get().setRelativePosition(0, 2);
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
			this.doPortConnection(this.pgop.getPortURI(), pgipURI, PetrolGeneratorConnector.class.getCanonicalName());
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
		this.doPortDisconnection(this.pgop.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.pgop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// TESTS
	// -------------------------------------------------------------------------

	/**
	 * Test the getMaxLevel method
	 */
	public void testGetMaxLevel() {
		Log.printAndLog(this, "test getMaxLevel()");
		try {
			assertEquals(5, this.pgop.getMaxLevel());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the getPetrolLevel method
	 */
	public void testGetPetrolLevel() {
		Log.printAndLog(this, "test getPetrolLevel()");
		try {
			assertEquals(2, this.pgop.getPetrolLevel());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the addPetrol method
	 */
	public void testAddPetrol() {
		Log.printAndLog(this, "test addPetrol()");
		try {
			this.pgop.addPetrol(1);
			assertEquals(3, this.pgop.getPetrolLevel());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Test the turnOn method
	 */
	public void testTurnOn() {
		Log.printAndLog(this, "test turnOn()");
		try {
			pgop.turnOn();
			assertTrue(pgop.isTurnedOn());
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
			pgop.turnOff();
			assertFalse(pgop.isTurnedOn());
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
			pgop.turnOn();
			assertTrue(pgop.isTurnedOn());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	public void testFillAll() {
		Log.printAndLog(this, "test fillAll()");
		try {
			pgop.fillAll();
			assertEquals(pgop.getMaxLevel(), pgop.getPetrolLevel());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Run all the tests
	 */
	private void runAllTests() {
		testGetMaxLevel();
		testGetPetrolLevel();
		testAddPetrol();
		testTurnOn();
		testTurnOff();
		testIsTurnedOn();
		testFillAll();
		Log.printAndLog(this, "all tests passed");
	}
}
