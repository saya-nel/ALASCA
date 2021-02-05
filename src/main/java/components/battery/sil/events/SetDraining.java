package main.java.components.battery.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.battery.sil.BatteryElectricalSILModel;
import main.java.components.battery.utils.BatteryState;

/**
 * The class <code>SetDraining</code> defines the MIL event of the battery being
 * set to drainging mode.
 *
 * <p>
 * <strong>Description</strong>
 * </p>
 *
 * <p>
 * <strong>Invariant</strong>
 * </p>
 *
 * <pre>
 * invariant		true
 * </pre>
 *
 * @author Bello Memmi
 */
public class SetDraining extends AbstractBatteryEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * create a SetDraining event.
	 *
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 * pre	{@code timeOfOccurrence != null}
	 * post	{@code getTimeOfOccurrence().equals(timeOfOccurrence)}
	 * </pre>
	 *
	 * @param timeOfOccurrence time of occurrence of the event.
	 */
	public SetDraining(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "SetDraining(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.es.events.ES_Event#hasPriorityOver(fr.sorbonne_u.devs_simulation.models.events.EventI)
	 */
	@Override
	public boolean hasPriorityOver(EventI e) {
		return false;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model) {
		assert model instanceof BatteryElectricalSILModel;

		BatteryElectricalSILModel m = (BatteryElectricalSILModel) model;
		if (m.getState() != BatteryState.DRAINING) {
			m.setState(BatteryState.DRAINING);
			m.toggleConsumptionHasChanged();
		}
	}

}
