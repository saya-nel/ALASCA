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
import main.java.utils.FanLevel;

@ModelExternalEvents(imported = { TurnOn.class, TurnOff.class, SetLow.class, SetMid.class, SetHigh.class })
public class FanElectricity_MILModel extends AtomicHIOA {

	private static final long serialVersionUID = 1L;

	public static final double LOW_MODE_CONSUMPTION = 20;
	public static final double MID_MODE_CONSUMPTION = 40;
	public static final double HIGH_MODE_CONSUMPTION = 60;
	public static final double TENSION = 220;

	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<Double>(this, 0.0, 0);

	protected boolean isOn = false;
	protected FanLevel currentLevel = FanLevel.LOW;
	protected boolean consumptionHasChanged = false;

	public FanElectricity_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	public void setLevel(FanLevel level) {
		currentLevel = level;
	}

	public FanLevel getLevel() {
		return currentLevel;
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
		assert ce instanceof AbstractFanEvent;
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
	}

}
