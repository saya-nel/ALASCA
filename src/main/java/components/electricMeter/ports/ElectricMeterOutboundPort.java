package main.java.components.electricMeter.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.components.electricMeter.interfaces.ElectricMeterCI;

/**
 * The class <code>ElectricMeterOutboundPort</code> implements an outbound port
 * for the component interface <code>ElectricMeterCI</code>.
 * 
 * @author Bello Memmi
 *
 */
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

	/**
	 * @see main.java.components.electricMeter.interfaces.ElectricMeterImplementationI#getProduction()
	 */
	@Override
	public double getProduction() throws Exception {
		return ((ElectricMeterCI) this.getConnector()).getProduction();
	}

	/**
	 * @see main.java.components.electricMeter.interfaces.ElectricMeterImplementationI#getIntensity()
	 */
	@Override
	public double getIntensity() throws Exception {
		return ((ElectricMeterCI) this.getConnector()).getIntensity();
	}

}
