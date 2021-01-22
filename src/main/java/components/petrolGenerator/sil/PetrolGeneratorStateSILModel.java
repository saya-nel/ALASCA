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

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ModelExternalEvents(
        imported = {TurnOn.class, TurnOff.class, FillAll.class, EmptyGenerator.class},
        exported = {TurnOff.class, TurnOn.class, FillAll.class, EmptyGenerator.class}
)
public class PetrolGeneratorStateSILModel
extends AtomicModel {
    /** URI for an instance mode; works as long as only one instance is created     */
    public static final String          URI = PetrolGeneratorStateSILModel.class.getSimpleName();
    /** the event that was received to change the state of the petrol generator.    */
    protected EventI                    lastReceivedEvent;
    /** owner component
     *  */
    protected PetrolGenerator           owner;
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * create an instance of hair dryer state SIL simulation model.
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     * @param uri					unique identifier of the model.
     * @param simulatedTimeUnit		time unit used for the simulation clock.
     * @param simulationEngine		simulation engine enacting the model.
     * @throws Exception   			<i>to do</i>.
     */
    public				PetrolGeneratorStateSILModel(
            String uri,
            TimeUnit simulatedTimeUnit,
            SimulatorI simulationEngine
    ) throws Exception
    {
        super(uri, simulatedTimeUnit, simulationEngine);
    }



    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------

    /**
     * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
     */
    @Override
    public void			setSimulationRunParameters(
            Map<String, Object> simParams
    ) throws Exception
    {
        this.owner = (PetrolGenerator) simParams.get(
                PetrolGeneratorUserSILModel.PETROL_GENERATOR_REFERENCE_NAME);
        // the memorising logger keeps all log messages until the end of the
        // simulation; they must explicitly be printed at the end to see them
        this.setLogger(new MemorisingComponentLogger(this.owner));
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
     */
    @Override
    public void			initialiseState(Time initialTime)
    {
        this.lastReceivedEvent = null;
        this.toggleDebugMode();
        this.logMessage("simulation begins.\n");

        super.initialiseState(initialTime);
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
     */
    @Override
    public ArrayList<EventI> output()
    {
        assert	this.lastReceivedEvent != null;
        ArrayList<EventI> ret = new ArrayList<EventI>();
        ret.add(this.lastReceivedEvent);
        this.lastReceivedEvent = null;
        return ret;
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
     */
    @Override
    public Duration timeAdvance()
    {
        if (this.lastReceivedEvent != null) {
            // trigger an immediate internal transition
            return Duration.zero(this.getSimulatedTimeUnit());
        } else {
            // wait until the next external event that will trigger an internal
            // transition
            return Duration.INFINITY;
        }
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
     */
    @Override
    public void			userDefinedExternalTransition(Duration elapsedTime)
    {
        super.userDefinedExternalTransition(elapsedTime);

        // get the vector of current external events
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        // when this method is called, there is at least one external event,
        // and for the hair dryer model, there will be exactly one by
        // construction.
        assert	currentEvents != null && currentEvents.size() == 1;

        this.lastReceivedEvent = (Event) currentEvents.get(0);
        assert	this.lastReceivedEvent instanceof AbstractPetrolGeneratorEvent;

        StringBuffer message =
                new StringBuffer("executes the external event ");
        message.append(this.lastReceivedEvent.getClass().getSimpleName());
        message.append("(");
        message.append(
                this.lastReceivedEvent.getTimeOfOccurrence().getSimulatedTime());
        message.append(")\n");
        this.logMessage(message.toString());
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
