package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.BatteryCI;
import interfaces.BatteryImplementationI;
import ports.BatteryInboundPort;
import utils.BatteryState;

/**
 * Class representing the Battery component
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { BatteryCI.class })
public class Battery extends AbstractComponent implements BatteryImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	/**
	 * Actual charge of battery in mA/h
	 */
	protected float batteryCharge;

	/**
	 * Actual state of battery
	 */
	protected BatteryState stateBattery;

	/**
	 * Maximum energy of the battery
	 */
	protected float maximumEnergy;

	/**
	 * Inbound port of the battery
	 */
	protected BatteryInboundPort bip;

	/**
	 * Constructor of battery
	 * 
	 * @param reflectionPortURI URI battery component
	 * @param bipURI            URI inbound port battery
	 * @throws Exception
	 */
	protected Battery(String reflectionPortURI, String bipURI, float maxEnergy) throws Exception {
		super(reflectionPortURI, 1, 0);
		myUri = reflectionPortURI;
		this.initialise(bipURI, maxEnergy);
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * Initialize the battery component
	 *
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 *     pre		{@code batteryInboundPortURI != null}
	 *     pre 		{@code batteryInboundPortURI.isEmpty()}
	 *     post 	{@code getBatteryState() == BatteryState.SLEEPING }
	 *     post 	{@code getBatteryCharge() == 0}
	 * </pre>
	 * 
	 * @param batteryInboundPortURI
	 * @throws Exception
	 */
	protected void initialise(String batteryInboundPortURI, float maximumEnergy) throws Exception {
		assert batteryInboundPortURI != null : new PreconditionException("batteryInboundPortURI != null");
		assert !batteryInboundPortURI.isEmpty() : new PreconditionException("batteryInboundPortURI.isEmpty()");
		this.stateBattery = BatteryState.SLEEPING;
		this.batteryCharge = 0;
		this.maximumEnergy = maximumEnergy;
		this.bip = new BatteryInboundPort(batteryInboundPortURI, this);
		this.bip.publishPort();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.bip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception {
		return this.batteryCharge;
	}

	/**
	 * @see interfaces.BatteryImplementationI#getBatteryState()
	 */
	@Override
	public BatteryState getBatteryState() throws Exception {
		return this.stateBattery;
	}

	/**
	 * @see interfaces.BatteryImplementationI#setBatteryState(BatteryState)
	 */
	@Override
	public void setBatteryState(BatteryState state) throws Exception {
		this.stateBattery = state;
	}

	/**
	 * @see interfaces.BatteryImplementationI#takeEnergy(float)
	 */
	@Override
	public float takeEnergy(float toTake) throws Exception {
		if (batteryCharge - toTake >= 0) {
			batteryCharge -= toTake;
			return toTake;
		} else {
			float res = batteryCharge;
			batteryCharge = 0;
			return res;
		}
	}

	/**
	 * @see interfaces.BatteryImplementationI#addEnergy(float)
	 */
	@Override
	public void addEnergy(float toAdd) throws Exception {
		if (batteryCharge + toAdd <= maximumEnergy)
			batteryCharge += toAdd;
		else
			batteryCharge = maximumEnergy;
	}

	/**
	 * @see interfaces.BatteryImplementationI#getMaximumEnergy()
	 */
	@Override
	public float getMaximumEnergy() throws Exception {
		return maximumEnergy;
	}

}
