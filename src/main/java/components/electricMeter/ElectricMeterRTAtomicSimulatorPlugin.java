package main.java.components.electricMeter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTCoupledHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSink;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.VariableSource;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import main.java.components.electricMeter.sil.ElectricMeterSILCoupledModel;
import main.java.components.electricMeter.sil.ElectricMeterSILModel;
import main.java.components.fan.sil.FanElectricalSILModel;
import main.java.components.fan.sil.FanUserSILModel;
import main.java.components.fan.sil.events.SetHigh;
import main.java.components.fan.sil.events.SetLow;
import main.java.components.fan.sil.events.SetMid;
import main.java.components.fan.sil.events.TurnOff;
import main.java.components.fan.sil.events.TurnOn;
import main.java.deployment.RunSILSimulation;

/**
 * 
 * @author Bello Memmi
 *
 */
public class ElectricMeterRTAtomicSimulatorPlugin extends RTAtomicSimulatorPlugin {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	/**
	 * name of the intensity variable used in the protocol allowing the owner
	 * component to access a value in the simulation models.
	 */
	public static final String INTENSITY_VARIABLE_NAME = "intensity";

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create the plug-in.
	 */
	public ElectricMeterRTAtomicSimulatorPlugin() {
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		// initialise the simulation parameters giving the references to the
		// owner component before passing the parameters to the simulation
		// models
		simParams.put(ElectricMeterSILModel.ELECTRIC_METER_REFERENCE_NAME, this.getOwner());
		// for the next two, a different name is used to be able to use the
		// models in two contexts: integration tests and unit through SIL
		// simulations
		simParams.put(FanUserSILModel.FAN_REFERENCE_NAME, this.getOwner());

		// this will pass the parameters to the simulation models that will then
		// be able to get their own parameters.
		super.setSimulationRunParameters(simParams);
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin#getModelStateValue(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Object getModelStateValue(String modelURI, String name) throws Exception {
		assert modelURI != null && name != null;

		// Get a Java reference on the object representing the corresponding
		// simulation model.
		ModelDescriptionI m = this.simulator.getDescendentModel(modelURI);
		// The only model in this example that provides access to some value
		// is the ElectricMeterSILModel.
		assert m instanceof ElectricMeterSILModel;
		// The only variable that can be accessed is the intensity.
		assert name.equals(INTENSITY_VARIABLE_NAME);

		return ((ElectricMeterSILModel) m).getIntensity();
	}

	/**
	 * initialise the local simulation architecture for the electric meter
	 * component.
	 */
	public void initialiseSimulationArchitecture() throws Exception {
		Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();
		Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

		Set<String> submodels = new HashSet<String>();
		submodels.add(ElectricMeterSILModel.URI);
		submodels.add(FanElectricalSILModel.URI);

		atomicModelDescriptors.put(ElectricMeterSILModel.URI,
				RTAtomicHIOA_Descriptor.create(ElectricMeterSILModel.class, ElectricMeterSILModel.URI, TimeUnit.SECONDS,
						null, SimulationEngineCreationMode.ATOMIC_RT_ENGINE, RunSILSimulation.ACC_FACTOR));
		atomicModelDescriptors.put(FanElectricalSILModel.URI,
				RTAtomicHIOA_Descriptor.create(FanElectricalSILModel.class, FanElectricalSILModel.URI, TimeUnit.SECONDS,
						null, SimulationEngineCreationMode.ATOMIC_RT_ENGINE, RunSILSimulation.ACC_FACTOR));

		Map<Class<? extends EventI>, EventSink[]> imported = new HashMap<Class<? extends EventI>, EventSink[]>();
		imported.put(TurnOn.class, new EventSink[] { new EventSink(FanElectricalSILModel.URI, TurnOn.class) });
		imported.put(TurnOff.class, new EventSink[] { new EventSink(FanElectricalSILModel.URI, TurnOff.class) });
		imported.put(SetLow.class, new EventSink[] { new EventSink(FanElectricalSILModel.URI, SetLow.class) });
		imported.put(SetMid.class, new EventSink[] { new EventSink(FanElectricalSILModel.URI, SetMid.class) });
		imported.put(SetHigh.class, new EventSink[] { new EventSink(FanElectricalSILModel.URI, SetHigh.class) });

		Map<VariableSource, VariableSink[]> bindings = new HashMap<VariableSource, VariableSink[]>();
		VariableSource source = new VariableSource("currentIntensity", Double.class, FanElectricalSILModel.URI);
		VariableSink[] sinks = new VariableSink[] {
				new VariableSink("FanIntensity", Double.class, ElectricMeterSILModel.URI) };
		bindings.put(source, sinks);

		coupledModelDescriptors.put(ElectricMeterSILCoupledModel.URI,
				new RTCoupledHIOA_Descriptor(ElectricMeterSILCoupledModel.class, ElectricMeterSILCoupledModel.URI,
						submodels, imported, null, // reeexported
						null, // connections
						null, // factory
						SimulationEngineCreationMode.COORDINATION_RT_ENGINE, //
						null, // imported vars
						null, // reexported vars
						bindings, RunSILSimulation.ACC_FACTOR));

		this.setSimulationArchitecture(new RTArchitecture(RunSILSimulation.SIM_ARCHITECTURE_URI,
				ElectricMeterSILCoupledModel.URI, atomicModelDescriptors, coupledModelDescriptors, TimeUnit.SECONDS,
				RunSILSimulation.ACC_FACTOR));
	}
}
// -----------------------------------------------------------------------------
