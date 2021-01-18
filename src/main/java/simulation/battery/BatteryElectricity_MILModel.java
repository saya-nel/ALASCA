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
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.simulation.battery.events.AbstractBatteryEvent;
import main.java.simulation.battery.events.SetDraining;
import main.java.simulation.battery.events.SetRecharging;
import main.java.simulation.battery.events.SetSleeping;
import main.java.simulation.utils.FileLogger;
import main.java.utils.BatteryState;

/**
 * The class <code>BatteryElectricity_MILModel</code> defines a MIL model of the
 * electricity consumption of a Battery.
 * <p>
 * <string>Description</string>
 * </p>
 * <p>
 * The battery can change mode and it changes the consumption.
 * </p>
 * </p>
 * 
 * @author Bello Memmi
 */
@ModelExternalEvents(imported = { SetDraining.class, SetRecharging.class, SetSleeping.class })
public class BatteryElectricity_MILModel extends AtomicHIOA {

	// TODO : ajouté la gestion de la recharge planifiable pour la batterie

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/**
	 * time interval between each reduction of petrol level
	 */
	protected static final double STEP_LENGTH = 1.0;
	protected final Duration standardStep;

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
	protected BatteryState currentState = BatteryState.DRAINING;

	// ah
	protected double currentPowerLevel = 189; // ah
	protected double maximumPowerLevel = 189; // ah

	/**
	 * true when the electricity consumption of the battery has changed after
	 * executing an external event (when <code>currentState</code> changes)
	 */
	protected boolean consumptionHasChanged = false;

	/**
	 * Create a battery MIL model instance.
	 * 
	 * @param uri               URI of the model.
	 * @param simulatedTimeUnit time unit used for the simulation time.
	 * @param simulationEngine  simulation engine to which the model is attached.
	 * @throws Exception
	 */
	public BatteryElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.standardStep = new Duration(STEP_LENGTH, simulatedTimeUnit);
		this.setLogger(new FileLogger("batteryElectricity.log"));
	}

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

	@Override
	public ArrayList<EventI> output() {
		return null;
	}

	@Override
	public Duration timeAdvance() {
		if (this.consumptionHasChanged) {
			this.toggleConsumptionHasChanged();
			return new Duration(0.0, this.getSimulatedTimeUnit());
		} else if (this.currentState == BatteryState.DRAINING || this.currentState == BatteryState.RECHARGING) {
			return standardStep;
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

		// no new program event otherwise
		// if the battery is draining, it loose power
		if (this.currentState == BatteryState.DRAINING) {
			if (currentPowerLevel - (DRAINING_MODE_PRODUCTION / 3600) > 0) {
				// the battery loose power
				this.currentProduction.v = DRAINING_MODE_PRODUCTION / TENSION;
				this.currentIntensity.v = 0.;
				// power loose for 1 second = consumption (amp) / 3600
				this.currentPowerLevel -= currentProduction.v / 3600;
			} else {
				this.currentState = BatteryState.SLEEPING;
				this.currentPowerLevel = 0;
				this.toggleConsumptionHasChanged();
			}
		} else if (this.currentState == BatteryState.SLEEPING) {
			this.currentProduction.v = 0.;
			this.currentIntensity.v = 0.;
		} else if (this.currentState == BatteryState.RECHARGING) {
			this.currentProduction.v = 0.;
			if (this.currentPowerLevel + (RECHARGING_MODE_CONSUMPTION / 3600) < maximumPowerLevel) {
				this.currentIntensity.v = RECHARGING_MODE_CONSUMPTION / TENSION;
				// power gain for 1 second = production (amp) / 3600
				this.currentPowerLevel += this.currentIntensity.v / 3600;
			} else {
				this.currentState = BatteryState.SLEEPING;
				this.currentIntensity.v = 0.;
				this.currentProduction.v = 0.;
				this.currentPowerLevel = maximumPowerLevel;
				toggleConsumptionHasChanged();
			}
		}
		this.logger.logMessage("", this.getCurrentStateTime() + " : current power level : " + this.currentPowerLevel);

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
}
