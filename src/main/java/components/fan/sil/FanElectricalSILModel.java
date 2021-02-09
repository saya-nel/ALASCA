package main.java.components.fan.sil;

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
import main.java.components.fan.sil.events.AbstractFanEvent;
import main.java.components.fan.sil.events.SetHigh;
import main.java.components.fan.sil.events.SetLow;
import main.java.components.fan.sil.events.SetMid;
import main.java.components.fan.sil.events.TurnOff;
import main.java.components.fan.sil.events.TurnOn;
import main.java.components.fan.utils.FanLevel;
import main.java.utils.FileLogger;

/**
 * The class <code>FanElectricalSILModel</code> defines a SIL model of the
 * electricity consumption of a fan.
 * 
 * @author Bello Memmi
 *
 */
@ModelExternalEvents(imported = { TurnOn.class, TurnOff.class, SetLow.class, SetMid.class, SetHigh.class })
public class FanElectricalSILModel extends AtomicHIOA {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	/**
	 * URI for an instance model; works as long as only one instance is created.
	 */
	public static final String URI = FanElectricalSILModel.class.getSimpleName();

	/**
	 * energy consumption during low mode in watts
	 */
	public static final double LOW_MODE_CONSUMPTION = 40;
	/**
	 * energy consumption during medium mode in watts
	 */
	public static final double MID_MODE_CONSUMPTION = 60;
	/**
	 * energy consumption during high mode in watts
	 */
	public static final double HIGH_MODE_CONSUMPTION = 80;

	/**
	 * nominal tension (in Volts) of the hair dryer.
	 */
	public static final double TENSION = 220.0;

	/**
	 * current intensity in Amperes; intensity is power/tension.
	 */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentIntensity = new Value<Double>(this, 0.0, 0);
	/**
	 * indicating whether the fan is on
	 */
	protected boolean isOn = false;
	/**
	 * current consumption level of the Fan
	 */
	protected FanLevel currentLevel = FanLevel.LOW;
	/**
	 * True if the solar panels has changed is consumption
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
	public FanElectricalSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.setLogger(new FileLogger("fanElectrical.log"));
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * set the level of the Fan
	 * 
	 * @param level the new level
	 */
	public void setLevel(FanLevel level) {
		currentLevel = level;
	}

	/**
	 * return the state of the Fan.
	 * 
	 * @return the level of the Fan.
	 */
	public FanLevel getLevel() {
		return currentLevel;
	}

	/**
	 * 
	 * @return true if the Fan is on.
	 */
	public boolean isOn() {
		return isOn;
	}

	/**
	 * switch on the fan if it is off or switch off if it is on
	 * 
	 * @return
	 */
	public void toggleIsOn() {
		this.isOn = (this.isOn) ? false : true;
	}

	/**
	 * Change the consumptionHasChanged value to the negation of the actual value
	 */
	public void toggleConsumptionHasChanged() {
		this.consumptionHasChanged = (this.consumptionHasChanged) ? false : true;
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

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
		assert ce instanceof AbstractFanEvent;
		this.logger.logMessage("", this.getCurrentStateTime() + " executing the external event " + ce.eventAsString());
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
