package main.java.components;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Vector;

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
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import main.java.interfaces.ControllerCI;
import main.java.interfaces.ControllerImplementationI;
import main.java.interfaces.PlanningEquipmentControlCI;
import main.java.interfaces.StandardEquipmentControlCI;
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
@RequiredInterfaces(required = { StandardEquipmentControlCI.class, SuspensionEquipmentControlCI.class,
		PlanningEquipmentControlCI.class })
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
	private Vector<StandardEquipmentControlOutboundPort> stecops;
	// ports used for controlling standard devices
	private Vector<PlanningEquipmentControlOutboundPort> plecops;
	// ports used for controlling standard devices
	private Vector<SuspensionEquipmentControlOutboundPort> suecops;

	public static final String REGISTERING_POOL = "registering-pool";

	protected Controller(String uri, boolean toogleTracing, String inboundPortRegisterURI) throws Exception {
		super(uri, 1, 0);
		assert uri != null;

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
				Thread.sleep(4000);
				// iter on planning equipments

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
		Class<?> generatedConnector;
		try {
			generatedConnector = generateConnector(serial_number, XMLFile);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (generatedConnector == null) {
			return false;
		}
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
			this.doPortConnection(suecop.getPortURI(), inboundPortURI, generatedConnector.getCanonicalName());
			break;
		case "planning":
			PlanningEquipmentControlOutboundPort plecop = new PlanningEquipmentControlOutboundPort(this);
			plecop.publishPort();
			plecops.add(plecop);
			this.doPortConnection(plecop.getPortURI(), inboundPortURI, generatedConnector.getCanonicalName());
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

	private Class<?> generateConnector(String serialNumber, String xmlFile) throws Exception {
		String generatedClassName = serialNumber + "_connector";
		Class<?> superClass = AbstractConnector.class;
		Class<?> offeredInterface;
		Class<?> connectorImplementedInterface;
		HashMap<String, String> implementedMethodNames = new HashMap<>();
		HashMap<String, String> notImplementedMethodBody = new HashMap<>();

		// on determine l'interface implémentée par le connecteur généré
		String equipmentType = getEquipmentType(xmlFile);
		switch (equipmentType) {
		case "suspension":
			connectorImplementedInterface = SuspensionEquipmentControlCI.class;
			break;
		case "planning":
			connectorImplementedInterface = PlanningEquipmentControlCI.class;
			break;
		default:
			connectorImplementedInterface = StandardEquipmentControlCI.class;
		}

		// on détermine l'interface offerte par le connecteur généré
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new StringReader(xmlFile)));

		String offeredInterfaceName = doc.getElementsByTagName("control-adapter").item(0).getAttributes()
				.getNamedItem("offered").getTextContent();
		offeredInterface = Class.forName(offeredInterfaceName);

		// on détermine la "traduction" du nom des méthodes entre ce qui est requis et
		// ce qui est offert et le stocke dans methodNames
		NodeList nodeList = doc.getElementsByTagName("control-adapter").item(0).getChildNodes();
		for (int i = 2; i < nodeList.getLength(); i++) {
			// method
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				if (eElement.getTagName().equals("mode-control")) {// methods in mode control balise
					NodeList nd = node.getChildNodes();
					for (int j = 0; j < nd.getLength(); j++) {
						if (nd.item(j).getNodeType() == Node.ELEMENT_NODE) {
							eElement = (Element) nd.item(j);
							String requiredMethodName = eElement.getTagName();
							String body = eElement.getElementsByTagName("body").item(0).getTextContent();
							int index = body.indexOf('.');
							// body is calling a method
							if (index != -1) {
								int endIndex = body.indexOf('(');
								String offeredMethodName = body.substring(index + 1, endIndex);
								implementedMethodNames.put(requiredMethodName, offeredMethodName);
							} else {
								notImplementedMethodBody.put(requiredMethodName, body);
							}
						}
					}
				} else {
					String requiredMethodName = eElement.getTagName();
					String body = eElement.getElementsByTagName("body").item(0).getTextContent();
					int index = body.indexOf('.');
					// body is calling a method
					if (index != -1) {
						int endIndex = body.indexOf('(');
						String offeredMethodName = body.substring(index + 1, endIndex);
						implementedMethodNames.put(requiredMethodName, offeredMethodName);
					} else {
						notImplementedMethodBody.put(requiredMethodName, body);
					}
				}
			}
		}
		return makeConnectorClassJavassist(generatedClassName, superClass, connectorImplementedInterface,
				offeredInterface, implementedMethodNames, notImplementedMethodBody);
	}

	public Class<?> makeConnectorClassJavassist(String connectorCanonicalClassName, Class<?> connectorSuperclass,
			Class<?> connectorImplementedInterface, Class<?> offeredInterface, HashMap<String, String> methodNamesMap,
			HashMap<String, String> notImplementedMethodBody) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		CtClass cs = pool.get(connectorSuperclass.getCanonicalName());
		CtClass cii = pool.get(connectorImplementedInterface.getCanonicalName());
		CtClass oi = pool.get(offeredInterface.getCanonicalName());
		CtClass connectorCtClass = pool.makeClass(connectorCanonicalClassName);
		connectorCtClass.setSuperclass(cs);
		Method[] methodsToImplement = connectorImplementedInterface.getMethods();

		for (int i = 0; i < methodsToImplement.length; i++) {
			String source = "public ";
			source += methodsToImplement[i].getReturnType().getName() + " ";
			source += methodsToImplement[i].getName() + "(";
			Class<?>[] pt = methodsToImplement[i].getParameterTypes();
			String callParam = "";
			for (int j = 0; j < pt.length; j++) {
				String pName = "aaa" + j;
				source += pt[j].getCanonicalName() + " " + pName;
				callParam += pName;
				if (j < pt.length - 1) {
					source += ", ";
					callParam += ", ";
				}
			}
			source += ")";
			Class<?>[] et = methodsToImplement[i].getExceptionTypes();
			if (et != null && et.length > 0) {
				source += "throws ";
				for (int z = 0; z < et.length; z++) {
					source += et[z].getCanonicalName();
					if (z < et.length - 1)
						source += ",";
				}
			}
			// si la méthode est implémenté par le composant
			if (methodNamesMap.containsKey(methodsToImplement[i].getName())) {
				source += "\n{return ((";
				source += offeredInterface.getCanonicalName() + ")this.offering).";
				source += methodNamesMap.get(methodsToImplement[i].getName());
				source += "(" + callParam + ") ;\n}";
			}
			// la méthode n'est pas implémenté par le composant
			else if (notImplementedMethodBody.containsKey(methodsToImplement[i].getName())) {
				source += "\n{";
				source += notImplementedMethodBody.get(methodsToImplement[i].getName());
				source += "\n}";
			} else {
				throw new Exception("The xml file for the connector : " + connectorCanonicalClassName
						+ " need to implements all the methods (" + methodsToImplement[i].getName() + " is missing).");
			}
			CtMethod theCtMethod = CtMethod.make(source, connectorCtClass);
			connectorCtClass.addMethod(theCtMethod);
		}
		connectorCtClass.setInterfaces(new CtClass[] { cii });
		cii.detach();
		cs.detach();
		oi.detach();
		Class<?> ret = connectorCtClass.toClass();
		connectorCtClass.detach();
		return ret;
	}
}
