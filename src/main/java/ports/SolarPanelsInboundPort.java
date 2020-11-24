package main.java.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.SolarPanels;
import main.java.interfaces.SolarPanelsCI;

/**
 * 
 * SolarPanels inbound port for SolarPanels component interface
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
	 * @see interfaces.SolarPanelsImplementationI#turnOn()
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
	 * @see interfaces.SolarPanelsImplementationI#turnOff()
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
	 * @see interfaces.SolarPanelsImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((SolarPanels) owner).isTurnedOn());
	}

}
