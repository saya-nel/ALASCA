package main.java.components.electricMeter.interfaces;

/**
 * 
 * The interface <code>ElectricMeterImplementationI</code> defines the service
 * that must be implemented by the {@link EletricMeter} component.
 * 
 * @author Bello Memmi
 *
 */
public interface ElectricMeterImplementationI {

	// -------------------------------------------------------------------------
	// Component services signatures
	// -------------------------------------------------------------------------

	/**
	 * Return the actuel energy production of the house
	 * 
	 * @return the actuel energy production of the house
	 * @throws Exception
	 */
	public double getProduction() throws Exception;

	/**
	 * Return the actuel energy consumption of the house
	 * 
	 * @return the actuel energy consumption of the house
	 * @throws Exception
	 */
	public double getIntensity() throws Exception;

}
