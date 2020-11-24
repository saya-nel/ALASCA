package main.java.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.interfaces.SolarPanelsCI;

/**
 * Outbound port for SolarPanels component
 * 
 * @author Bello Memmi
 *
 */
public class SolarPanelsOutboundPort extends AbstractOutboundPort implements SolarPanelsCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of SolarPanelsOubtboundPort
	 * 
	 * @param owner owner component
	 * @throws Exception
	 */
	public SolarPanelsOutboundPort(ComponentI owner) throws Exception {
		super(SolarPanelsCI.class, owner);
	}

	/**
	 * @see interfaces.SolarPanelsImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((SolarPanelsCI) this.getConnector()).turnOn();
	}

	/**
	 * @see interfaces.SolarPanelsImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((SolarPanelsCI) this.getConnector()).turnOff();
	}

	@Override
	public boolean isTurnedOn() throws Exception {
		return ((SolarPanelsCI) this.getConnector()).isTurnedOn();
	}

}
