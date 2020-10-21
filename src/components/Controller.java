package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import interfaces.ControllerCI;
import interfaces.ControllerImplementationI;

import java.util.HashMap;
import java.util.Map;


@OfferedInterfaces(offered = { ControllerCI.class })

public class Controller extends AbstractComponent implements ControllerImplementationI {
    //Map serial number in key and XMLfile value
    private Map<String,String> registeredDevices;
    public Controller(String uri) throws Exception
    {
        super(uri, 1, 0);
        registeredDevices = new HashMap<>();
    }
    @Override
    public void register(String serial_number, String XMLFile) throws Exception {
        registeredDevices.put(serial_number, XMLFile);
    }
}
