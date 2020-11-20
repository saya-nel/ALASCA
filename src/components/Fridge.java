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

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.ControllerCI;
import interfaces.FridgeCI;
import interfaces.FridgeImplementationI;
import ports.FridgeInboundPort;

@OfferedInterfaces(offered = { FridgeCI.class })
@RequiredInterfaces(required = { ControllerCI.class })
public class Fridge extends AbstractComponent implements FridgeImplementationI {

	public static final String CONTROL_INTERFACE_DESCRIPTOR = "<control-adapter type=\"suspension\" uid=\"1A10000\" offered=\"interfaces.FridgeCI\">  <consumption nominal=\"2000\" />  <on>  <required>interfaces.FridgeCI</required> <body equipmentRef=\"fridge\"> fridge.switchOn(); </body> </on> <off> <body equipmentRef=\"fridge\">fridge.switchOff();</body> </off> <suspend><body equipmentRef=\"fridge\"> return fridge.passivate();</body> </suspend> <resume> <body equipmentRef=\"fridge\">return fridge.activate();</body> </resume> <active> <body equipmentRef=\"fridge\">return fridge.active();</body> </active> <emergency> <body equipmentRef=\"fridge\">return fridge.degreeOfEmergency();</body> </emergency> </control-adapter>";

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

	/** maximum time during which the fridge can be suspended **/
	protected static long MAX_SUSPENSION = Duration.ofHours(12).toMillis();

	/** last time the fridge has been suspended while it is still suspended. **/
	protected final AtomicReference<LocalTime> lastSuspensionTime;

	/** true if the fridge is passive **/
	protected final AtomicBoolean passive;

	/**
	 * @param uri    of the component
	 * @param fipURI inbound port's uri
	 * @throws Exception
	 */
	protected Fridge(String uri, String fipURI) throws Exception {
		super(uri, 1, 0);
		myUri = uri;
		this.passive = new AtomicBoolean(false);
		this.lastSuspensionTime = new AtomicReference<>();
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
	 * @see FridgeImplementationI#switchOff()
	 */
	@Override
	public void switchOff() throws Exception {
		this.isOn = false;
	}

	/**
	 * @see FridgeImplementationI#switchOn()
	 */
	@Override
	public void switchOn() throws Exception {
		this.isOn = true;
	}

	/**
	 * @see FridgeImplementationI#getState()
	 */
	@Override
	public boolean getState() throws Exception {
		return this.isOn;
	}

	@Override
	public boolean active() throws Exception {
		return !this.passive.get();
	}

	@Override
	public boolean activate() throws Exception {
		boolean succeed;
		synchronized (this.passive) {
			succeed = this.passive.compareAndSet(true, false);
			if (succeed) {
				this.lastSuspensionTime.set(null);
			}
		}
		return succeed;
	}

	@Override
	public boolean passivate() throws Exception {
		boolean succeed = false;
		synchronized (this.passive) {
			succeed = this.passive.compareAndSet(false, true);
			if (succeed) {
				this.lastSuspensionTime.set(LocalTime.now());
			}
		}
		return succeed;
	}

	@Override
	public double degreeOfEmergency() throws Exception {
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
