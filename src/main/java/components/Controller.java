package main.java.components;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import fr.sorbonne_u.components.connectors.ConnectorI;
import main.java.interfaces.*;
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
@RequiredInterfaces(required = {StandardEquipmentControlCI.class , SuspensionEquipmentControlCI.class, PlanningEquipmentControlCI.class })
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
				Thread.sleep(5000);
				// iter on planning equipments
				for (PlanningEquipmentControlOutboundPort plecop : this.plecops) {
					plecop.upMode();
					plecop.downMode();
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
		Class<?> generatedConnector = generateConnector(serial_number, XMLFile);
		System.out.println(generatedConnector);
		if (generatedConnector == null) {
			System.out.println("generated connector is null");
			return false;
		}
		// connector is generated, we can register the component
		System.out.println("generated connector cannonical name: "+generatedConnector.getCanonicalName());
		// get the equipment type dans create is port in the associated list
		String equipmentType = getEquipmentType(XMLFile);
		if (equipmentType == null)
			return false;
		switch (equipmentType) {
		case "suspension":
			SuspensionEquipmentControlOutboundPort suecop = new SuspensionEquipmentControlOutboundPort(this);
			suecop.publishPort();
			suecops.add(suecop);
			this.doPortConnection(suecop.getPortURI(), inboundPortURI, generatedConnector.getCanonicalName());
			break;
		case "planning":
			PlanningEquipmentControlOutboundPort plecop = new PlanningEquipmentControlOutboundPort(this);
			plecop.publishPort();
			plecops.add(plecop);
			System.out.println("avant connection avec le nouveau connecteur");
			this.doPortConnection(plecop.getPortURI(), inboundPortURI, generatedConnector.getCanonicalName());
			System.out.println("apres connection avec le nouveau connecteur");
			break;
		default:
			StandardEquipmentControlOutboundPort stecop = new StandardEquipmentControlOutboundPort(this);
			stecop.publishPort();
			stecops.add(stecop);
			this.doPortConnection(stecop.getPortURI(), inboundPortURI, generatedConnector.getCanonicalName());
			break;
		}
		return true;
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

	/**
	 * Generate a connector Class from an xml string
	 * 
	 * @param serial_number
	 * @param XMLFile
	 * @return
	 */
	private Class<?> generateConnector(String serial_number, String XMLFile) {
		try {
			// Génération de classe
			ClassPool classPool = ClassPool.getDefault();
			CtClass cc = classPool.makeClass(serial_number + "_connector");

			CtClass cs = classPool.get("fr.sorbonne_u.components.connectors.AbstractConnector");
			// extends abstractConnector
			cc.setSuperclass(cs);

			// parse xml
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(XMLFile)));

			// Détermination du type d'équipement interface à implémenter
			String typeEquipment = doc.getElementsByTagName("control-adapter").item(0).getAttributes()
					.getNamedItem("type").getTextContent();
			CtClass cii;
			switch (typeEquipment) {
			case "suspension":
				cii = classPool.get("main.java.interfaces.SuspensionEquipmentControlCI");
				cc.setInterfaces(new CtClass[] { cii});
				break;
			case "planning":
				cii = classPool.get("main.java.interfaces.PlanningEquipmentControlCI");
				cc.setInterfaces(new CtClass[] { cii });
				break;
			default:
				cii = classPool.get("main.java.interfaces.StandardEquipmentControlCI");
				cc.setInterfaces(new CtClass[] { cii });
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
						prototypeFunction = "public int currentMode() throws Exception";
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
					case "setMode":
						prototypeFunction = "public boolean setMode(int "+eElement.getElementsByTagName("parameter")
								.item(0).getAttributes().getNamedItem("name").getTextContent() + ") throws Exception";
						break;
					case "postpone":
						prototypeFunction = "public boolean postpone(java.time.Duration "+eElement.getElementsByTagName("parameter")
								.item(0).getAttributes().getNamedItem("name").getTextContent() + ")throws Exception";
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
			cs.detach();
			cii.detach();
			Class<?> ret = cc.toClass();
			cc.detach();

			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
