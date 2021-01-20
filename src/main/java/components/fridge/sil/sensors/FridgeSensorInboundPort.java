package main.java.components.fridge.sil.sensors;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.fridge.Fridge;
import main.java.components.fridge.sil.actuator.FridgeActuatorCI;
import main.java.components.fridge.sil.FridgeReactiveControlImplementationI;

public class FridgeSensorInboundPort
extends     AbstractInboundPort
implements FridgeSensorCI {
    public FridgeSensorInboundPort(ComponentI owner) throws Exception {
        super(Fridge.SENSOR_INBOUND_PORT_URI, FridgeActuatorCI.class, owner);
        assert owner instanceof FridgeReactiveControlImplementationI;
    }


    /**
     * @see FridgeSensorCI#getContentTemperatureInCelsius()
     */
    @Override
    public double getContentTemperatureInCelsius() throws Exception {
        return this.getOwner().handleRequestSync(
                o-> ((FridgeReactiveControlImplementationI)o).contentTemperatureSensor());
    }
}
