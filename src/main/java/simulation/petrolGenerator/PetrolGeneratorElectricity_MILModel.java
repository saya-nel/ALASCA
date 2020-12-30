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

@ModelExternalEvents(imported = { TurnOff.class, AddPetrol.class, GetPetrolLevel.class, IsTurnedOn.class, TurnOn.class,
		GetMaxLevel.class })
public class PetrolGeneratorElectricity_MILModel extends AtomicHIOA {

	private static final long serialVersionUID = 1L;

	public static final double DRAINING_MODE_PRODUCTION = 50;

	public static final double TENSION = 220;

	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentProduction = new Value<>(this, 0.0, 0);

	// TODO : modeliser la consommation en petrol

	protected float currentPetrolLevel = 0;

	protected float maximumPetrolLevel = 50; // 50 liters max

	protected boolean consumptionHasChanged = false;

	protected boolean isOn = false;

	public PetrolGeneratorElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	public float getMaximumPetrolLevel() {
		return maximumPetrolLevel;
	}

	public float getCurrentPetrolLevel() {
		return currentPetrolLevel;
	}

	public boolean getIsOn() {
		return isOn;
	}

	public void turnOn() {
		isOn = true;
	}

	public void turnOff() {
		isOn = false;
	}

	public void toggleConsumptionHasChanged() {
		this.consumptionHasChanged = (this.consumptionHasChanged) ? false : true;
	}

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
			this.currentProduction.v = DRAINING_MODE_PRODUCTION / TENSION;
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
