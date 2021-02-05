package main.java.components.fridge.sil;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.components.fridge.sil.events.Activate;
import main.java.components.fridge.sil.events.Passivate;
import main.java.components.fridge.sil.events.SetEco;
import main.java.components.fridge.sil.events.SetNormal;
import main.java.components.fridge.sil.events.SetRequestedTemperature;
import main.java.components.fridge.utils.FridgeMode;

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
@ModelExternalEvents(imported = { SetEco.class, SetNormal.class, SetRequestedTemperature.class, Activate.class,
		Passivate.class })
public class FridgeElectricalSILModel extends AtomicHIOA {

	public static final String URI = FridgeElectricalSILModel.class.getSimpleName();
	/** integration step for the differential equation(assumed in seconds). */
	protected static final double STEP = 1.0;
	/** owner component */
	protected ComponentI owner;
	private static final long serialVersionUID = 1L;
	/** energy generated during eco mode */
	public static final double ECO_MODE_CONSUMPTION = 40;
	/** energy generated during normal mode */
	public static final double NORMAL_MODE_CONSUMPTION = 80;
	/** tension same for all the house */
	public static final double TENSION = 15;

	/** current mode of the fridge */
	protected FridgeMode currentMode = FridgeMode.NORMAL;
	/**
	 * true when the electricity consumption of the washer has changed after
	 * executing an external event (when <code>currentState</code> changes
	 */
	// protected boolean consumptionHasChanged = false;

	/** true if the fridge is currently suspended */
	protected boolean isSuspended = false;

	/** target temperature */
	protected double targetTemperature;

	protected EventI lastReceivedEvent;
	// -------------------------------------------------------------------------
	// HIOA model variables
	// -------------------------------------------------------------------------
	/** current intensity in Amperes; intensity is power/tension. */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<Double>(this, 0.0, 0);

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
	public FridgeElectricalSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);

	}

	public void setTargetTemperature(double temperature) {
		this.targetTemperature = temperature;
	}

	public double getTargetTemperature() {
		return this.targetTemperature;
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
	 * the fridge switch to passive.
	 *
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 */
	public void suspend() {
		this.isSuspended = true;
		this.updateIntensity();
	}

	/**
	 * the fridge switch to active.
	 *
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 */
	public void resume() {
		this.isSuspended = false;
		this.updateIntensity();
	}

	/**
	 * update the intensity of electric concsumption of the fridge given its current
	 * mode.
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
	protected void updateIntensity() {
		if (this.isSuspended) {
			this.currentIntensity.v = 0.0;
		} else {
			if (this.currentMode == FridgeMode.NORMAL) {
				this.currentIntensity.v = NORMAL_MODE_CONSUMPTION / TENSION;
			} else {
				this.currentIntensity.v = ECO_MODE_CONSUMPTION / TENSION;
			}
		}
		this.currentIntensity.time = this.getCurrentStateTime();
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void initialiseState(Time initialTime) {
		this.lastReceivedEvent = null;
		this.isSuspended = false;
		this.targetTemperature = 0;
		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void initialiseVariables(Time startTime) {
		updateIntensity();
		super.initialiseVariables(startTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.owner = (ComponentI) simParams.get(FridgeTemperatureSILModel.FRIDGE_REFERENCE_NAME);
		super.setSimulationRunParameters(simParams);
	}

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
		if (this.lastReceivedEvent != null) {
			return Duration.zero(this.getSimulatedTimeUnit());
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
		updateIntensity();
		this.lastReceivedEvent = null;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		// get the vector of current external events
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		// when this method is called, there is at least one external event,
		// and for the fridge model, there will be exactly one by
		// construction.
		assert currentEvents != null && currentEvents.size() == 1;

		this.lastReceivedEvent = currentEvents.get(0);
		StringBuffer message = new StringBuffer("executing the external event ");
		message.append(this.lastReceivedEvent.getClass().getSimpleName());
		message.append("(");
		message.append(this.lastReceivedEvent.getTimeOfOccurrence().getSimulatedTime());
		message.append(")\n");
		this.logger.logMessage("", message.toString());
		// events have a method execute on to perform their effect on this
		// model
		this.lastReceivedEvent.executeOn(this);
		updateIntensity();
		super.userDefinedExternalTransition(elapsedTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void endSimulation(Time endTime) throws Exception {
		this.logger.logMessage("", "simulation ends.\n");
		super.endSimulation(endTime);
	}
}
