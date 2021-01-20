package main.java.components.fridge.sil;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.components.fridge.Fridge;
import main.java.components.fridge.sil.actuator.FridgeActuatorCI;
import main.java.components.fridge.sil.actuator.FridgeActuatorConnector;
import main.java.components.fridge.sil.actuator.FridgeActuatorOutboundPort;
import main.java.components.fridge.sil.sensors.FridgeSensorCI;
import main.java.components.fridge.sil.sensors.FridgeSensorConnector;
import main.java.components.fridge.sil.sensors.FridgeSensorOutboundPort;
import main.java.deployment.RunSILSimulation;

import java.util.concurrent.TimeUnit;

/**
 * The class <code>FridgeReactiveController</code> implements
 * a simple reactive controller with hysteresis for a Fridge.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant	{@code Boiler.EXTERNAL_TEMP < targetTemp}
 * invariant	{@code targetTemp < Boiler.STANDARD_HEATING_TEMP}
 * </pre>
 *
 * @author  Bello Memmi
 */

@RequiredInterfaces(required={FridgeSensorCI.class, FridgeActuatorCI.class})
public class FridgeReactiveController
extends AbstractComponent {
    // -------------------------------------------------------------------------
    // Constants and variables
    // -------------------------------------------------------------------------

    /** sensor outbound port connected to the fridge component              */
    protected FridgeSensorOutboundPort                      sensorOBP;
    /** actuator outbound port conntected to the fridge component           */
    protected FridgeActuatorOutboundPort                    actuatorOBP;
    /** true if the fridge is in passive mode not freezing                  */
    protected boolean                                       isPassive;
    /** period of the reactive controller                                   */
    protected static final long                             CONTROL_PERIOD = 1000L;
    /** total number of control loop executions (for testing purposes).     */
    protected static final int                              TOTAL_LOOPS = 10;
    /** number of control loops executed so far                             */
    protected int                                           numberOfLoops;


    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** target water temperature (in celcius).								*/
    protected static final double			TARGET_TEMP = 5.0;
    /** the tolerance (in celcius) on the target water temperature to get
     *  a control with hysteresis.											*/
    protected static final double			TARGET_TOLERANCE = 2.0;

    /**
     * create a fridge reactive controller component.
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     */
    protected			FridgeReactiveController()
    {
        // one scheduled executor service used to execute the control loop
        super(1, 1);

        this.tracer.get().setTitle("Boiler reactive controller component");
        this.tracer.get().setRelativePosition(1, 1);
        this.toggleTracing();
    }

    // -------------------------------------------------------------------------
    // Component life-cycle
    // -------------------------------------------------------------------------

    /**
     * @see fr.sorbonne_u.components.AbstractComponent#start()
     */
    @Override
    public synchronized void start() throws ComponentStartException
    {
        super.start();

        this.numberOfLoops = 0;
        try {
            this.sensorOBP = new FridgeSensorOutboundPort(this);
            this.sensorOBP.publishPort();
            this.doPortConnection(
                    this.sensorOBP.getPortURI(),
                    Fridge.SENSOR_INBOUND_PORT_URI,
                    FridgeSensorConnector.class.getCanonicalName());
            this.actuatorOBP = new FridgeActuatorOutboundPort(this);
            this.actuatorOBP.publishPort();
            this.doPortConnection(
                    this.actuatorOBP.getPortURI(),
                    Fridge.ACTUATOR_INBOUND_PORT_URI,
                    FridgeActuatorConnector.class.getCanonicalName());
        } catch (Exception e) {
            throw new ComponentStartException(e) ;
        }
    }

    /**
     * @see fr.sorbonne_u.components.AbstractComponent#execute()
     */
    @Override
    public synchronized void execute() throws Exception
    {
        super.execute();

        this.isPassive = false;
        double delay = CONTROL_PERIOD/ RunSILSimulation.ACC_FACTOR;
        this.scheduleTask(
                o ->	{ try {
                    ((FridgeReactiveController)o).controlLoop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                },
                RunSILSimulation.DELAY_TO_START_SIMULATION + (long)delay,
                TimeUnit.MILLISECONDS);
    }

    /**
     * @see fr.sorbonne_u.components.AbstractComponent#finalise()
     */
    @Override
    public synchronized void	finalise() throws Exception
    {
        this.doPortDisconnection(this.sensorOBP.getPortURI());
        this.doPortDisconnection(this.actuatorOBP.getPortURI());
        super.finalise();
    }

    /**
     * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
     */
    @Override
    public synchronized void	shutdown() throws ComponentShutdownException
    {
        try {
            this.sensorOBP.unpublishPort();
            this.actuatorOBP.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e) ;
        }
        super.shutdown();
    }

    // -------------------------------------------------------------------------
    // Component internal services
    // -------------------------------------------------------------------------

    /**
     * water temperature control.
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true			// no precondition.
     * post	true			// no postcondition.
     * </pre>
     *
     * @throws Exception	<i>to do</i>.
     */
    protected synchronized void		controlLoop() throws Exception
    {
        double currentTemp = this.sensorOBP.getContentTemperatureInCelsius();
        if (!this.isPassive) {
            if (currentTemp <= TARGET_TEMP + TARGET_TOLERANCE) {

                this.logMessage("Boiler reactive controller stops heating.");
                this.actuatorOBP.startPassive();
                this.isPassive = true;
            } else {
                this.logMessage("Boiler reactive controller heats.");
            }
        } else {
            if (currentTemp > TARGET_TEMP + TARGET_TOLERANCE) {
                this.logMessage("Boiler reactive controller starts heating.");
                this.actuatorOBP.stopPassive();
                this.isPassive = false;
            } else {
                this.logMessage("Boiler reactive controller does not heat.");
            }
        }
        this.numberOfLoops++;
        if (this.numberOfLoops < TOTAL_LOOPS) {
            double delay = CONTROL_PERIOD/RunSILSimulation.ACC_FACTOR;
            this.scheduleTask(o -> { try {
                        ((FridgeReactiveController)o).
                                controlLoop();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    },
                    (long)delay, TimeUnit.MILLISECONDS);
        }
    }
}
