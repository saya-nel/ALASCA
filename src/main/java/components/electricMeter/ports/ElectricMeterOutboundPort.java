package main.java.components.electricMeter.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.components.electricMeter.interfaces.ElectricMeterCI;

public class ElectricMeterOutboundPort extends AbstractOutboundPort implements ElectricMeterCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the controller outbound port
	 *
	 * @param owner owner component
	 * @throws Exception
	 */
	public ElectricMeterOutboundPort(ComponentI owner) throws Exception {
		super(ElectricMeterCI.class, owner);
	}

	@Override
	public double getProduction() throws Exception {
		return ((ElectricMeterCI) this.getConnector()).getProduction();
	}

	@Override
	public double getIntensity() throws Exception {
		return ((ElectricMeterCI) this.getConnector()).getIntensity();
	}

}
