package main.java.components.solarPanels.sil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * Abstract events for solar panels
 * 
 * @author Bello Memmi
 *
 */
public class AbstractSolarPanelEvent extends ES_Event {

	private static final long serialVersionUID = 1L;

	public AbstractSolarPanelEvent(Time timeOfOccurrence, EventInformationI content) {
		super(timeOfOccurrence, content);
	}
}