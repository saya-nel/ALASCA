package main.java.components.electricMeter.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.components.electricMeter.interfaces.ElectricMeterCI;

public class ElectricMeterConnector extends AbstractConnector implements ElectricMeterCI {

	@Override
	public double getProduction() throws Exception {
		return ((ElectricMeterCI) this.offering).getProduction();
	}

	@Override
	public double getIntensity() throws Exception {
		return ((ElectricMeterCI) this.offering).getIntensity();
	}

}
