package main.java.components.fridge.sil.events;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventInformationI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import main.java.components.fridge.sil.FridgeElectricalSILModel;

public class SetRequestedTemperature extends AbstractFridgeEvent {

	public static class RequestedTemperature implements EventInformationI {

		private static final long serialVersionUID = 1L;

		protected double requestedTemperature;

		public RequestedTemperature(double requestedTemperature) {
			this.requestedTemperature = requestedTemperature;
		}

		public double getRequestedTemperature() {
			return requestedTemperature;
		}

	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	public SetRequestedTemperature(Time timeOfOccurrence, RequestedTemperature content) {
		super(timeOfOccurrence, content);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#eventAsString()
	 */
	@Override
	public String eventAsString() {
		return "SetRequestedTemperature(time : " + this.getTimeOfOccurrence().getSimulatedTime()
				+ ", requestedTemperature : " + ((RequestedTemperature) this.getEventInformation()).requestedTemperature
				+ ")";
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.es.events.ES_Event#hasPriorityOver(EventI)
	 */
	@Override
	public boolean hasPriorityOver(EventI e) {
		return false;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.Event#executeOn(AtomicModel)
	 */
	@Override
	public void executeOn(AtomicModel model) {
		assert model instanceof FridgeElectricalSILModel;
		FridgeElectricalSILModel m = (FridgeElectricalSILModel) model;
		m.setTargetTemperature(((RequestedTemperature) this.getEventInformation()).requestedTemperature);
		// m.setRequestedTemperature(((RequestedTemperature)
		// this.getEventInformation()).requestedTemperature);
	}

}
