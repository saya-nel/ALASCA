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
import main.java.simulation.petrolGenerator.events.AddPetrol;
import main.java.simulation.petrolGenerator.events.GetMaxLevel;
import main.java.simulation.petrolGenerator.events.GetPetrolLevel;
import main.java.simulation.petrolGenerator.events.IsTurnedOn;
import main.java.simulation.petrolGenerator.events.TurnOff;
import main.java.simulation.petrolGenerator.events.TurnOn;

/**
 * The class <code>PetrolGeneratorElectricity_MILModel</code> defines a MIL model
 * of the electricity consumption of a Washer.
 * <p><string>Description</string></p>
 * <p>
 *     The petrol generator can be switched on/off and it changes the consumption.
 * </p>
 * </p>
 * @author 	Bello Memmi
 */
@ModelExternalEvents(imported = { TurnOff.class, AddPetrol.class, GetPetrolLevel.class, IsTurnedOn.class, TurnOn.class,
		GetMaxLevel.class })
public class PetrolGeneratorElectricity_MILModel extends AtomicHIOA {

	private static final long serialVersionUID = 1L;
	/** energy generated 			 												*/
	protected static final double GENERATING = 5;
	/** tension same for all the house 												*/
	public static final double TENSION = 220;
	/** current intensity production in Amperes; intensity is power/tension. 		*/
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentProduction = new Value<>(this, 0.0, 0);
	/** current petrol level in liter												*/
	protected float currentPetrolLevel = 0;
	/** maximum capacity of the reservoir 											*/
	protected float maximumPetrolLevel = 50; // 50 liters max
	/** true when the electricity consumption of the washer has
	 * changed after executing an external event (when
	 * <code>currentState</code> changes 											*/
	protected boolean consumptionHasChanged = false;
	/** indicates whether the petrol generator is on								*/
	protected boolean isOn = false;

	/**
	 * Create a PetrolGenerator MIL model instance.
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 *     pre 	true //no precondition
	 *     post	true // no postcondition
	 * </pre>
	 * @param uri					URI of the model.
	 * @param simulatedTimeUnit		time unit used for the simulation time.
	 * @param simulationEngine		simulation engine to which the model is attached.
	 * @throws Exception
	 */
	public PetrolGeneratorElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	/**
	 * return the maximum capacity of the reservoir of the component PetrolGenerator.
	 *
	 * <p><strong>Contract</strong></p>
	 * <pre>
	 *     pre 	true 	// no precondition
	 *     post	{@code ret != null}
	 * </pre>
	 * @return	the maximum capacity of the reservoir.
	 */
	public float getMaximumPetrolLevel() {
		return maximumPetrolLevel;
	}

	/**
	 * return the current level of the reservoir of the component PetrolGenerator.
	 *
	 * <p><strong>Contract</strong></p>
	 * <pre>
	 *     pre 	true 	// no precondition
	 *     post	{@code ret != null}
	 * </pre>
	 * @return	the current level of the reservoir.
	 */
	public float getCurrentPetrolLevel() {
		return currentPetrolLevel;
	}

	/**
	 *
	 * <p><strong>Contract</strong></p>
	 * <pre>
	 *     pre 	true 	// no precondition
	 *     post	{@code ret != null}
	 * </pre>
	 * @return	true if the PetrolGenerator is on.
	 */
	public boolean getIsOn() {
		return isOn;
	}

	/**
	 * switch on the petrol generator
	 * <p><strong>Contract</strong></p>
	 * <pre>
	 *     pre 	true 	// no precondition
	 *     post	{@code ret != null}
	 * </pre>
	 * @return
	 */
	public void turnOn() {
		isOn = true;
	}

	/**
	 * switch off the petrol generator
	 * <p><strong>Contract</strong></p>
	 * <pre>
	 *     pre 	true 	// no precondition
	 *     post	{@code ret != null}
	 * </pre>
	 * @return
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
	 * add one liter to the reservoir
	 * <p><strong>Contract</strong></p>
	 * <pre>
	 *     pre 	{@code this.getCurrentPetrolLevel()+1 <= this.getMaximumPetrolLevel()}
	 *     post	{@code ret != null}
	 * </pre>
	 * @return
	 */
	public void addOneLPetrol() {
		this.currentPetrolLevel += 1;
	}
	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void initialiseVariables(Time startTime) {
		this.currentProduction.v = 0.0;
		super.initialiseVariables(startTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#initialiseState()
	 */
	@Override
	public void initialiseState() {
		this.isOn = false;
		this.consumptionHasChanged = false;
		this.currentPetrolLevel = 0;
		super.initialiseState();
	}

	@Override
	public ArrayList<EventI> output() {
		return null;
	}

	@Override
	public Duration timeAdvance() {
		if (this.consumptionHasChanged) {
			this.toggleConsumptionHasChanged();
			return new Duration(0.0, this.getSimulatedTimeUnit());
		} else {
			return Duration.INFINITY;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedInternalTransition(Duration elapsedTime) {
		super.userDefinedInternalTransition(elapsedTime);
		if (this.isOn) {
			this.currentProduction.v = GENERATING / TENSION;
		} else {
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
		assert ce instanceof AbstractPetrolGeneratorEvent;
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
	}
}
