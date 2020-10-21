package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.SolarPanelsCI;

/**
 * Connector for the SolarPanelsCI component interface
 * 
 * @author Bello Memmi
 *
 */
public class SolarPanelsConnector extends AbstractConnector implements SolarPanelsCI {

    @Override
    public float getCurrentEnergyProduction() throws Exception {
        return ((SolarPanelsCI) this.offering).getCurrentEnergyProduction();
    }
}
