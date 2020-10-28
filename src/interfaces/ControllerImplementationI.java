package interfaces;

import java.util.Map;

/**
 *  Contains all methods required from the controller by the devices
 * @author  Bello Memmi
 */
public interface ControllerImplementationI {
    /**
     *
     * @param serial_number         of the device that has to register in Controller
     * @param XMLFile               describing offered control and how to connect required interfaces and offered interfaces
     * @throws Exception
     */
    public void register(String serial_number, String XMLFile) throws Exception;

    /**
     * Useful method for tests
     * @return              every element registered by the controller
     * @throws Exception
     */
    public Map<?,?> getRegisteredDevices() throws Exception;
}
