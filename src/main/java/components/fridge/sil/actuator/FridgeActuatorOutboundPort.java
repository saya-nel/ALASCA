package main.java.components.fridge.sil.actuator;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.components.fridge.Fridge;

public class FridgeActuatorOutboundPort
extends    AbstractOutboundPort
implements FridgeActuatorCI
{
    public FridgeActuatorOutboundPort(ComponentI owner) throws Exception
    {
        super(FridgeActuatorCI.class, owner);
    }

    /**
     * @see FridgeActuatorCI#startPassive()
     */
    @Override
    public void startPassive() throws Exception {
        ((FridgeActuatorCI)this.getConnector()).startPassive();
    }

    /**
     * @see FridgeActuatorCI#stopPassive()
     */
    @Override
    public void stopPassive() throws Exception {
        ((FridgeActuatorCI)this.getConnector()).stopPassive();
    }
}
