package components;

import connectors.BatteryConnector;
import connectors.ControllerConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.BatteryCI;
import interfaces.BatteryImplementationI;
import interfaces.ControllerCI;
import ports.BatteryInboundPort;
import ports.BatteryOutboundPort;
import ports.ControllerOutboundPort;
import utils.BatteryState;

/**
 * Class representing the Battery component
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { BatteryCI.class })
@RequiredInterfaces(required = {ControllerCI.class })
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
	 * Outbound port of the battery for registering
	 */
	protected ControllerOutboundPort cop;

	/**
	 *
	 */
	protected String cip_uri;
	/**
	 * Constructor of battery
	 * 
	 * @param reflectionPortURI URI battery component
	 * @param bipURI            URI inbound port battery
	 * @throws Exception
	 */
	protected Battery(String reflectionPortURI, String bipURI, String cip_URI, float maxEnergy) throws Exception {
		super(reflectionPortURI, 1, 0);
		myUri = reflectionPortURI;
		this.cip_uri = cip_URI;
		this.cop = new ControllerOutboundPort(this);
		this.cop.publishPort();
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
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();
		try {
			this.doPortConnection(this.cop.getPortURI(), this.cip_uri, ControllerConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
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
	
	@Override
	public synchronized void	execute() throws Exception {
		
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

}
