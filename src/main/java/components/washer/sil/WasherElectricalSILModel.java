package main.java.components.washer.sil;

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
import main.java.components.washer.sil.events.AbstractWasherEvent;
import main.java.components.washer.sil.events.SetEco;
import main.java.components.washer.sil.events.SetPerformance;
import main.java.components.washer.sil.events.SetStd;
import main.java.components.washer.sil.events.TurnOff;
import main.java.components.washer.sil.events.TurnOn;
import main.java.utils.FileLogger;
import main.java.utils.WasherModes;

@ModelExternalEvents(imported = { SetEco.class, SetPerformance.class, SetStd.class, TurnOn.class, TurnOff.class })
public class WasherElectricalSILModel extends AtomicHIOA {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	/**
	 * URI for an instance model; works as long as only one instance is created.
	 */
	public static final String URI = WasherElectricalSILModel.class.getSimpleName();

	/** energy generated during eco mode */
	public static final double ECO_MODE_CONSUMPTION = 20;
	/** energy generated during standard mode */
	public static final double STD_MODE_CONSUMPTION = 22;
	/** energy generated during performance mode */
	public static final double PERFORMANCE_MODE_CONSUMPTION = 23;
	/** tension same for all the house */
	public static final double TENSION = 15;
	/** current intensity in Amperes; intensity is power/tension. */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<Double>(this, 0.0, 0);
	/** indicates whether the washer is on or not */
	protected boolean isOn;
	/** current state of the washer */
	protected WasherModes currentMode = WasherModes.ECO;
	/**
	 * true when the electricity consumption of the washer has changed after
	 * executing an external event (when <code>currentState</code> changes
	 */
	protected boolean consumptionHasChanged = false;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a fan SIL model instance.
	 *
	 * @param uri               URI of the model.
	 * @param simulatedTimeUnit time unit used for the simulation time.
	 * @param simulationEngine  simulation engine to which the model is attached.
	 * @throws Exception <i>to do</i>.
	 */
	public WasherElectricalSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.setLogger(new FileLogger("washerElectrical.log"));
	}

	/**
	 * set the state of the Battery
	 * 
	 * @param mode the new state
	 */
	public void setMode(WasherModes mode) {
		currentMode = mode;
	}

	/**
	 * return the mode of the Washer.
	 * 
	 * @return the state of the Washer.
	 */
	public WasherModes getMode() {
		return currentMode;
	}

	/**
	 * 
	 * @return true if the washer is on.
	 */
	public boolean isOn() {
		return isOn;
	}

	/**
	 * switch on the washer if it is off or switch off if it is on
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
		this.isOn = false;
		this.currentMode = WasherModes.ECO;
		this.consumptionHasChanged = false;
		super.initialiseVariables(startTime);
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

		if (isOn) {
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
		this.logger.logMessage("",
				this.getCurrentStateTime() + " Washer executing the external event " + ce.eventAsString());
		assert ce instanceof AbstractWasherEvent;
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
