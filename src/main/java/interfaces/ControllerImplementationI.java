package main.java.interfaces;

/**
 * Contains all methods required from the controller by the devices
 * 
 * @author Bello Memmi
 */
public interface ControllerImplementationI {
	/**
	 *
	 * @param serial_number  of the device that has to register in Controller
	 * @param inboundPortURI of the device that has to register in Controller
	 * @param XMLFile        describing offered control and how to connect required
	 *                       interfaces and offered interfaces
	 * 
	 * @return true if the device has been registrated, false else
	 * @throws Exception
	 */
	public boolean register(String serial_number, String inboundPortURI, String XMLFile) throws Exception;

}
