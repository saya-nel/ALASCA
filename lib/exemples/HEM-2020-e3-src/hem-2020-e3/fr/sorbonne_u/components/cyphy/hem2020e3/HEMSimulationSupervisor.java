package fr.sorbonne_u.components.cyphy.hem2020e3;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a
// new implementation of the DEVS simulation standard for Java.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability. 
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or 
// data to be ensured and,  more generally, to use and operate it in the 
// same conditions as regards security. 
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.Boiler;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.Activate;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.BoilerWaterSILModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.DoNotHeat;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.Heat;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.Passivate;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.HairDryer;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.HairDryerSILCoupledModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SetHigh;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SetLow;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SwitchOff;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SwitchOn;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.meter.ElectricMeter;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.meter.sil.ElectricMeterSILCoupledModel;
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

// -----------------------------------------------------------------------------
/**
 * The class <code>HEMSimulationSupervisor</code> defines the component used
 * in the HEM example to execute the simulation supervisor.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2020-12-21</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			HEMSimulationSupervisor
extends		AbstractCyPhyComponent
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** the sole simulation architecture has this URI, but several can be
	 *  created in general.													*/
	protected static final String	SIMULATION_ARCHITECTURE_URI = "sim-arch-uri";
	/** URI of the supervisor plug-in.										*/
	protected static final String	SUPERVISOR_PLUGIN_URI = "supervisor-uri";
	/** the supervisor plug-in attached to this component.					*/
	protected SupervisorPlugin		sp;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create the supervisor component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
	protected			HEMSimulationSupervisor()
	{
		super(1, 0);
		this.initialise();
	}

	/**
	 * create the supervisor component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param reflectionInboundPortURI URI of the reflection inbound port.
	 */
	protected			HEMSimulationSupervisor(
		String reflectionInboundPortURI
		)
	{
		super(reflectionInboundPortURI, 1, 0);
		this.initialise();
	}

	/**
	 * initialise the supervisor component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
	protected void		initialise()
	{
		try {
			this.sp = new SupervisorPlugin(this.createArchitecture());
			this.sp.setPluginURI(SUPERVISOR_PLUGIN_URI);
			this.installPlugin(this.sp);
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * create the simulation architecture over the components.
	 * 
	 * <p><strong>Description</strong></p>
	 * 
	 * <p>
	 * The architecture shows how models are projected onto components and
	 * how to interconnect them. Thanks to the DEVS property that models are
	 * closed under composition, even coupled models on each component can be
	 * seen at this level as atomic models.
	 * </p>
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @return				the component simulation architecture for the example.
	 * @throws Exception	<i>to do</i>.
	 */
	@SuppressWarnings("unchecked")
	protected ComponentModelArchitecture	createArchitecture()
	throws Exception
	{
		Map<String,AbstractAtomicModelDescriptor> atomicModelDescriptors =
																new HashMap<>();

		atomicModelDescriptors.put(
				ElectricMeterSILCoupledModel.URI, // coupled model seen as atomic
				RTComponentAtomicModelDescriptor.create(
						ElectricMeterSILCoupledModel.URI,
						new Class[]{SwitchOn.class, SwitchOff.class,
									SetHigh.class, SetLow.class,
									Heat.class,DoNotHeat.class,
									Passivate.class, Activate.class},
						new Class[]{},
						TimeUnit.SECONDS,
						ElectricMeter.REFLECTION_INBOUHD_PORT_URI));

		atomicModelDescriptors.put(
				HairDryerSILCoupledModel.URI, // coupled model seen as atomic
				RTComponentAtomicModelDescriptor.create(
						HairDryerSILCoupledModel.URI,
						new Class[]{},
						new Class[]{SwitchOn.class, SwitchOff.class,
									SetHigh.class, SetLow.class},
						TimeUnit.SECONDS,
						HairDryer.REFLECTION_INBOUND_PORT_URI));

		atomicModelDescriptors.put(
				BoilerWaterSILModel.URI,
				RTComponentAtomicModelDescriptor.create(
						BoilerWaterSILModel.URI,
						new Class[]{Heat.class,DoNotHeat.class,
								    Passivate.class,Activate.class},
						new Class[]{Heat.class,DoNotHeat.class,
								    Passivate.class,Activate.class},
						TimeUnit.SECONDS,
						Boiler.REFLECTION_INBOUND_PORT_URI));

		Map<String,CoupledModelDescriptor> coupledModelDescriptors =
																new HashMap<>();

		Set<String> submodels = new HashSet<String>();
		submodels.add(ElectricMeterSILCoupledModel.URI);
		submodels.add(HairDryerSILCoupledModel.URI);
		submodels.add(BoilerWaterSILModel.URI);

		Map<EventSource,EventSink[]> connections =
										new HashMap<EventSource,EventSink[]>();

		connections.put(
				new EventSource(HairDryerSILCoupledModel.URI, SwitchOn.class),
				new EventSink[] {
						new EventSink(ElectricMeterSILCoupledModel.URI,
									  SwitchOn.class)
				});
		connections.put(
				new EventSource(HairDryerSILCoupledModel.URI, SwitchOff.class),
				new EventSink[] {
						new EventSink(ElectricMeterSILCoupledModel.URI,
									  SwitchOff.class)
				});
		connections.put(
				new EventSource(HairDryerSILCoupledModel.URI, SetHigh.class),
				new EventSink[] {
						new EventSink(ElectricMeterSILCoupledModel.URI,
									  SetHigh.class)
				});
		connections.put(
				new EventSource(HairDryerSILCoupledModel.URI, SetLow.class),
				new EventSink[] {
						new EventSink(ElectricMeterSILCoupledModel.URI,
									  SetLow.class)
				});
		connections.put(
				new EventSource(BoilerWaterSILModel.URI, Heat.class),
				new EventSink[] {
						new EventSink(ElectricMeterSILCoupledModel.URI,
									  Heat.class)
				});
		connections.put(
				new EventSource(BoilerWaterSILModel.URI, DoNotHeat.class),
				new EventSink[] {
						new EventSink(ElectricMeterSILCoupledModel.URI,
									  DoNotHeat.class)
				});
		connections.put(
				new EventSource(BoilerWaterSILModel.URI, Passivate.class),
				new EventSink[] {
						new EventSink(ElectricMeterSILCoupledModel.URI,
									  Passivate.class)
				});
		connections.put(
				new EventSource(BoilerWaterSILModel.URI, Activate.class),
				new EventSink[] {
						new EventSink(ElectricMeterSILCoupledModel.URI,
									  Activate.class)
				});

		coupledModelDescriptors.put(
				HEMProjectCoupledModel.URI,
				RTComponentCoupledModelDescriptor.create(
						HEMProjectCoupledModel.class,
						HEMProjectCoupledModel.URI,
						submodels,
						null,
						null,
						connections,
						null,
						SimulationEngineCreationMode.COORDINATION_RT_ENGINE,
						null,
						HEMSimulationCoordinator.REFLECTION_INBOUND_PORT_URI,
						RTCoordinatorPlugin.class,
						null));

		RTComponentModelArchitecture arch =
				new RTComponentModelArchitecture(
						SIMULATION_ARCHITECTURE_URI,
						HEMProjectCoupledModel.URI,
						atomicModelDescriptors,
						coupledModelDescriptors,
						TimeUnit.SECONDS);

		return arch;
	}

	// -------------------------------------------------------------------------
	// Component life-cycle.
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void	execute() throws Exception
	{
		super.execute();

		this.sp.createSimulator();
		this.sp.setSimulationRunParameters(new HashMap<String, Object>());
		long realTimeOfStart = System.currentTimeMillis() +
									RunSILSimulation.DELAY_TO_START_SIMULATION;
		this.sp.startRTSimulation(realTimeOfStart, 0.0, 10.1);
	}
}
// -----------------------------------------------------------------------------
