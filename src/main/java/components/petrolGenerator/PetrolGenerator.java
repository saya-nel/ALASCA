package main.java.components.petrolGenerator;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.components.petrolGenerator.interfaces.PetrolGeneratorCI;
import main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI;
import main.java.components.petrolGenerator.ports.PetrolGeneratorInboundPort;
import main.java.components.petrolGenerator.sil.PetrolGeneratorRTAtomicSimulatorPlugin;
import main.java.components.petrolGenerator.sil.PetrolGeneratorSILCoupledModel;
import main.java.components.petrolGenerator.sil.PetrolGeneratorStateSILModel;
import main.java.components.petrolGenerator.sil.PetrolGeneratorUserSILModel;
import main.java.deployment.RunSILSimulation;
import main.java.utils.Log;

/**
 * Class representing the petrol generator component
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { PetrolGeneratorCI.class })
public class PetrolGenerator extends AbstractCyPhyComponent implements PetrolGeneratorImplementationI {

	public enum Operations {
		TurnOn, TurnOff, FillAll, EmptyGenerator
	}

	/**
	 * URI of the reflection inbound port of this component; works for singleton.
	 */
	public static final String REFLECTION_INBOUND_PORT_URI = "pg-ibp-uri";

	/** true if the component is executed in a SIL simulation mode. */
	protected boolean isSILSimulated;
	/** true if the component is under unit test. */
	protected boolean isUnitTest;

	protected PetrolGeneratorRTAtomicSimulatorPlugin simulatorPlugin;
	protected static final String SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

	/**
	 * Maximum petrol level
	 */
	protected float maximumPetrolLevel;

	/**
	 * generator petrol level
	 */
	protected float petrolLevel;

	protected final double PETROL_CONSUMPTION = 0.0005; // petrol consumed for 1 second

	/**
	 * True if the generator is running
	 */
	protected boolean isTurnedOn;

	/**
	 * Inbound port of the petrol generator
	 */
	protected PetrolGeneratorInboundPort pgip;

	protected boolean hasSendEmptyGenerator;

	/**
	 * Constructor of petrol generator
	 * 
	 * @param reflectionPortURI URI of the component
	 * @param bipURI            URI of the petrol generator inbound port
	 * @throws Exception
	 */
	protected PetrolGenerator(String pgipURI, boolean isSILSimulated, boolean isUnitTest) throws Exception {
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);
		this.initialise(pgipURI, isSILSimulated, isUnitTest);
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * Initialize the petrol generator component
	 * 
	 * @param batteryInboundPortURI
	 * @throws Exception
	 */
	protected void initialise(String pgipURI, boolean isSILSimulated, boolean isUnitTest) throws Exception {
		this.isSILSimulated = isSILSimulated;
		this.isUnitTest = isUnitTest;
		this.isTurnedOn = false;
		this.maximumPetrolLevel = 5;
		this.hasSendEmptyGenerator = false;
		this.petrolLevel = 2;
		this.pgip = new PetrolGeneratorInboundPort(pgipURI, this);
		this.pgip.publishPort();

		this.tracer.get().setTitle("PetrolGenerator component");
		this.tracer.get().setRelativePosition(0, 3);
		this.toggleTracing();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();

		if (this.isSILSimulated) {
			try {
				// create the scheduled executor service that will run the
				// simulation tasks
				this.createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
				// create and initialise the atomic simulator plug-in that will
				// hold and execute the SIL simulation models
				this.simulatorPlugin = new PetrolGeneratorRTAtomicSimulatorPlugin();
				this.simulatorPlugin.setPluginURI(PetrolGeneratorSILCoupledModel.URI);
				this.simulatorPlugin.setSimulationExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI);
				this.simulatorPlugin.initialiseSimulationArchitecture(this.isUnitTest);
				this.installPlugin(this.simulatorPlugin);
			} catch (Exception e) {
				throw new ComponentStartException(e);
			}
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void execute() throws Exception {
		super.execute();

		if (this.isSILSimulated && this.isUnitTest) {
			this.simulatorPlugin.setSimulationRunParameters(new HashMap<String, Object>());
			this.simulatorPlugin.startRTSimulation(System.currentTimeMillis() + 100, 0.0, 10.1);
		}

		class DecreasePetrol extends TimerTask {
			@Override
			public void run() {
				try {
					if (!isStarted()) {
						cancel();
						throw new Exception();
					}

					if (isTurnedOn) {
						petrolLevel -= PETROL_CONSUMPTION;
						if (petrolLevel < 0)
							petrolLevel = 0;
						if (isSILSimulated && petrolLevel == 0 && !hasSendEmptyGenerator) {

							turnOff();
							simulateOperation(Operations.EmptyGenerator);
						}
						logMessage("current petrol level : " + petrolLevel);
					}
				} catch (Exception e) {
				}
			}
		}

		// wait the start of simulation and run Decrease petrol each simulated second
		Thread.sleep(RunSILSimulation.DELAY_TO_START_SIMULATION);
		Timer t = new Timer();
		t.schedule(new DecreasePetrol(), 0, (long) (1000 / RunSILSimulation.ACC_FACTOR));
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.pgip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#getMaxLevel()
	 */
	@Override
	public float getMaxLevel() throws Exception {
		Log.printAndLog(this, "getMaxLevel() service result : " + maximumPetrolLevel);
		return maximumPetrolLevel;
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#getPetrolLevel()
	 */
	@Override
	public float getPetrolLevel() throws Exception {
		Log.printAndLog(this, "getPetrolLevel() service result : " + petrolLevel);
		return petrolLevel;
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#addPetrol(float)
	 */
	@Override
	public void addPetrol(float quantity) throws Exception {
		if (petrolLevel + quantity <= maximumPetrolLevel)
			petrolLevel = petrolLevel + quantity;
		else
			petrolLevel = maximumPetrolLevel;
		Log.printAndLog(this, "addPetrol(" + quantity + ") service called, new petrol level :  " + petrolLevel);
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		isTurnedOn = true;
		if (hasSendEmptyGenerator)
			hasSendEmptyGenerator = false;

		Log.printAndLog(this, "turnOn() service called.");

		if (this.isSILSimulated) {
			this.simulateOperation(Operations.TurnOn);
		}
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		isTurnedOn = false;

		Log.printAndLog(this, "turnOff() service called.");

		if (this.isSILSimulated) {
			this.simulateOperation(Operations.TurnOff);
		}
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		Log.printAndLog(this, "isTurnedOn() service result : " + isTurnedOn);
		return isTurnedOn;
	}

	@Override
	public void fillAll() throws Exception {
		this.petrolLevel = this.maximumPetrolLevel;
		Log.printAndLog(this, "fillAll() service called, new petrol level : " + petrolLevel);
	}

	protected void simulateOperation(Operations op) throws Exception {
		switch (op) {
		case TurnOn:
			this.simulatorPlugin.triggerExternalEvent(PetrolGeneratorStateSILModel.URI,
					t -> new main.java.components.petrolGenerator.sil.events.TurnOn(t));
			break;
		case TurnOff:
			this.simulatorPlugin.triggerExternalEvent(PetrolGeneratorStateSILModel.URI,
					t -> new main.java.components.petrolGenerator.sil.events.TurnOff(t));
			break;
		case EmptyGenerator:
			hasSendEmptyGenerator = true;
			this.simulatorPlugin.triggerExternalEvent(PetrolGeneratorUserSILModel.URI,
					t -> new main.java.components.petrolGenerator.sil.events.EmptyGenerator(t));
		default:
			break;
		}
	}
}
