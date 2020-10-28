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

@OfferedInterfaces(offered = { ControllerCI.class })

public class Controller extends AbstractComponent implements ControllerImplementationI {
	// ports used for registering
	private List<ControllerInboundPort> registerRequestPort;

	// ports used for controlling devices
	private List<ControllerOutboundPort> controlDevicesPorts;

	// uri of component
	private String myURI;

	// Map serial number in key and XMLfile value
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

	public void initialise(String[] inboundPortRegisterURI, String[] outboundPortDeviceURI) throws Exception {
		registeredDevices = new ConcurrentHashMap<>();

		// Initialize ports relative to registering
		this.registerRequestPort = new Vector<>();

		for (String out : inboundPortRegisterURI) {
			registerRequestPort.add(new ControllerInboundPort(out, this));
		}
		for (ControllerInboundPort bom : registerRequestPort) {
			bom.publishPort();
		}

		// Initialize ports relative to devices control
		for (String in : outboundPortDeviceURI) {
			this.controlDevicesPorts.add(new ControllerOutboundPort(in, this));
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

	@Override
	public void register(String serial_number, String XMLFile) throws Exception {
		registeredDevices.put(serial_number, XMLFile);
	}
}
