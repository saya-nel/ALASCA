package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.PlanningEquipmentControlCI;
import interfaces.SuspensionEquipmentControlCI;

public class SuspensionEquipmentControlOutboundPort extends AbstractOutboundPort implements SuspensionEquipmentControlCI {
    public SuspensionEquipmentControlOutboundPort(ComponentI owner) throws Exception {
        super(SuspensionEquipmentControlCI.class, owner);
    }

    @Override
    public boolean suspended() throws Exception {
        return ((SuspensionEquipmentControlCI) this.getConnector()).suspended();
    }

    @Override
    public boolean suspend() throws Exception {
        return ((SuspensionEquipmentControlCI) this.getConnector()).suspend();
    }

    @Override
    public boolean resume() throws Exception {
        return ((SuspensionEquipmentControlCI) this.getConnector()).resume();
    }

    @Override
    public double emergency() throws Exception {
        return ((SuspensionEquipmentControlCI) this.getConnector()).emergency();
    }

    @Override
    public boolean on() throws Exception {
        return ((SuspensionEquipmentControlCI) this.getConnector()).on();
    }

    @Override
    public boolean off() throws Exception {
        return ((SuspensionEquipmentControlCI) this.getConnector()).off();
    }

    @Override
    public boolean upMode() throws Exception {
        return ((SuspensionEquipmentControlCI) this.getConnector()).upMode();
    }

    @Override
    public boolean downMode() throws Exception {
        return ((SuspensionEquipmentControlCI) this.getConnector()).downMode();
    }

    @Override
    public boolean setMode(int modeIndex) throws Exception {
        return ((SuspensionEquipmentControlCI) this.getConnector()).setMode(modeIndex);
    }

    @Override
    public int currentMode() throws Exception {
        return ((SuspensionEquipmentControlCI) this.getConnector()).currentMode();
    }
}
