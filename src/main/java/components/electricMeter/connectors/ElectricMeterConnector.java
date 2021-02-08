package main.java.components.electricMeter.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.components.electricMeter.interfaces.ElectricMeterCI;

/**
 * The class <code>ElectricMeterConnector</code> implements a connector for the
 * component interface <code>ElectricMeterCI</code>.
 * 
 * @author Bello Memmi
 *
 */
public class ElectricMeterConnector extends AbstractConnector implements ElectricMeterCI {

	/**
	 * @see main.java.components.electricMeter.interfaces.ElectricMeterImplementationI#getProduction()
	 */
	@Override
	public double getProduction() throws Exception {
		return ((ElectricMeterCI) this.offering).getProduction();
	}

	/**
	 * @see main.java.components.electricMeter.interfaces.ElectricMeterImplementationI#getIntensity()
	 */
	@Override
	public double getIntensity() throws Exception {
		return ((ElectricMeterCI) this.offering).getIntensity();
	}

}
