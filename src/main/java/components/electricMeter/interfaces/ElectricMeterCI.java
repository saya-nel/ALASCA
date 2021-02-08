package main.java.components.electricMeter.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import main.java.components.electricMeter.ElectricMeter;

/**
 * 
 * The component interface <code>ElectricMeterCI</code> defines the services a
 * {@link ElectricMeter} component offers and that can be required from it.
 * 
 * @author Bello Memmi
 *
 */
public interface ElectricMeterCI extends ElectricMeterImplementationI, OfferedCI, RequiredCI {

	/**
	 * @see main.java.components.electricMeter.interfaces.ElectricMeterImplementationI#getProduction()
	 */
	@Override
	public double getProduction() throws Exception;

	/**
	 * @see main.java.components.electricMeter.interfaces.ElectricMeterImplementationI#getIntensity()
	 */
	@Override
	public double getIntensity() throws Exception;
}
