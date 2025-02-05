package main.java.deployment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.plugins.devs.RTCoordinatorPlugin;
import fr.sorbonne_u.components.cyphy.plugins.devs.SupervisorPlugin;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.ComponentModelArchitecture;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.RTComponentAtomicModelDescriptor;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.RTComponentCoupledModelDescriptor;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.RTComponentModelArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import main.java.components.battery.Battery;
import main.java.components.battery.sil.BatteryStateSILModel;
import main.java.components.battery.sil.events.SetDraining;
import main.java.components.battery.sil.events.SetRecharging;
import main.java.components.battery.sil.events.SetSleeping;
import main.java.components.electricMeter.ElectricMeter;
import main.java.components.electricMeter.sil.ElectricMeterSILCoupledModel;
import main.java.components.fan.Fan;
import main.java.components.fan.sil.FanSILCoupledModel;
import main.java.components.fan.sil.events.SetHigh;
import main.java.components.fan.sil.events.SetLow;
import main.java.components.fan.sil.events.SetMid;
import main.java.components.fan.sil.events.TurnOff;
import main.java.components.fan.sil.events.TurnOn;
import main.java.components.fridge.Fridge;
import main.java.components.fridge.sil.FridgeTemperatureSILModel;
import main.java.components.fridge.sil.events.Activate;
import main.java.components.fridge.sil.events.Passivate;
import main.java.components.fridge.sil.events.SetNormal;
import main.java.components.petrolGenerator.PetrolGenerator;
import main.java.components.petrolGenerator.sil.PetrolGeneratorSILCoupledModel;
import main.java.components.solarPanels.SolarPanels;
import main.java.components.solarPanels.sil.SolarPanelsStateSILModel;
import main.java.components.washer.Washer;
import main.java.components.washer.sil.WasherSILCoupledModel;
import main.java.components.washer.sil.events.SetEco;
import main.java.components.washer.sil.events.SetPerformance;
import main.java.components.washer.sil.events.SetStd;

/**
 * The class <code>HEMSimulationSupervisor</code> defines the component used in
 * the HEM example to execute the simulation supervisor.
 * 
 * @author Bello Memmi
 */
public class HEMSimulationSupervisor extends AbstractCyPhyComponent {

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/**
	 * the sole simulation architecture has this URI, but several can be created in
	 * general.
	 */
	protected static final String SIMULATION_ARCHITECTURE_URI = "sim-arch-uri";
	/** URI of the supervisor plug-in. */
	protected static final String SUPERVISOR_PLUGIN_URI = "supervisor-uri";
	/** the supervisor plug-in attached to this component. */
	protected SupervisorPlugin sp;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create the supervisor component
	 */
	protected HEMSimulationSupervisor() {
		super(1, 0);
		this.initialise();
	}

	/**
	 * create the supervisor component.
	 * 
	 * @param reflectionInboundPortURI URI of the reflection inbound port.
	 */
	protected HEMSimulationSupervisor(String reflectionInboundPortURI) {
		super(reflectionInboundPortURI, 1, 0);
		this.initialise();
	}

	/**
	 * initialise the supervisor component.
	 *
	 */
	protected void initialise() {
		try {
			this.sp = new SupervisorPlugin(this.createArchitecture());
			this.sp.setPluginURI(SUPERVISOR_PLUGIN_URI);
			this.installPlugin(this.sp);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * create the simulation architecture over the components.
	 *
	 * @return the component simulation architecture for the example.
	 */
	@SuppressWarnings("unchecked")
	protected ComponentModelArchitecture createArchitecture() throws Exception {
		Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors = new HashMap<>();

		atomicModelDescriptors.put(ElectricMeterSILCoupledModel.URI, // coupled model seen as atomic
				RTComponentAtomicModelDescriptor.create(ElectricMeterSILCoupledModel.URI,
						new Class[] { TurnOn.class, TurnOff.class, SetLow.class, SetMid.class, SetHigh.class,
								main.java.components.petrolGenerator.sil.events.TurnOn.class,
								main.java.components.petrolGenerator.sil.events.TurnOff.class,
								main.java.components.washer.sil.events.TurnOn.class,
								main.java.components.washer.sil.events.TurnOff.class, SetEco.class, SetStd.class,
								SetPerformance.class, main.java.components.fridge.sil.events.SetEco.class,
								SetNormal.class, Activate.class, Passivate.class },
						new Class[] {}, TimeUnit.SECONDS, ElectricMeter.REFLECTION_INBOUND_PORT_URI));

		atomicModelDescriptors.put(FanSILCoupledModel.URI, // coupled model seen as atomic
				RTComponentAtomicModelDescriptor.create(FanSILCoupledModel.URI, new Class[] {},
						new Class[] { TurnOn.class, TurnOff.class, SetLow.class, SetMid.class, SetHigh.class },
						TimeUnit.SECONDS, Fan.REFLECTION_INBOUND_PORT_URI));

		atomicModelDescriptors.put(SolarPanelsStateSILModel.URI,
				RTComponentAtomicModelDescriptor.create(SolarPanelsStateSILModel.URI, new Class[] {},
						new Class[] { main.java.components.solarPanels.sil.events.TurnOn.class,
								main.java.components.solarPanels.sil.events.TurnOff.class },
						TimeUnit.SECONDS, SolarPanels.REFLECTION_INBOUND_PORT_URI));

		atomicModelDescriptors.put(PetrolGeneratorSILCoupledModel.URI, // coupled model seen as atomic
				RTComponentAtomicModelDescriptor.create(PetrolGeneratorSILCoupledModel.URI, new Class[] {},
						new Class[] { main.java.components.petrolGenerator.sil.events.TurnOn.class,
								main.java.components.petrolGenerator.sil.events.TurnOff.class },
						TimeUnit.SECONDS, PetrolGenerator.REFLECTION_INBOUND_PORT_URI));

		atomicModelDescriptors.put(BatteryStateSILModel.URI,
				RTComponentAtomicModelDescriptor.create(BatteryStateSILModel.URI,
						new Class[] { SetDraining.class, SetSleeping.class, SetRecharging.class },
						new Class[] { SetDraining.class, SetSleeping.class, SetRecharging.class }, TimeUnit.SECONDS,
						Battery.REFLECTION_INBOUND_PORT_URI));

		atomicModelDescriptors.put(WasherSILCoupledModel.URI, // coupled model seen as atomic
				RTComponentAtomicModelDescriptor.create(WasherSILCoupledModel.URI, new Class[] {},
						new Class[] { main.java.components.washer.sil.events.TurnOn.class,
								main.java.components.washer.sil.events.TurnOff.class, SetEco.class, SetStd.class,
								SetPerformance.class },
						TimeUnit.SECONDS, Washer.REFLECTION_INBOUND_PORT_URI));
		atomicModelDescriptors.put(FridgeTemperatureSILModel.URI,
				RTComponentAtomicModelDescriptor.create(FridgeTemperatureSILModel.URI,
						new Class[] { main.java.components.fridge.sil.events.SetEco.class, SetNormal.class,
								Passivate.class, Activate.class },
						new Class[] { main.java.components.fridge.sil.events.SetEco.class, SetNormal.class,
								Passivate.class, Activate.class },
						TimeUnit.SECONDS, Fridge.REFLECTION_INBOUND_PORT_URI));

		Map<String, CoupledModelDescriptor> coupledModelDescriptors = new HashMap<>();

		Set<String> submodels = new HashSet<String>();
		submodels.add(ElectricMeterSILCoupledModel.URI);
		submodels.add(FanSILCoupledModel.URI);
		submodels.add(SolarPanelsStateSILModel.URI);
		submodels.add(PetrolGeneratorSILCoupledModel.URI);
		submodels.add(BatteryStateSILModel.URI);
		submodels.add(WasherSILCoupledModel.URI);
		submodels.add(FridgeTemperatureSILModel.URI);

		Map<EventSource, EventSink[]> connections = new HashMap<EventSource, EventSink[]>();

		// Fan -> ElectricMeterSIL
		connections.put(new EventSource(FanSILCoupledModel.URI, TurnOn.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, TurnOn.class) });
		connections.put(new EventSource(FanSILCoupledModel.URI, TurnOff.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, TurnOff.class) });
		connections.put(new EventSource(FanSILCoupledModel.URI, SetHigh.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, SetHigh.class) });
		connections.put(new EventSource(FanSILCoupledModel.URI, SetMid.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, SetMid.class) });
		connections.put(new EventSource(FanSILCoupledModel.URI, SetLow.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, SetLow.class) });
		// SolarPanels -> ElectricMeterSIL
		connections.put(
				new EventSource(SolarPanelsStateSILModel.URI, main.java.components.solarPanels.sil.events.TurnOn.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI,
						main.java.components.solarPanels.sil.events.TurnOn.class) });
		connections.put(
				new EventSource(SolarPanelsStateSILModel.URI,
						main.java.components.solarPanels.sil.events.TurnOff.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI,
						main.java.components.solarPanels.sil.events.TurnOff.class) });
		// PetrolGenerator -> ElectricMeterSIL
		connections.put(
				new EventSource(PetrolGeneratorSILCoupledModel.URI,
						main.java.components.petrolGenerator.sil.events.TurnOn.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI,
						main.java.components.petrolGenerator.sil.events.TurnOn.class) });
		connections.put(
				new EventSource(PetrolGeneratorSILCoupledModel.URI,
						main.java.components.petrolGenerator.sil.events.TurnOff.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI,
						main.java.components.petrolGenerator.sil.events.TurnOff.class) });
		// Battery -> ElectricMeterSIL
		connections.put(new EventSource(BatteryStateSILModel.URI, SetDraining.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, SetDraining.class) });
		connections.put(new EventSource(BatteryStateSILModel.URI, SetSleeping.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, SetSleeping.class) });
		connections.put(new EventSource(BatteryStateSILModel.URI, SetRecharging.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, SetRecharging.class) });
		// Washer -> ElectricMeterSIL
		connections.put(new EventSource(WasherSILCoupledModel.URI, main.java.components.washer.sil.events.TurnOn.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI,
						main.java.components.washer.sil.events.TurnOn.class) });
		connections.put(
				new EventSource(WasherSILCoupledModel.URI, main.java.components.washer.sil.events.TurnOff.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI,
						main.java.components.washer.sil.events.TurnOff.class) });
		connections.put(new EventSource(WasherSILCoupledModel.URI, SetEco.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, SetEco.class) });
		connections.put(new EventSource(WasherSILCoupledModel.URI, SetStd.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, SetStd.class) });
		connections.put(new EventSource(WasherSILCoupledModel.URI, SetPerformance.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, SetPerformance.class) });
		// fridge -> ElectricMeterSIL
		connections.put(
				new EventSource(FridgeTemperatureSILModel.URI, main.java.components.fridge.sil.events.SetEco.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI,
						main.java.components.fridge.sil.events.SetEco.class) });
		connections.put(new EventSource(FridgeTemperatureSILModel.URI, SetNormal.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, SetNormal.class) });
		connections.put(new EventSource(FridgeTemperatureSILModel.URI, Activate.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, Activate.class) });
		connections.put(new EventSource(FridgeTemperatureSILModel.URI, Passivate.class),
				new EventSink[] { new EventSink(ElectricMeterSILCoupledModel.URI, Passivate.class) });

		coupledModelDescriptors.put(HEMProjectCoupledModel.URI,
				RTComponentCoupledModelDescriptor.create(HEMProjectCoupledModel.class, HEMProjectCoupledModel.URI,
						submodels, null, null, connections, null, SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
						null, HEMSimulationCoordinator.REFLECTION_INBOUND_PORT_URI, RTCoordinatorPlugin.class, null));

		RTComponentModelArchitecture arch = new RTComponentModelArchitecture(SIMULATION_ARCHITECTURE_URI,
				HEMProjectCoupledModel.URI, atomicModelDescriptors, coupledModelDescriptors, TimeUnit.SECONDS);

		return arch;
	}

	// -------------------------------------------------------------------------
	// Component life-cycle.
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void execute() throws Exception {
		super.execute();

		this.sp.createSimulator();
		this.sp.setSimulationRunParameters(new HashMap<String, Object>());
		long realTimeOfStart = System.currentTimeMillis() + RunSILSimulation.DELAY_TO_START_SIMULATION;
		this.sp.startRTSimulation(realTimeOfStart, 0.0, RunSILSimulation.SIMULATION_DURATION);
	}
}
// -----------------------------------------------------------------------------
