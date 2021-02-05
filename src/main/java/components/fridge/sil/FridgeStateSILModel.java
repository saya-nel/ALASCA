package main.java.components.fridge.sil;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.components.fridge.Fridge;
import main.java.components.fridge.sil.events.AbstractFridgeEvent;
import main.java.components.fridge.sil.events.SetEco;
import main.java.components.fridge.sil.events.SetNormal;
import main.java.components.fridge.sil.events.SetRequestedTemperature;

/**
 * The class <code>FridgeStateSILModel</code> defines a simulation model
 * tracking the state changes on a fridge.
 *
 * @author Bello Memmi
 */

@ModelExternalEvents(imported = { SetEco.class, SetNormal.class, SetRequestedTemperature.class }, exported = {
		SetEco.class, SetNormal.class, SetRequestedTemperature.class })
public class FridgeStateSILModel extends AtomicModel {

	private static final long serialVersionUID = 1L;

	/** URI for an instance model; words al long one instance is created */
	public static final String URI = FridgeStateSILModel.class.getSimpleName();
	/** the event that was received to change the state of the hair dryer. */
	protected EventI lastReceivedEvent;
	/** owner component */
	protected Fridge owner;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create an instance of fridge state SIL simulation model.
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
	 * @param uri               unique identifier of the model.
	 * @param simulatedTimeUnit time unit used for the simulation clock.
	 * @param simulationEngine  simulation engine enacting the model.
	 * @throws Exception <i>to do</i>.
	 */
	public FridgeStateSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.owner = (Fridge) simParams.get(FridgeUserSILModel.FRIDGE_REFERENCE_NAME);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void initialiseState(Time initialTime) {
		this.lastReceivedEvent = null;
		this.toggleDebugMode();
		this.logMessage("simulation begins.\n");

		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI> output() {
		assert this.lastReceivedEvent != null;
		ArrayList<EventI> ret = new ArrayList<EventI>();
		ret.add(this.lastReceivedEvent);
		this.lastReceivedEvent = null;
		return ret;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration timeAdvance() {
		if (this.lastReceivedEvent != null) {
			// trigger an immediate internal transition
			return Duration.zero(this.getSimulatedTimeUnit());
		} else {
			// wait until the next external event that will trigger an internal
			// transition
			return Duration.INFINITY;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		super.userDefinedExternalTransition(elapsedTime);

		// get the vector of current external events
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		// when this method is called, there is at least one external event,
		// and for the hair dryer model, there will be exactly one by
		// construction.
		assert currentEvents != null && currentEvents.size() == 1;

		this.lastReceivedEvent = currentEvents.get(0);
		assert this.lastReceivedEvent instanceof AbstractFridgeEvent;

		StringBuffer message = new StringBuffer("executes the external event ");
		message.append(this.lastReceivedEvent.getClass().getSimpleName());
		message.append("(");
		message.append(this.lastReceivedEvent.getTimeOfOccurrence().getSimulatedTime());
		message.append(")\n");
		this.logMessage(message.toString());
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void endSimulation(Time endTime) throws Exception {
		super.endSimulation(endTime);
	}
}
