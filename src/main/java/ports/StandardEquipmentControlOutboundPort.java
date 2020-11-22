package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.StandardEquipmentControlCI;

public class StandardEquipmentControlOutboundPort extends AbstractOutboundPort implements StandardEquipmentControlCI {
    public StandardEquipmentControlOutboundPort(ComponentI owner) throws Exception {
        super(StandardEquipmentControlCI.class, owner);
    }
    @Override
    public boolean on() throws Exception {
        return ((StandardEquipmentControlCI) this.getConnector()).on();
    }

    @Override
    public boolean off() throws Exception {
        return ((StandardEquipmentControlCI) this.getConnector()).off();
    }

    @Override
    public boolean upMode() throws Exception {
        return ((StandardEquipmentControlCI) this.getConnector()).upMode();
    }

    @Override
    public boolean downMode() throws Exception {
        return ((StandardEquipmentControlCI) this.getConnector()).downMode();
    }

    @Override
    public boolean setMode(int modeIndex) throws Exception {
        return ((StandardEquipmentControlCI) this.getConnector()).setMode(modeIndex);
    }

    @Override
    public int currentMode() throws Exception {
        return ((StandardEquipmentControlCI) this.getConnector()).currentMode();
    }
}
