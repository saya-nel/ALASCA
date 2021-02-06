package main.java.components.fridge.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.fridge.Fridge;
import main.java.components.fridge.interfaces.FridgeReactiveControlImplementationI;
import main.java.components.fridge.interfaces.FridgeSensorCI;

public class FridgeSensorInboundPort extends AbstractInboundPort implements FridgeSensorCI {

	private static final long serialVersionUID = 1L;

	public FridgeSensorInboundPort(ComponentI owner) throws Exception {
		super(Fridge.SENSOR_INBOUND_PORT_URI, FridgeSensorCI.class, owner);
		assert owner instanceof FridgeReactiveControlImplementationI;
	}

	/**
	 * @see FridgeSensorCI#getContentTemperatureInCelsius()
	 */
	@Override
	public double getContentTemperatureInCelsius() throws Exception {
		return this.getOwner()
				.handleRequestSync(o -> ((FridgeReactiveControlImplementationI) o).contentTemperatureSensor());
	}
}
