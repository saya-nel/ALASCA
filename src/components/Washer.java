package components;

import java.util.Date;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.WasherCI;
import interfaces.WasherImplementationI;
import ports.WasherInboundPort;

@OfferedInterfaces(offered = { WasherCI.class })
public class Washer extends AbstractComponent implements WasherImplementationI {
	/**
	 * Component URI
	 */
	protected String myUri;

	/**
	 * Current state of the washer
	 */
	protected boolean isWorking;

	/**
	 * Program temperature in °C
	 */
	protected int programTemperature;

	/**
	 * Program duration in minutes
	 */
	protected int programDuration;

	/**
	 * Inbound port of the washer
	 */
	protected WasherInboundPort wip;

	/**
	 * Delay for the program
	 */
	protected Date delay;

	/**
	 *
	 * @param reflectionPortURI
	 * @param bipURI
	 * @throws Exception
	 */
	protected Washer(String reflectionPortURI, String wipURI) throws Exception {
		super(reflectionPortURI, 1, 0);
		myUri = reflectionPortURI;
		initialise(wipURI);
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * <pre>
	 *     pre      {@code washerInboundPortURI != null}
	 *     pre      {@code washerInboundPortURI.isEmpty()}
	 *     post     {@code getStateWasher == false }
	 *     post     {@code getTemperatureOperating == 0}
	 * </pre>
	 * 
	 * @param washerInboundPortURI
	 * @throws Exception
	 */
	protected void initialise(String washerInboundPortURI) throws Exception {
		assert washerInboundPortURI != null : new PreconditionException("washerInboundPortUri != null");
		assert !washerInboundPortURI.isEmpty() : new PreconditionException("washerInboundPortURI.isEmpty()");
		this.isWorking = false;
		this.programTemperature = 30;
		this.programDuration = 60;
		this.wip = new WasherInboundPort(washerInboundPortURI, this);
		this.wip.publishPort();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.wip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see interfaces.WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return isWorking;
	}

	/**
	 * @see interfaces.WasherImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		this.isWorking = true;
	}

	/**
	 * @see interfaces.WasherImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		this.isWorking = false;
	}

	/**
	 * @see interfaces.WasherImplementationI#getProgramTemperature()
	 */
	@Override
	public int getProgramTemperature() throws Exception {
		return this.programTemperature;
	}

	/**
	 * @see interfaces.WasherImplementationI#setProgramTemperature(int)
	 */
	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		this.programTemperature = temperature;
	}

	/**
	 * @see interfaces.WasherImplementationI#setProgramDuration(int)
	 */
	@Override
	public void setProgramDuration(int duration) throws Exception {
		this.programDuration = duration;
	}

	/**
	 * @see interfaces.WasherImplementationI#getProgramDuration()
	 */
	@Override
	public int getProgramDuration() throws Exception {
		return this.programDuration;
	}

	/**
	 * @see interfaces.WasherImplementationI#setDelay(Date)
	 */
	@Override
	public void setDelay(Date date) throws Exception {
		delay = date;
	}

	/**
	 * @see interfaces.WasherImplementationI#getDelay()
	 */
	@Override
	public Date getDelay() throws Exception {
		return delay;
	}
}
