package main.java.components.fridge.sil.sensors;
// -----------------------------------------------------------------------------

import fr.sorbonne_u.components.connectors.AbstractConnector;

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
