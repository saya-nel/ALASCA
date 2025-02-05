package main.java.components.fan.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.fan.sil.FanElectricalSILModel;
import main.java.components.fan.utils.FanLevel;

/**
 * The class <code>SetLow</code> defines the event of the fan being on Low
 * consumption mode
 *
 * @author Bello Memmi
 */
public class SetLow extends AbstractFanEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * create a SetLow event.
	 *
	 * @param timeOfOccurrence time of occurrence of the event.
	 */
	public SetLow(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "SetLow(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
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
		if (m.isOn() && m.getLevel() != FanLevel.LOW) {
			m.setLevel(FanLevel.LOW);
			m.toggleConsumptionHasChanged();
		}
	}

}
