package interfaces;

import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 *  Contains all methods required from the controller by the devices
 * @author  Bello Memmi
 */

// TODO Implements register in the controller and in every devices
public interface ControllableCI extends RequiredCI {
    /**
     *
     * @param serial_number         of the device that has to register in Controller
     * @param XMLFile               describing offered control and how to connect required interfaces and offered interfaces
     * @throws Exception
     */
    public void register(String serial_number, String XMLFile) throws Exception;
}
