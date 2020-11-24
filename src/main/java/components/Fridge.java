package main.java.components;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;
import main.java.connectors.ControllerConnector;
import main.java.interfaces.ControllerCI;
import main.java.interfaces.FridgeCI;
import main.java.interfaces.FridgeImplementationI;
import main.java.ports.ControllerOutboundPort;
import main.java.ports.FridgeInboundPort;
import main.java.utils.FridgeMode;

@OfferedInterfaces(offered = { FridgeCI.class })
@RequiredInterfaces(required = { ControllerCI.class })
public class Fridge extends AbstractComponent implements FridgeImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	/**
	 * Serial number for registering on controller
	 */
	protected String serialNumber;

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
	protected Fridge(String uri, String serialNumber, String fipURI, String cip_URI) throws Exception {
		super(uri, 1, 0);
		myUri = uri;
		this.serialNumber = serialNumber;
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
		byte[] encoded = Files.readAllBytes(Paths.get("src/main/java/adapter/fridge-control.xml"));
		String xmlFile = new String(encoded, "UTF-8");
		boolean isRegister = this.cop.register(this.serialNumber, this.fip.getPortURI(), xmlFile);
		if (!isRegister)
			throw new Exception("Fridge can't register to controller");
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
	public boolean upMode() throws Exception {
		mode = FridgeMode.NORMAL;
		return true;
	}

	/**
	 * @see FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		mode = FridgeMode.ECO;
		return true;
	}

	/**
	 * @see FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		try {
			mode = FridgeMode.values()[modeIndex];
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @see FridgeImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return mode.ordinal();
	}

	/**
	 * @see FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		return this.passive.get();
	}

	/**
	 * @see FridgeImplementationI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception {
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
	public boolean resume() throws Exception {
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
	public double emergency() throws Exception {
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
