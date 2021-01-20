package main.java.components.fan.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.fan.sil.FanElectricity_MILModel;

/**
 * The class <code>TurnOn</code> defines the MIL event of the fan being switched
 * on.
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
public class TurnOn extends AbstractFanEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * create a TurnOn event.
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
	public TurnOn(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "TurnOn(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
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
		assert model instanceof FanElectricity_MILModel;

		FanElectricity_MILModel m = (FanElectricity_MILModel) model;
		if (!m.isOn()) {
			m.toggleIsOn();
			m.toggleConsumptionHasChanged();
		}
	}

}
