package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

import java.util.Map;

public interface ControllerCI extends ControllerImplementationI, OfferedCI, RequiredCI {
    @Override
    void register(String serial_number, String XMLFile) throws Exception;

    @Override
    Map<String, String> getRegisteredDevices() throws Exception;
}
