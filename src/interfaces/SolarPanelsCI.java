package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * 
 * SolarPanels component interface
 * 
 * @author Bello Memmi
 *
 */
public interface SolarPanelsCI extends SolarPanelsImplementationI, OfferedCI, RequiredCI {

	/**
	 * @see interfaces.SolarPanelsImplementationI#getCurrentEnergyProduction()
	 */
	@Override
	public float getCurrentEnergyProduction() throws Exception;
}
