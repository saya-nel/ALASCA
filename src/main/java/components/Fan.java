package main.java.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.exceptions.PreconditionException;
import main.java.interfaces.FanCI;
import main.java.interfaces.FanImplementationI;
import main.java.ports.FanInboundPort;
import main.java.utils.FanLevel;

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
	protected Fan(String uri, String fipURI) throws Exception {
		super(uri, 1, 0);
		myUri = uri;
		initialise(fipURI);
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * Initialise the fan component
	 *
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 *     pre 		{@code fanInboundPort != null}
	 *     pre 		{@code !fanInboundPort.isEmpty()}
	 *     post		{@code getFanLevel() == FanLevel.MID}
	 *     post 	{@code isTurnedOn() == False}
	 * </pre>
	 * 
	 * @param fanInboundPort
	 * @throws Exception
	 */
	public void initialise(String fanInboundPort) throws Exception {
		assert fanInboundPort != null : new PreconditionException("fanInboundPort != null");
		assert !fanInboundPort.isEmpty() : new PreconditionException("!fanInboundPort.isEmpty()");
		this.currentLevel = FanLevel.MID;
		this.isOn = false;
		fip = new FanInboundPort(fanInboundPort, this);
		fip.publishPort();
	}

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
	 * @see main.java.interfaces.FanImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		this.isOn = true;
	}

	/**
	 * @see main.java.interfaces.FanImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		this.isOn = false;
	}

	/**
	 * @see main.java.interfaces.FanImplementationI#adjustPower(FanLevel)
	 */
	@Override
	public void adjustPower(FanLevel level) throws Exception {
		this.currentLevel = level;
	}

	/**
	 * @see main.java.interfaces.FanImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return isOn;
	}

	/**
	 * @see main.java.interfaces.FanImplementationI#getFanLevel()
	 */
	@Override
	public FanLevel getFanLevel() throws Exception {
		return currentLevel;
	}

}
