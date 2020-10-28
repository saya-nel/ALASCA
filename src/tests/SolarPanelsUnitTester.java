package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import connectors.SolarPanelsConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.SolarPanelsCI;
import ports.SolarPanelsOutboundPort;
import utils.Log;

/**
 * Tester for the SolarPanels component
 * 
 * @author Bello Memmi
 *
 */
@RequiredInterfaces(required = { SolarPanelsCI.class })
public class SolarPanelsUnitTester extends AbstractComponent {

	/**
	 * SolarPanels outbound port for SolarPanelsUnitTester
	 */
	protected SolarPanelsOutboundPort spop;

	/**
	 * SolarPanels inbound port to connect to URI
	 */
	protected String spipURI;

	/**
	 * SolarPanelsUnitTester constructor
	 * 
	 * @param spipURI solar panels inbound port uri to connect to
	 * @throws Exception
	 */
	protected SolarPanelsUnitTester(String spipURI) throws Exception {
		super(1, 0);
		this.initialise(spipURI);
	}

	/**
	 * initialise the component
	 * 
	 * @param spipURI solar panels inbound port uri to connect to
	 * @throws Exception
	 */
	protected void initialise(String spipURI) throws Exception {
		this.spipURI = spipURI;
		this.spop = new SolarPanelsOutboundPort(this);
		this.spop.publishPort();

		this.tracer.get().setTitle("SolarPanels tester component");
		this.tracer.get().setRelativePosition(1, 3);
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
			this.doPortConnection(this.spop.getPortURI(), spipURI, SolarPanelsConnector.class.getCanonicalName());
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
		this.doPortDisconnection(this.spop.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.spop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// TESTS
	// -------------------------------------------------------------------------

	/**
	 * Test the getCurrentEnergyProduction method
	 */
	public void testGetCurrentEnergyProduction() {
		Log.printAndLog(this, "test getCurrentEnergyProduction()");
		try {
			assertEquals(0, spop.getCurrentEnergyProduction());
		} catch (Exception e) {
			assertTrue(false);
		}
		Log.printAndLog(this, "done...");
	}

	/**
	 * Run all the tests
	 */
	protected void runAllTests() {
		testGetCurrentEnergyProduction();
	}

}
