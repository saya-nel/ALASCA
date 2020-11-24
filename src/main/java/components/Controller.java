package main.java.components;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import main.java.connectors.ControlBatteryConnector;
import main.java.interfaces.ControllerCI;
import main.java.interfaces.ControllerImplementationI;
import main.java.interfaces.PlanningEquipmentControlCI;
import main.java.interfaces.SuspensionEquipmentControlCI;
import main.java.ports.ControllerInboundPort;
import main.java.ports.PlanningEquipmentControlOutboundPort;
import main.java.ports.StandardEquipmentControlOutboundPort;
import main.java.ports.SuspensionEquipmentControlOutboundPort;

/**
 *
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { ControllerCI.class })
@RequiredInterfaces(required = { SuspensionEquipmentControlCI.class, PlanningEquipmentControlCI.class })
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
	private ControllerInboundPort cip;

	// ports used for controlling standard devices
	private List<StandardEquipmentControlOutboundPort> stecops;
	// ports used for controlling standard devices
	private List<PlanningEquipmentControlOutboundPort> plecops;
	// ports used for controlling standard devices
	private List<SuspensionEquipmentControlOutboundPort> suecops;

	// uri of component
	private String myURI;

	// mutex
	private ReentrantLock mutex_register = new ReentrantLock();

	public static final String REGISTERING_POOL = "registering-pool";

	// TODO parametres a revoir, bizarre
	protected Controller(String uri, boolean toogleTracing, String inboundPortRegisterURI) throws Exception {
		super(uri, 1, 0);
		assert uri != null;
		this.myURI = uri;

		this.createNewExecutorService(CONTROL_EXECUTOR_URI, 1, false);
		this.createNewExecutorService(REGISTER_EXECUTOR_URI, 1, false);

		initialise(inboundPortRegisterURI);
		if (toogleTracing) {
			this.tracer.get().setTitle("Controller component");
			this.tracer.get().setRelativePosition(0, 0);
			this.toggleTracing();
		}
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	protected void initialise(String inboundPortRegisterURI) throws Exception {
		// Initialize ports relative to registering
		this.cip = new ControllerInboundPort(inboundPortRegisterURI,
				this.getExecutorServiceIndex(REGISTER_EXECUTOR_URI), this);
		this.cip.publishPort();

		// Initialize ports lists relative to controlling devices
		this.stecops = new Vector<>();
		this.plecops = new Vector<>();
		this.suecops = new Vector<>();
	}

	@Override
	public synchronized void finalise() throws Exception {
		for (StandardEquipmentControlOutboundPort stecop : this.stecops)
			stecop.doDisconnection();
		for (PlanningEquipmentControlOutboundPort plecop : this.plecops)
			plecop.doDisconnection();
		for (SuspensionEquipmentControlOutboundPort suecop : this.suecops)
			suecop.doDisconnection();
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			cip.unpublishPort();
			for (StandardEquipmentControlOutboundPort stecop : this.stecops)
				stecop.unpublishPort();
			for (PlanningEquipmentControlOutboundPort plecop : this.plecops)
				plecop.unpublishPort();
			for (SuspensionEquipmentControlOutboundPort suecop : this.suecops)
				suecop.unpublishPort();
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
				// iter on planning equipments
				for (PlanningEquipmentControlOutboundPort plecop : this.plecops) {
					plecop.upMode();
					plecop.setMode(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// ------------------------ -------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see ControllerImplementationI#register(String,String, String)
	 */
	@Override
	public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception {
		// TODO connector generation here
		if (!generateConnector(serial_number, XMLFile))
			return false;

		// connector is generated, we can register the component

		// get the equipment type dans create is port in the associated list
		String equipmentType = getEquipmentType(XMLFile);
		if (equipmentType == null)
			return false;
		switch (equipmentType) {
		case "suspension":
			SuspensionEquipmentControlOutboundPort suecop = new SuspensionEquipmentControlOutboundPort(this);
			suecop.publishPort();
			suecops.add(suecop);
			// TODO remplacer ici avec le conneteur générer
			this.doPortConnection(suecop.getPortURI(), inboundPortURI,
					ControlBatteryConnector.class.getCanonicalName());
			break;
		case "planning":
			PlanningEquipmentControlOutboundPort plecop = new PlanningEquipmentControlOutboundPort(this);
			plecop.publishPort();
			plecops.add(plecop);
			// TODO remplacer ici avec le conneteur générer
			this.doPortConnection(plecop.getPortURI(), inboundPortURI,
					ControlBatteryConnector.class.getCanonicalName());
			break;
		default:
			StandardEquipmentControlOutboundPort stecop = new StandardEquipmentControlOutboundPort(this);
			stecop.publishPort();
			stecops.add(stecop);
			// TODO remplacer ici avec le conneteur générer
//			stecop.doConnection(inboundPortURI, ControlBatteryConnector.class.getCanonicalName());
			break;
		}
		return true;
	}

	/**
	 * @see ControllerImplementationI#getRegisteredDevices()
	 */
	@Override
	public Map<String, String> getRegisteredDevices() throws Exception {
//		return registeredDevices;
		return null;
	}

	// -------------------------------------------------------------------------
	// Component private methods
	// -------------------------------------------------------------------------

	private String getEquipmentType(String xml) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
			return doc.getElementsByTagName("control-adapter").item(0).getAttributes().getNamedItem("type")
					.getTextContent();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean generateConnector(String serial_number, String XMLFile) {
		try {
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
			String typeEquipment = doc.getElementsByTagName("control-adapter").item(0).getAttributes()
					.getNamedItem("type").getTextContent();

			switch (typeEquipment) {
			case "suspension":
				cc.setInterfaces(new CtClass[] { classPool.get("main.java.interfaces.SuspensionEquipmentControlCI") });
				break;
			case "planning":
				cc.setInterfaces(new CtClass[] { classPool.get("main.java.interfaces.PlanningEquipmentControlCI") });
				break;
			default:
				cc.setInterfaces(new CtClass[] { classPool.get("main.java.interfaces.StandardEquipmentControlCI") });
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
			System.out.println("WRITE ok");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
