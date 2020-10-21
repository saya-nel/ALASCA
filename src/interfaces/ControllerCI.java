package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ControllerCI extends ControllerImplementationI, OfferedCI, RequiredCI {
    @Override
    void register(String serial_number, String XMLFile) throws Exception;
}
