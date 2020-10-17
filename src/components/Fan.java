package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import interfaces.FanCI;
import interfaces.FanImplementationI;
import ports.FanInboundPort;
import utils.FanLevel;

/**
 * Class representing the Fan component
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { FanCI.class })
public class Fan extends AbstractComponent implements FanImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	/**
	 * Actual level of the fan
	 */
	protected FanLevel currentLevel;

	/**
	 * Actual power state of the fan
	 */
	protected boolean isOn;

	/**
	 * Inbound port of the fan component
	 */
	protected FanInboundPort fip;

	/**
	 * Constructor of the fan
	 * 
	 * @param uri of the Fan component
	 */
	public Fan(String uri, String fipURI) throws Exception {
		super(uri, 1, 0);
		myUri = uri;
		this.currentLevel = FanLevel.MID;
		this.isOn = false;
		fip = new FanInboundPort(fipURI, this);
		fip.publishPort();
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
			this.fip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see interfaces.FanImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		this.isOn = true;
	}

	/**
	 * @see interfaces.FanImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		this.isOn = false;
	}

	/**
	 * @see interfaces.FanImplementationI#adjustPower(FanLevel)
	 */
	@Override
	public void adjustPower(FanLevel level) throws Exception {
		this.currentLevel = level;
	}

	/**
	 * @see interfaces.FanImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return isOn;
	}

	/**
	 * @see interfaces.FanImplementationI#getFanLevel()
	 */
	@Override
	public FanLevel getFanLevel() throws Exception {
		return currentLevel;
	}

}
