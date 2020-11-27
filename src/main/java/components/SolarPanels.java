package main.java.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import main.java.interfaces.SolarPanelsCI;
import main.java.interfaces.SolarPanelsImplementationI;
import main.java.ports.SolarPanelsInboundPort;

/**
 * Class representing the SolarPanels component
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { SolarPanelsCI.class })
public class SolarPanels extends AbstractComponent implements SolarPanelsImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	/**
	 * Inboud port of the heater
	 */
	protected SolarPanelsInboundPort spip;

	/**
	 * True if the solar panels are turnedOn, false else
	 */
	protected boolean isTurnedOn;

	/**
	 * Constructor of the solar panels
	 * 
	 * @param uri of the SolarPanels component
	 */
	protected SolarPanels(String uri, String spipURI) throws Exception {
		super(uri, 1, 0);
		myUri = uri;
		isTurnedOn = false;
		this.spip = new SolarPanelsInboundPort(spipURI, this);
		this.spip.publishPort();
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.spip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see main.java.interfaces.SolarPanelsImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		isTurnedOn = true;
	}

	/**
	 * @see main.java.interfaces.SolarPanelsImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		isTurnedOn = false;
	}

	/**
	 * @see main.java.interfaces.SolarPanelsImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return isTurnedOn;
	}
}
