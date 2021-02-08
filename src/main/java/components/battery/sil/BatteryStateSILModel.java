package main.java.components.battery.sil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.components.battery.sil.events.AbstractBatteryEvent;
import main.java.components.battery.sil.events.SetDraining;
import main.java.components.battery.sil.events.SetRecharging;
import main.java.components.battery.sil.events.SetSleeping;
import main.java.utils.FileLogger;

/**
 * The class <code>BattryStateSILModel</code> defines a simulation model
 * tracking the state changes on a battery
 * 
 * @author Bello Memmi
 *
 */
@ModelExternalEvents(imported = { SetDraining.class, SetRecharging.class, SetSleeping.class }, exported = {
		SetDraining.class, SetRecharging.class, SetSleeping.class })
public class BatteryStateSILModel extends AtomicModel {

	private static final long serialVersionUID = 1L;

	/**
	 * URI for an instance mode; works as long as only one instance is created
	 */
	public static final String URI = BatteryStateSILModel.class.getSimpleName();
	/**
	 * the event that was received to change the state of the battery.
	 */
	protected EventI lastReceivedEvent;

	public BatteryStateSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.setLogger(new FileLogger("batteryState.log"));
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
		assert this.lastReceivedEvent instanceof AbstractBatteryEvent;
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
