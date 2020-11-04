package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
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
	protected WasherInboundPort bip;

	/**
	 *
	 * @param reflectionPortURI
	 * @param bipURI
	 * @throws Exception
	 */
	protected Washer(String reflectionPortURI, String bipURI) throws Exception {
		super(reflectionPortURI, 1, 0);
		myUri = reflectionPortURI;
	}

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
		this.bip = new WasherInboundPort(washerInboundPortURI, this);
		this.bip.publishPort();
	}

	/**
	 * @see interfaces.WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return isWorking;
	}

	/**
	 * @see interfaces.WasherImplementationI#turnOnWasher()
	 */
	@Override
	public void turnOnWasher() throws Exception {
		this.isWorking = true;
	}

	/**
	 * @see interfaces.WasherImplementationI#turnOffWasher()
	 */
	@Override
	public void turnOffWasher() throws Exception {
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
}
