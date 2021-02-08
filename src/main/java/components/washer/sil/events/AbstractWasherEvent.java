package main.java.components.washer.sil.events;

import fr.sorbonne_u.devs_simulation.es.events.ES_Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * The abstract class <code>AbstractWasher Event</code> enforces a common type
 * for all washer events.
 *
 * @author Bello Memmi
 */
public class AbstractWasherEvent extends ES_Event {

	private static final long serialVersionUID = 1L;

	/**
	 * used to create an event used by the washer simulation model.
	 *
	 * @param timeOfOccurrence time of occurrence of the event.
	 * @param content          content (data) associated with the event.
	 */
	public AbstractWasherEvent(Time timeOfOccurrence, EventInformationI content) {
		super(timeOfOccurrence, content);
	}

}
