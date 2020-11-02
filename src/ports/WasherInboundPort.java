package ports;

import components.Washer;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.WasherCI;
/**
 * Class representing the inbound port of the component Washer
 *
 * @author Bello Memmi
 *
 */
public class WasherInboundPort extends AbstractInboundPort implements WasherCI {
    public WasherInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, WasherCI.class, owner);
    }
    @Override
    public boolean getStateWasher() throws Exception {
        return this.getOwner().handleRequestSync(owner -> ((Washer) owner)).getStateWasher();
    }

    @Override
    public void turnOnWasher(int operating_temperature) throws Exception {
        this.getOwner().runTask(owner -> {
            try{
                ((Washer) owner).turnOnWasher(operating_temperature);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void turnOffWasher() throws Exception {
        this.getOwner().runTask(owner -> {
            try{
                ((Washer) owner).turnOffWasher();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getOperatingTemperature() throws Exception {
        return this.getOwner().handleRequestSync(owner -> ((Washer) owner).getOperatingTemperature());
    }

}
