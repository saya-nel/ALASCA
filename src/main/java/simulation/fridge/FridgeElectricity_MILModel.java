package main.java.simulation.fridge;

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
import main.java.simulation.fridge.events.AbstractFridgeEvent;
import main.java.simulation.fridge.events.SetEco;
import main.java.simulation.fridge.events.SetNormal;
import main.java.utils.FridgeMode;

@ModelExternalEvents(imported = { SetEco.class, SetNormal.class })
public class FridgeElectricity_MILModel extends AtomicHIOA {

	private static final long serialVersionUID = 1L;

	public static final double ECO_MODE_CONSUMPTION = 15;
	public static final double NORMAL_MODE_CONSUMPTION = 20;
	public static final double TENSION = 220;

	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<Double>(this, 0.0, 0);

	protected FridgeMode currentMode = FridgeMode.ECO;
	protected boolean consumptionHasChanged = false;
	protected float requestedTemperature = 0;

	public FridgeElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	public void setMode(FridgeMode mode) {
		currentMode = mode;
	}

	public FridgeMode getMode() {
		return currentMode;
	}

	public void toggleConsumptionHasChanged() {
		this.consumptionHasChanged = (this.consumptionHasChanged) ? false : true;
	}

	public void lowerRequestedTemperature(){
		this.requestedTemperature--;
	}
	public void upperRequestedTemperature(){
		this.requestedTemperature++;
	}
	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void initialiseVariables(Time startTime) {
		this.currentIntensity.v = 0.0;
		super.initialiseVariables(startTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#initialiseState()
	 */
	@Override
	public void initialiseState() {
		this.currentMode = FridgeMode.ECO;
		this.consumptionHasChanged = false;
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
		switch (this.currentMode) {
		case ECO:
			this.currentIntensity.v = ECO_MODE_CONSUMPTION / TENSION;
			break;
		case NORMAL:
			this.currentIntensity.v = NORMAL_MODE_CONSUMPTION / TENSION;
		}
		this.currentIntensity.time = this.getCurrentStateTime();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		assert currentEvents != null && currentEvents.size() == 1;
		Event ce = (Event) currentEvents.get(0);
		assert ce instanceof AbstractFridgeEvent;
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
	}

}
