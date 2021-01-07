package main.java.simulation.petrolGenerator;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.simulation.petrolGenerator.events.AbstractPetrolGeneratorEvent;
import main.java.simulation.petrolGenerator.events.EmptyGenerator;
import main.java.simulation.petrolGenerator.events.FillAll;
import main.java.simulation.petrolGenerator.events.TurnOff;
import main.java.simulation.petrolGenerator.events.TurnOn;

/**
 * Simulation of the petrolGenerator component, at the start he is turned off
 * with 20 liters of petrol in. he produce electricity while he is turned on and
 * get petrol. when he doesnt have petrol, he turn off. Then the user need to
 * fill it and turn it on again.
 * 
 * @author Bello Memmi
 *
 */
@ModelExternalEvents(imported = { TurnOff.class, TurnOn.class, FillAll.class }, exported = { EmptyGenerator.class })
public class PetrolGeneratorElectricity_MILModel extends AtomicHIOA {

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/**
	 * time interval between each reduction of petrol level
	 */
	protected static final double STEP_LENGTH = 1.0;
	protected final Duration standardStep;

	/**
	 * Electricity produced when the petrol generator is on and get petrol
	 */
	protected static final double GENERATING = 5;
	/** tension same for all the house 												*/
	public static final double TENSION = 220;

	/**
	 * Current production of the generator
	 */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentProduction = new Value<>(this, 0.0, 0);

	/**
	 * Current petrol level
	 */
	protected float currentPetrolLevel = 20;

	/**
	 * Maximum petrol level when full filled
	 */
	protected float maximumPetrolLevel = 50;

	/**
	 * Change when an event that can impact the production is received
	 */
	protected boolean consumptionHasChanged = false;

	/**
	 * True if the generator is on, false else
	 */
	protected boolean isOn = false;

	protected boolean needToBeFilled = false;
	protected boolean hasSendEmptyGenerator = false;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public PetrolGeneratorElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.standardStep = new Duration(STEP_LENGTH, simulatedTimeUnit);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * @return the maximum petrol capacity of the generator
	 */
	public float getMaximumPetrolLevel() {
		return maximumPetrolLevel;
	}

	/**
	 * @return the current petrol level
	 */
	public float getCurrentPetrolLevel() {
		return currentPetrolLevel;
	}

	/**
	 * @return true if the generator is turned on, false else
	 */
	public boolean getIsOn() {
		return isOn;
	}

	/**
	 * Turn the generator on, work only if the generator get petrol
	 */
	public void turnOn() {
		if (currentPetrolLevel > 0) {
			isOn = true;
			needToBeFilled = false;
			hasSendEmptyGenerator = false;
		}
	}

	/**
	 * Turn the generator off
	 */
	public void turnOff() {
		isOn = false;
	}

	/**
	 * toggle the value of the state of the model telling whether the
	 * electricity consumption level has just changed or not; when it changes
	 * after receiving an external event, an immediate internal transition
	 * is triggered to update the level of electricity consumption.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 * @author 	Bello Memmi
	 */
	public void toggleConsumptionHasChanged() {
		this.consumptionHasChanged = (this.consumptionHasChanged) ? false : true;
	}

	/**
	 * Add one litter of petrol to the generator
	 */
	public void addOneLPetrol() {
		this.currentPetrolLevel += 1;
	}

	/**
	 * Full fill the generator of petrol
	 */
	public void fillAll() {
		this.currentPetrolLevel = maximumPetrolLevel;
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void initialiseVariables(Time startTime) {
		super.initialiseVariables(startTime);
		this.currentProduction.v = 0.0;
	}

	/**
	 * At the start, the generator get 20 liters of petrol, and is on
	 * 
	 * @see fr.sorbonne_u.devs_simulation.models.Model#initialiseState()
	 */
	@Override
	public void initialiseState() {
		this.isOn = false;
		this.consumptionHasChanged = false;
		this.currentPetrolLevel = 20;
		super.initialiseState();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI> output() {
		if (needToBeFilled && !hasSendEmptyGenerator) {
			System.out.println("SEND EMPTY");
			ArrayList<EventI> ret = new ArrayList<EventI>();
			ret.add(new EmptyGenerator(this.getTimeOfNextEvent()));
			hasSendEmptyGenerator = true;
			return ret;
		} else
			return null;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration timeAdvance() {
		if (consumptionHasChanged) {
			this.toggleConsumptionHasChanged();
			return new Duration(0.0, this.getSimulatedTimeUnit());
		} else if (needToBeFilled && !hasSendEmptyGenerator)
			return new Duration(0.0, this.getSimulatedTimeUnit());
		return standardStep;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedInternalTransition(Duration elapsedTime) {
		super.userDefinedInternalTransition(elapsedTime);
		if (this.isOn && currentPetrolLevel > 0)
			this.currentPetrolLevel -= 1;
		System.out.println("petrol level : " + currentPetrolLevel);
		// if the generator is on and get petrol, he produce electicity
		if (this.isOn && currentPetrolLevel > 0) {
			this.currentProduction.v = GENERATING / TENSION;
		}
		// if the generator is on but dont have petrol, he turn off and dont produce
		// electicity
		else if (this.isOn && currentPetrolLevel <= 0 && !hasSendEmptyGenerator) {
			this.currentProduction.v = 0.;
			this.needToBeFilled = true;
			this.hasSendEmptyGenerator = false;
			this.turnOff();
		}
		// else the generator is off, he dont produce electicity
		else {
			this.currentProduction.v = 0.;
		}
		this.currentProduction.time = this.getCurrentStateTime();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		assert currentEvents != null && currentEvents.size() == 1;
		Event ce = (Event) currentEvents.get(0);
		System.out.println("PetrolGenerator executing the external event " + ce.getClass().getSimpleName() + "("
				+ ce.getTimeOfOccurrence().getSimulatedTime() + ")");
		assert ce instanceof AbstractPetrolGeneratorEvent;
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
	}
}
