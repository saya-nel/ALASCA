package main.java.simulation.washer;

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
import main.java.simulation.washer.events.AbstractWasherEvent;
import main.java.simulation.washer.events.SetEco;
import main.java.simulation.washer.events.SetPerformance;
import main.java.simulation.washer.events.SetStd;
import main.java.simulation.washer.events.TurnOff;
import main.java.simulation.washer.events.TurnOn;
import main.java.utils.WasherModes;

/**
 * The class <code>WasherElectricity_MILModel</code> defines a MIL model
 * of the electricity consumption of a Washer.
 * <p><string>Description</string></p>
 * <p>
 *     The washer can change mode, switch on, off and it changes the consumption.
 * </p>
 * </p>
 * @author Bello Memmi
 */
@ModelExternalEvents(imported = { TurnOn.class, TurnOff.class, SetEco.class, SetStd.class, SetPerformance.class })
public class WasherElectricity_MILModel extends AtomicHIOA {

	private static final long serialVersionUID = 1L;
	/** energy generated during eco mode		 						*/
	public static final double ECO_MODE_CONSUMPTION = 20;
	/** energy generated during standard mode 							*/
	public static final double STD_MODE_CONSUMPTION = 22;
	/** energy generated during performance mode 						*/
	public static final double PERFORMANCE_MODE_CONSUMPTION = 23;
	/** tension same for all the house 									*/
	public static final double TENSION = 220;
	/** current intensity in Amperes; intensity is power/tension. 		*/
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<Double>(this, 0.0, 0);
	/** indicates whether the washer is on or not						*/
	protected boolean isOn = false;
	/** current state of the washer										*/
	protected WasherModes currentMode = WasherModes.ECO;
	/** true when the electricity consumption of the washer has
	 * changed after executing an external event (when
	 * <code>currentState</code> changes 								*/
	protected boolean consumptionHasChanged = false;

	/**
	 * Create a Washer MIL model instance.
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
	public WasherElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	/**
	 * set the state of the Battery
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 *     pre 		state != null
	 *     post 	true			//no post condition
	 * </pre>
	 * @param mode 	the new state
	 */
	public void setMode(WasherModes mode) {
		currentMode = mode;
	}

	/**
	 * return the mode of the Washer.
	 *
	 * <p><strong>Contract</strong></p>
	 * <pre>
	 *     pre 	true 	// no precondition
	 *     post	{@code ret != null}
	 * </pre>
	 * @return	the state of the Washer.
	 */
	public WasherModes getMode() {
		return currentMode;
	}

	/**
	 *
	 * <p><strong>Contract</strong></p>
	 * <pre>
	 *     pre 	true 	// no precondition
	 *     post	{@code ret != null}
	 * </pre>
	 * @return	true if the washer is on.
	 */
	public boolean isOn() {
		return isOn;
	}

	/**
	 * switch on the washer if it is off or switch off if it is on
	 * <p><strong>Contract</strong></p>
	 * <pre>
	 *     pre 	true 	// no precondition
	 *     post	{@code ret != null}
	 * </pre>
	 * @return
	 */
	public void toggleIsOn() {
		this.isOn = (this.isOn) ? false : true;
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
		super.initialiseVariables(startTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#initialiseState()
	 */
	@Override
	public void initialiseState() {
		this.isOn = false;
		this.currentMode = WasherModes.ECO;
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
			switch (this.currentMode) {
			case ECO:
				this.currentIntensity.v = ECO_MODE_CONSUMPTION / TENSION;
				break;
			case STD:
				this.currentIntensity.v = STD_MODE_CONSUMPTION / TENSION;
			case PERFORMANCE:
				this.currentIntensity.v = PERFORMANCE_MODE_CONSUMPTION / TENSION;
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
		assert ce instanceof AbstractWasherEvent;
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
	}

}
