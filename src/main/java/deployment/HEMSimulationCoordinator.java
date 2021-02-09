package main.java.deployment;

import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;

/**
 * The class <code>HEMSimulationCoordinator</code> defines the component used in
 * the HEM example to execute the simulation coordinator.
 * 
 * @author Bello Memmi
 */
public class HEMSimulationCoordinator extends AbstractCyPhyComponent {

	public static final String REFLECTION_INBOUND_PORT_URI = "coordination-ribpuri";

	protected HEMSimulationCoordinator() {
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);
	}
}
