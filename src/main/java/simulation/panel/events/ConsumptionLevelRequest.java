package main.java.simulation.panel.events;

import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public class ConsumptionLevelRequest extends Event {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public ConsumptionLevelRequest(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}
}