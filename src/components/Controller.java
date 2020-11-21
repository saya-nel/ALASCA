package components;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import connectors.ControlBatteryConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import interfaces.ControllerCI;
import interfaces.ControllerImplementationI;
import ports.BatteryOutboundPort;
import ports.ControllerInboundPort;
import ports.ControllerOutboundPort;
import utils.Log;

/**
 *
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { ControllerCI.class })

public class Controller extends AbstractComponent implements ControllerImplementationI {

	/**
	 * URI of the pool of threads for control
	 */
	public static final String CONTROL_EXECUTOR_URI = "control";

	/**
	 * URI of the pool of threads for registering
	 */
	public static final String REGISTER_EXECUTOR_URI = "register";

	// ports used for registering
	private List<ControllerInboundPort> registerRequestPort;

	// ports used for controlling devices
	private List<ControllerOutboundPort> controlDevicesPorts;

	// uri of component
	private String myURI;

	// Map serial number in key and XMLFile content this xml is supposed to contain
	// the implementation of the connector's controlled methods
	private Map<String, String> registeredDevices;

	// TODO parametres a revoir, bizarre
	protected Controller(String uri, boolean toogleTracing, String[] inboundPortRegisterURI,
			String[] outboundPortDeviceURI) throws Exception {
		super(uri, 1, 0);
		assert uri != null;
		this.myURI = uri;

		this.createNewExecutorService(CONTROL_EXECUTOR_URI, 4, false);
		this.createNewExecutorService(REGISTER_EXECUTOR_URI, 4, false);

		initialise(inboundPortRegisterURI, outboundPortDeviceURI);
		if (toogleTracing) {
			this.tracer.get().setTitle("Controller component");
			this.tracer.get().setRelativePosition(0, 0);
			this.toggleTracing();
		}
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	protected void initialise(String[] inboundPortRegisterURI, String[] outboundPortDeviceURI) throws Exception {
		registeredDevices = new ConcurrentHashMap<>();

		// Initialize ports relative to registering
		this.registerRequestPort = new Vector<>();
		// Initialize ports relative to controlling devices
		this.controlDevicesPorts = new Vector<>();

		int executorServiceIndex = this.getExecutorServiceIndex(REGISTER_EXECUTOR_URI);
		for (String in : inboundPortRegisterURI) {
			registerRequestPort.add(new ControllerInboundPort(in, executorServiceIndex, this));
		}
		for (ControllerInboundPort bom : registerRequestPort) {
			bom.publishPort();
		}

		// Initialize ports relative to devices control
		for (String out : outboundPortDeviceURI) {
			this.controlDevicesPorts.add(new ControllerOutboundPort(out, this));
		}
		for (ControllerOutboundPort port : this.controlDevicesPorts) {
			port.publishPort();
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		// print debug_mode log
		try {
			for (ControllerInboundPort in : this.registerRequestPort)
				in.unpublishPort();
			for (ControllerOutboundPort out : this.controlDevicesPorts)
				out.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	@Override
	public synchronized void execute() throws Exception {
		this.runTask(CONTROL_EXECUTOR_URI, owner -> {
			try {
				// wait for components to register
				Thread.sleep(2000);
				// connect to the battery and change the mode of the battery
				String bipUri = (String) registeredDevices.values().toArray()[0];
				BatteryOutboundPort bop = new BatteryOutboundPort(this);
				bop.publishPort();
				bop.doConnection(bipUri, ControlBatteryConnector.class.getCanonicalName());
				bop.setMode(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see ControllerImplementationI#register(String, String)
	 */
	@Override
	public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception {
		// TODO connector generation here

		// connector is generated, we can register the component
		registeredDevices.put(serial_number, inboundPortURI);
		Log.printAndLog(this, "register(" + serial_number + ", " + inboundPortURI + ") service result : " + true);
		return true;
	}

	// ---------------------------------------------------------------------------
	// Methods useful for tests
	// ---------------------------------------------------------------------------
	/**
	 * @see ControllerImplementationI#getRegisteredDevices()
	 */
	@Override
	public Map<String, String> getRegisteredDevices() throws Exception {
		return registeredDevices;
	}
}
