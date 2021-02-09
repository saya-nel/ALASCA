package main.java.components.fridge.sil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * The abstract class <code>AbstractFridgeEvent</code> enforces a common type
 * for all fridge events.
 *
 * @author Bello Memmi
 */
public class AbstractFridgeEvent extends ES_Event {

	private static final long serialVersionUID = 1L;

	/**
	 * used to create an event used by the fridge simulation model.
	 *
	 * @param timeOfOccurrence time of occurrence of the event.
	 * @param content          content (data) associated with the event.
	 */
	public AbstractFridgeEvent(Time timeOfOccurrence, EventInformationI content) {
		super(timeOfOccurrence, content);
	}

}
