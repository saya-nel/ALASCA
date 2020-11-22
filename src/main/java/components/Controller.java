package components;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import connectors.ControllerConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import interfaces.ControllerCI;
import interfaces.ControllerImplementationI;
import javassist.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ports.ControllerInboundPort;
import ports.ControllerOutboundPort;
import utils.Log;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static java.lang.reflect.Modifier.PUBLIC;

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
				// wait for components to register
				//Thread.sleep(4000);
				// connect to the battery and change the mode of the battery
				//System.out.println(registeredDevices.values());
				//String bipUri = (String) registeredDevices.values().toArray()[1];
				//System.out.println("bipuri: "+bipUri);
				//AbstractInboundPort
				// get uri
				// discover from what component type it is from
				// instantiate the correct outbound port with the uri
				//System.out.println();
				//BatteryOutboundPort bop = new BatteryOutboundPort(bipUri,this);
				//bop.publishPort();
				//bop.doConnection(bipUri, ControlBatteryConnector.class.getCanonicalName());
				//bop.upMode();
				//bop.setMode(0);
				//System.out.println(this.registeredDevices);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
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
		controlDevicesPorts.get(controlDevicesPorts.size()-1).publishPort();
		this.doPortConnection(this.controlDevicesPorts.get(controlDevicesPorts.size()-1).getPortURI(), inboundPortURI, ControllerConnector.class.getCanonicalName());

		// Génération de classe
		ClassPool classPool = ClassPool.getDefault();
		CtClass cc = classPool.makeClass(serial_number+"connector");
		// extends abstractConnector
		cc.setSuperclass(classPool.get("fr.sorbonne_u.components.connectors.AbstractConnector"));
		//cc.setInterfaces(new CtClass[]{classPool.get("interfaces.ControlBatteryConnector")});

		//parse xml
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new StringReader(XMLFile)));


		System.out.println("My doc is : "+doc.getElementsByTagName("control-adapter"));
		//Détermination du type d'équipement interface à implémenter
		String typeEquipment = doc.getElementsByTagName("control-adapter").item(0).getAttributes().getNamedItem("type").getTextContent();
		switch(typeEquipment)
		{
			case "suspension":
				cc.setInterfaces(new CtClass[] {classPool.get("interfaces.SuspensionEquipmentControlCI")});
				break;
			//case "standard":
			//	cc.setInterfaces();

		}
		System.out.println("type : "+typeEquipment);


		NodeList nodeList = doc.getElementsByTagName("control-adapter").item(0).getChildNodes();
		for (int i=2; i< nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			System.out.println("i"+i);

			if(node.getNodeType() == Node.ELEMENT_NODE)
			{
				//System.out.println(node);
				Element eElement = (Element) node;
				System.out.println("eElement: "+eElement);
				System.out.println("eElement tag name: "+eElement.getTagName());
				CtMethod m = new CtMethod(CtClass.voidType, eElement.getTagName(), new CtClass[] {}, cc);

				String body = eElement.getElementsByTagName("body").item(0).getTextContent() ;
				String req = eElement.getElementsByTagName("required").item(0).getTextContent();
				//System.out.println("required: "+req);
				String nameComponent = eElement.getElementsByTagName("body").item(0).getAttributes().getNamedItem("equipmentRef").getTextContent();
				System.out.println("name component :"+nameComponent);
				String body2 = req + " " + nameComponent + " = "+ "("+req+") this.offering;\n";
				System.out.println("body2: "+body2);
				String body3 = body2 + body;
				//System.out.println("body: "+ body);
				System.out.println("body3: "+body3);
				//m.setBody(body);
				//System.out.println(m);
				System.out.println("content source: "+eElement.getElementsByTagName("body").item(0).getTextContent());
				cc.addMethod(m);
			}
		}


		CtMethod m = new CtMethod(CtClass.intType, "move", new CtClass[] {}, cc);//CtNewMethod.make("public int xmove(int dx) { x += dx; }",cc);
		m.setBody("return 3;");
		cc.addMethod(m);

		cc.writeFile("src/main/generatedClasses");
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
