package main.java.components.petrolGenerator.sil;

import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTCoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;
import main.java.components.petrolGenerator.sil.events.EmptyGenerator;
import main.java.components.petrolGenerator.sil.events.FillAll;
import main.java.components.petrolGenerator.sil.events.TurnOff;
import main.java.components.petrolGenerator.sil.events.TurnOn;
import main.java.deployment.RunSILSimulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class PetrolGeneratorRTAtomicSimulatorPlugin
extends RTAtomicSimulatorPlugin {
    // -------------------------------------------------------------------------
    // Constants and variables
    // -------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;
    protected boolean	isUnitTest;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public				PetrolGeneratorRTAtomicSimulatorPlugin()
    {
    }

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------

    /**
     * .
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	{@code !simParams.containsKey(PetrolGeneratorUserSILModel.HAIR_DRYER_REFERENCE_NAME)}
     * post	true		// no postconditions.
     * </pre>
     *
     * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#setSimulationRunParameters(java.util.Map)
     */
    @Override
    public void			setSimulationRunParameters(
            Map<String, Object> simParams
    ) throws Exception
    {
        // initialise the simulation parameter giving the reference to the
        // owner component before passing the parameters to the simulation
        // models
        simParams.put(PetrolGeneratorUserSILModel.PETROL_GENERATOR_REFERENCE_NAME,
                this.getOwner());

        // this will pass the parameters to the simulation models that will
        // then be able to get their own parameters.
        super.setSimulationRunParameters(simParams);
    }

    /**
     * initialise the local simulation architecture for the hair dryer
     * component.
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     * @param isUnitTest	true if the component is under unit test.
     * @throws Exception	<i>to do</i>.
     */
    public void			initialiseSimulationArchitecture(
            boolean isUnitTest
    ) throws Exception
    {
        this.isUnitTest = isUnitTest;

        Map<String, AbstractAtomicModelDescriptor>
                atomicModelDescriptors = new HashMap<>();
        Map<String, CoupledModelDescriptor>
                coupledModelDescriptors = new HashMap<>();

        Set<String> submodels = new HashSet<String>();
        submodels.add(PetrolGeneratorStateSILModel.URI);
        submodels.add(PetrolGeneratorUserSILModel.URI);
        if (isUnitTest) {
            submodels.add(PetrolGeneratorElectricitySILModel.URI);
        }

        atomicModelDescriptors.put(
                PetrolGeneratorStateSILModel.URI,
                RTAtomicModelDescriptor.create(
                        PetrolGeneratorStateSILModel.class,
                        PetrolGeneratorStateSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        RunSILSimulation.ACC_FACTOR));
        atomicModelDescriptors.put(
                PetrolGeneratorUserSILModel.URI,
                RTAtomicModelDescriptor.create(
                        PetrolGeneratorUserSILModel.class,
                        PetrolGeneratorUserSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        RunSILSimulation.ACC_FACTOR));
        if (isUnitTest) {
            atomicModelDescriptors.put(
                    PetrolGeneratorElectricitySILModel.URI,
                    RTAtomicHIOA_Descriptor.create(
                            PetrolGeneratorElectricitySILModel.class,
                            PetrolGeneratorElectricitySILModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                            RunSILSimulation.ACC_FACTOR));
        }

        Map<Class<? extends EventI>, ReexportedEvent> reexported = null;
        Map<EventSource, EventSink[]> connections = null;
        if (isUnitTest) {
            connections = new HashMap<EventSource, EventSink[]>();
            EventSource source =
                    new EventSource(PetrolGeneratorStateSILModel.URI, TurnOn.class);
            EventSink[] sinks =
                    new EventSink[] {
                            new EventSink(PetrolGeneratorElectricitySILModel.URI,
                                    TurnOn.class)
                    };
            connections.put(source, sinks);
            source =
                    new EventSource(PetrolGeneratorStateSILModel.URI, TurnOff.class);
            sinks = new EventSink[] {
                    new EventSink(PetrolGeneratorElectricitySILModel.URI,
                            TurnOff.class)
            };
            connections.put(source, sinks);
            source =
                    new EventSource(PetrolGeneratorStateSILModel.URI, FillAll.class);
            sinks = new EventSink[] {
                    new EventSink(PetrolGeneratorElectricitySILModel.URI,
                            FillAll.class)
            };
            connections.put(source, sinks);
            source =
                    new EventSource(PetrolGeneratorStateSILModel.URI, EmptyGenerator.class);
            sinks = new EventSink[] {
                    new EventSink(PetrolGeneratorElectricitySILModel.URI,
                            EmptyGenerator.class)
            };
            connections.put(source, sinks);
        } else {
            reexported =
                    new HashMap<Class<? extends EventI>,ReexportedEvent>();
            reexported.put(TurnOn.class,
                    new ReexportedEvent(PetrolGeneratorStateSILModel.URI,
                            TurnOn.class));
            reexported.put(TurnOff.class,
                    new ReexportedEvent(PetrolGeneratorStateSILModel.URI,
                            TurnOff.class));
            reexported.put(FillAll.class,
                    new ReexportedEvent(PetrolGeneratorStateSILModel.URI,
                            FillAll.class));
            reexported.put(EmptyGenerator.class,
                    new ReexportedEvent(PetrolGeneratorStateSILModel.URI,
                            EmptyGenerator.class));
        }

        coupledModelDescriptors.put(
                PetrolGeneratorSILCoupledModel.URI,
                new RTCoupledModelDescriptor(
                        PetrolGeneratorSILCoupledModel.class,
                        PetrolGeneratorSILCoupledModel.URI,
                        submodels,
                        null,
                        reexported,
                        connections,
                        null,
                        SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
                        RunSILSimulation.ACC_FACTOR));

        this.setSimulationArchitecture(
                new RTArchitecture(
                        RunSILSimulation.SIM_ARCHITECTURE_URI,
                        PetrolGeneratorSILCoupledModel.URI,
                        atomicModelDescriptors,
                        coupledModelDescriptors,
                        TimeUnit.SECONDS,
                        RunSILSimulation.ACC_FACTOR));
    }
}
