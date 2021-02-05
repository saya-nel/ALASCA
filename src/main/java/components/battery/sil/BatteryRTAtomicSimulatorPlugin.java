package main.java.components.battery.sil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor;
import main.java.deployment.RunSILSimulation;

public class BatteryRTAtomicSimulatorPlugin extends RTAtomicSimulatorPlugin {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	protected boolean isUnitTest;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public BatteryRTAtomicSimulatorPlugin() {
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * initialise the local simulation architecture for the hair dryer component.
	 *
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 *
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param isUnitTest true if the component is under unit test.
	 * @throws Exception <i>to do</i>.
	 */
	public void initialiseSimulationArchitecture(boolean isUnitTest) throws Exception {
		this.isUnitTest = isUnitTest;

		Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();
		Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

		atomicModelDescriptors.put(BatteryStateSILModel.URI,
				RTAtomicModelDescriptor.create(BatteryStateSILModel.class, BatteryStateSILModel.URI, TimeUnit.SECONDS,
						null, SimulationEngineCreationMode.ATOMIC_RT_ENGINE, RunSILSimulation.ACC_FACTOR));
		if (isUnitTest) {
			atomicModelDescriptors.put(BatteryElectricalSILModel.URI,
					RTAtomicHIOA_Descriptor.create(BatteryElectricalSILModel.class, BatteryElectricalSILModel.URI,
							TimeUnit.SECONDS, null, SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
							RunSILSimulation.ACC_FACTOR));
		}

		this.setSimulationArchitecture(new RTArchitecture(RunSILSimulation.SIM_ARCHITECTURE_URI,
				BatteryStateSILModel.URI, atomicModelDescriptors, coupledModelDescriptors, TimeUnit.SECONDS,
				RunSILSimulation.ACC_FACTOR));

	}

}
