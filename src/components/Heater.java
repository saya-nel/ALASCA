package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.HeaterCI;
import interfaces.HeaterImplementationI;
import ports.HeaterInboundPort;

/**
 * Class representing the Heater component
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { HeaterCI.class })
public class Heater extends AbstractComponent implements HeaterImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	/**
	 * Requested temperature for the heater
	 */
	protected float requestedTemperature;

	/**
	 * True if the heater is turned on, false else
	 */
	protected boolean isOn;

	/**
	 * Inboud port of the heater
	 */
	protected HeaterInboundPort hip;

	/**
	 * Constructor of the heater
	 *
	 * @param uri    of the Heater component
	 * @param hipURI uri of the heater inbound port
	 * @throws Exception
	 */
	public Heater(String uri, String hipURI) throws Exception {
		super(uri, 1, 0);
		myUri = uri;
		initialise(hipURI);
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * Initialise the heater component
	 * <pre>
	 *     pre		{@code heaterInboundPortURI != null}
	 *     pre 		{@code heaterInboundPortURI.isEmpty()}
	 *     post 	{@code isHeaterOn() == False }
	 * </pre>
	 * @param heaterInboundPort
	 * @throws Exception
	 */
	public void initialise(String heaterInboundPort) throws Exception
	{
		assert heaterInboundPort != null : new PreconditionException("heaterInboundPort != null");
		assert !heaterInboundPort.isEmpty() : new PreconditionException("heaterInboundPort.isEmpty()");
		this.requestedTemperature = 20;
		this.isOn = false;
		hip = new HeaterInboundPort(heaterInboundPort, this);
		hip.publishPort();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.hip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see interfaces.HeaterImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		return this.requestedTemperature;
	}

	/**
	 * @see interfaces.HeaterImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		this.isOn = false;
	}

	/**
	 * @see interfaces.HeaterImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		this.isOn = true;
	}

	/**
	 * @see interfaces.HeaterImplementationI#isHeaterOn()
	 */
	@Override
	public boolean isHeaterOn() throws Exception {
		return this.isOn;
	}

	/**
	 * @see interfaces.HeaterImplementationI#setRequestedTemperature(float)
	 */
	@Override
	public void setRequestedTemperature(float requestedTemperature) throws Exception {
		this.requestedTemperature = requestedTemperature;
	}

}
