package main.java.simulation.panel.events;

import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

public class ElectricityLevel extends Event {

	public static class Level implements EventInformationI {

		private static final long serialVersionUID = 1L;

		protected double consumptionLevel;
		protected double productionLevel;

		public Level(double consumptionLevel, double productionLevel) {
			super();
			this.consumptionLevel = consumptionLevel;
			this.productionLevel = productionLevel;
		}

		public double getConsumptionLevel() {
			return this.consumptionLevel;
		}

		public double getProductionLevel() {
			return this.productionLevel;
		}
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	public ElectricityLevel(Time timeOfOccurrence, double consumptionLevel, double productionLevel) {
		super(timeOfOccurrence, new Level(consumptionLevel, productionLevel));
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	@Override
	public boolean hasPriorityOver(EventI e) {
		return false;
	}

	@Override
	public String eventAsString() {
		return "ElectricityLevel(" + this.getTimeOfOccurrence().getSimulatedTime() + ")";
	}

	public double getConsumptionLevel() {
		return ((Level) this.getEventInformation()).getConsumptionLevel();
	}

	public double getProductionLevel() {
		return ((Level) this.getEventInformation()).getProductionLevel();
	}
}