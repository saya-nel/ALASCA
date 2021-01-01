package main.java.simulation.battery;

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
import main.java.simulation.battery.events.AbstractBatteryEvent;
import main.java.simulation.battery.events.SetDraining;
import main.java.simulation.battery.events.SetRecharging;
import main.java.simulation.battery.events.SetSleeping;
import main.java.utils.BatteryState;

@ModelExternalEvents(imported = { SetDraining.class, SetRecharging.class, SetSleeping.class })
public class BatteryElectricity_MILModel extends AtomicHIOA {

	private static final long serialVersionUID = 1L;

	public static final double DRAINING_MODE_CONSUMPTION = 50;
	//pas sur négatif
	public static final double RECHARGING_MODE_PRODUCTION = -50;

	public static final double SLEEPING_MODE_PRODUCTION = 0;
	public static final double TENSION = 220;

	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<>(this, 0.0, 0);

	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentProduction = new Value<>(this, 0.0, 0);

	protected BatteryState currentState = BatteryState.SLEEPING;
	protected boolean consumptionHasChanged = false;

	public BatteryElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	public void setState(BatteryState state) {
		this.currentState = state;
	}

	public BatteryState getState() {
		return this.currentState;
	}

	public void toggleConsumptionHasChanged() {
		this.consumptionHasChanged = (this.consumptionHasChanged) ? false : true;
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
		this.currentProduction.v = 0.0;
		super.initialiseVariables(startTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#initialiseState()
	 */
	@Override
	public void initialiseState() {
		this.currentState = BatteryState.SLEEPING;
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
		switch (this.currentState) {
		case SLEEPING:
			this.currentIntensity.v = 0.;
			break;
		case DRAINING:
			this.currentIntensity.v = RECHARGING_MODE_PRODUCTION / TENSION;
			break;
		case RECHARGING:
			this.currentIntensity.v = DRAINING_MODE_CONSUMPTION / TENSION;
			break;
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
		assert ce instanceof AbstractBatteryEvent;
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
	}
}
