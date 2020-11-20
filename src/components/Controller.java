package components;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import interfaces.ControllerCI;
import interfaces.ControllerImplementationI;
import ports.ControllerInboundPort;
import ports.ControllerOutboundPort;

/**
 *
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { ControllerCI.class })

public class Controller extends AbstractComponent implements ControllerImplementationI {
	// ports used for registering
	private List<ControllerInboundPort> registerRequestPort;

	// ports used for controlling devices
	private List<ControllerOutboundPort> controlDevicesPorts;

	// uri of component
	private String myURI;

	// Map serial number in key and XMLFile content this xml is supposed to contain
	// the implementation of the connector's controlled methods
	private Map<String, String> registeredDevices;

	protected Controller(String uri, String[] inboundPortRegisterURI, String[] outboundPortDeviceURI) throws Exception {
		super(uri, 1, 0);
		assert uri != null;
		this.myURI = uri;
		initialise(inboundPortRegisterURI, outboundPortDeviceURI);
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

		for (String in : inboundPortRegisterURI) {
			registerRequestPort.add(new ControllerInboundPort(in, this));
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

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see ControllerImplementationI#register(String, String)
	 */
	@Override
	public boolean register(String serial_number,  String inboundPortURI, String XMLFile) throws Exception {
		// TODO connector generation here

		// connector is generated, we can register the component
		registeredDevices.put(serial_number, XMLFile);
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
