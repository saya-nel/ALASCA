package fr.sorbonne_u.components.cyphy.plugins.devs;

import java.util.ArrayList;

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

import java.util.Set;

import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.ComponentRTScheduler;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import fr.sorbonne_u.devs_simulation.models.architectures.AtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.RTAtomicModelDescriptor.RTSchedulerProviderFI;
import fr.sorbonne_u.devs_simulation.models.architectures.RTCoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.RTAtomicSimulatorI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSchedulingI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSimulatorI;

// -----------------------------------------------------------------------------
/**
 * The class <code>RTAtomicSimulatorPlugin</code> extends an atomic simulator
 * plug-in with the methods required by a real time atomic simulator plug-in.
 *
 * <p>
 * <strong>Description</strong>
 * </p>
 * 
 * <p>
 * <strong>Invariant</strong>
 * </p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>
 * Created on : 2020-12-15
 * </p>
 * 
 * @author <a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class RTAtomicSimulatorPlugin extends AtomicSimulatorPlugin implements RTAtomicSimulatorPluginI {
	// -------------------------------------------------------------------------
	// Plug-in internal constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	protected String simulationExecutorServiceURI;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	/**
	 * create a real time atomic simulator plug-in.
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
	 */
	public RTAtomicSimulatorPlugin() {
		super();
	}

	// -------------------------------------------------------------------------
	// Plug-in specific methods
	// -------------------------------------------------------------------------

	/**
	 * .
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code archi.isRealTime()}
	 * post	true		// no postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin#setSimulationArchitecture(fr.sorbonne_u.devs_simulation.architectures.Architecture)
	 */
	@Override
	public void setSimulationArchitecture(Architecture archi) {
		assert archi.isRealTime();
		super.setSimulationArchitecture(archi);
	}

	/**
	 * return true if the simulation executor service URI has been set.
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
	 * @return true if the simulation executor service URI has been set.
	 */
	public boolean isSimulationExecutorServiceSet() {
		return this.simulationExecutorServiceURI != null;
	}

	/**
	 * set the URI of the scheduled executor service to be used to execute real time
	 * simulation tasks for the simulation engine attached to this plug-in.
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code uri != null}
	 * post	{@code isSimulationExecutorServiceSet()}
	 * </pre>
	 *
	 * @param uri URI of the scheduled executor service to be used to execute real
	 *            time simulation tasks.
	 */
	public void setSimulationExecutorService(String uri) {
		assert uri != null;

		this.simulationExecutorServiceURI = uri;
	}

	/**
	 * create the real time scheduler provider that will be used as a factory to
	 * create the real time scheduler to be used by the simulation engine to execute
	 * its real time tasks.
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code isSimulationExecutorServiceSet()}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @return the real time scheduler provider that will be used as a factory to
	 *         create a real time scheduler.
	 */
	protected RTSchedulerProviderFI createRTSchedulerProvider() {
		assert this.isSimulationExecutorServiceSet();
		assert this.getOwner().validExecutorServiceURI(this.simulationExecutorServiceURI);
		assert this.getOwner().isSchedulable(this.simulationExecutorServiceURI);

		final AbstractCyPhyComponent comp = (AbstractCyPhyComponent) this.getOwner();
		final String uri = this.simulationExecutorServiceURI;
		return new RTSchedulerProviderFI() {
			private static final long serialVersionUID = 1L;

			@Override
			public RTSchedulingI provide() {
				return new ComponentRTScheduler(comp, uri);
			}
		};
	}

	/**
	 * .
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code isSimulationArchitectureSet()}
	 * pre	{@code isSimulationExecutorServiceSet()}
	 * post	{@code isSimulatorSet()}
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AtomicSimulatorPlugin#constructSimulator()
	 */
	@Override
	protected void constructSimulator() throws Exception {
		assert this.isSimulationExecutorServiceSet();

		RTSchedulerProviderFI schedulerProvider = this.createRTSchedulerProvider();
		Set<String> modelURIs = this.localArchitecture.getAllModelURIs();
		for (String uri : modelURIs) {
			if (this.localArchitecture.isAtomicModel(uri)) {
				AtomicModelDescriptor amd = (AtomicModelDescriptor) this.localArchitecture.getModelDescriptor(uri);
				if (amd instanceof RTAtomicModelDescriptor) {
					((RTAtomicModelDescriptor) amd).setSchedulerProvider(schedulerProvider);
				}
			} else {
				assert this.localArchitecture.isCoupledModel(uri);
				CoupledModelDescriptor cmd = (CoupledModelDescriptor) this.localArchitecture.getModelDescriptor(uri);
				if (cmd instanceof RTCoupledModelDescriptor) {
					if (((RTCoupledModelDescriptor) cmd).hasAtomicEngine()) {
						((RTCoupledModelDescriptor) cmd).setSchedulerProvider(schedulerProvider);
					}
				}
			}
		}
		super.constructSimulator();
	}

	/**
	 * .
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code isSimulatorSet()}
	 * post	true		// no postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSimulatorI#startRTSimulation(long,
	 *      double, double)
	 */
	@Override
	public void startRTSimulation(long realTimeOfStart, double simulationStartTime, double simulationDuration)
			throws Exception {
		assert this.isSimulatorSet();

		((RTSimulatorI) this.simulator).startRTSimulation(realTimeOfStart, simulationStartTime, simulationDuration);
	}

	/**
	 * .
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code isSimulatorSet()}
	 * post	true		// no postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTAtomicSimulatorI#isRTSchedulerSet()
	 */
	@Override
	public boolean isRTSchedulerSet() {
		assert this.isSimulatorSet();

		return ((RTAtomicSimulatorI) this.simulator).isRTSchedulerSet();
	}

	/**
	 * .
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code isSimulatorSet()}
	 * post	true		// no postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTAtomicSimulatorI#setRTScheduler(fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSchedulingI)
	 */
	@Override
	public void setRTScheduler(RTSchedulingI scheduler) {
		assert this.isSimulatorSet();

		((RTAtomicSimulatorI) this.simulator).setRTScheduler(scheduler);
	}

	/**
	 * .
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	{@code isSimulatorSet()}
	 * post	true		// no postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTAtomicSimulatorI#getRTScheduler()
	 */
	@Override
	public RTSchedulingI getRTScheduler() {
		assert this.isSimulatorSet();

		return ((RTAtomicSimulatorI) this.simulator).getRTScheduler();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSimulatorI#computeCurrentSimulationTime(java.lang.String)
	 */
	@Override
	public Time computeCurrentSimulationTime(String rtAtomicModelURI) throws Exception {
		// TODO not needed, refactoring?
		return null;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPluginI#triggerExternalEvent(java.lang.String,
	 *      fr.sorbonne_u.components.cyphy.plugins.devs.RTAtomicSimulatorPluginI.EventFactoryFI)
	 */
	@Override
	public void triggerExternalEvent(String destinationModelURI, EventFactoryFI ef) throws Exception {
		assert destinationModelURI != null && !destinationModelURI.isEmpty();
		assert ef != null;
		Time t = ((RTSimulatorI) this.simulator).computeCurrentSimulationTime(destinationModelURI);
		ArrayList<EventI> es = new ArrayList<>();
		es.add(ef.createEvent(t));
		this.storeInput(destinationModelURI, es);
	}
}
// -----------------------------------------------------------------------------
