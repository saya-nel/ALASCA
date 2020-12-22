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

@ModelExternalEvents(imported = { TurnOn.class, TurnOff.class, SetEco.class, SetStd.class, SetPerformance.class })
public class WasherElectricity_MILModel extends AtomicHIOA {

	private static final long serialVersionUID = 1L;

	public static final double ECO_MODE_CONSUMPTION = 20;
	public static final double STD_MODE_CONSUMPTION = 40;
	public static final double PERFORMANCE_MODE_CONSUMPTION = 60;
	public static final double TENSION = 220;

	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<Double>(this, 0.0, 0);

	protected boolean isOn = false;
	protected WasherModes currentMode = WasherModes.ECO;
	protected boolean consumptionHasChanged = false;

	public WasherElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	public void setMode(WasherModes mode) {
		currentMode = mode;
	}

	public WasherModes getLevel() {
		return currentMode;
	}

	public boolean isOn() {
		return isOn;
	}

	public void toggleIsOn() {
		this.isOn = (this.isOn) ? false : true;
	}

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
