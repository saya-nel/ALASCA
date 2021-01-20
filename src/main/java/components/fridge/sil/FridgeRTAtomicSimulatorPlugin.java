package main.java.components.fridge.sil;

import fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPlugin;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.architectures.SimulationEngineCreationMode;
import fr.sorbonne_u.devs_simulation.hioa.architectures.RTAtomicHIOA_Descriptor;
import fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTCoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.events.EventSink;
import fr.sorbonne_u.devs_simulation.models.events.EventSource;
import fr.sorbonne_u.devs_simulation.models.events.ReexportedEvent;
import main.java.components.fridge.sil.events.SetEco;
import main.java.components.fridge.sil.events.SetNormal;
import main.java.components.fridge.sil.events.SetRequestedTemperature;
import main.java.components.fridge.sil.models.*;
import main.java.deployment.RunSILSimulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * The class <code>FridgeRTAtomicSimulatorPlugin</code> implements the atomic
 * simulator plug-in for the fridge component.
 * @author Bello Memmi
 */
public class FridgeRTAtomicSimulatorPlugin
extends RTAtomicSimulatorPlugin {
    // -------------------------------------------------------------------------
    // Constants and variables
    // -------------------------------------------------------------------------
    /** name of the variable to be used in the protocol to access data in
     *  a simulation model from its owner component.						*/
    public static final String CURRENT_TEMPERATURE_FRIDGE_NAME="contentTemperature";

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
    public FridgeRTAtomicSimulatorPlugin(){}

    // -------------------------------------------------------------------------
    // Methods
    // -------------------------------------------------------------------------


    /**
     * initialise the local simulation architecture for the Fridge.
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true		// no precondition.
     * post	true		// no postcondition.
     * </pre>
     *
     * @throws Exception	<i>to do</I>.
     */
    public void			initialiseSimulationArchitecture(boolean isUnitTest) throws Exception
    {
        Map<String, AbstractAtomicModelDescriptor>
                atomicModelDescriptors = new HashMap<>();
        Map<String, CoupledModelDescriptor>
                coupledModelDescriptors = new HashMap<>();
        Set<String> submodels = new HashSet<String>();
        submodels.add(FridgeUser_MILModel.URI);
        if (isUnitTest)
            submodels.add(FridgeElectricity_SILModel.URI);
        atomicModelDescriptors.put(
                FridgeTemperatureSILModel.URI,
                RTAtomicHIOA_Descriptor.create(
                        FridgeTemperatureSILModel.class,
                        FridgeTemperatureSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        RunSILSimulation.ACC_FACTOR));

        this.setSimulationArchitecture(
                new RTArchitecture(
                        RunSILSimulation.SIM_ARCHITECTURE_URI,
                        FridgeTemperatureSILModel.URI,
                        atomicModelDescriptors,
                        new HashMap<>(),
                        TimeUnit.SECONDS,
                        RunSILSimulation.ACC_FACTOR));
        if (isUnitTest) {
            atomicModelDescriptors.put(
                    FridgeElectricity_SILModel.URI,
                    RTAtomicHIOA_Descriptor.create(
                            FridgeElectricity_SILModel.class,
                            FridgeElectricity_SILModel.URI,
                            TimeUnit.SECONDS,
                            null,
                            SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                            RunSILSimulation.ACC_FACTOR));
        }
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
     * @throws Exception	<i>to do</i>.
     */
    public void			initialiseSimulationArchitecture() throws Exception
    {

        Map<String,AbstractAtomicModelDescriptor>
                atomicModelDescriptors = new HashMap<>();
        Map<String,CoupledModelDescriptor>
                coupledModelDescriptors = new HashMap<>();

        Set<String> submodels = new HashSet<String>();
        submodels.add(FridgeStateSILModel.URI);
        submodels.add(FridgeUser_MILModel.URI);
        atomicModelDescriptors.put(
                FridgeTemperatureSILModel.URI,
                RTAtomicHIOA_Descriptor.create(
                        FridgeTemperatureSILModel.class,
                        FridgeTemperatureSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        RunSILSimulation.ACC_FACTOR));

        this.setSimulationArchitecture(
                new RTArchitecture(
                        RunSILSimulation.SIM_ARCHITECTURE_URI,
                        FridgeTemperatureSILModel.URI,
                        atomicModelDescriptors,
                        new HashMap<>(),
                        TimeUnit.SECONDS,
                        RunSILSimulation.ACC_FACTOR));
        atomicModelDescriptors.put(
                FridgeStateSILModel.URI,
                RTAtomicModelDescriptor.create(
                        FridgeStateSILModel.class,
                        FridgeStateSILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        RunSILSimulation.ACC_FACTOR));
        atomicModelDescriptors.put(
                FridgeUser_MILModel.URI,
                RTAtomicModelDescriptor.create(
                        FridgeUser_MILModel.class,
                        FridgeUser_MILModel.URI,
                        TimeUnit.SECONDS,
                        null,
                        SimulationEngineCreationMode.ATOMIC_RT_ENGINE,
                        RunSILSimulation.ACC_FACTOR));


        Map<Class<? extends EventI>, ReexportedEvent> reexported = null;
        Map<EventSource, EventSink[]> connections = null;
            reexported =
                    new HashMap<Class<? extends EventI>,ReexportedEvent>();
            reexported.put(SetRequestedTemperature.class,
                    new ReexportedEvent(FridgeStateSILModel.URI,
                            SetRequestedTemperature.class));
            reexported.put(SetEco.class,
                    new ReexportedEvent(FridgeStateSILModel.URI,
                            SetEco.class));
            reexported.put(SetNormal.class,
                    new ReexportedEvent(FridgeStateSILModel.URI,
                            SetNormal.class));



        coupledModelDescriptors.put(
                FridgeSILCoupledModel.URI,
                new RTCoupledModelDescriptor(
                        FridgeSILCoupledModel.class,
                        FridgeSILCoupledModel.URI,
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
                        FridgeSILCoupledModel.URI,
                        atomicModelDescriptors,
                        coupledModelDescriptors,
                        TimeUnit.SECONDS,
                        RunSILSimulation.ACC_FACTOR));
    }


    /**
     * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#setSimulationRunParameters(java.util.Map)
     */
    @Override
    public void			setSimulationRunParameters(
            Map<String, Object> simParams
    ) throws Exception
    {
        assert	!simParams.containsKey(
                FridgeTemperatureSILModel.FRIDGE_REFERENCE_NAME);
        simParams.put(FridgeTemperatureSILModel.FRIDGE_REFERENCE_NAME,
                this.getOwner());
        simParams.put(FridgeUser_MILModel.FRIDGE_REFERENCE_NAME,
                this.getOwner());
        super.setSimulationRunParameters(simParams);
    }

    /**
     * @see fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin#getModelStateValue(java.lang.String, java.lang.String)
     */
    @Override
    public Object		getModelStateValue(String modelURI, String name)
            throws Exception
    {
        assert	modelURI != null && name != null;
        assert	modelURI.equals(FridgeTemperatureSILModel.URI);
        assert	name.equals(CURRENT_TEMPERATURE_FRIDGE_NAME);

        // Get a Java reference on the object representing the corresponding
        // simulation model.
        ModelDescriptionI m = this.simulator.getDescendentModel(modelURI);
        assert	m instanceof FridgeTemperatureSILModel;
        return ((FridgeTemperatureSILModel)m).getContentTemperature();
    }
}
