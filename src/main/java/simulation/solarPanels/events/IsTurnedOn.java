package main.java.simulation.solarPanels.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.simulation.petrolGenerator.events.TurnOn;
import main.java.simulation.solarPanels.SolarPanelsElectricity_MILModel;

public class IsTurnedOn extends AbstractSolarPanelEvent {

	private static final long serialVersionUID = 1L;

	public IsTurnedOn(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "is turned on solar panels(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.es.events.ES_Event#hasPriorityOver(fr.sorbonne_u.devs_simulation.models.events.EventI)
	 */
	@Override
	public boolean hasPriorityOver(EventI e) {
		if (e instanceof TurnOn) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(fr.sorbonne_u.devs_simulation.models.AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model) {
		assert model instanceof SolarPanelsElectricity_MILModel;

		SolarPanelsElectricity_MILModel m = (SolarPanelsElectricity_MILModel) model;
		m.getIsOn();
		m.toggleConsumptionHasChanged();

	}
}
