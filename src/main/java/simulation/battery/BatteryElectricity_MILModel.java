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
import main.java.simulation.battery.events.EmptyBattery;
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

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/**
	 * time interval between each reduction of petrol level
	 */
	protected static final double STEP_LENGTH = 1.0;
	protected final Duration standardStep;

	/** energy generated during draining mode */
	public static final double DRAINING_MODE_PRODUCTION = 5;

	/** consumption during Recharging mode */
	public static final double RECHARGING_MODE_CONSUMPTION = 8;

	/** tension same for all the house */
	public static final double TENSION = 220;

	/** current consumption in Amperes; intensity is power/tension. */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<>(this, 0.0, 0);

	/** current production in Amperes in production mode */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentProduction = new Value<>(this, 0.0, 0);

	/** current state of the Battery */
	protected BatteryState currentState = BatteryState.DRAINING;

	protected float currentPowerLevel = 50;
	protected float maximumPowerLevel = 50;

	/**
	 * true when the electricity consumption of the battery has changed after
	 * executing an external event (when <code>currentState</code> changes
	 */
	protected boolean consumptionHasChanged = false;

	protected boolean needToBeRefilled = false;
	protected boolean hasSendEmptyBattery = false;

	/**
	 * Create a battery MIL model instance.
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 *     pre 	true //no precondition
	 *     post	true // no postcondition
	 * </pre>
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
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 *     pre 		state != null
	 *     post 	true			//no post condition
	 * </pre>
	 * 
	 * @param state the new state
	 */
	public void setState(BatteryState state) {
		this.currentState = state;
	}

	/**
	 * return the state of the Battery.
	 *
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 *     pre 	true 	// no precondition
	 *     post	{@code ret != null}
	 * </pre>
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
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
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
		this.currentPowerLevel = this.maximumPowerLevel;
		this.consumptionHasChanged = false;
		this.currentState = BatteryState.DRAINING;
		super.initialiseVariables(startTime);
	}

	@Override
	public void initialiseState() {
		this.consumptionHasChanged = false;
		super.initialiseState();
	}

	@Override
	public ArrayList<EventI> output() {
		if (needToBeRefilled && !hasSendEmptyBattery) {
			ArrayList<EventI> ret = new ArrayList<EventI>();
			ret.add(new EmptyBattery(this.getTimeOfNextEvent()));
			hasSendEmptyBattery = true;
			return ret;
		} else
			return null;
	}

	@Override
	public Duration timeAdvance() {
		if (this.consumptionHasChanged) {
			this.toggleConsumptionHasChanged();
			return new Duration(0.0, this.getSimulatedTimeUnit());
		} else if (this.needToBeRefilled && !hasSendEmptyBattery) {
			return new Duration(0.0, this.getSimulatedTimeUnit());
		}
		// TODO : pourquoi petrol generator bug si j'enleve ça ???
		else if (this.needToBeRefilled && this.hasSendEmptyBattery) {
			return Duration.INFINITY;
		} else {
			return standardStep;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedInternalTransition(Duration elapsedTime) {
		super.userDefinedInternalTransition(elapsedTime);

		// if the battery is draining, it loose power
		if (this.currentState == BatteryState.DRAINING) {
			if (currentPowerLevel > 0) {
				// the battery loose power
				this.currentPowerLevel -= 1;
				this.logger.logMessage("", "current power level : " + this.currentPowerLevel);
				this.currentProduction.v = DRAINING_MODE_PRODUCTION / TENSION;
			} else if (currentPowerLevel <= 0 && !hasSendEmptyBattery) {
				this.currentProduction.v = 0.;
				this.needToBeRefilled = true;
				this.hasSendEmptyBattery = false;
				this.currentState = BatteryState.SLEEPING;
			}
		} else if (this.currentState == BatteryState.SLEEPING) {
			this.currentProduction.v = 0.;
			this.currentIntensity.v = 0.;
		} else if (this.currentState == BatteryState.RECHARGING) {
			this.currentProduction.v = 0.;
			if (this.currentPowerLevel < maximumPowerLevel) {
				this.currentIntensity.v = RECHARGING_MODE_CONSUMPTION / TENSION;
				this.currentProduction.v = 0.;
				this.currentPowerLevel += 1;
			} else {
				this.currentState = BatteryState.SLEEPING;
				this.currentIntensity.v = 0.;
				this.currentProduction.v = 0.;
			}
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
		assert ce instanceof AbstractBatteryEvent;
		this.logger.logMessage("", "BatteryElectricity executing the external event " + ce.getClass().getSimpleName()
				+ "(" + ce.getTimeOfOccurrence().getSimulatedTime() + ")");
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
	}
}
