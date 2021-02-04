package main.java.components.petrolGenerator.sil;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.components.petrolGenerator.PetrolGenerator;
import main.java.components.petrolGenerator.sil.events.AbstractPetrolGeneratorEvent;
import main.java.components.petrolGenerator.sil.events.EmptyGenerator;
import main.java.utils.FileLogger;

/**
 * The users actions that interacts with the simulated petrol generator
 * 
 * @author Bello Memmi
 *
 */
@ModelExternalEvents(imported = { EmptyGenerator.class })
public class PetrolGeneratorUserSILModel extends AtomicModel {

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	public static final String URI = PetrolGeneratorUserSILModel.class.getSimpleName();

	/** default time interval between event outputs. */
	protected static final double STEP = 1.0;

	protected PetrolGenerator.Operations currentOperation;

	/** time interval between event outputs. */
	protected Duration time2next;

	public static final String PETROL_GENERATOR_REFERENCE_NAME = URI + ":" + "PGRN";

	protected PetrolGenerator owner;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public PetrolGeneratorUserSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.setLogger(new FileLogger("petrolGeneratorUser.log"));
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	public void receiveEmptyGenerator(EmptyGenerator event) {
		this.currentOperation = PetrolGenerator.Operations.EmptyGenerator;
		// we wait 20 minutes before filling
		this.time2next = new Duration(20 * 60, TimeUnit.SECONDS);
	}

	/**
	 * set the current event to be output and return it as result; this method
	 * implements a simple simulation scenario just to test the model.
	 * 
	 * @param t time at which the next event must occur.
	 * @return the next event to be output.
	 */
	protected PetrolGenerator.Operations getNextOperation() {
		PetrolGenerator.Operations ret = null; // to return
		if (this.currentOperation == null) {
			ret = PetrolGenerator.Operations.TurnOn;
			// we wait while the generator is empty
			this.time2next = Duration.INFINITY;
		} else if (this.currentOperation == PetrolGenerator.Operations.FillAll) {
			ret = PetrolGenerator.Operations.TurnOn;
			this.time2next = Duration.INFINITY;
		} else if (this.currentOperation == PetrolGenerator.Operations.EmptyGenerator) {
			ret = PetrolGenerator.Operations.FillAll;
			this.time2next = new Duration(10., this.getSimulatedTimeUnit());
		}
		return ret;
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.owner = (PetrolGenerator) simParams.get(PETROL_GENERATOR_REFERENCE_NAME);
	}

	/**
	 * @see AtomicModel#initialiseState(Time)
	 */
	@Override
	public void initialiseState(Time initialTime) {
		this.time2next = new Duration(STEP, this.getSimulatedTimeUnit());
		this.currentOperation = null;
		this.toggleDebugMode();
		this.logMessage("simulation begins.\n");

		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorsalutbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI> output() {
		return null;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration timeAdvance() {
		if (this.currentOperation == null) {
			return new Duration(0, TimeUnit.SECONDS);
		} else
			return this.time2next;
	}

	/**
	 * @see AtomicModel#userDefinedExternalTransition(Duration)
	 */
	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		assert currentEvents != null && currentEvents.size() == 1;
		Event ce = (Event) currentEvents.get(0);
		this.logger.logMessage("",
				this.getCurrentStateTime() + " PetrolGenerator executing the external event " + ce.eventAsString());
		assert ce instanceof AbstractPetrolGeneratorEvent;
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedInternalTransition(Duration elapsedTime) {
		super.userDefinedInternalTransition(elapsedTime);

		this.currentOperation = this.getNextOperation();
		StringBuffer message = new StringBuffer("executes ");
		message.append(this.currentOperation);
		message.append(".\n");
		this.logMessage(message.toString());

		switch (this.currentOperation) {
		case TurnOn:
			this.owner.runTask(o -> {
				try {
					((PetrolGenerator) o).turnOn();
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			});
			break;
		case TurnOff:
			this.owner.runTask(o -> {
				try {
					((PetrolGenerator) o).turnOff();
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			});
			break;
		case FillAll:
			this.owner.runTask(o -> {
				try {
					((PetrolGenerator) o).fillAll();
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			});
		default:
			break;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void endSimulation(Time endTime) throws Exception {
		this.logMessage("simulation ends.\n");
		super.endSimulation(endTime);
	}

}
