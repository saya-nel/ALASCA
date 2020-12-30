package main.java.simulation.panel.events;

import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public class ProductionLevelRequest extends Event {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public ProductionLevelRequest(Time timeOfOccurrence) {
		super(timeOfOccurrence, null);
	}

}
