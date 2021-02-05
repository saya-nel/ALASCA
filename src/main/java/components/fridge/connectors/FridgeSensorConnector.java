package main.java.components.fridge.connectors;
// -----------------------------------------------------------------------------

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.components.fridge.interfaces.FridgeSensorCI;

/**
 * The class <code>FridgeSensorConnector</code> implements a connector for
 * the <code>FridgeSensorCI</code> component interface.
 *
 * @author	Bello Memmi
 */
public class FridgeSensorConnector
extends     AbstractConnector
implements FridgeSensorCI {
    /**
     * @see FridgeSensorCI#getContentTemperatureInCelsius()
     */
    @Override
    public double getContentTemperatureInCelsius() throws Exception {
        return ((FridgeSensorCI)this.offering).getContentTemperatureInCelsius();
    }
}
