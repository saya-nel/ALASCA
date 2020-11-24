package main.java.components;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import main.java.connectors.ControllerConnector;
import main.java.interfaces.ControllerCI;
import main.java.interfaces.ControllerImplementationI;
import main.java.ports.ControllerInboundPort;
import main.java.ports.ControllerOutboundPort;
import main.java.ports.PlanningEquipmentControlOutboundPort;
import main.java.ports.SuspensionEquipmentControlOutboundPort;
import main.java.utils.Log;

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

	// mutex
	private ReentrantLock mutex_register = new ReentrantLock();

	public static final String REGISTERING_POOL = "registering-pool";

	// TODO parametres a revoir, bizarre
	protected Controller(String uri, boolean toogleTracing, String[] inboundPortRegisterURI,
			String[] outboundPortDeviceURI) throws Exception {
		super(uri, 1, 0);
		assert uri != null;
		this.myURI = uri;

		this.createNewExecutorService(CONTROL_EXECUTOR_URI, 1, false);
		this.createNewExecutorService(REGISTER_EXECUTOR_URI, 1, false);

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
				Log.printAndLog(this, "in execute");

				// this.mutex_register.lock();
				// wait for components to register
				// connect to the battery and change the mode of the battery
				// System.out.println(registeredDevices.values());
				// String bipUri = (String) registeredDevices.values().toArray()[1];
				// System.out.println("bipuri: "+bipUri);
				// AbstractInboundPort
				// get uri
				// discover from what component type it is from
				// instantiate the correct outbound port with the uri
				// System.out.println();
				// BatteryOutboundPort bop = new BatteryOutboundPort(bipUri,this);
				// bop.publishPort();
				// bop.doConnection(bipUri, ControlBatteryConnector.class.getCanonicalName());
				// bop.upMode();
				// bop.setMode(0);
				// System.out.println(this.registeredDevices);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();

	}
	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see ControllerImplementationI#register(String,String, String)
	 */
	@Override
	public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception {
		// TODO connector generation here

		// connector is generated, we can register the component
		registeredDevices.put(serial_number, inboundPortURI);
		controlDevicesPorts.add(new ControllerOutboundPort(this));
		controlDevicesPorts.get(controlDevicesPorts.size() - 1).publishPort();
		this.doPortConnection(this.controlDevicesPorts.get(controlDevicesPorts.size() - 1).getPortURI(), inboundPortURI,
				ControllerConnector.class.getCanonicalName());

		// Génération de classe
		ClassPool classPool = ClassPool.getDefault();
		CtClass cc = classPool.makeClass(serial_number + "_connector");

		// extends abstractConnector
		cc.setSuperclass(classPool.get("fr.sorbonne_u.components.connectors.AbstractConnector"));

		// parse xml
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new StringReader(XMLFile)));

		// Détermination du type d'équipement interface à implémenter
		String typeEquipment = doc.getElementsByTagName("control-adapter").item(0).getAttributes().getNamedItem("type")
				.getTextContent();

		switch (typeEquipment) {
		case "suspension":
			cc.setInterfaces(new CtClass[] { classPool.get("interfaces.SuspensionEquipmentControlCI") });
			break;
		case "planning":
			cc.setInterfaces(new CtClass[] { classPool.get("interfaces.PlanningEquipmentControlCI") });
			break;
		default:
			cc.setInterfaces(new CtClass[] { classPool.get("interfaces.StandardEquipmentControlCI") });
		}
		System.out.println("type : " + typeEquipment);

		NodeList nodeList = doc.getElementsByTagName("control-adapter").item(0).getChildNodes();
		for (int i = 2; i < nodeList.getLength(); i++) {
			// method
			String prototypeFunction;
			Node node = nodeList.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				String functionName = eElement.getTagName();
				switch (functionName) {
				case "currentMode":
					prototypeFunction = "public int currentMode() throws Exception ";
					break;
				case "emergency":
					prototypeFunction = "public double emergency() throws Exception";
					break;
				case "startTime":
					prototypeFunction = "public java.time.LocalTime startTime() throws Exception";
					break;
				case "duration":
					prototypeFunction = "public java.time.Duration duration() throws Exception";
					break;
				default:
					prototypeFunction = "public boolean " + functionName + "() throws Exception";
				}

				String body = eElement.getElementsByTagName("body").item(0).getTextContent();
				String req = eElement.getElementsByTagName("required").item(0).getTextContent();
				String nameEquipment = eElement.getElementsByTagName("body").item(0).getAttributes()
						.getNamedItem("equipmentRef").getTextContent();
				String body2 = req + " " + nameEquipment + " = " + "(" + req + ") this.offering;";
				String body3 = body2 + body;
				String function = prototypeFunction + "{\n" + body3 + "}";
				System.out.println("function: " + function);
				CtMethod m = CtMethod.make(function, cc);
				cc.addMethod(m);
			}
		}

		cc.writeFile("src/main/java/generatedClasses");

		switch (typeEquipment) {
		case "suspension":
			this.doPortConnection(inboundPortURI, new SuspensionEquipmentControlOutboundPort(this).getPortURI(),
					"generatedClasses." + serial_number + "_connector");
			System.out.println("ports " + this.portURIs2ports);
			break;
		case "planning":
			System.out.println("in planning");
			PlanningEquipmentControlOutboundPort out = new PlanningEquipmentControlOutboundPort(inboundPortURI, this);
			out.publishPort();

			// System.out.println("ports" + out.getPortURI());
			try {

				out.doConnection(inboundPortURI, "generatedClasses." + serial_number + "_connector");
				boolean succeed = out.downMode();
			} catch (Exception e) {
				e.printStackTrace();

			}

			// TODO Régler le problème de synchronisation doPortConnection pas fait

			break;

		}
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
