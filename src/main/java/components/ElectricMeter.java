package main.java.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import main.java.connectors.*;
import main.java.ports.*;
import main.java.utils.FanLevel;

import java.time.Duration;
import java.time.LocalTime;

/**
 *
 * class of the component Electric Meter connected to every components
 *
 * @author Bello Memmi
 *
 */
public class ElectricMeter extends AbstractComponent {
    /**
     * Outbound port
     */
    private ControllerOutboundPort controllerOutboundPort           = null;
    private WasherOutboundPort washerOutboundPort                   = null;
    private FridgeOutboundPort fridgeOutboundPort                   = null;
    private FanOutboundPort fanOutboundPort                         = null;
    private BatteryOutboundPort batteryOutboundPort                 = null;
    private PetrolGeneratorOutboundPort petrolGeneratorOutboundPort = null;
    private SolarPanelsOutboundPort solarPanelsOutboundPort         = null;

    /**
     * URIs
     */
    /**
     * URI of component
     */
    protected String uri;
    /**
     * URI of inbound port of controller
     */
    protected String cip;
    /**
     * URI of inbound port of Washer
     */
    protected String wip;
    /**
     * URI of inbound port of Fridge
     */
    protected String fridge_ip;
    /**
     * URI of inbound port of Fan
     */
    protected String fan_ip;
    /**
     * URI of inbound port of Battery
     */
    protected String bip;
    /**
     * URI of inbound port of Petrol_generator
     */
    protected String pg_ip;
    /**
     * URI of inbound port of SolarPanel
     */
    protected String sp;
    protected ElectricMeter(String uri, String cip, String wip, String fridge_ip, String fan_ip,
                            String bip, String pg_ip, String sp) throws Exception
    {
        super(uri, 1, 0);
        this.cip                = cip;
        this.wip                = wip;
        this.fridge_ip          = fridge_ip;
        this.fan_ip             = fan_ip;
        this.bip                = bip;
        this.pg_ip              = pg_ip;
        this.sp                 = sp;
        this.initialise(cip,wip,fridge_ip,fan_ip,bip,pg_ip,sp);
    }

    /**
     *
     * @param cip                   inbound port's uri of controller
     * @param wip                   inbound port's uri of washer
     * @param fridge_ip             inbound port's uri of fridge
     * @param fan_ip                inbound port's uri of fan
     * @param bip                   inbound port's uri of battery
     * @param pg_ip                 inbound port's uri of petrolgenerator
     * @param sp                    inbound port's uri of solar panel
     *
     * uri empty are not published
     * @throws Exception
     */
    public void initialise(String cip, String wip, String fridge_ip, String fan_ip,
                           String bip, String pg_ip, String sp) throws Exception
    {
        controllerOutboundPort          = !cip.equals("")?new ControllerOutboundPort(this):null;
        if(controllerOutboundPort!=null)        controllerOutboundPort.publishPort();
        washerOutboundPort              = !wip.equals("")?new WasherOutboundPort(this):null;
        if(washerOutboundPort!=null)            washerOutboundPort.publishPort();
        fridgeOutboundPort              = !fridge_ip.equals("")?new FridgeOutboundPort(this):null;
        if(fridgeOutboundPort!=null)            fridgeOutboundPort.publishPort();
        fanOutboundPort                 = !fan_ip.equals("")?new FanOutboundPort(this):null;
        if(fanOutboundPort!=null)               fanOutboundPort.publishPort();
        batteryOutboundPort             = !bip.equals("")?new BatteryOutboundPort(this):null;
        if(batteryOutboundPort!=null)           batteryOutboundPort.publishPort();
        petrolGeneratorOutboundPort     = !pg_ip.equals("")?new PetrolGeneratorOutboundPort(this):null;
        if(petrolGeneratorOutboundPort!=null)   petrolGeneratorOutboundPort.publishPort();
        solarPanelsOutboundPort         = !sp.equals("")?new SolarPanelsOutboundPort(this):null;
        if(solarPanelsOutboundPort!=null)       solarPanelsOutboundPort.publishPort();
    }

    /**
     * @see fr.sorbonne_u.components.AbstractComponent#start()
     */
    @Override
    public synchronized void start() throws ComponentStartException {
        super.start();
        try {
            if(controllerOutboundPort!=null)
                this.doPortConnection(this.controllerOutboundPort.getPortURI(), this.cip,
                        ControllerConnector.class.getCanonicalName());
            if(washerOutboundPort!=null)
                this.doPortConnection(this.washerOutboundPort.getPortURI(), this.wip,
                        WasherConnector.class.getCanonicalName());
            if(fridgeOutboundPort!=null)
                this.doPortConnection(this.fridgeOutboundPort.getPortURI(), this.fridge_ip,
                        FridgeConnector.class.getCanonicalName());
            if(fanOutboundPort!=null)
                this.doPortConnection(this.fanOutboundPort.getPortURI(), this.fan_ip,
                        FanConnector.class.getCanonicalName());
            if(batteryOutboundPort!=null)
                this.doPortConnection(this.batteryOutboundPort.getPortURI(), this.bip,
                        BatteryConnector.class.getCanonicalName());
            if(petrolGeneratorOutboundPort!=null)
                this.doPortConnection(this.petrolGeneratorOutboundPort.getPortURI(), this.pg_ip,
                        PetrolGeneratorConnector.class.getCanonicalName());
            if(solarPanelsOutboundPort!=null)
                this.doPortConnection(this.solarPanelsOutboundPort.getPortURI(), this.sp,
                        SolarPanelsConnector.class.getCanonicalName());
        } catch (Exception e) {
            throw new ComponentStartException(e);
        }
    }
    /**
     * @see fr.sorbonne_u.components.AbstractComponent#finalise()
     */
    @Override
    public synchronized void finalise() throws Exception {
        if(controllerOutboundPort!=null && controllerOutboundPort.connected())
            controllerOutboundPort.doDisconnection();
        if(washerOutboundPort!=null && washerOutboundPort.connected())
            washerOutboundPort.doDisconnection();
        if(fridgeOutboundPort!=null && fridgeOutboundPort.connected())
            fridgeOutboundPort.doDisconnection();
        if(fanOutboundPort!=null && fanOutboundPort.connected())
            fanOutboundPort.doDisconnection();
        if(batteryOutboundPort!=null && batteryOutboundPort.connected())
            batteryOutboundPort.doDisconnection();
        if(petrolGeneratorOutboundPort!=null && petrolGeneratorOutboundPort.connected())
            petrolGeneratorOutboundPort.doDisconnection();
        if(solarPanelsOutboundPort!=null && solarPanelsOutboundPort.connected())
            solarPanelsOutboundPort.doDisconnection();
        super.finalise();
    }
    /**
     * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
     */
    @Override
    public synchronized void shutdown() throws ComponentShutdownException {
        try {
            if(controllerOutboundPort!=null)        controllerOutboundPort.unpublishPort();
            if(washerOutboundPort!=null)            washerOutboundPort.unpublishPort();
            if(fridgeOutboundPort!=null)            fridgeOutboundPort.unpublishPort();
            if(fanOutboundPort!=null)               fanOutboundPort.unpublishPort();
            if(batteryOutboundPort!=null)           batteryOutboundPort.unpublishPort();
            if(petrolGeneratorOutboundPort!=null)   petrolGeneratorOutboundPort.unpublishPort();
            if(solarPanelsOutboundPort!=null)       solarPanelsOutboundPort.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

    /**
     *
     * CONTROLLER METHODS
     *
     */
    public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception {
        return controllerOutboundPort.register(serial_number, inboundPortURI, XMLFile);
    }

    /**
     *
     * WASHER METHODS
     *
     */

    public boolean washerIsTurnedOn() throws Exception {
        return washerOutboundPort.isTurnedOn();
    }

    public void washerSetProgramTemperature(int temperature) throws Exception {
        washerOutboundPort.setProgramTemperature(temperature);
    }

    public int washerGetProgramTemperature() throws Exception {
        return washerOutboundPort.getProgramTemperature();
    }

    public void washerSetProgramDuration(int duration) throws Exception {
        washerOutboundPort.setProgramDuration(duration);
    }

    public int washerGetProgramDuration() throws Exception {
        return washerOutboundPort.getProgramDuration();
    }

    public boolean washerTurnOn() throws Exception {
        return washerOutboundPort.turnOn();
    }

    public boolean washerTurnOff() throws Exception {
        return washerOutboundPort.turnOff();
    }

    public boolean washerUpMode() throws Exception {
        return washerOutboundPort.upMode();
    }

    public boolean washerDownMode() throws Exception {
        return washerOutboundPort.downMode();
    }

    public boolean washerSetMode(int modeIndex) throws Exception {
        return washerOutboundPort.setMode(modeIndex);
    }

    public int washerCurrentMode() throws Exception {
        return washerOutboundPort.currentMode();
    }

    public boolean washerHasPlan() throws Exception {
        return washerOutboundPort.hasPlan();
    }

    public LocalTime washerStartTime() throws Exception {
        return washerOutboundPort.startTime();
    }

    public Duration washerDuration() throws Exception {
        return washerOutboundPort.duration();
    }

    public LocalTime washerDeadline() throws Exception {
        return washerOutboundPort.deadline();
    }

    public boolean washerPostpone(Duration d) throws Exception {
        return washerOutboundPort.postpone(d);
    }

    public boolean washerCancel() throws Exception {
        return washerOutboundPort.cancel();
    }

    public boolean washerPlanifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception {
        return washerOutboundPort.planifyEvent(durationLastPlanned, deadline);
    }

    /**
     *
     *  FRIDGE METHODS
     *
     */

    public float fridgeGetRequestedTemperature() throws Exception {
        return fridgeOutboundPort.getRequestedTemperature();
    }

    public void fridgeSetRequestedTemperature(float temp) throws Exception {
        fridgeOutboundPort.setRequestedTemperature(temp);
    }

    public float fridgeGetCurrentTemperature() throws Exception {
        return fridgeOutboundPort.getCurrentTemperature();
    }

    public boolean fridgeUpMode() throws Exception {
        return fridgeOutboundPort.upMode();
    }

    public boolean fridgeDownMode() throws Exception {
        return fridgeOutboundPort.downMode();
    }

    public boolean fridgeSetMode(int modeIndex) throws Exception {
        return fridgeOutboundPort.setMode(modeIndex);
    }

    public int fridgeCurrentMode() throws Exception {
        return fridgeOutboundPort.currentMode();
    }

    public boolean fridgeSuspended() throws Exception {
        return fridgeOutboundPort.suspended();
    }

    public boolean fridgeSuspend() throws Exception {
        return fridgeOutboundPort.suspend();
    }

    public boolean fridgeResume() throws Exception {
        return fridgeOutboundPort.resume();
    }

    public double fridgeEmergency() throws Exception {
        return fridgeOutboundPort.emergency();
    }

    /**
     *
     * FAN METHODS
     *
     */
    public void fanTurnOn() throws Exception {
        fanOutboundPort.turnOn();
    }

    public void fanTurnOff() throws Exception {
        fanOutboundPort.turnOff();
    }

    public void fanAdjustPower(FanLevel level) throws Exception {
        fanOutboundPort.adjustPower(level);
    }

    public boolean fanIsTurnedOn() throws Exception {
        return fanOutboundPort.isTurnedOn();
    }

    public FanLevel fanGetFanLevel() throws Exception {
        return fanOutboundPort.getFanLevel();
    }

    /**
     *
     * BATTERY METHODS
     *
     */

    public float getBatteryCharge() throws Exception {
        return batteryOutboundPort.getBatteryCharge();
    }

    public boolean batteryUpMode() throws Exception {
        return batteryOutboundPort.upMode();
    }

    public boolean batteryDownMode() throws Exception {
        return batteryOutboundPort.downMode();
    }

    public boolean batterySetMode(int modeIndex) throws Exception {
        return batteryOutboundPort.setMode(modeIndex);
    }

    public int batteryCurrentMode() throws Exception {
        return batteryOutboundPort.currentMode();
    }

    public boolean batteryHasPlan() throws Exception {
        return batteryOutboundPort.hasPlan();
    }

    public LocalTime batteryStartTime() throws Exception {
        return batteryOutboundPort.startTime();
    }

    public Duration batteryDuration() throws Exception {
        return batteryOutboundPort.duration();
    }

    public LocalTime batteryDeadline() throws Exception {
        return batteryOutboundPort.deadline();
    }

    public boolean batteryPostpone(Duration d) throws Exception {
        return batteryOutboundPort.postpone(d);
    }

    public boolean batteryCancel() throws Exception {
        return batteryOutboundPort.cancel();
    }

    public boolean batteryPlanifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception {
        return batteryOutboundPort.planifyEvent(durationLastPlanned, deadline);
    }

    /**
     *
     * PETROL GENERATOR METHODS
     *
     */
    public float getMaxLevel() throws Exception {
        return petrolGeneratorOutboundPort.getMaxLevel();
    }

    public float getPetrolLevel() throws Exception {
        return petrolGeneratorOutboundPort.getPetrolLevel();
    }

    public void addPetrol(float quantity) throws Exception {
        petrolGeneratorOutboundPort.addPetrol(quantity);
    }

    public void petrolTurnOn() throws Exception {
        petrolGeneratorOutboundPort.turnOn();
    }

    public void petrolTurnOff() throws Exception {
        petrolGeneratorOutboundPort.turnOff();
    }

    public boolean petrolIsTurnedOn() throws Exception {
        return petrolGeneratorOutboundPort.isTurnedOn();
    }

    /**
     *
     * SOLAR PANEL METHODS
     *
     */

    public void solarPanelsTurnOn() throws Exception {
        solarPanelsOutboundPort.turnOn();
    }

    public void solarPanelsTurnOff() throws Exception {
        solarPanelsOutboundPort.turnOff();
    }

    public boolean solarPanelsIsTurnedOn() throws Exception {
        return solarPanelsOutboundPort.isTurnedOn();
    }
}
