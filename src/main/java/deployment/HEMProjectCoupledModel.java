package main.java.deployment;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.CoupledModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

/**
 * 
 * The class <code>HEMProjectCoupledModel</code> defines the coupled model to be
 * used to compose at the highest level the simulation models in the HEM
 * example.
 * 
 * @author Bello Memmi
 */
public class HEMProjectCoupledModel extends CoupledModel {

	private static final long serialVersionUID = 1L;
	/** URI of the model, works for singleton. */
	protected static final String URI = HEMProjectCoupledModel.class.getSimpleName();

	public HEMProjectCoupledModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine,
			ModelDescriptionI[] submodels, Map<Class<? extends EventI>, EventSink[]> imported,
			Map<Class<? extends EventI>, ReexportedEvent> reexported, Map<EventSource, EventSink[]> connections)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine, submodels, imported, reexported, connections);
	}
}
