package components;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Fridge component
 *
 * @author Bello Memmi
 *
 */

import connectors.BatteryConnector;
import connectors.ControllerConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.ControllerCI;
import interfaces.FridgeCI;
import interfaces.FridgeImplementationI;
import ports.ControllerOutboundPort;
import ports.FridgeInboundPort;
import utils.FridgeMode;
import utils.GeneratingSerialNumber;

@OfferedInterfaces(offered = { FridgeCI.class })
@RequiredInterfaces(required = { ControllerCI.class })
public class Fridge extends AbstractComponent implements FridgeImplementationI {

	public static final String CONTROL_INTERFACE_DESCRIPTOR = "<control-adapter type=\"suspension\" uid=\"1A10000\" offered=\"interfaces.FridgeCI\">  <consumption nominal=\"2000\" />  <on>  <required>interfaces.FridgeCI</required> <body equipmentRef=\"fridge\"> fridge.switchOn(); </body> </on> <off> <body equipmentRef=\"fridge\">fridge.switchOff();</body> </off> <suspend><body equipmentRef=\"fridge\"> return fridge.passivate();</body> </suspend> <resume> <body equipmentRef=\"fridge\">return fridge.activate();</body> </resume> <active> <body equipmentRef=\"fridge\">return fridge.active();</body> </active> <emergency> <body equipmentRef=\"fridge\">return fridge.degreeOfEmergency();</body> </emergency> </control-adapter>";

	protected String serial_number;
	/**
	 * Component URI
	 */
	protected String myUri;
	/**
	 * Requested Temperature for the fridge
	 */
	protected float requestedTemperature;
	/**
	 * Actual state of the fridge
	 */
	protected boolean isOn;
	/**
	 * current temperature inside of fridge pas sûr de l'utilité intégré à la
	 * simulation
	 */
	protected int currentTemperature;
	/**
	 * Inbound port of the fridge component
	 */
	protected FridgeInboundPort fip;

	/**
	 * Outbound port of the controller from which the fridge will register
	 */
	protected ControllerOutboundPort cop;

	/**
	 * inbound port of controller to connect fridge with
	 */
	protected String cip_URI;

	/** maximum time during which the fridge can be suspended **/
	protected static long MAX_SUSPENSION = Duration.ofHours(12).toMillis();

	/** last time the fridge has been suspended while it is still suspended. **/
	protected final AtomicReference<LocalTime> lastSuspensionTime;

	/** true if the fridge is passive **/
	protected final AtomicBoolean passive;
	
	/**
	 * Current mode of the Fridge
	 */
	protected FridgeMode mode;

	/**
	 * @param uri    of the component
	 * @param fipURI inbound port's uri
	 * @throws Exception
	 */
	protected Fridge(String uri, String fipURI, String cip_URI) throws Exception {
		super(uri, 1, 0);
		myUri = uri;
		this.passive = new AtomicBoolean(false);
		this.lastSuspensionTime = new AtomicReference<>();
		this.mode = FridgeMode.NORMAL;
		this.cop = new ControllerOutboundPort(this);
		this.cop.publishPort();
		this.initialise(fipURI);

	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * Initialize the fridge component
	 *
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 *     pre          {@code fridgeInboundPortURI != null}
	 *     pre          {@code !fridgeInboundPortURI.isEmpty()}
	 *     post         {@code getCurrentTemperature() == 20}
	 *     post         {@code getRequestedTemperature() == 10}
	 *     post         {@code getState() == false}
	 * </pre>
	 * 
	 * @param fridgeInboundPortURI
	 * @throws Exception
	 */
	public void initialise(String fridgeInboundPortURI) throws Exception {
		assert fridgeInboundPortURI != null : new PreconditionException("fridgeInboundPortURI != null");
		assert !fridgeInboundPortURI.isEmpty() : new PreconditionException("!fridgeInboundPortURI.isEmpty()");
		serial_number = GeneratingSerialNumber.generateSerialNumber();
		this.currentTemperature = 20;
		this.isOn = false;
		this.requestedTemperature = 10;
		this.fip = new FridgeInboundPort(fridgeInboundPortURI, this);
		this.fip.publishPort();
	}

	/**
	 * @see AbstractComponent#shutdown()
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

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();
		try {
			this.doPortConnection(this.cop.getPortURI(), cip_URI, ControllerConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
	}

	@Override
	public synchronized void execute() throws Exception {
		this.cop.register(this.serial_number, this.fip.getPortURI(), Fridge.CONTROL_INTERFACE_DESCRIPTOR);
	}
	// ----------------------------------------------------------------------------
	// Component services implementation
	// ----------------------------------------------------------------------------

	/**
	 * @see FridgeImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		return requestedTemperature;
	}

	/**
	 * @see interfaces.FridgeImplementationI#setRequestedTemperature(float)
	 */
	@Override
	public void setRequestedTemperature(float temp) throws Exception {
		this.requestedTemperature = temp;
	}

	/**
	 * @see FridgeImplementationI#getCurrentTemperature()
	 */
	@Override
	public float getCurrentTemperature() throws Exception {
		return this.currentTemperature;
	}

	/**
	 * @see FridgeImplementationI#upMode()
	 */
	@Override
	public boolean upMode() {
		mode = FridgeMode.NORMAL;
		return true;
	}

	/**
	 * @see FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() {
		mode = FridgeMode.ECO;
		return true;
		}

	/**
	 * @see FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) {
		try {
			mode = FridgeMode.values()[modeIndex];
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see FridgeImplementationI#currentMode()
	 */
	@Override
	public int currentMode() {
		return mode.getValue();
	}

	/**
	 * @see FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() {
		return this.passive.get();
	}

	/**
	 * @see FridgeImplementationI#suspend()
	 */
	@Override
	public boolean suspend() {
		boolean succeed = false;
		synchronized (this.passive) {
			succeed = this.passive.compareAndSet(false, true);
			if (succeed) {
				this.lastSuspensionTime.set(LocalTime.now());
			}
		}
		return succeed;
	}

	/**
	 * @see FridgeImplementationI#resume()
	 */
	@Override
	public boolean resume() {
		boolean succeed;
		synchronized (this.passive) {
			succeed = this.passive.compareAndSet(true, false);
			if (succeed) {
				this.lastSuspensionTime.set(null);
			}
		}
		return succeed;
	}

	/**
	 * @see FridgeImplementationI#emergency()
	 */
	@Override
	public double emergency() {
		synchronized (this.passive) {
			if (!this.passive.get()) {
				return 0.0;
			} else {
				Duration d = Duration.between(this.lastSuspensionTime.get(), LocalTime.now());
				long inMillis = d.toMillis();
				if (inMillis > MAX_SUSPENSION) {
					return 1.0;
				} else {
					return ((double) inMillis) / ((double) MAX_SUSPENSION);
				}
			}
		}
	}
}
