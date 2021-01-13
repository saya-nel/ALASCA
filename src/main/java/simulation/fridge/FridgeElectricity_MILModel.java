package main.java.simulation.fridge;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.InternalVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE;
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
import main.java.simulation.utils.FileLogger;
import main.java.utils.FridgeMode;

/**
 * The class <code>FridgeElectricity_MILModel</code> defines a MIL model of the
 * electricity consumption of a Fridge.
 * <p>
 * <string>Description</string>
 * </p>
 * <p>
 * The fridge can change mode and it changes the consumption.
 * </p>
 * </p>
 * 
 * @author Bello Memmi
 */
@ModelExternalEvents(imported = { SetEco.class, SetNormal.class })
public class FridgeElectricity_MILModel extends AtomicHIOAwithDE {

	// TODO : gérer consommation suspensible

	/** integration step for the differential equation(assumed in seconds).	*/
	protected static final double	STEP = 1.0;

	private static final long serialVersionUID = 1L;
	/** energy generated during eco mode */
	public static final double ECO_MODE_CONSUMPTION = 15;
	/** energy generated during normal mode */
	public static final double NORMAL_MODE_CONSUMPTION = 20;
	/** tension same for all the house */
	public static final double TENSION = 220;
	/** current intensity in Amperes; intensity is power/tension. */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<Double>(this, 0.0, 0);


	/** current mode of the fridge */
	protected FridgeMode currentMode = FridgeMode.ECO;
	/**
	 * true when the electricity consumption of the washer has changed after
	 * executing an external event (when <code>currentState</code> changes
	 */
	protected boolean consumptionHasChanged = false;

	/**
	 * true if the fridge is currently suspended
	 */
	protected boolean isSuspended = false;

	/**
	 * beginning's time of the suspension
	 */
	protected Time beginSuspension = null;

	// -------------------------------------------------------------------------
	// HIOA model variables
	// -------------------------------------------------------------------------

	protected final Duration 	integrationStep;

	/**
	 * temps of fridge variable ?
	 */
	@InternalVariable(type = Double.class)
	protected final Value<Double> currentTemp = new Value<Double>(this, 20.0, 0);

	/** the current evaporator refregirant liquid temperature. 					*/
	@InternalVariable(type = Double.class)
	protected final Value<Double>  	evaporatorTemp = new Value<Double>(this, -20.);

	protected final double 			STANDARD_FREEZE_TEMP = -20;
	protected final double 			ECO_FREEZE_TEMP		= -10;

	protected  double 				currentTempDerivative = 0.0;
	/** requested temperature */
	protected double 				requestedTemperature = 0;
	/** the tolerance on the target water temperature to get a control with hysteresis */
	protected double 				targetTolerance = 3.0;

	protected double EXTERNAL_TEMPERATURE = 25;

	protected double FREEZE_TRANSFER_CONSTANT = 1000;


	/**
	 * Create a Fridge MIL model instance.
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
	public FridgeElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.integrationStep = new Duration(STEP, simulatedTimeUnit);
		this.setLogger(new FileLogger("fridgeElectricity.log"));
	}



	/**
	 * set the mode of the Fridge
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 *     pre 		state != null
	 *     post 	true			//no post condition
	 * </pre>
	 * 
	 * @param mode the new mode
	 */
	public void setMode(FridgeMode mode) {
		currentMode = mode;
	}

	/**
	 * return the mode of the fridge.
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
	 * @return the state of the Fridge.
	 */
	public FridgeMode getMode() {
		return currentMode;
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

	/**
	 * lower 1 degre the requested temperature
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 *     pre 	true 	// no precondition
	 *     post	true 	// no postcondition
	 * </pre>
	 * 
	 * @return
	 */
	public void lowerRequestedTemperature() {
		this.requestedTemperature--;
	}

	/**
	 * upper 1 degre the requested temperature
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 *     pre 	true 	// no precondition
	 *     post	true 	// no postcondition
	 * </pre>
	 * 
	 * @return
	 */
	public void upperRequestedTemperature() {
		this.requestedTemperature++;
	}

	public void suspend() {
		this.isSuspended = true;
		this.beginSuspension = this.getCurrentStateTime();
	}

	public void resume() {
		this.isSuspended = false;
		this.beginSuspension = null;
	}
	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------


	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE#initialiseDerivatives()
	 */
	@Override
	protected void initialiseDerivatives() {
		this.computeDerivatives();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE#computeDerivatives()
	 */
	@Override
	public void computeDerivatives()
	{
		this.currentTempDerivative = 0.0;
		if (!this.isSuspended){
			if(this.currentMode == FridgeMode.NORMAL)
				this.currentTempDerivative =
						(STANDARD_FREEZE_TEMP - this.evaporatorTemp.v)/
								FREEZE_TRANSFER_CONSTANT;
			else
				this.currentTempDerivative =
						(ECO_FREEZE_TEMP - this.evaporatorTemp.v)/
								FREEZE_TRANSFER_CONSTANT;
		}
		else {
			this.currentTempDerivative =
					(EXTERNAL_TEMPERATURE - this.evaporatorTemp.v)/
							FREEZE_TRANSFER_CONSTANT;
		}
		System.out.println("current Temperature Der "+this.currentTempDerivative);
		System.out.println("target temperature: "+this.requestedTemperature);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void initialiseVariables(Time startTime) {
		this.currentMode = FridgeMode.ECO;
		this.consumptionHasChanged = false;
		this.isSuspended = false;

		super.initialiseVariables(startTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#initialiseState()
	 */
	@Override
	public void initialiseState() {
		super.initialiseState();
	}

	@Override
	public ArrayList<EventI> output() {
		return null;
	}

	@Override
	public Duration timeAdvance() {
		return this.integrationStep;
//		if (this.consumptionHasChanged && this.isSuspended){
//			this.toggleConsumptionHasChanged();
//			return this.integrationStep;
//		}
//		if (this.consumptionHasChanged) {
//			this.toggleConsumptionHasChanged();
//			return new Duration(0.0, this.getSimulatedTimeUnit());
//		} else {
//			return Duration.INFINITY;
//		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedInternalTransition(Duration elapsedTime) {
		super.userDefinedInternalTransition(elapsedTime);
		this.currentTemp.v = this.currentTemp.v - this.currentTempDerivative*STEP;
		this.currentTemp.time = this.getCurrentStateTime();
		// compute consumption
		switch (this.currentMode) {
		case ECO:
			this.currentIntensity.v = ECO_MODE_CONSUMPTION / TENSION;
			break;
		case NORMAL:
			this.currentIntensity.v = NORMAL_MODE_CONSUMPTION / TENSION;
			break;
		}
		// if current temp > requested temp + target tolerance switch on the fridge
		if(this.currentTemp.v > this.requestedTemperature + this.targetTolerance)
			this.isSuspended = false;
		// switch off the fridge otherwise
		else
			this.isSuspended = true;
		// change consumption if the fridge is suspended
		if(this.isSuspended) {
			this.currentIntensity.v = 0.;
			this.consumptionHasChanged=true;
		}
		this.logger.logMessage("", this.getCurrentStateTime()+" current temperature of the fridge "+this.currentTemp.v);
		//System.out.println("current temperature of the fridge "+this.currentTemp.v);
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
		this.logger.logMessage("", this.getCurrentStateTime() + " Fridge executing the external event "
				+ ce.getClass().getSimpleName() + "(" + ce.getTimeOfOccurrence().getSimulatedTime() + ")");
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
	}

}
