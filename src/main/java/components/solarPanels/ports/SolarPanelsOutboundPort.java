package main.java.components.solarPanels.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.components.solarPanels.interfaces.SolarPanelsCI;

/**
 * The class <code>SolarPanelsInboundPort</code> implements an inbound port for
 * the component interface <code>SolarPanelsCI</code>.
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
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((SolarPanelsCI) this.getConnector()).turnOn();
	}

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((SolarPanelsCI) this.getConnector()).turnOff();
	}

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return ((SolarPanelsCI) this.getConnector()).isTurnedOn();
	}

}
