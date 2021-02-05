package main.java.components.fridge.sil.models;


import fr.sorbonne_u.devs_simulation.hioa.annotations.InternalVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.components.fridge.Fridge;
import main.java.components.fridge.sil.events.Activate;
import main.java.components.fridge.sil.events.Passivate;
import main.java.utils.FridgeMode;
import main.java.utils.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The class <code>FridgeTemperatureSILModel</code> defines a simple simulation model for the content
 * of a fridge
 *
 * @author Bello Memmi
 */
// -----------------------------------------------------------------------------
@ModelExternalEvents(imported={Passivate.class, Activate.class},
        exported={Passivate.class,Activate.class})
// -----------------------------------------------------------------------------
public class FridgeTemperatureSILModel
extends AtomicHIOAwithDE
{
    // -------------------------------------------------------------------------
    // Constants and variables
    // -------------------------------------------------------------------------
    /** URI for an instance model; works as long as only one instance is
     *  created.															*/
    public static final String		URI = FridgeTemperatureSILModel.class.
            getSimpleName();
    /** tracing flag.														*/
    public static boolean			TRACE = true;

    /** integration step for the differential equation(assumed in seconds).	*/
    protected static final double	STEP = 1.0;
    /** integration step as a duration, including the time unit.			*/
    protected final Duration integrationStep;
    /** last received external event.										*/
    protected Event                 lastReceivedEvent;
    /** URI of the variable pointing to the fridge component.				*/
    public static final String		FRIDGE_REFERENCE_NAME = URI + ":BRN";
    /** owner component.													*/
    protected Fridge owner;
    // -------------------------------------------------------------------------
    // HIOA model variables
    // -------------------------------------------------------------------------

    /** the current content temperature.										*/
    @InternalVariable(type = Double.class)
    protected final Value<Double> contentTemperature = new Value<Double>(this, 1.1, 0);
    /** the current derivative of the water temperature.					*/
    protected double				currentTempDerivative = 0.0;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    /**
     * create an instance of the fridge model.
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
     * @throws Exception			<i>to do</i>.
     */
    public				FridgeTemperatureSILModel(
            String uri,
            TimeUnit simulatedTimeUnit,
            SimulatorI simulationEngine
    ) throws Exception
    {
        super(uri, simulatedTimeUnit, simulationEngine);
        this.integrationStep = new Duration(STEP, simulatedTimeUnit);
    }

    // -------------------------------------------------------------------------
    // Instance methods
    // -------------------------------------------------------------------------
    /**
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     * @return	the current content temperature.
     */
    public double getContentTemperature() {return this.contentTemperature.v;}

    // -------------------------------------------------------------------------
    // Simulation protocol and related methods
    // -------------------------------------------------------------------------

    /**
     * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
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
     * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
     */
    @Override
    public void			setSimulationRunParameters(
            Map<String, Object> simParams
    ) throws Exception
    {
        this.owner = (Fridge) simParams.get(FRIDGE_REFERENCE_NAME);
        super.setSimulationRunParameters(simParams);
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
     */
    @Override
    public ArrayList<EventI> output()
    {
        if (this.lastReceivedEvent != null) {
            ArrayList<EventI> ret = new ArrayList<EventI>();
            ret.add(this.lastReceivedEvent);
            this.lastReceivedEvent = null;
            return ret;
        } else {
            return null;
        }
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
     */
    @Override
    public Duration		timeAdvance()
    {
        if (this.lastReceivedEvent == null) {
            return this.integrationStep;
        } else {
            return Duration.zero(this.getSimulatedTimeUnit());
        }
    }
    /**
     * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE#computeDerivatives()
     */
    @Override
    protected void		computeDerivatives()
    {
        // this method is called at each internal transition, just after
        // executing the method userDefinedInternalTransition.
        this.logMessage("currently in computeDerivatives");
        this.currentTempDerivative = 0.0;
        try {
            if (!this.owner.suspended()) {
                // the heating contribution: temperature difference between the
                // plate and the water divided by the heat transfer constant
                if(this.owner.getMode()== FridgeMode.NORMAL)
                {
                    this.currentTempDerivative =
                            (Fridge.STANDARD_FREEZE_TEMP- this.contentTemperature.v)/
                                    Fridge.FREEZE_TRANSFER_CONSTANT;
                } else{
                    this.currentTempDerivative =
                            (Fridge.ECO_FREEZE_TEMP - this.contentTemperature.v)/
                                    Fridge.FREEZE_TRANSFER_CONSTANT;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e) ;
        }
        // the cooling contribution: temperature difference between the
        // fridge and outside divided by the insulation transfer constant
        this.currentTempDerivative +=
                (Fridge.EXTERNAL_TEMPERATURE - this.contentTemperature.v)/
                        Fridge.TRANSFER_OUTSIDE_CONSTANT;
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE#initialiseDerivatives()
     */
    @Override
    protected void		initialiseDerivatives()
    {
        this.computeDerivatives();
    }

    /**
     * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
     */
    @Override
    public void			userDefinedInternalTransition(Duration elapsedTime)
    {
        super.userDefinedInternalTransition(elapsedTime);
        this.logger.logMessage("", "current temp " +contentTemperature.v);
        // update the water temperature using the Euler integration of the
        // differential equation
        Duration d = this.getCurrentStateTime().subtract(this.contentTemperature.time);
        this.contentTemperature.v =
                this.contentTemperature.v +
                        this.currentTempDerivative*d.getSimulatedDuration();
        this.contentTemperature.time = this.getCurrentStateTime();

        // Tracing
        if (TRACE) {
            String mark = "";
            try {
                mark = (!this.owner.suspended()) ?
                        " (+)" 		// active
                        :	" (-)";		// suspended
                this.logger.logMessage("", mark);
            } catch (Exception e) {
                throw new RuntimeException(e) ;
            }
        }
    }


    /**
     * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
     */
    @Override
    public void			userDefinedExternalTransition(Duration elapsedTime)
    {
        // get the vector of current external events
        ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
        // when this method is called, there is at least one external event,
        // and for the hair dryer model, there will be exactly one by
        // construction.
        assert	currentEvents != null && currentEvents.size() == 1;

        this.lastReceivedEvent = (Event) currentEvents.get(0);
        StringBuffer message =
                new StringBuffer("executes the external event ");
        message.append(this.lastReceivedEvent.getClass().getSimpleName());
        message.append("(");
        message.append(
                this.lastReceivedEvent.getTimeOfOccurrence().getSimulatedTime());
        message.append(")\n");
        this.logger.logMessage("", message.toString());

        super.userDefinedExternalTransition(elapsedTime);
    }


    /**
     * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
     */
    @Override
    public void			endSimulation(Time endTime) throws Exception
    {
        this.logger.logMessage("","simulation ends.\n");
        super.endSimulation(endTime);
    }
}
