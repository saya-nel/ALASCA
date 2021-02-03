package main.java.components.solarPanels.sil;

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
import main.java.components.solarPanels.sil.events.AbstractSolarPanelEvent;
import main.java.components.solarPanels.sil.events.TurnOff;
import main.java.components.solarPanels.sil.events.TurnOn;
import main.java.utils.FileLogger;

@ModelExternalEvents(imported = { TurnOn.class, TurnOff.class })
public class SolarPanelsElectricalSILModel extends AtomicHIOA {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	/**
	 * URI for an instance model; works as long as only one instance is created.
	 */
	public static final String URI = SolarPanelsElectricalSILModel.class.getSimpleName();

	public static final double DRAINING_MODE_PRODUCTION = 50; // supposed to vary according to weather

	public static final double TENSION = 220;

	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentProduction = new Value<>(this, 0.0, 0);

	protected boolean isOn = true;

	protected boolean consumptionHasChanged = true;

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
	public SolarPanelsElectricalSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.setLogger(new FileLogger("SolarPanelsElectrical.log"));
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	public boolean getIsOn() {
		return isOn;
	}

	public void turnOn() {
		isOn = true;
	}

	public void turnOff() {
		isOn = false;
	}

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
			this.currentProduction.v = DRAINING_MODE_PRODUCTION / TENSION;
		} else {
			this.currentProduction.v = 0.;
		}
		this.currentProduction.time = this.getCurrentStateTime();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		assert currentEvents != null && currentEvents.size() == 1;
		Event ce = (Event) currentEvents.get(0);
		assert ce instanceof AbstractSolarPanelEvent;
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
