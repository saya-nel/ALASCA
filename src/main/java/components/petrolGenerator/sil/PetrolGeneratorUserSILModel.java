package main.java.components.petrolGenerator.sil;

import fr.sorbonne_u.components.cyphy.plugins.devs.utils.MemorisingComponentLogger;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.components.petrolGenerator.PetrolGenerator;
import main.java.components.petrolGenerator.sil.events.*;
import main.java.utils.FileLogger;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The users actions that interacts with the simulated petrol generator
 * 
 * @author Bello Memmi
 *
 */
@ModelExternalEvents(exported = { TurnOn.class, TurnOff.class, FillAll.class },
		imported = { EmptyGenerator.class })
public class PetrolGeneratorUserSILModel extends AtomicModel {

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------
	public static final String 		URI = PetrolGeneratorUserSILModel.class.getSimpleName();

	/** default time interval between event outputs. */
	protected static final double STEP = 1.0;

	/** the current event being output. */
	protected AbstractPetrolGeneratorEvent currentEvent;

	/** time interval between event outputs. */
	protected Duration time2next;

	public static final String 				PETROL_GENERATOR_REFERENCE_NAME= URI + ":" + "HDCRN";

	protected PetrolGenerator				owner;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public PetrolGeneratorUserSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.setLogger(new FileLogger("petrolGeneratorUser.log"));
		setDebugLevel(2);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	public void receiveEmptyGenerator(EmptyGenerator event) {
		this.currentEvent = event;
	}

	/**
	 * set the current event to be output and return it as result; this method
	 * implements a simple simulation scenario just to test the model.
	 * 
	 * @param t time at which the next event must occur.
	 * @return the next event to be output.
	 */
	protected AbstractPetrolGeneratorEvent getCurrentEventAndSetNext(Time t) {
		// at the start, we turn the generator on
		if (this.currentEvent == null) {
			this.currentEvent = new TurnOn(t);
			// the next event will be a FillAll, from the moment when we can do it (when we
			// receive EmptyGenerator, we wait 30 minutes before filling
			this.time2next = new Duration(0.5 * 3600., this.getSimulatedTimeUnit());
		} else {
			@SuppressWarnings("unchecked")
			Class<AbstractPetrolGeneratorEvent> c = (Class<AbstractPetrolGeneratorEvent>) this.currentEvent.getClass();
			if (c.equals(EmptyGenerator.class)) {
				this.currentEvent = new FillAll(t);
				// next event will be turnOn, we wait 1 second before doing it
				this.time2next = new Duration(1., this.getSimulatedTimeUnit());
			} else if (c.equals(FillAll.class)) {
				this.currentEvent = new TurnOn(t);
				// next event will be FillAll, we wait 1 second before doing it
				this.time2next = new Duration(10., this.getSimulatedTimeUnit());
			} else if (c.equals(TurnOn.class)) {
				// nothing to do, we wait the EmptyGenerator event
				return null;
			}
		}
		return currentEvent;
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void			setSimulationRunParameters(
			Map<String, Object> simParams
	) throws Exception
	{
		this.owner = (PetrolGenerator) simParams.get(PETROL_GENERATOR_REFERENCE_NAME);
		// the memorising logger keeps all log messages until the end of the
		// simulation; they must explicitly be printed at the end to see them
		this.setLogger(new MemorisingComponentLogger(this.owner));
	}

	/**
	 * @see AtomicModel#initialiseState(Time)
	 */
	@Override
	public void initialiseState(Time initialTime) {
		this.time2next = new Duration(STEP, this.getSimulatedTimeUnit());
		this.currentEvent = null;
		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorsalutbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI> output() {
		ArrayList<EventI> ret = new ArrayList<EventI>();
		AbstractPetrolGeneratorEvent nextEvent = this.getCurrentEventAndSetNext(this.getTimeOfNextEvent());
		if (nextEvent == null)
			return null;
		ret.add(nextEvent);
		return ret;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration timeAdvance() {
		return this.time2next;
	}

	/**
	 * @see AtomicModel#userDefinedExternalTransition(Duration)
	 */
	@Override
	public void userDefinedExternalTransition(Duration elapsedTime) {
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		assert currentEvents != null && currentEvents.size() == 1;
		Event ce = (Event) currentEvents.get(0);
		this.logger.logMessage("",
				this.getCurrentStateTime() + " PetrolGeneratorUser executing the external event " + ce.eventAsString());
		assert ce instanceof AbstractPetrolGeneratorEvent;
		ce.executeOn(this);
		super.userDefinedExternalTransition(elapsedTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			userDefinedInternalTransition(Duration elapsedTime)
	{
		super.userDefinedInternalTransition(elapsedTime);

		this.currentEvent = this.getCurrentEventAndSetNext(this.getCurrentStateTime());
		StringBuffer message = new StringBuffer("executes ");
		message.append(this.currentEvent);
		message.append(".\n");
		this.logMessage(message.toString());
		AbstractPetrolGeneratorEvent e = (AbstractPetrolGeneratorEvent) currentEvent;
		if(e instanceof TurnOn)
			this.owner.runTask(o -> {
				try {
					((PetrolGenerator)o).turnOn();
				} catch (Exception ex){
					throw new RuntimeException(ex);
				}
			});
		else if(e instanceof TurnOff)
			this.owner.runTask(o -> {
				try {
					((PetrolGenerator)o).turnOff();
				} catch (Exception ex){
					throw new RuntimeException(ex);
				}
			});
		else if(e instanceof EmptyGenerator)
			this.owner.runTask(o -> {
				try {
					((PetrolGenerator)o).emptyGenerator();
				} catch(Exception ex){
					throw new RuntimeException(ex);
				}
			});
		else
			this.owner.runTask(o -> {
				try {
					((PetrolGenerator)o).fillAll();
				} catch(Exception ex){
					throw new RuntimeException(ex);
				}
			});
		}
	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			endSimulation(Time endTime) throws Exception
	{
		this.logMessage("simulation ends.\n");
		((MemorisingComponentLogger)this.logger).printLog();
		super.endSimulation(endTime);
	}

}
