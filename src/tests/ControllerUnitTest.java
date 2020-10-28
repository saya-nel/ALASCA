package tests;

import connectors.ControllerConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ControllerCI;
import ports.ControllerOutboundPort;
import utils.Log;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredInterfaces(required = { ControllerCI.class})
public class ControllerUnitTest extends AbstractComponent {

    /**
     * Controller outbound port for ControllerUnitTester
     */
    protected ControllerOutboundPort cop;
    /**
     * Controller inbound port to connect to URI
     */
    protected String cipURI;

    /**
     * Controller outbound port uri
     */
    protected String copURI;

    /**
     *
     * @param cipURI        Controller inbound port uri to connect to
     * @throws Exception
     */
    protected ControllerUnitTest(String cipURI) throws Exception {
        super(1,0);
        this.initialise(cipURI);
    }

    /**
     * Initialize the component testing the controller
     * @param cipURI            uri inbound port of controller to connect to
     * @throws Exception
     */
    protected void initialise(String cipURI) throws Exception {
        this.cipURI = cipURI;
        this.cop = new ControllerOutboundPort(this.cipURI,this);
        this.cop.publishPort();

        this.tracer.get().setTitle("Controller tester component");
        this.tracer.get().setRelativePosition(1,0);
        this.toggleTracing();
    }
    /* -----------------------------------------------------------
       Component life-cycle
       -----------------------------------------------------------
    */

    /**
     * @see AbstractComponent#start()
     */
    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();
        try{
            this.doPortConnection(this.cop.getPortURI(), cipURI, ControllerConnector.class.getCanonicalName());
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
        this.doPortDisconnection(this.cop.getPortURI());
        super.finalise();
    }

    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            this.cop.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }
    // ----------------------------------------------------------------------------------
    // TESTS
    // ----------------------------------------------------------------------------------

    /**
     * test registering
     */
    public void testRegister() {
        Log.printAndLog(this, "test register()");
        try {
            String serialNumber = "MYSERIALNUMBER1";
            String XMLFile = "XMLFILE1";
            cop.register(serialNumber, XMLFile);
            assertTrue(this.cop.getRegisteredDevices().get(serialNumber).equals(XMLFile));
            serialNumber = "MYSERIALNUMBER2";
            XMLFile = "XMLFILE2";
            cop.register(serialNumber, XMLFile);
            assertTrue(this.cop.getRegisteredDevices().get(serialNumber).equals(XMLFile));
            // Test register with same key but different value (XMLFile)
            XMLFile = "XMLFILE3";
            cop.register(serialNumber, XMLFile);
            assertTrue(this.cop.getRegisteredDevices().get(serialNumber).equals(XMLFile));
        } catch (Exception e) {
            assertTrue(false);
        }
        Log.printAndLog(this, "done...");
    }

    /**
     * Run all the tests
     */
    protected void runAllTests() {
        this.testRegister();
        Log.printAndLog(this, "all tests passed");
    }
}
