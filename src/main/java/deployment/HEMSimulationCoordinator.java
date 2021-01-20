package main.java.deployment;

import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;

/**
 * @author Bello Memmi
 */
public class HEMSimulationCoordinator extends AbstractCyPhyComponent {

	public static final String REFLECTION_INBOUND_PORT_URI = "coordination-ribpuri";

	protected HEMSimulationCoordinator() {
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);
	}
}
