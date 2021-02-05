package main.java.components.fridge.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.components.fridge.interfaces.FridgeActuatorCI;

/**
 * The class <code>FridgeActuatorConnector</code> implements a connector
 * for the <code>FridgeActuatorCI</code> component interface.
 *
 * @author	Bello Memmi
 */
public class FridgeActuatorConnector
extends     AbstractConnector
implements  FridgeActuatorCI
{
    /**
     * @see FridgeActuatorCI#startPassive()
     */
    @Override
    public void startPassive() throws Exception {
        ((FridgeActuatorCI)this.offering).startPassive();
    }

    /**
     * @see FridgeActuatorCI#stopPassive()
     */
    @Override
    public void stopPassive() throws Exception {
        ((FridgeActuatorCI)this.offering).stopPassive();
    }
}
