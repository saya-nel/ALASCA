package main.java.components.battery.sil;

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
import main.java.components.battery.sil.events.AbstractBatteryEvent;
import main.java.components.battery.sil.events.SetDraining;
import main.java.components.battery.sil.events.SetRecharging;
import main.java.components.battery.sil.events.SetSleeping;
import main.java.components.battery.utils.BatteryState;
import main.java.utils.FileLogger;

@ModelExternalEvents(imported = { SetDraining.class, SetRecharging.class, SetSleeping.class })
public class BatteryElectricalSILModel extends AtomicHIOA {

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/**
	 * URI for an instance model; works as long as only one instance is created.
	 */
	public static final String URI = BatteryElectricalSILModel.class.getSimpleName();

	/** power energy generated during draining mode */
	public static final double DRAINING_MODE_PRODUCTION = 5000; // watts

	/** power consumption during Recharging mode */
	public static final double RECHARGING_MODE_CONSUMPTION = 5000; // watts

	/** tension */
	public static final double TENSION = 48; // volts

	/** current consumption in Amperes; intensity is power/tension. */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<>(this, 0.0, 0);

	/** current production in Amperes in production mode */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentProduction = new Value<>(this, 0.0, 0);

	/** current state of the Battery */
	protected BatteryState currentState = BatteryState.SLEEPING;

	/**
	 * true when the electricity consumption of the battery has changed after
	 * executing an external event (when <code>currentState</code> changes)
	 */
	protected boolean consumptionHasChanged = false;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public BatteryElectricalSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.setLogger(new FileLogger("batteryElectrical.log"));
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * set the state of the Battery
	 * 
	 * @param state the new state
	 */
	public void setState(BatteryState state) {
		this.currentState = state;
	}

	/**
	 * return the state of the Battery.
	 * 
	 * @return the state of the Battery.
	 */
	public BatteryState getState() {
		return this.currentState;
	}

	/**
	 * toggle the value of the state of the model telling whether the electricity
	 * consumption level has just changed or not; when it changes after receiving an
	 * external event, an immediate internal transition is triggered to update the
	 * level of electricity consumption.
	 *
	 */
	public void toggleConsumptionHasChanged() {
		this.consumptionHasChanged = (this.consumptionHasChanged) ? false : true;
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
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
		if (consumptionHasChanged) {
			this.toggleConsumptionHasChanged();
			return new Duration(0.0, this.getSimulatedTimeUnit());
		} else
			return Duration.INFINITY;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedInternalTransition(Duration elapsedTime) {
		super.userDefinedInternalTransition(elapsedTime);

		if (this.currentState == BatteryState.DRAINING) {
			this.currentProduction.v = DRAINING_MODE_PRODUCTION / TENSION;
			this.currentIntensity.v = 0.;
		} else if (this.currentState == BatteryState.SLEEPING) {
			this.currentProduction.v = 0.;
			this.currentIntensity.v = 0.;
		} else {
			this.currentProduction.v = 0.;
			this.currentIntensity.v = RECHARGING_MODE_CONSUMPTION / TENSION;

		}
		this.currentIntensity.time = this.getCurrentStateTime();
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
		this.logger.logMessage("",
				this.getCurrentStateTime() + "BatteryElectricity executing the external event " + ce.eventAsString());
		assert ce instanceof AbstractBatteryEvent;
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
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
