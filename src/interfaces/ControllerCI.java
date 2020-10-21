package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;

public interface ControllerCI extends ControllerImplementationI, OfferedCI {
    @Override
    void register(String serial_number, String XMLFile) throws Exception;
}
