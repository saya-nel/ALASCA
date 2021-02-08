package main.java.components.electricMeter.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ElectricMeterCI extends ElectricMeterImplementationI, OfferedCI, RequiredCI {

	@Override
	public double getProduction() throws Exception;

	@Override
	public double getIntensity() throws Exception;
}
