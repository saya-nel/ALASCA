package main.java.components.fridge.sil.models;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.components.fridge.Fridge;
import main.java.simulation.fridge.events.AbstractFridgeEvent;
import main.java.simulation.fridge.events.SetEco;
import main.java.simulation.fridge.events.SetNormal;
import main.java.simulation.fridge.events.SetRequestedTemperature;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The class <code>FridgeUser_MILModel</code> defines a very simple user model for a fridge.
 *
 * @author Bello Memmi
 */
@ModelExternalEvents(exported = { SetEco.class, SetNormal.class, SetRequestedTemperature.class })
public class FridgeUser_MILModel extends AtomicModel {
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long 			serialVersionUID = 1L;

	/** time interval between event outputs. */
	protected static final double 		STEP = 60 * 60 * 6; // 5 hours
	/** URI for an instance model; works as long as only one instance is
	 *  created.															*/
	public static final String			URI = FridgeUser_MILModel.class.
			getSimpleName();
	/** the current event being output. */
	protected AbstractFridgeEvent currentEvent;
	/** time interval between event outputs. */
	protected Duration 					time2next;
	/** name used to pass the owner component reference as simulation
	 *  parameter.															*/
	public static final String 			FRIDGE_REFERENCE_NAME 	= URI + ":"+"HDCRN";
	/** owner component */
	protected Fridge 					owner;

	public FridgeUser_MILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
	}


	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------


	protected AbstractFridgeEvent getCurrentEventAndSetNext(Time t){
		if (this.currentEvent == null) {
			this.currentEvent = new SetRequestedTemperature(t, new SetRequestedTemperature.RequestedTemperature(6.));
		} else {
			@SuppressWarnings("unchecked")
			Class<AbstractFridgeEvent> c = (Class<AbstractFridgeEvent>) this.currentEvent.getClass();
			if (c.equals(SetEco.class)) {
				this.currentEvent = new SetNormal(t);
			} else if (c.equals(SetNormal.class)) {
				this.currentEvent = new SetEco(t);
			}

		}
		return this.currentEvent;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void			setSimulationRunParameters(
			Map<String, Object> simParams
	) throws Exception
	{
		this.owner = (Fridge) simParams.get(FRIDGE_REFERENCE_NAME);
	}

	/**
	 * @see AtomicModel#initialiseState(Time)
	 */
	@Override
	public void initialiseState(Time initialTime) {
		// intialize the duration between two events
		this.time2next = new Duration(STEP, this.getSimulatedTimeUnit());
		this.currentEvent = null;
		this.logger.logMessage("", "simulation begins");
		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI> output() {
		ArrayList<EventI> ret = new ArrayList<EventI>();
		ret.add(this.getCurrentEventAndSetNext(this.getTimeOfNextEvent()));
		return ret;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration timeAdvance() {
		if (currentEvent == null) {
			return new Duration(1., TimeUnit.SECONDS);
		} else {
			return this.time2next;
		}
	}



	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			endSimulation(Time endTime) throws Exception
	{
		this.logger.logMessage("","user simulation ends");
		super.endSimulation(endTime);
	}
}
