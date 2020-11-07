package components;

/**
 * Fridge component
 *
 * @author Bello Memmi
 *
 */

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.FridgeCI;
import interfaces.FridgeImplementationI;
import ports.FridgeInboundPort;

@OfferedInterfaces(offered = { FridgeCI.class })
public class Fridge extends AbstractComponent implements FridgeCI {
    /**
     * Component URI
     */
    protected String myUri;
    /**
     * Requested Temperature for the fridge
     */
    protected float requestedTemperature;
    /**
     * Actual state of the fridge
     */
    protected boolean isOn;
    /**
     * current temperature inside of fridge
     * pas sûr de l'utilité intégré à la simulation
     */
    protected int currentTemperature;
    /**
     * Inbound port of the fridge component
     */
    protected FridgeInboundPort fip;

    /**
     * @param uri               of the component
     * @param fipURI            inbound port's uri
     * @throws Exception
     */
    protected Fridge(String uri, String fipURI) throws Exception {
        super(uri, 1, 0);
        myUri = uri;

    }

    // -------------------------------------------------------------------------
    // Component life-cycle
    // -------------------------------------------------------------------------

    /**
     * Initialize the fridge component
     *
     * <p>
     *     <strong>Contract</strong>
     * </p>
     *
     * <pre>
     *     pre          {@code fridgeInboundPortURI != null}
     *     pre          {@code !fridgeInboundPortURI.isEmpty()}
     *     post         {@code getCurrentTemperature() == 20}
     *     post         {@code getRequestedTemperature() == 10}
     *     post         {@code getState() == false}
     * </pre>
     * @param fridgeInboundPortURI
     * @throws Exception
     */
    public void initialise(String fridgeInboundPortURI) throws Exception {
        assert fridgeInboundPortURI != null : new PreconditionException("fridgeInboundPortURI != null");
        assert !fridgeInboundPortURI.isEmpty() : new PreconditionException("!fridgeInboundPortURI.isEmpty()");
        this.currentTemperature = 20;
        this.isOn = false;
        this.requestedTemperature = 10;
        this.fip = new FridgeInboundPort(fridgeInboundPortURI,this);
        this.fip.publishPort();
    }

    // ----------------------------------------------------------------------------
    //  Component services implementation
    // ----------------------------------------------------------------------------

    /**
     * @see FridgeImplementationI#getRequestedTemperature() 
     */
    @Override
    public float getRequestedTemperature() throws Exception {
        return requestedTemperature;
    }

    /**
     * @see interfaces.FridgeImplementationI#setRequestedTemperature(float)
     */
    @Override
    public void setRequestedTemperature(float temp) throws Exception {
        this.requestedTemperature = temp;
    }

    /**
     * @see FridgeImplementationI#getCurrentTemperature()
     */
    @Override
    public float getCurrentTemperature() throws Exception {
        return this.currentTemperature;
    }

    /**
     * @see FridgeImplementationI#switchOff()
     */
    @Override
    public void switchOff() throws Exception {
        this.isOn=false;
    }

    /**
     * @see FridgeImplementationI#switchOn()
     */
    @Override
    public void switchOn() throws Exception {
        this.isOn=true;
    }

    /**
     * @see FridgeImplementationI#getState()
     */
    @Override
    public boolean getState() throws Exception {
        return this.isOn;
    }
}
