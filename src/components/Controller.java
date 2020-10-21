package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import interfaces.ControllerCI;
import interfaces.ControllerImplementationI;
import ports.ControllerInboundPort;
import ports.ControllerOutboundPort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


@OfferedInterfaces(offered = { ControllerCI.class })

public class Controller extends AbstractComponent implements ControllerImplementationI {
    //ports used for registering
    private List<ControllerOutboundPort> registerRequestPort;

    //ports used for controlling devices
    private List<ControllerInboundPort> controlDevicesPorts;

    //uri of component
    private String myURI;

    //Map serial number in key and XMLfile value
    private Map<String,String> registeredDevices;



    public Controller(String uri,
                      String[] outboundPortRegisterURI,
                      String[] inboundPortDeviceURI) throws Exception
    {
        super(uri, 1, 0);
        assert uri != null;
        this.myURI = uri;

        registeredDevices = new HashMap<>();


        // Initialize ports relative to registering
        this.registerRequestPort = new Vector<>();

        for (String out : outboundPortRegisterURI)
        {
            registerRequestPort.add(new ControllerOutboundPort(out, this));
        }
        for (ControllerOutboundPort bom : registerRequestPort)
        {
            bom.publishPort();
        }

        // Initialize ports relative to devices control
        for ( String in : inboundPortDeviceURI)
        {
            this.controlDevicesPorts.add(new ControllerInboundPort(in,this));
        }
        for(ControllerInboundPort port : this.controlDevicesPorts)
        {
            port.publishPort();
        }
    }
    @Override
    public void register(String serial_number, String XMLFile) throws Exception {
        registeredDevices.put(serial_number, XMLFile);
    }
}
