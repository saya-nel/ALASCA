package main.java.components.washer.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.washer.sil.WasherElectricalSILModel;
import main.java.utils.WasherModes;

public class SetPerformance extends AbstractWasherEvent {

	private static final long serialVersionUID = 1L;

	public SetPerformance(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "SetPerformance(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
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
		assert model instanceof WasherElectricalSILModel;

		WasherElectricalSILModel m = (WasherElectricalSILModel) model;
		if (m.isOn() && m.getMode() != WasherModes.PERFORMANCE) {
			m.setMode(WasherModes.PERFORMANCE);
			m.toggleConsumptionHasChanged();
		}
	}

}
