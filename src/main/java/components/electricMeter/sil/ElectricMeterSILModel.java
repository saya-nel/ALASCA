package main.java.components.electricMeter.sil;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ImportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

// -----------------------------------------------------------------------------
//@ModelExternalEvents(imported = {ConsumptionLevelRequest.class},
//					 exported = {ConsumptionLevel.class})
// -----------------------------------------------------------------------------
public class ElectricMeterSILModel extends AtomicHIOA {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	/**
	 * URI for an instance model; works as long as only one instance is created.
	 */
	public static final String URI = ElectricMeterSILModel.class.getSimpleName();

	/** URI of the variable pointing to the electric meter component. */
	public static final String ELECTRIC_METER_REFERENCE_NAME = URI + ":EMRN";
	/** owner component. */
	protected ComponentI owner;

	/**
	 * time interval between electricity consumption levels are computed from the
	 * consumption of all connected and switched on appliances, expressed in the
	 * simulation time unit of the model.
	 */
	protected static final double STEP_LENGTH = 1.;
	/**
	 * standard interval between electricity consumption level computations as
	 * simulation duration, including the time unit.
	 */
	protected final Duration standardStep;

	/** current intensity in amperes; intensity is power/tension. */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<Double>(this, 0.0, 0);
	/**
	 * current intensity of the fan in amperes; intensity is power/tension.
	 */
	@ImportedVariable(type = Double.class)
	protected Value<Double> FanIntensity;

	/**
	 * time interval until the next global electricity consumption computation.
	 */
	protected Duration nextStep;
	/**
	 * true if a request for the current value of the global electricity consumption
	 * (through a <code>ConsumptionLevelRequest</code> event, false otherwise.
	 */
	protected boolean requestReceived = false;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a new panel model instance.
	 *
	 * @param uri               URI of the model.
	 * @param simulatedTimeUnit time unit used for the simulation time.
	 * @param simulationEngine  simulation engine to which the model is attached.
	 * @throws Exception <i>to do.</i>
	 */
	public ElectricMeterSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.standardStep = new Duration(STEP_LENGTH, simulatedTimeUnit);
	}

	// -------------------------------------------------------------------------
	// Instance methods
	// -------------------------------------------------------------------------

	/**
	 * return the current intensity computed by the model.
	 *
	 * @return the current intensity computed by the model.
	 */
	public double getIntensity() {
		return this.currentIntensity.v;
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void initialiseVariables(Time startTime) {
		// initial intensity, before the first computation
		this.currentIntensity.v = 0.0;
		super.initialiseVariables(startTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void initialiseState(Time initialTime) {
		// initially, no request has been received yet.
		this.requestReceived = false;
		// the first time interval until the first computation.
		this.nextStep = this.standardStep;

		this.toggleDebugMode();
		this.logMessage("simulation begins.\n");

		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.owner = (ComponentI) simParams.get(ELECTRIC_METER_REFERENCE_NAME);
		this.setLogger(new StandardComponentLogger(this.owner));
		super.setSimulationRunParameters(simParams);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI> output() {
		if (this.requestReceived) {
			// when a request has been received, output a ConsumptionLevel
			// event with the current global electricity consumption.
			ArrayList<EventI> ret = new ArrayList<EventI>();
//			ret.add(new ConsumptionLevel(this.getTimeOfNextEvent(),
//										 this.currentIntensity.v));
			return ret;
		} else {
			// otherwise, no output event.
			return null;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration timeAdvance() {
		if (this.requestReceived) {
			// trigger an immediate internal transition.
			return Duration.zero(this.getSimulatedTimeUnit());
		} else {
			// wait until the next planned computation.
			return this.nextStep;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedInternalTransition(Duration elapsedTime) {
		super.userDefinedInternalTransition(elapsedTime);

		if (!this.requestReceived) {
			// no request received, hence this is a computation step
			// compute the new global electricity consumption
			this.currentIntensity.v = this.FanIntensity.v;
			this.currentIntensity.time = this.getCurrentStateTime();

			StringBuffer message = new StringBuffer("total consumption = ");
			message.append(this.currentIntensity.v);
			message.append(" at ");
			message.append(this.currentIntensity.time);
			message.append(".\n");
			this.logMessage(message.toString());

			// the next planned computation
			this.nextStep = this.standardStep;
		} else {
			// a request has been received before the next computation
			assert elapsedTime.lessThanOrEqual(this.standardStep);
			// the event has already been output, simply replan the next
			// computation at its previously planned time.
			this.requestReceived = false;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		// get the vector of current external events
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		// when this method is called, there is at least one external event,
		// and for the panel model, there will be exactly one by
		// construction which is a consumption level request.
		assert currentEvents != null && currentEvents.size() == 1;

		// The received event can only be a ConsumptionLevelRequest, as this
		// is the only imported event by this model.
//		assert	currentEvents.get(0) instanceof ConsumptionLevelRequest;
//
//		ConsumptionLevelRequest ce =
//							(ConsumptionLevelRequest)currentEvents.get(0);
//		System.out.println("Panel receiving the external event " +
//							ce.getClass().getSimpleName() + "(" +
//							ce.getTimeOfOccurrence().getSimulatedTime() + ")");

		// this will trigger an immediate internal transition and the output
		// of the ConsumptionLevel event.
		this.requestReceived = true;
		this.nextStep = this.standardStep.subtract(elapsedTime);

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
// -----------------------------------------------------------------------------
