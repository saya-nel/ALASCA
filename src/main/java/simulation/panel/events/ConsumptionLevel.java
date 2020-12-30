package main.java.simulation.panel.events;

import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public class ConsumptionLevel extends Event {

	public static class Level implements EventInformationI {

		private static final long serialVersionUID = 1L;
		protected double level;

		public Level(double level) {
			super();
			this.level = level;
		}

		public double getLevel() {
			return this.level;
		}
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	public ConsumptionLevel(Time timeOfOccurrence, double consumptionLevel) {
		super(timeOfOccurrence, new Level(consumptionLevel));
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	public double getConsumptionLevel() {
		return ((Level) this.getEventInformation()).getLevel();
	}
}