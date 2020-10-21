package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import interfaces.ControllerCI;
import interfaces.ControllerImplementationI;
import ports.ControllerInboundPort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@OfferedInterfaces(offered = { ControllerCI.class })

public class Controller extends AbstractComponent implements ControllerImplementationI {
    private ControllerInboundPort registerRequestPort;//register
    private String myURI;

    //Map serial number in key and XMLfile value
    private Map<String,String> registeredDevices;
    public Controller(String uri,
                      String[] inboundPortDeviceURI,
                      String[] outboundPortDeviceURI) throws Exception
    {
        super(uri, 1, 0);
        assert uri != null;
        this.myURI = uri;

        registeredDevices = new HashMap<>();
        this.registerRequestPort = new ControllerInboundPort(myURI, this);
    }
    @Override
    public void register(String serial_number, String XMLFile) throws Exception {
        registeredDevices.put(serial_number, XMLFile);
    }
}
