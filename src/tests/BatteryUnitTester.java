package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import connectors.ControlBatteryConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.BatteryCI;
import ports.BatteryOutboundPort;
import utils.BatteryState;
import utils.Log;

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
	protected  String cipURI;

	
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
			this.doPortConnection(this.bop.getPortURI(), bipURI, ControlBatteryConnector.class.getCanonicalName());
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


	public void testUpMode()  {
		Log.printAndLog(this, "testUpMode()");
		try{
			int cur_value = this.bop.currentMode();
			this.bop.upMode();
			assertEquals(this.bop.currentMode(),(cur_value+1)%3);
		}catch (Exception e) {
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


	/**
	 * Run all the tests
	 */
	protected void runAllTests() {
		this.testGetBatteryCharge();
		this.testUpMode();
		Log.printAndLog(this, "all tests passed");
	}

}
