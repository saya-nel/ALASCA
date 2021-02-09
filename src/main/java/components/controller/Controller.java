package main.java.components.controller;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import main.java.components.controller.interfaces.ControllerCI;
import main.java.components.controller.interfaces.ControllerImplementationI;
import main.java.components.controller.interfaces.PlanningEquipmentControlCI;
import main.java.components.controller.interfaces.StandardEquipmentControlCI;
import main.java.components.controller.interfaces.SuspensionEquipmentControlCI;
import main.java.components.controller.ports.ControllerInboundPort;
import main.java.components.controller.ports.PlanningEquipmentControlOutboundPort;
import main.java.components.controller.ports.StandardEquipmentControlOutboundPort;
import main.java.components.controller.ports.SuspensionEquipmentControlOutboundPort;
import main.java.components.electricMeter.connectors.ElectricMeterConnector;
import main.java.components.electricMeter.interfaces.ElectricMeterCI;
import main.java.components.electricMeter.ports.ElectricMeterOutboundPort;
import main.java.deployment.RunSILSimulation;
import main.java.utils.FileLogger;
import main.java.utils.Log;

/**
 * The class <code>Controller</code> implements the controller component.
 *
 * The controller component handle the management of the energy in the house the
 * components register to the controller, which connect to them after this and
 * call their services according to the actual available energy
 *
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { ControllerCI.class })
@RequiredInterfaces(required = { StandardEquipmentControlCI.class, SuspensionEquipmentControlCI.class,
		PlanningEquipmentControlCI.class, ElectricMeterCI.class })
public class Controller extends AbstractCyPhyComponent implements ControllerImplementationI {

	/**
	 * postpone duration step
	 */
	public static final int POSTPONE_DURATION = 2;

	/**
	 * URI of the reflection inbound port of this component; works for singleton.
	 */
	public static final String REFLECTION_INBOUND_PORT_URI = "controller-ibp-uri";

	/**
	 * URI of the pool of threads for control
	 */
	public static final String CONTROL_EXECUTOR_URI = "control";

	/**
	 * URI of the pool of threads for registering
	 */
	public static final String REGISTER_EXECUTOR_URI = "register";

	/**
	 * ports used for registering
	 */
	private ControllerInboundPort cip;

	/**
	 * ports used for controlling standard devices
	 */
	private Vector<StandardEquipmentControlOutboundPort> stecops;
	/**
	 * ports used for controlling planning devices
	 */
	private Vector<PlanningEquipmentControlOutboundPort> plecops;
	/**
	 * ports used for controlling suspensible devices
	 */
	private Vector<SuspensionEquipmentControlOutboundPort> suecops;

	/**
	 * outboundPort to use ElectricMeter services
	 */
	private ElectricMeterOutboundPort eop;

	/**
	 * uri of the {@link ElectricMeterOutboundPort}
	 */
	private String eipURI;

	private FileLogger fileLogger;

	/**
	 * Constructor of the controller
	 * 
	 * @param cipURI inbound port uri of controller for registering
	 * @param eipURI inbound port uri of the electric meter
	 * @throws Exception
	 */
	protected Controller(String cipURI, String eipURI) throws Exception {
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);

		this.createNewExecutorService(CONTROL_EXECUTOR_URI, 1, false);
		this.createNewExecutorService(REGISTER_EXECUTOR_URI, 1, false);

		this.eipURI = eipURI;

		initialise(cipURI);

		fileLogger = new FileLogger("controller.log");
		this.tracer.get().setTitle("Controller component");
		this.tracer.get().setRelativePosition(2, 1);
		this.toggleTracing();
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * Initialise the controller component
	 * 
	 * @param cipURI inbound port uri of the controller for registering
	 * @throws Exception
	 */
	protected void initialise(String cipURI) throws Exception {
		// Initialize ports relative to registering
		this.cip = new ControllerInboundPort(cipURI, this.getExecutorServiceIndex(REGISTER_EXECUTOR_URI), this);
		this.cip.publishPort();

		// Initialize ports lists relative to controlling devices
		this.stecops = new Vector<>();
		this.plecops = new Vector<>();
		this.suecops = new Vector<>();
		this.eop = new ElectricMeterOutboundPort(this);
		this.eop.localPublishPort();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();
		try {
			this.doPortConnection(this.eop.getPortURI(), this.eipURI, ElectricMeterConnector.class.getCanonicalName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void finalise() throws Exception {
		for (StandardEquipmentControlOutboundPort stecop : this.stecops)
			stecop.doDisconnection();
		for (PlanningEquipmentControlOutboundPort plecop : this.plecops)
			plecop.doDisconnection();
		for (SuspensionEquipmentControlOutboundPort suecop : this.suecops)
			suecop.doDisconnection();
		eop.doDisconnection();
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
			eop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	double lastLoopEnergy = 0;
	boolean isRunning = false;

	/**
	 * Handle the management of the available energy in the house, the controller
	 * look each second the consumption and production and make decisions
	 * 
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void execute() throws Exception {

		ComponentI me = this;

		class RunControl extends TimerTask {

			@Override
			public void run() {
				try {
					if (!isRunning) {
						isRunning = true;

						if (!isStarted()) {
							cancel();
							throw new Exception();
						}

						double prod = eop.getProduction();
						double cons = eop.getIntensity();

						double energy = prod - cons;

						if (energy < 0) {
							if (lastLoopEnergy >= 0) {
								String message = "House energy being negative";
								Log.printAndLog(me, message);
								fileLogger.logMessage("", message);
							}

							// down mode all equipments, but dont ask to battery to produce energy
							int totalDowned = 0;
							for (StandardEquipmentControlOutboundPort stecop : stecops) {
								boolean res = stecop.downMode();
								if (res)
									totalDowned++;
							}
							for (SuspensionEquipmentControlOutboundPort suecop : suecops) {
								boolean res = suecop.downMode();
								if (res)
									totalDowned++;
							}
							for (PlanningEquipmentControlOutboundPort plecop : plecops) {
								if (!plecop.getServerPortURI().equals(RunSILSimulation.BATTERY_INBOUND_PORT_URI)) {
									boolean res = plecop.downMode();
									if (res)
										totalDowned++;
								}
							}
							if (totalDowned > 0) {
								String message = "have execute downMode() with success on " + totalDowned
										+ " equipments";
								Log.printAndLog(me, message);
								fileLogger.logMessage("", message);
							}

							// if no equipment was downed, we try to launch the battery
							if (totalDowned == 0) {
								boolean batteryProduce = false;
								for (PlanningEquipmentControlOutboundPort plecop : plecops) {
									if (plecop.getServerPortURI().equals(RunSILSimulation.BATTERY_INBOUND_PORT_URI)) {
										batteryProduce = plecop.setMode(0);
									}
								}
								if (batteryProduce) {
									String message = "battery wasn't producing energy, start to produce now";
									Log.printAndLog(me, message);
									fileLogger.logMessage("", message);
								}

								// if the battery can't product energy or was already producting energy, we try
								// postpone equipments that arent battery
								if (!batteryProduce) {
									int nbPostponed = 0;
									for (PlanningEquipmentControlOutboundPort plecop : plecops) {
										if (!plecop.getServerPortURI()
												.equals(RunSILSimulation.BATTERY_INBOUND_PORT_URI)) {
											boolean res = plecop.postpone(Duration.ofMinutes(30));
											if (res)
												nbPostponed++;
										}
									}
									if (nbPostponed > 0) {
										String message = "have execute postpone(30 minutes) with success on "
												+ nbPostponed + " equipments";
										Log.printAndLog(me, message);
										fileLogger.logMessage("", message);
									}

									if (nbPostponed == 0) {
										int nbSuspended = 0;
										// if no equipment can be postponed, we stop the suspensibles
										for (SuspensionEquipmentControlCI suecop : suecops) {
											boolean res = suecop.suspend();
											if (res)
												nbSuspended++;
										}
										if (nbSuspended > 0) {
											String message = "have execute suspend() with success on " + nbSuspended
													+ " equipments";
											Log.printAndLog(me, message);
											fileLogger.logMessage("", message);
										}
									}
								}
							}

						}
						// positive house energy
						else {
							if (lastLoopEnergy < 0) {
								String message = "House energy being positive";
								Log.printAndLog(me, message);
								fileLogger.logMessage("", message);
							}

							// if the battery is draining and we have enought energy to stop it, we stop it
							boolean batteryStoppedDraining = false;
							if (energy > 7) {
								for (PlanningEquipmentControlOutboundPort plecop : plecops) {
									if (plecop.getServerPortURI().equals(RunSILSimulation.BATTERY_INBOUND_PORT_URI)) {
										if (plecop.currentMode() == 0)
											batteryStoppedDraining = plecop.setMode(1);
									}
								}
								if (batteryStoppedDraining) {
									String message = "force battery to go sleep";
									Log.printAndLog(me, message);
									fileLogger.logMessage("", message);
								}
							}

							// if the battery wasn't draining, we unsuspend equipment
							if (!batteryStoppedDraining) {

								// we unsuspend equipment if we have enougth energy
								int nbActivated = 0;
								if (energy > 2) {
									for (SuspensionEquipmentControlCI suecop : suecops) {
										boolean res = suecop.resume();
										if (res)
											nbActivated++;
									}
									if (nbActivated > 0) {
										String message = "have unsuspended " + nbActivated + " equipments";
										Log.printAndLog(me, message);
										fileLogger.logMessage("", message);
									}
								}

								// if no equipment was unsuspended, we upmode equipments except battery
								if (nbActivated == 0 && energy > 2) {
									int totalUpped = 0;
									for (StandardEquipmentControlOutboundPort stecop : stecops) {
										boolean res = stecop.upMode();
										if (res)
											totalUpped++;
									}
									for (SuspensionEquipmentControlOutboundPort suecop : suecops) {
										boolean res = suecop.upMode();
										if (res)
											totalUpped++;
									}
									for (PlanningEquipmentControlOutboundPort plecop : plecops) {
										if (!plecop.getServerPortURI()
												.equals(RunSILSimulation.BATTERY_INBOUND_PORT_URI)) {
											boolean res = plecop.upMode();
											if (res)
												totalUpped++;
										}
									}
									if (totalUpped > 0) {
										String message = "have execute upMode() with success on " + totalUpped
												+ " equipments";
										Log.printAndLog(me, message);
										fileLogger.logMessage("", message);
									}

									// if no equipment was upped, we put battery in charging mode if we have enougth
									// energy
									if (totalUpped == 0 && energy > 7) {
										boolean batteryIsCharging = false;
										for (PlanningEquipmentControlOutboundPort plecop : plecops) {
											if (plecop.getServerPortURI()
													.equals(RunSILSimulation.BATTERY_INBOUND_PORT_URI)) {
												batteryIsCharging = plecop.setMode(2);
											}
										}
										if (batteryIsCharging) {
											String message = "battery is now recharging.";
											Log.printAndLog(me, message);
											fileLogger.logMessage("", message);
										}
									}
								}
							}

						}

						lastLoopEnergy = energy;
						isRunning = false;
					}
				} catch (Exception e) {
				}
			}
		}
		this.runTask(CONTROL_EXECUTOR_URI, owner -> {
			try {

				// wait the start of simulation and run Decrease petrol each simulated second
				Thread.sleep(RunSILSimulation.DELAY_TO_START_SIMULATION);
				Timer t = new Timer();
				t.schedule(new RunControl(), 0, (long) (1000 / RunSILSimulation.ACC_FACTOR));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// ------------------------ -------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see main.java.components.controller.interfaces.ControllerImplementationI#register(String,String,
	 *      String)
	 */
	@Override
	public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception {
		Log.printAndLog(this, "try to register equipment : " + serial_number);
		Class<?> generatedConnector;
		try {
			generatedConnector = generateConnector(serial_number, XMLFile);
		} catch (Exception e) {
			Log.printAndLog(this, e.getMessage());
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
			suecop.localPublishPort();
			suecops.add(suecop);
			this.doPortConnection(suecop.getPortURI(), inboundPortURI, generatedConnector.getCanonicalName());
			break;
		case "planning":
			PlanningEquipmentControlOutboundPort plecop = new PlanningEquipmentControlOutboundPort(this);
			plecop.localPublishPort();
			plecops.add(plecop);
			this.doPortConnection(plecop.getPortURI(), inboundPortURI, generatedConnector.getCanonicalName());
			break;
		default:
			StandardEquipmentControlOutboundPort stecop = new StandardEquipmentControlOutboundPort(this);
			stecop.localPublishPort();
			stecops.add(stecop);
			this.doPortConnection(stecop.getPortURI(), inboundPortURI, generatedConnector.getCanonicalName());
			break;
		}
		Log.printAndLog(this, "Equipment : " + serial_number + " is registered.");
		return true;
	}

	// -------------------------------------------------------------------------
	// Component private methods
	// -------------------------------------------------------------------------

	/**
	 * Return the equipment type (String) from is xml adapter
	 * 
	 * @param xml adapter
	 * @return equipment type
	 */
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
	 * Create a connector to connect to the equipment
	 * 
	 * @param serialNumber serial number of the equipment
	 * @param xmlFile      adapter
	 * @return the generated connector
	 * @throws Exception
	 */
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
