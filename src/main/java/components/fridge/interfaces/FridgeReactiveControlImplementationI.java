package main.java.components.fridge.interfaces;

/**
 * The class <code>FridgeReactiveControlImplementationI</code> defines the
 * services to be implemented by a Fridge component to support reactive
 * controller
 *
 * @author Bello Memmi
 */
public interface FridgeReactiveControlImplementationI {
	/**
	 * return the current temperature in the fridge.
	 *
	 * Should return a better representation of a sensor data (with time, etc.).
	 * 
	 * @return the current temperature temperature in the fridge.
	 */
	public double contentTemperatureSensor();

	/**
	 *
	 * @return true if the fridge is in passive mode.
	 */
	public boolean isPassive();

	/**
	 * switch the passive mode to the argument
	 * 
	 * @param passive true if the controller wants to make the fridge passive
	 */
	public void passiveSwitch(boolean passive);
}
