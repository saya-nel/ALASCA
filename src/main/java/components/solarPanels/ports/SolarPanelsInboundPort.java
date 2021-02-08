package main.java.components.solarPanels.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.solarPanels.SolarPanels;
import main.java.components.solarPanels.interfaces.SolarPanelsCI;

/**
 * 
 * The class <code>SolarPanelsInboundPort</code> implements an inbound port for
 * the component interface <code>SolarPanelsCI</code>.
 * 
 * @author Bello Memmi
 *
 */
public class SolarPanelsInboundPort extends AbstractInboundPort implements SolarPanelsCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor of the solar panels inbound port
	 * 
	 * @param uri   of the solar panels inbound port
	 * @param owner owner component
	 * @throws Exception
	 */
	public SolarPanelsInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, SolarPanelsCI.class, owner);
	}

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((SolarPanels) owner).turnOn();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((SolarPanels) owner).turnOff();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see main.java.components.solarPanels.interfaces.SolarPanelsImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((SolarPanels) owner).isTurnedOn());
	}

}
