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
import main.java.utils.Log;

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
	protected AtomicReference<FridgeMode> mode;

	/**
	 * @param uri    of the component
	 * @param fipURI inbound port's uri
	 * @throws Exception
	 */
	protected Fridge(String uri, boolean toogleTracing, String serialNumber, String fipURI, String cip_URI)
			throws Exception {
		super(uri, 1, 0);
		myUri = uri;
		this.serialNumber = serialNumber;
		this.passive = new AtomicBoolean(false);
		this.lastSuspensionTime = new AtomicReference<>();
		this.mode = new AtomicReference<>(FridgeMode.NORMAL);
		this.cop = new ControllerOutboundPort(this);
		this.cop.localPublishPort();
		this.cip_URI = cip_URI;
		this.initialise(fipURI);
		if (toogleTracing) {
			this.tracer.get().setTitle("Fridge component");
			this.tracer.get().setRelativePosition(1, 2);
			this.toggleTracing();
		}

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
		this.requestedTemperature = 10;
		this.fip = new FridgeInboundPort(fridgeInboundPortURI, this);
		this.fip.publishPort();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void finalise() throws Exception {
		if (cop.connected())
			this.cop.doDisconnection();
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.cop.unpublishPort();
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
			if (cip_URI.length() > 0)
				this.doPortConnection(this.cop.getPortURI(), cip_URI, ControllerConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
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
	 * @see main.java.interfaces.FridgeImplementationI#getRequestedTemperature()
	 */
	@Override
	public float getRequestedTemperature() throws Exception {
		Log.printAndLog(this, "getRequestedTemperature() service result : " + requestedTemperature);
		return requestedTemperature;
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#setRequestedTemperature(float)
	 */
	@Override
	public void setRequestedTemperature(float temp) throws Exception {
		Log.printAndLog(this, "setRequestedTemperature(" + temp + ") service called, new temp = " + temp);
		this.requestedTemperature = temp;
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#getCurrentTemperature()
	 */
	@Override
	public float getCurrentTemperature() throws Exception {
		Log.printAndLog(this, "getCurrentTemperature() service result : " + this.currentTemperature);
		return this.currentTemperature;
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		boolean succeed = false;
		succeed = this.mode.compareAndSet(this.mode.get(), FridgeMode.values()[(this.mode.get().ordinal() + 1) % 2]);
		Log.printAndLog(this, "upMode() service result : " + true);
		return succeed;
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		boolean succeed = false;
		succeed = this.mode.compareAndSet(this.mode.get(),
				FridgeMode.values()[Math.floorMod(this.mode.get().ordinal() - 1, 2)]);
		Log.printAndLog(this, "downmode() service result : " + true);
		return true;
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		boolean succeed = false;
		try {
			succeed = this.mode.compareAndSet(this.mode.get(), FridgeMode.values()[modeIndex]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.printAndLog(this, "setMode(" + modeIndex + ") service result : " + succeed);
		return succeed;
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		int res = this.mode.get().ordinal();
		Log.printAndLog(this, "currentMode() service result : " + res);
		return res;
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		boolean res = this.passive.get();
		Log.printAndLog(this, "suspended() service result : " + res);
		return res;
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#suspend()
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
		Log.printAndLog(this, "suspend() service result : " + succeed);
		return succeed;
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#resume()
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
		Log.printAndLog(this, "resume() service result : " + succeed);
		return succeed;
	}

	/**
	 * @see main.java.interfaces.FridgeImplementationI#emergency()
	 */
	@Override
	public double emergency() throws Exception {
		synchronized (this.passive) {
			double res = 0d;
			if (!this.passive.get()) {
				res = 0.0;
			} else {
				Duration d = Duration.between(this.lastSuspensionTime.get(), LocalTime.now());
				long inMillis = d.toMillis();
				if (inMillis > MAX_SUSPENSION) {
					res = 1.0;
				} else {
					res = ((double) inMillis) / ((double) MAX_SUSPENSION);
				}
			}
			Log.printAndLog(this, "emergency() service result : " + res);
			return res;
		}
	}
}
