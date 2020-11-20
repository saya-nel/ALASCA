package ports;
/**
 * Inbound port of Fridge component
 *
 * @author Bello Memmi
 *
 */

import components.Fridge;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.FridgeCI;
import interfaces.FridgeImplementationI;

public class FridgeInboundPort extends AbstractInboundPort implements FridgeCI {


	public FridgeInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, FridgeCI.class, owner);
    }

    /**
     * @see FridgeImplementationI#getCurrentTemperature()
     */
    @Override
    public float getRequestedTemperature() throws Exception {
        return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).getRequestedTemperature());
    }

    /**
     * @see FridgeImplementationI#setRequestedTemperature(float)
     */
    @Override
    public void setRequestedTemperature(float temp) throws Exception {
        this.getOwner().runTask(owner -> {
            try{
                ((Fridge) owner).setRequestedTemperature(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @see FridgeImplementationI#getCurrentTemperature()
     */
    @Override
    public float getCurrentTemperature() throws Exception {
        return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).getCurrentTemperature());
    }

    /**
     * @see FridgeImplementationI#switchOff()
     */
    @Override
    public void switchOff() throws Exception {
        this.getOwner().runTask(owner -> {
            try{
                ((Fridge) owner).switchOff();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @see FridgeImplementationI#switchOn()
     */
    @Override
    public void switchOn() throws Exception {
        this.getOwner().runTask(owner -> {
            try{
                ((Fridge) owner).switchOn();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @see FridgeImplementationI#getState()
     */
    @Override
    public boolean getState() throws Exception {
        return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).getState());
    }

    @Override
    public boolean active() throws Exception {
        return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).active());
    }

    @Override
    public boolean activate() throws Exception {
        return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).activate());
    }

    @Override
    public boolean passivate() throws Exception {
        return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).passivate());
    }

    @Override
    public double degreeOfEmergency() throws Exception {
        return this.getOwner().handleRequestSync(owner -> ((Fridge) owner).degreeOfEmergency());
    }
}
