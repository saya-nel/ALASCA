package tests;

import connectors.FridgeConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.FridgeCI;
import ports.FridgeOutboundPort;
import utils.Log;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tester for the Fridge component
 *
 * @author Bello Memmi
 *
 */
@RequiredInterfaces(required = {FridgeCI.class })
public class FridgeUnitTester extends AbstractComponent {
    /**
     * Fridge outbound port for FridgeUnitTester
     */
    protected FridgeOutboundPort fop;

    /**
     * Fridge inbound port to connect to URI
     */
    protected String fipURI;

    /**
     * FridgeUnitTester constructor
     * @param fipURI        fridge inbound port uri
     * @throws Exception
     */
    protected FridgeUnitTester(String fipURI) throws Exception {
        super(1, 0);
        this.initialise(fipURI);
    }

    /**
     *
     * @param fipURI        inbound port's uri
     * @throws Exception
     */
    protected void initialise(String fipURI) throws Exception {
        this.fipURI = fipURI;
        this.fop = new FridgeOutboundPort(this);
        this.fop.publishPort();
        this.tracer.get().setTitle("Fridge tester component");
        this.tracer.get().setRelativePosition(0,0);
        this.toggleTracing();
    }
    // -------------------------------------------------------------------------
    // Component life-cycle
    // -------------------------------------------------------------------------

    /**
     * @see AbstractComponent#start()
     */
    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();
        try {
            this.doPortConnection(this.fop.getPortURI(), this.fipURI, FridgeConnector.class.getCanonicalName());
        } catch (Exception e) {
            throw new ComponentStartException(e);
        }
    }

    /**
     * @see AbstractComponent#execute()
     */
    @Override
    public synchronized void execute() throws Exception {
        super.execute();
        this.runAllTests();
    }

    /**
     * @see AbstractComponent#finalise()
     */
    @Override
    public synchronized void finalise() throws Exception {
        this.doPortDisconnection(this.fop.getPortURI());
        super.finalise();
    }

    /**
     * @see AbstractComponent#shutdown()
     */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            this.fop.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException();
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
            assertEquals(10, this.fop.getRequestedTemperature());
        } catch (Exception e) {
            assertTrue(false);
        }
        Log.printAndLog(this, "done...");
    }

    /**
     * Test the setRequestedTemperature method
     */
    public void testSetRequestedTemperature() {
        Log.printAndLog(this, "test setRequestedTemperatuer()");
        try {
            fop.setRequestedTemperature(12);
            assertEquals(12, this.fop.getRequestedTemperature());
        } catch (Exception e) {
            assertTrue(false);
        }
        Log.printAndLog(this, "done...");
    }
    /**
     * Test the getCurrentTemperature method
     */
    public void testGetCurrentTemperature() {
        Log.printAndLog(this, "test getCurrentTemperature()");
        try {
            assertEquals(20, this.fop.getCurrentTemperature());
        } catch (Exception e)
        {
            assertTrue(false);
        }
        Log.printAndLog(this, "done...");
    }

    /**
     * Test the getState() method
     */
    public void testGetState() {
        Log.printAndLog(this, "test getState()");
        try {
            assertEquals(false, this.fop.getState());
        } catch (Exception e) {
            assertTrue(false);
        }
        Log.printAndLog(this, "done...");
    }

    /**
     * Test the switchOn() method
     */
    public void testSwitchOn() {
        Log.printAndLog(this, "test switchOn()");
        try {
            this.fop.switchOn();
            assertTrue(this.fop.getState());
        } catch (Exception e)
        {
            assertTrue(false);
        }
        Log.printAndLog(this, "done...");
    }

    /**
     * Test the switchOff() method
     */
    public void testSwitchOff() {
        Log.printAndLog(this, "test switchOff()");
        try {
            this.fop.switchOff();
            assertFalse(this.fop.getState());
        } catch (Exception e)
        {
            assertTrue(false);
        }
        Log.printAndLog(this, "done...");
    }

    /**
     * Run all the tests
     */
    protected void runAllTests() {
        this.testGetCurrentTemperature();
        this.testGetRequestedTemperature();
        this.testSetRequestedTemperature();
        this.testGetState();
        this.testSwitchOn();
        this.testSwitchOff();
        Log.printAndLog(this, "all test passed");
    }
}
