package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.WasherCI;
import interfaces.WasherImplementationI;
import ports.WasherInboundPort;

@OfferedInterfaces(offered = {WasherCI.class})
public class Washer extends AbstractComponent implements WasherImplementationI {
    /**
     * Component URI
     */
    protected String myUri;

    /**
     * Current state of the washer
     */
    protected boolean isWorking;

    /**
     * operating water temperature
     */
    protected int operating_temperature;

    /**
     * Inbound port of the washer
     */
    protected WasherInboundPort bip;

    /**
     *
     * @param reflectionPortURI
     * @param bipURI
     * @throws Exception
     */
    protected Washer(String reflectionPortURI, String bipURI) throws Exception {
        super(reflectionPortURI, 1, 0);
        myUri = reflectionPortURI;
    }

    /**
     * <pre>
     *     pre      {@code washerInboundPortURI != null}
     *     pre      {@code washerInboundPortURI.isEmpty()}
     *     post     {@code getStateWasher == false }
     *     post     {@code getTemperatureOperating == 0}
     * </pre>
     * @param washerInboundPortURI
     * @throws Exception
     */
    protected void initialise(String washerInboundPortURI) throws Exception{
        assert washerInboundPortURI != null : new PreconditionException("washerInboundPortUri != null");
        assert !washerInboundPortURI.isEmpty() : new PreconditionException("washerInboundPortURI.isEmpty()");
        this.isWorking = false;
        this.operating_temperature = 20; //ambient temperature for now
        this.bip = new WasherInboundPort(washerInboundPortURI, this);
        this.bip.publishPort();
    }

    @Override
    public boolean getStateWasher() throws Exception {
        return isWorking;
    }

    @Override
    public void turnOnWasher(int operating_temperature) throws Exception {
        this.isWorking = true;
        this.operating_temperature = operating_temperature;
    }

    @Override
    public void turnOffWasher() throws Exception {
        this.isWorking = false;
    }

    @Override
    public int getOperatingTemperature() throws Exception {
        return this.operating_temperature;
    }
}
