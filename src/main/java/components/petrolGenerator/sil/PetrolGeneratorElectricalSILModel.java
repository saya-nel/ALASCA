package main.java.components.petrolGenerator.sil;

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
import main.java.components.petrolGenerator.sil.events.AbstractPetrolGeneratorEvent;
import main.java.components.petrolGenerator.sil.events.TurnOff;
import main.java.components.petrolGenerator.sil.events.TurnOn;
import main.java.utils.FileLogger;

/**
 * The class <code>PetrolGeneratorElectricalSILModel</code> defines a SIL model
 * of the electricity consumption of a petrol generator.
 * 
 * @author Bello Memmi
 *
 */
@ModelExternalEvents(imported = { TurnOff.class, TurnOn.class })
public class PetrolGeneratorElectricalSILModel extends AtomicHIOA {

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/**
	 * URI for an instance model; works as long as only one instance is created.
	 */
	public static final String URI = PetrolGeneratorElectricalSILModel.class.getSimpleName();

	/**
	 * Electricity produced when the petrol generator is on and get petrol in watts
	 */
	protected static final double GENERATING = 1700;
	/**
	 * tension in volts
	 */
	public static final double TENSION = 220;

	/**
	 * Current production of the generator
	 */
	@ExportedVariable(type = Double.class)
	protected final Value<Double> currentProduction = new Value<>(this, 0.0, 0);

	/**
	 * Change when an event that can impact the production is received
	 */
	protected boolean consumptionHasChanged = false;

	/**
	 * True if the generator is on, false else
	 */
	protected boolean isOn = false;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a petrol generator SIL model instance.
	 *
	 * @param uri               URI of the model.
	 * @param simulatedTimeUnit time unit used for the simulation time.
	 * @param simulationEngine  simulation engine to which the model is attached.
	 * @throws Exception
	 */
	public PetrolGeneratorElectricalSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.setLogger(new FileLogger("petrolGeneratorElectrical.log"));
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * @return true if the generator is turned on, false else
	 */
	public boolean getIsOn() {
		return isOn;
	}

	/**
	 * Turn the generator on, work only if the generator get petrol
	 */
	public void turnOn() {
		isOn = true;
	}

	/**
	 * Turn the generator off
	 */
	public void turnOff() {
		isOn = false;
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
		if (consumptionHasChanged) {
			this.toggleConsumptionHasChanged();
			return new Duration(0.0, this.getSimulatedTimeUnit());
		} else
			return Duration.INFINITY;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(Duration)
	 */
	@Override
	public void userDefinedInternalTransition(Duration elapsedTime) {
		super.userDefinedInternalTransition(elapsedTime);
		// if the generator is on and get petrol, he produce electicity
		if (this.isOn) {
			this.currentProduction.v = GENERATING / TENSION;
		}
		// else the generator is off, he dont produce electicity
		else {
			this.currentProduction.v = 0.;
		}
		this.currentProduction.time = this.getCurrentStateTime();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(Duration)
	 */
	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		assert currentEvents != null && currentEvents.size() == 1;
		Event ce = (Event) currentEvents.get(0);
		this.logger.logMessage("",
				this.getCurrentStateTime() + " PetrolGenerator executing the external event " + ce.eventAsString());
		assert ce instanceof AbstractPetrolGeneratorEvent;
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
