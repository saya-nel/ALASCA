package main.java.components.fridge.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.components.fridge.interfaces.FridgeSensorCI;

/**
 * The class <code>FridgeSensorOutboundPort</code> implements an outbound port
 * for the <code>FridgeSensorCI</code> component interface.
 *
 * @author Bello Memmi
 */
public class FridgeSensorOutboundPort extends AbstractOutboundPort implements FridgeSensorCI {

	private static final long serialVersionUID = 1L;

	public FridgeSensorOutboundPort(ComponentI owner) throws Exception {
		super(FridgeSensorCI.class, owner);
	}

	@Override
	public double getContentTemperatureInCelsius() throws Exception {
		return ((FridgeSensorCI) this.getConnector()).getContentTemperatureInCelsius();
	}
}
