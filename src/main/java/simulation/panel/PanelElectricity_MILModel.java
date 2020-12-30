package main.java.simulation.panel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ImportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.simulation.panel.events.ConsumptionLevel;
import main.java.simulation.panel.events.ConsumptionLevelRequest;

@ModelExternalEvents(imported = { ConsumptionLevelRequest.class }, exported = { ConsumptionLevel.class })
public class PanelElectricity_MILModel extends AtomicHIOA {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	protected static final double STEP_LENGTH = 1.0;

	protected final Duration standardStep;

	// TODO : pour le moment que la consommation, modéliser la production ensuite

	/** current intensity in amperes; intensity is power/tension. */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<Double>(this, 0.0, 0);

	@ImportedVariable(type = Double.class)
	protected Value<Double> fanIntensity;

	@ImportedVariable(type = Double.class)
	protected Value<Double> fridgeIntensity;

	@ImportedVariable(type = Double.class)
	protected Value<Double> batteryIntensity;

	@ImportedVariable(type = Double.class)
	protected Value<Double> washerIntensity;

	/**
	 * time interval until the next global electricity consumption computation.
	 */
	protected Duration nextStep;
	/**
	 * true if a request for the current value of the global electricity consumption
	 * (through a ConsumptionLevelRequest event), false otherwise.
	 */
	protected boolean requestReceived = false;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public PanelElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.standardStep = new Duration(STEP_LENGTH, simulatedTimeUnit);
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
		super.initialiseState(initialTime);
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
			ret.add(new ConsumptionLevel(this.getTimeOfNextEvent(), this.currentIntensity.v));
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
			this.currentIntensity.v = fanIntensity.v + fridgeIntensity.v + batteryIntensity.v + washerIntensity.v;
			this.currentIntensity.time = this.getCurrentStateTime();
			// the next planned computation
			this.nextStep = this.standardStep;
		} else {
			// a request has been received before the next computation
			assert elapsedTime.lessThanOrEqual(this.standardStep);
			// the event has already been output, simply replan the next
			// computation at its previously planned time.
			this.nextStep = this.standardStep.subtract(elapsedTime);
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
		assert currentEvents.get(0) instanceof ConsumptionLevelRequest;

		ConsumptionLevelRequest ce = (ConsumptionLevelRequest) currentEvents.get(0);
		System.out.println("Panel receiving the external event " + ce.getClass().getSimpleName() + "("
				+ ce.getTimeOfOccurrence().getSimulatedTime() + ")");

		// this will trigger an immediate internal transition and the ouput
		// of the ConsumptionLevel event.
		this.requestReceived = true;

		super.userDefinedExternalTransition(elapsedTime);
	}
}
