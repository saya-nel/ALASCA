package fr.sorbonne_u.components.cyphy.plugins.devs;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide an extension
// of the BCM component model that aims to define a components tailored for
// cyber-physical control systems (CPCS) for Java.
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

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.ComponentParentReference;
import fr.sorbonne_u.components.cyphy.plugins.devs.connectors.SupervisorNotificationConnector;
import fr.sorbonne_u.components.cyphy.plugins.devs.interfaces.ParentNotificationCI;
import fr.sorbonne_u.components.cyphy.plugins.devs.interfaces.SimulatorCI;
import fr.sorbonne_u.components.cyphy.plugins.devs.interfaces.SimulatorPluginManagementCI;
import fr.sorbonne_u.components.cyphy.plugins.devs.interfaces.SupervisorNotificationCI;
import fr.sorbonne_u.components.cyphy.plugins.devs.ports.ParentNotificationOutboundPort;
import fr.sorbonne_u.components.cyphy.plugins.devs.ports.SimulatorInboundPort;
import fr.sorbonne_u.components.cyphy.plugins.devs.ports.SimulatorPluginManagementInboundPort;
import fr.sorbonne_u.components.cyphy.plugins.devs.ports.SupervisorNotificationOutboundPort;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionCI;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.StaticVariableDescriptor;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.interfaces.EventsExchangingI;
import fr.sorbonne_u.devs_simulation.interfaces.ParentNotificationI;
import fr.sorbonne_u.devs_simulation.interfaces.SimulationReportI;
import fr.sorbonne_u.devs_simulation.models.ParentReferenceI;
import fr.sorbonne_u.devs_simulation.models.events.CallableEventAtomicSink;
import fr.sorbonne_u.devs_simulation.models.events.EventAtomicSource;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.interfaces.ModelI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.exceptions.PreconditionException;

// -----------------------------------------------------------------------------
/**
 * The abstract class <code>AbstractSimulatorPlugin</code> implements the core
 * simulation methods (DEVS protocol and management) as a BCM plug-in.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * <code>AbstractSimulatorPlugin</code> contains the shared methods between
 * the plug-ins for atomic models and coupled models simulation that are
 * implemented as plug-ins inheriting from this class.
 * </p>
 * <p>
 * Basically, the plug-in contains a reference to a DEVS simulation engine
 * (from the DEVS simulation library) which itself contains a reference to
 * a DEVS model. The current implementation assumes that a plug-in corresponds
 * to one component and vice versa. Hence, the simulation engine is seen as
 * an atomic simulation model from the supervisor or the parent engine, but
 * it can correspond to a local composite model (coupled model and/or
 * coordination engine). Another assumption is that the plug-in URI is the
 * same as the local architecture root model URI. A third assumption is that
 * models cannot share continuous variables among different components, hence
 * the local architecture root model is necessarily a TIOA.
 * </p>
 * <p>
 * The plug-in implements the interface <code>ModelDescriptionI</code> (the
 * DEVS simulation library interface that is used to describe models for
 * composition purposes), <code>SimulatorI</code>,
 * <code>ParentNotificationI</code> and <code>EventsExchangingI</code>.
 * Most of the time, the methods simply delegate to  the plug-in simulation
 * engine, sometimes checking stricter invariants. It also implements the
 * interface <code>SimulationManagementI</code> with mixed implementations
 * as some of the methods must here take into account the fact that simulation
 * engines and models reside in components and must communication through their
 * host component ports.
 * </p>
 * <p>
 * The plug-in, as other plug-ins, is in charge of declaring offered and
 * required interfaces and it implements the interface
 * <code>SimulatorPluginManagementI</code>, which declares methods specific to
 * the plug-in management. Any component that holds a DEVS simulation engine,
 * be it only an atomic engine or a coordination engine, must offer the
 * component interfaces <code>SimulatorCI</code> that proposes the simulation
 * protocol and <code>SimulatorPluginManagementCI</code> that proposes methods
 * for the management of the composition of the simulation architecture and
 * the management of the simulation runs. The component must also require the
 * component interfaces <code>ParentNotificationCI</code> used to notify
 * parent models when sending events to siblings and the component interface
 * <code>SupervisorNotificationCI</code> to send the simulation reports back to
 * the supervisor component when the simulation runs end. At creation time,
 * the plug-in will declare the above required/offered interfaces and create
 * the corresponding inbound ports and (some) outbound ports. To connect
 * models among different components, the plug-in uses the URI of the reflection
 * inbound port of the other components in order to use the reflection interface
 * to get the URIs of the ports to which it must connect. Hence, the component
 * must also require the <code>ReflectionI</code> interface.
 * </p>
 * <p>
 * The concrete subclasses implements the methods that require specific
 -* algorithms for the different roles: atomic model simulation or coordination
 * of submodels, acting as a coupled model.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	{@code getSimulator() == null || getSimulator().getURI().equals(getPluginURI())}
 * </pre>
 * 
 * <p>Created on : 2018-06-14</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public abstract class	AbstractSimulatorPlugin
extends		AbstractPlugin
implements	SimulatorPluginI
{
	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Plug-in internal constants and variables
	// -------------------------------------------------------------------------

	/** The simulation engine executing the simulation for the plug-in.		*/
	protected SimulatorI							simulator;
	/** Port through which simulation commands are issued.					*/
	protected SimulatorInboundPort					sip;
	/** Port connecting the simulator to the simulation supervisor.			*/
	protected SupervisorNotificationOutboundPort	snop;
	/** Management inbound port of the simulator component.					*/
	protected SimulatorPluginManagementInboundPort	smip;
	/** Port to notify the parent model of the reception of
	 *  external events.													*/
	protected ParentNotificationOutboundPort		pnop;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				AbstractSimulatorPlugin()
	{
		super();
	}

	// -------------------------------------------------------------------------
	// Plug-in generic methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#setPluginURI(java.lang.String)
	 */
	@Override
	public void			setPluginURI(String uri)
	{
		if (this.getSimulator() != null) {
			try {
				assert	this.getSimulator().getURI().equals(uri);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		super.setPluginURI(uri);
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#installOn(fr.sorbonne_u.components.ComponentI)
	 */
	@Override
	public void			installOn(ComponentI owner) throws Exception
	{
		assert	owner != null;

		super.installOn(owner);

		if (!owner.isRequiredInterface(ReflectionCI.class)) {
			this.addRequiredInterface(ReflectionCI.class);
		}

		this.addOfferedInterface(SimulatorCI.class);
		this.addRequiredInterface(SupervisorNotificationCI.class);
		this.addOfferedInterface(SimulatorPluginManagementCI.class);
		this.addRequiredInterface(ParentNotificationCI.class);
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#initialise()
	 */
	@Override
	public void			initialise() throws Exception
	{
		super.initialise();

		this.sip = new SimulatorInboundPort(
										SimulatorCI.class,
										this.getOwner(),
										this.getPluginURI(),
										this.getPreferredExecutionServiceURI());
		this.sip.publishPort();
		this.snop = new SupervisorNotificationOutboundPort(this.getOwner());
		this.snop.publishPort();
		this.smip =
				new SimulatorPluginManagementInboundPort(
										this.getOwner(),
										this.getPluginURI(),
										this.getPreferredExecutionServiceURI());
		this.smip.publishPort();
		this.pnop = new ParentNotificationOutboundPort(this.getOwner());
		this.pnop.publishPort();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#finalise()
	 */
	@Override
	public void			finalise() throws Exception
	{
		if (this.snop != null && this.snop.connected()) {
			this.getOwner().doPortDisconnection(this.snop.getPortURI());
		}
		if (this.pnop != null && this.pnop.connected()) {
			this.getOwner().doPortDisconnection(this.pnop.getPortURI());
		}
		assert	this.snop == null || !this.snop.connected();
		assert	this.pnop == null || !this.pnop.connected();

		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#uninstall()
	 */
	@Override
	public void			uninstall() throws Exception
	{
		if (this.sip != null) {
			this.sip.unpublishPort();
			this.sip.destroyPort();
			this.sip = null;
		}
		this.removeOfferedInterface(SimulatorCI.class);

		if (this.smip != null) {
			this.smip.unpublishPort();
			this.smip.destroyPort();
			this.smip = null;
		}
		this.removeOfferedInterface(SimulatorPluginManagementCI.class);

		if (this.snop != null) {
			assert	!this.snop.connected();
			this.snop.unpublishPort();
			this.snop.destroyPort();
			this.snop = null;
		}
		this.removeRequiredInterface(SupervisorNotificationCI.class);

		if (this.pnop != null) {
			assert	!this.pnop.connected();
			this.pnop.unpublishPort();
			this.pnop.destroyPort();
			this.pnop = null;
		}
		this.removeRequiredInterface(ParentNotificationCI.class);

		assert	this.snop == null && this.pnop == null;

		super.uninstall();
	}

	// -------------------------------------------------------------------------
	// Model manipulation methods (e.g., description, composition, ...)
	// -------------------------------------------------------------------------

	/**
	 * return the reference to the simulation engine associated to this plug-in.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @return	the reference to the simulation engine associated to this plug-in.
	 */
	protected SimulatorI	getSimulator()
	{
		return this.simulator;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.SimulatorPluginI#isSimulatorSet()
	 */
	@Override
	public boolean		isSimulatorSet()
	{
		return this.simulator != null;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getURI()
	 */
	@Override
	public String		getURI() throws Exception
	{
		return this.getPluginURI();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getSimulatedTimeUnit()
	 */
	@Override
	public TimeUnit		getSimulatedTimeUnit() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().getSimulatedTimeUnit();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getParentURI()
	 */
	@Override
	public String		getParentURI() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().getParentURI();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#isParentSet()
	 */
	@Override
	public boolean		isParentSet() throws Exception
	{
		return this.getSimulator().isParentSet();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#setParent(fr.sorbonne_u.devs_simulation.models.ParentReferenceI)
	 */
	@Override
	public void			setParent(ParentReferenceI p) throws Exception
	{
		assert	 p != null && p instanceof ComponentParentReference;

		((ComponentParentReference)p).setComponentParentReference(
							(AbstractComponent)this.getOwner(), this.pnop);
		this.getSimulator().setParent(p);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getParent()
	 */
	@Override
	public ParentNotificationI	getParent() throws Exception
	{
		return this.getSimulator().getParent();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#isRoot()
	 */
	@Override
	public boolean		isRoot() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().isRoot();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#isDescendentModel(java.lang.String)
	 */
	@Override
	public boolean		isDescendentModel(String uri) throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().isDescendentModel(uri);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getDescendentModel(java.lang.String)
	 */
	@Override
	public ModelI		getDescendentModel(String uri) throws Exception
	{
		throw new Exception("The method getDescendentModel should not be" +
							" called through a plug-in as it may cause " +
							" a reference leak to a non-serialisable Java " +
							"object.");
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getEventExchangingDescendentModel(java.lang.String)
	 */
	@Override
	public EventsExchangingI	getEventExchangingDescendentModel(String uri)
	throws Exception
	{
		return this.getSimulator().getEventExchangingDescendentModel(uri);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#closed()
	 */
	@Override
	public boolean		closed() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().closed();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getImportedEventTypes()
	 */
	@Override
	public Class<? extends EventI>[]	getImportedEventTypes()
	throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().getImportedEventTypes();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#isImportedEventType(java.lang.Class)
	 */
	@Override
	public boolean		isImportedEventType(Class<? extends EventI> ec)
	throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().isImportedEventType(ec);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getExportedEventTypes()
	 */
	@Override
	public Class<? extends EventI>[]	getExportedEventTypes()
	throws Exception
	{
		assert	this.getSimulator() != null;

		System.out.println("AbstractSimulatorPlugin#getExportedEventTypes 1 " +
											this.getSimulator());
		return this.getSimulator().getExportedEventTypes();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#isExportedEventType(java.lang.Class)
	 */
	@Override
	public boolean		isExportedEventType(Class<? extends EventI> ec)
	throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().isExportedEventType(ec);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getEventAtomicSource(java.lang.Class)
	 */
	@Override
	public EventAtomicSource	getEventAtomicSource(
		Class<? extends EventI> ce
		) throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().getEventAtomicSource(ce);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getInfluencees(java.lang.String, java.lang.Class)
	 */
	@Override
	public Set<CallableEventAtomicSink>	getInfluencees(
		String modelURI,
		Class<? extends EventI> ce
		) throws Exception
	{
		assert	this.getSimulator() != null;
		assert	modelURI != null;
		assert	this.getSimulator().getURI().equals(modelURI) ||
					this.getSimulator().isDescendentModel(modelURI);
		assert	ce != null;

		// TODO: is this OK for components?
		return this.getSimulator().getInfluencees(modelURI, ce);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#areInfluencedThrough(java.lang.String, java.util.Set, java.lang.Class)
	 */
	@Override
	public boolean		areInfluencedThrough(
		String modelURI,
		Set<String> destinationModelURIs,
		Class<? extends EventI> ce
		) throws Exception
	{
		assert	this.getSimulator() != null;
		assert	modelURI != null;
		assert	this.getSimulator().getURI().equals(modelURI) ||
					this.getSimulator().isDescendentModel(modelURI);
		assert	destinationModelURIs != null &&
					!destinationModelURIs.isEmpty();
		assert	ce != null;

		return this.getSimulator().areInfluencedThrough(
										modelURI, destinationModelURIs, ce);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#isInfluencedThrough(java.lang.String, java.lang.String, java.lang.Class)
	 */
	@Override
	public boolean		isInfluencedThrough(
		String modelURI,
		String destinationModelURI,
		Class<? extends EventI> ce
		) throws Exception
	{
		assert	this.getSimulator() != null;
		assert	modelURI != null;
		assert	this.getSimulator().getURI().equals(modelURI) ||
					this.getSimulator().isDescendentModel(modelURI);
		assert	destinationModelURI != null;
		assert	ce != null;

		return this.getSimulator().isInfluencedThrough(
									modelURI, destinationModelURI, ce);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#isTIOA()
	 */
	@Override
	public boolean		isTIOA() throws Exception
	{
		// TODO: A model at the top level of a component is always a TIOA.
		assert	this.getSimulator().isTIOA();
		return this.getSimulator().isTIOA();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#isOrdered()
	 */
	@Override
	public boolean		isOrdered() throws Exception
	{
		return this.getSimulator().isOrdered();
	}

	/**
	 * .
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isTIOA()}
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#isExportedVariable(java.lang.String, java.lang.Class)
	 */
	@Override
	public boolean		isExportedVariable(String name, Class<?> type)
	throws Exception
	{
		assert	this.isTIOA() : new PreconditionException("isTIOA()");
		throw new Exception(
				"A model associated with a component can't export variables!");
	}

	/**
	 * .
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isTIOA()}
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#isImportedVariable(java.lang.String, java.lang.Class)
	 */
	@Override
	public boolean		isImportedVariable(String name, Class<?> type)
	throws Exception
	{
		assert	this.isTIOA() : new PreconditionException("isTIOA()");
		throw new Exception(
				"A model associated with a component can't import variables!");
	}

	/**
	 * .
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isTIOA()}
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getImportedVariables()
	 */
	@Override
	public StaticVariableDescriptor[]	getImportedVariables()
	throws Exception
	{
		assert	this.isTIOA() : new PreconditionException("isTIOA()");
		throw new Exception(
				"A model associated with a component can't import variables!");
	}

	/**
	 * .
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isTIOA()}
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getExportedVariables()
	 */
	@Override
	public StaticVariableDescriptor[]	getExportedVariables()
	throws Exception
	{
		assert	this.isTIOA() : new PreconditionException("isTIOA()");
		throw new Exception(
				"A model associated with a component can't export variables!");
	}

	/**
	 * .
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isTIOA()}
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getActualExportedVariableValueReference(java.lang.String, java.lang.String, java.lang.Class)
	 */
	@Override
	public Value<?>		getActualExportedVariableValueReference(
		String modelURI,
		String sourceVariableName,
		Class<?> sourceVariableType
		) throws Exception
	{
		assert	this.isTIOA() : new PreconditionException("isTIOA()");
		throw new Exception(
				"A model associated with a component can't export variables!");
	}

	/**
	 * .
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isTIOA()}
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#setImportedVariableValueReference(java.lang.String, java.lang.String, java.lang.Class, fr.sorbonne_u.devs_simulation.hioa.models.vars.Value)
	 */
	@Override
	public void			setImportedVariableValueReference(
		String modelURI,
		String sinkVariableName,
		Class<?> sinkVariableType,
		Value<?> value
		) throws Exception
	{
		assert	this.isTIOA() : new PreconditionException("isTIOA()");
		throw new Exception(
				"A model associated with a component can't import variables!");
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void			setSimulationRunParameters(
		Map<String, Object> simParams
		) throws Exception
	{
		this.getSimulator().setSimulationRunParameters(simParams);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#getFinalReport()
	 */
	@Override
	public SimulationReportI	getFinalReport() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().getFinalReport();
	}

	// -------------------------------------------------------------------------
	// Model simulation methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#setSimulatedModel(fr.sorbonne_u.devs_simulation.models.interfaces.ModelI)
	 */
	@Override
	public void			setSimulatedModel(ModelI simulatedModel)
	throws Exception
	{
		assert	this.getSimulator() != null;
		assert	this.getSimulator().getURI().equals(simulatedModel.getURI());
		assert	this.getPluginURI().equals(this.getSimulator().getURI());

		this.getSimulator().setSimulatedModel(simulatedModel);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#isModelSet()
	 */
	@Override
	public boolean		isModelSet() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.isModelSet();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#initialiseSimulation(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			initialiseSimulation(Duration simulationDuration)
	throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().initialiseSimulation(simulationDuration);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#initialiseSimulation(fr.sorbonne_u.devs_simulation.models.time.Time, fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			initialiseSimulation(
		Time startTime,
		Duration simulationDuration
		) throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().initialiseSimulation(startTime, simulationDuration);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#isSimulationInitialised()
	 */
	@Override
	public boolean		isSimulationInitialised() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().isSimulationInitialised();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#internalEventStep()
	 */
	@Override
	public void			internalEventStep() throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().internalEventStep();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#causalEventStep(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			causalEventStep(Time current) throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().causalEventStep(current);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#externalEventStep(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			externalEventStep(Duration elapsedTime)
	throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().externalEventStep(elapsedTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#confluentEventStep(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			confluentEventStep(Duration elapsedTime)
	throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().confluentEventStep(elapsedTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#produceOutput(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			produceOutput(Time current) throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().produceOutput(current);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			endSimulation(Time current) throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().endSimulation(current);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#getTimeOfLastEvent()
	 */
	@Override
	public Time			getTimeOfLastEvent() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().getTimeOfLastEvent();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#getTimeOfNextEvent()
	 */
	@Override
	public Time			getTimeOfNextEvent() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().getTimeOfNextEvent();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#getNextTimeAdvance()
	 */
	@Override
	public Duration		getNextTimeAdvance() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().getNextTimeAdvance();
	}

	// -------------------------------------------------------------------------
	// Parent notification methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ParentNotificationI#hasReceivedExternalEvents(java.lang.String)
	 */
	@Override
	public void			hasReceivedExternalEvents(String modelURI)
	throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().hasReceivedExternalEvents(modelURI);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ParentNotificationI#hasPerformedExternalEvents(java.lang.String)
	 */
	@Override
	public void			hasPerformedExternalEvents(String modelURI)
	throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().hasPerformedExternalEvents(modelURI);
	}

	// -------------------------------------------------------------------------
	// Events exchanging methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.EventsExchangingI#storeInput(java.lang.String, ArrayList)
	 */
	@Override
	public void			storeInput(String destinationURI, ArrayList<EventI> es)
	throws Exception
	{
		assert	this.getSimulator() != null;
		assert	destinationURI != null;
		assert	this.getSimulator().getURI().equals(destinationURI) ||
							this.getSimulator().isDescendentModel(destinationURI);
		assert	es != null && !es.isEmpty();

		this.getSimulator().storeInput(destinationURI, es);
	}

	// -------------------------------------------------------------------------
	// Simulation management methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationManagementI#getTimeOfStart()
	 */
	@Override
	public Time			getTimeOfStart() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().getTimeOfStart();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationManagementI#getSimulationEndTime()
	 */
	@Override
	public Time			getSimulationEndTime() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().getSimulationEndTime();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationManagementI#doStandAloneSimulation(double, double)
	 */
	@Override
	public void			doStandAloneSimulation(
		double startTime,
		double simulationDuration
		) throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().doStandAloneSimulation(startTime, simulationDuration);
		if (this.snop != null && this.snop.connected()) {
			this.snop.acceptSimulationReport(this.getFinalReport());
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationManagementI#startRTSimulation(long, double, double)
	 */
	@Override
	public void			startRTSimulation(
		long realTimeOfStart,
		double simulationStartTime,
		double simulationDuration
		) throws Exception
	{
		throw new RuntimeException(
					"The method AbstractSimulatorPlugin#startRTSimulation must"
					+ " be implemented by a subclass for real time model,"
					+ " otherwise it is not available.");
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationManagementI#isSimulationRunning()
	 */
	@Override
	public boolean		isSimulationRunning() throws Exception
	{
		assert	this.getSimulator() != null;

		return this.getSimulator().isSimulationRunning();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationManagementI#stopSimulation()
	 */
	@Override
	public void			stopSimulation() throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().stopSimulation();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulationManagementI#finaliseSimulation()
	 */
	@Override
	public void			finaliseSimulation() throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().finaliseSimulation();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.interfaces.ModelStateAccessI#getModelStateValue(java.lang.String, java.lang.String)
	 */
	@Override
	public Object		getModelStateValue(String modelURI, String name)
	throws Exception
	{
		throw new Exception(
						"The method getModelStateValue called on a " +
						"simulator plug-in must be defined by the user in a " +
						"subclass to use the approriate way to access the " +
						"values in models.");
	}

	// -------------------------------------------------------------------------
	// Simulation debugging methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#showCurrentState(java.lang.String, fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			showCurrentState(String indent, Duration elapsedTime)
	throws Exception
	{
		assert	this.getSimulator() != null;

		this.getSimulator().showCurrentState(indent, elapsedTime);
	}

	// -------------------------------------------------------------------------
	// DEVS Simulator plug-in specific methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.SimulatorPluginI#getSimulatorPluginManagementInboundPortURI()
	 */
	@Override
	public String		getSimulatorPluginManagementInboundPortURI()
	throws Exception
	{
		if (this.smip != null) {
			return this.smip.getPortURI();
		} else {
			return null;
		}
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.SimulatorPluginI#getSimulatorInboundPortURI()
	 */
	@Override
	public String		getSimulatorInboundPortURI() throws Exception
	{
		if (this.sip != null) {
			return this.sip.getPortURI();
		} else {
			return null;
		}
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.interfaces.SimulatorPluginManagementI#connectSupervision(java.lang.String)
	 */
	@Override
	public void			connectSupervision(String supervisionInboundPortURI)
	throws Exception
	{
		assert	supervisionInboundPortURI != null;
		assert	this.snop != null && !this.snop.connected();

		this.getOwner().doPortConnection(
					this.snop.getPortURI(),
					supervisionInboundPortURI,
					SupervisorNotificationConnector.class.getCanonicalName());
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.interfaces.SimulatorPluginManagementI#reinitialise()
	 */
	@Override
	public void			reinitialise() throws Exception
	{
		if (this.snop != null && this.snop.connected()) {
			this.getOwner().doPortDisconnection(this.snop.getPortURI());
		}
		if (this.pnop != null && this.pnop.connected()) {
			this.getOwner().doPortDisconnection(this.pnop.getPortURI());
		}
	}

	// -------------------------------------------------------------------------
	// Debugging
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#modelAsString(java.lang.String)
	 */
	@Override
	public String		modelAsString(String indent) throws Exception
	{
		return "ASP:" + this.getSimulator().modelAsString(indent);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#simulatorAsString()
	 */
	@Override
	public String		simulatorAsString() throws Exception
	{
		return "ASP:" + this.getSimulator().simulatorAsString();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#toggleDebugMode()
	 */
	@Override
	public void			toggleDebugMode() throws Exception
	{
		this.getSimulator().toggleDebugMode();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#isDebugModeOn()
	 */
	@Override
	public boolean		isDebugModeOn() throws Exception
	{
		return this.getSimulator().isDebugModeOn();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#setDebugLevel(int)
	 */
	@Override
	public void			setDebugLevel(int newDebugLevel) throws Exception
	{
		this.getSimulator().setDebugLevel(newDebugLevel);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#hasDebugLevel(int)
	 */
	@Override
	public boolean		hasDebugLevel(int debugLevel) throws Exception
	{
		return this.getSimulator().hasDebugLevel(debugLevel);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#showCurrentStateContent(java.lang.String, fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			showCurrentStateContent(
		String indent,
		Duration elapsedTime
		) throws Exception
	{
		this.getSimulator().showCurrentState(indent, elapsedTime);
	}
}
// -----------------------------------------------------------------------------
