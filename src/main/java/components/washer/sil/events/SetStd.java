package main.java.components.washer.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.washer.sil.WasherElectricalSILModel;
import main.java.components.washer.utils.WasherModes;

/**
 * The class <code>SetStd</code> defines the event of the washer being on
 * standard mode
 *
 * @author Bello Memmi
 */
public class SetStd extends AbstractWasherEvent {

	private static final long serialVersionUID = 1L;

	/**
	 * create a SetStd event.
	 *
	 * @param timeOfOccurrence time of occurrence of the event.
	 */
	public SetStd(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "SetStd(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
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
		if (m.isOn() && m.getMode() != WasherModes.STD) {
			m.setMode(WasherModes.STD);
			m.toggleConsumptionHasChanged();
		}
	}

}
