package main.java.simulation.fan;

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
import main.java.simulation.fan.events.AbstractFanEvent;
import main.java.simulation.fan.events.SetHigh;
import main.java.simulation.fan.events.SetLow;
import main.java.simulation.fan.events.SetMid;
import main.java.simulation.fan.events.TurnOff;
import main.java.simulation.fan.events.TurnOn;
import main.java.simulation.utils.FileLogger;
import main.java.utils.FanLevel;

/**
 * The class <code>FanElectricity_MILModel</code> defines a MIL model of the
 * electricity consumption of a Fan.
 * <p>
 * <string>Description</string>
 * </p>
 * <p>
 * The fan can change mode be switched on or off and it changes the consumption.
 * </p>
 * </p>
 * 
 * @author Bello Memmi
 */
@ModelExternalEvents(imported = { TurnOn.class, TurnOff.class, SetLow.class, SetMid.class, SetHigh.class })
public class FanElectricity_MILModel extends AtomicHIOA {

	private static final long serialVersionUID = 1L;
	/** energy generated during low mode */
	public static final double LOW_MODE_CONSUMPTION = 4;
	/** energy generated during medium mode */
	public static final double MID_MODE_CONSUMPTION = 5;
	/** energy generated during high mode */
	public static final double HIGH_MODE_CONSUMPTION = 6;
	/** tension same for all the house */
	public static final double TENSION = 220;
	/** current intensity in Amperes; intensity is power/tension. */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<Double>(this, 0.0, 0);
	/** indicating whether the fan is on */
	protected boolean isOn = false;
	/** current level of the Fan */
	protected FanLevel currentLevel = FanLevel.LOW;
	/**
	 * true when the electricity consumption of the battery has changed after
	 * executing an external event (when <code>currentState</code> changes
	 */
	protected boolean consumptionHasChanged = false;

	/**
	 * Create a Fan MIL model instance.
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
	public FanElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.setLogger(new FileLogger("fanElectricity.log"));
	}

	/**
	 * set the level of the Fan
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 *     pre 		state != null
	 *     post 	true			//no post condition
	 * </pre>
	 * 
	 * @param level the new level
	 */
	public void setLevel(FanLevel level) {
		currentLevel = level;
	}

	/**
	 * return the state of the Fan.
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
	 * @return the level of the Fan.
	 */
	public FanLevel getLevel() {
		return currentLevel;
	}

	/**
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
	 * @return true if the Fan is on.
	 */
	public boolean isOn() {
		return isOn;
	}

	/**
	 * switch on the fan if it is off or switch off if it is on
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 *     pre 	true 	// no precondition
	 *     post	{@code ret != null}
	 * </pre>
	 * 
	 * @return
	 */
	public void toggleIsOn() {
		this.isOn = (this.isOn) ? false : true;
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
		super.initialiseVariables(startTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#initialiseState()
	 */
	@Override
	public void initialiseState() {
		this.isOn = false;
		this.currentLevel = FanLevel.LOW;
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
		if (this.isOn) {
			switch (this.currentLevel) {
			case LOW:
				this.currentIntensity.v = LOW_MODE_CONSUMPTION / TENSION;
				break;
			case MID:
				this.currentIntensity.v = MID_MODE_CONSUMPTION / TENSION;
				break;
			case HIGH:
				this.currentIntensity.v = HIGH_MODE_CONSUMPTION / TENSION;
			}
		} else {
			this.currentIntensity.v = 0.;
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
		this.logger.logMessage("", "Fan executing the external event " + ce.getClass().getSimpleName() + "("
				+ ce.getTimeOfOccurrence().getSimulatedTime() + ")");
		assert ce instanceof AbstractFanEvent;
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
	}

}
