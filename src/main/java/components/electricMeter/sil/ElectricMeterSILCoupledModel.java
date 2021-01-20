package main.java.components.electricMeter.sil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.hioa.models.vars.StaticVariableDescriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.CoupledModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

public class ElectricMeterSILCoupledModel extends CoupledModel {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	public static final String URI = ElectricMeterSILCoupledModel.class.getSimpleName();

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create the coupled model.
	 * 
	 * @param uri               URI of the coupled model to be created.
	 * @param simulatedTimeUnit time unit used in the simulation by the model.
	 * @param simulationEngine  simulation engine enacting the model.
	 * @param submodels         array of submodels of the new coupled model.
	 * @param imported          map from imported event types to submodels consuming
	 *                          them.
	 * @param reexported        map from event types exported by submodels that are
	 *                          reexported by this coupled model.
	 * @param connections       map connecting event sources to arrays of event
	 *                          sinks among submodels.
	 * @param importedVars      variables imported by the coupled model that are
	 *                          consumed by submodels.
	 * @param reexportedVars    variables exported by submodels that are reexported
	 *                          by the coupled model.
	 * @param bindings          bindings between exported and imported variables
	 *                          among submodels.
	 * @throws Exception <i>to do</i>.
	 */
	public ElectricMeterSILCoupledModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine,
			ModelDescriptionI[] submodels, Map<Class<? extends EventI>, EventSink[]> imported,
			Map<Class<? extends EventI>, ReexportedEvent> reexported, Map<EventSource, EventSink[]> connections,
			Map<StaticVariableDescriptor, VariableSink[]> importedVars,
			Map<VariableSource, StaticVariableDescriptor> reexportedVars, Map<VariableSource, VariableSink[]> bindings)
			throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine, submodels, imported, reexported, connections, importedVars,
				reexportedVars, bindings);
	}
}
// -----------------------------------------------------------------------------
