package main.java.components.fridge.sil.actuator;


import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.fridge.Fridge;
import main.java.components.fridge.sil.FridgeReactiveControlImplementationI;

/**
 * The class <code>FridgeActuatorInboundPort</code> implements an inbound port
 * for the <code>FridgeActuatorCI</code> component interface.
 *
 * @author	Bello Memmi
 */
public class FridgeActuatorInboundPort
extends     AbstractInboundPort
implements  FridgeActuatorCI
{
    public FridgeActuatorInboundPort(ComponentI owner) throws Exception
    {
        super(Fridge.ACTUATOR_INBOUND_PORT_URI, FridgeActuatorCI.class, owner);
        assert owner instanceof FridgeReactiveControlImplementationI;
    }

    /**
     * @see FridgeActuatorCI#startPassive()
     */
    @Override
    public void startPassive() throws Exception {
        this.getOwner().handleRequestSync(
                o -> {
                    ((FridgeReactiveControlImplementationI)o).passiveSwitch(true);
                    return null;
                });
    }

    /**
     * @see FridgeActuatorCI#stopPassive()
     */
    @Override
    public void stopPassive() throws Exception {
        this.getOwner().handleRequestSync(
                o -> {
                    ((FridgeReactiveControlImplementationI)o).passiveSwitch(false);
                    return null;
                });
    }
}
