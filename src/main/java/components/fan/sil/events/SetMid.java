package main.java.components.fan.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.fan.sil.FanElectricalSILModel;
import main.java.components.fan.utils.FanLevel;

/**
 * The class <code>SetMid</code> defines the event of the fan being on Mid
 * consumption mode
 *
 * @author Bello Memmi
 */
public class SetMid extends AbstractFanEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * create a SetMid event.
	 *
	 * @param timeOfOccurrence time of occurrence of the event.
	 */
	public SetMid(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "SetMid(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
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
		assert model instanceof FanElectricalSILModel;

		FanElectricalSILModel m = (FanElectricalSILModel) model;
		if (m.isOn() && m.getLevel() != FanLevel.MID) {
			m.setLevel(FanLevel.MID);
			m.toggleConsumptionHasChanged();
		}
	}

}
