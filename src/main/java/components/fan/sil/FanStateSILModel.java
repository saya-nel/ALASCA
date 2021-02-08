package main.java.components.fan.sil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
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
import main.java.utils.FileLogger;

/**
 * The class <code>FanStateSILModel</code> defines a simulation model tracking
 * the state changes on a fan
 * 
 * @author Bello Memmi
 *
 */
@ModelExternalEvents(imported = { TurnOn.class, TurnOff.class, SetLow.class, SetMid.class, SetHigh.class }, exported = {
		TurnOn.class, TurnOff.class, SetLow.class, SetMid.class, SetHigh.class })
public class FanStateSILModel extends AtomicModel {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	/**
	 * URI for an instance model; works as long as only one instance is created.
	 */
	public static final String URI = FanStateSILModel.class.getSimpleName();

	/** the event that was received to change the state of the fan */
	protected EventI lastReceivedEvent;

	/**
	 * create an instance of fan state SIL simulation model.
	 * 
	 * @param uri               unique identifier of the model.
	 * @param simulatedTimeUnit time unit used for the simulation clock.
	 * @param simulationEngine  simulation engine enacting the model.
	 * @throws Exception
	 */
	public FanStateSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.setLogger(new FileLogger("fanState.log"));
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

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
			return Duration.zero(this.getSimulatedTimeUnit());
		} else {
			return Duration.INFINITY;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		super.userDefinedExternalTransition(elapsedTime);

		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		assert currentEvents != null && currentEvents.size() == 1;
		this.lastReceivedEvent = currentEvents.get(0);
		assert this.lastReceivedEvent instanceof AbstractFanEvent;
		this.logMessage(
				this.getCurrentStateTime() + " executing the external event " + lastReceivedEvent.eventAsString());
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
