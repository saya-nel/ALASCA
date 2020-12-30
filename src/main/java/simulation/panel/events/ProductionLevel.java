package main.java.simulation.panel.events;

import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.simulation.panel.events.ConsumptionLevel.Level;

public class ProductionLevel extends Event {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	public ProductionLevel(Time timeOfOccurrence, double productionLevel) {
		super(timeOfOccurrence, new Level(productionLevel));
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	public double getProductionLevel() {
		return ((Level) this.getEventInformation()).getLevel();
	}

}
