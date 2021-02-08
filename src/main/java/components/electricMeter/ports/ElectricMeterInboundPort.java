package main.java.components.electricMeter.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.electricMeter.ElectricMeter;
import main.java.components.electricMeter.interfaces.ElectricMeterCI;

/**
 * 
 * The class <code>ElectricMetterInboundPort</code> implements an inbound port
 * for the component interface <code>ElectricMetterCI</code>.
 * 
 * @author Bello Memmi
 *
 */
public class ElectricMeterInboundPort extends AbstractInboundPort implements ElectricMeterCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the ElectricMetterInboundPort
	 * 
	 * @param uri           reflexion uri of the port
	 * @param executorIndex index of the thread pool for running services
	 * @param owner         owner component
	 */
	public ElectricMeterInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ElectricMeterCI.class, owner);
	}

	/**
	 * @see main.java.components.electricMeter.interfaces.ElectricMeterImplementationI#getProduction()
	 */
	@Override
	public double getProduction() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((ElectricMeter) owner).getProduction());
	}

	/**
	 * @see main.java.components.electricMeter.interfaces.ElectricMeterImplementationI#getIntensity()
	 */
	@Override
	public double getIntensity() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((ElectricMeter) owner).getIntensity());
	}

}
