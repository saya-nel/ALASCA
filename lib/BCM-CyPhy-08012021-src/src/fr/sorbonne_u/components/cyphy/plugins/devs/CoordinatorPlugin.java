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

import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.ComponentCoupledModelDescriptorI;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.ComponentModelArchitecture;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.ComponentModelArchitectureHelper;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.ComponentModelArchitectureI;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.ComponentModelDescriptorI;
import fr.sorbonne_u.components.cyphy.plugins.devs.connectors.SimulatorConnector;
import fr.sorbonne_u.components.cyphy.plugins.devs.interfaces.ParentNotificationCI;
import fr.sorbonne_u.components.cyphy.plugins.devs.interfaces.SimulatorCI;
import fr.sorbonne_u.components.cyphy.plugins.devs.interfaces.SimulatorPluginManagementCI;
import fr.sorbonne_u.components.cyphy.plugins.devs.ports.ParentNotificationInboundPort;
import fr.sorbonne_u.components.cyphy.plugins.devs.ports.SimulatorOutboundPort;
import fr.sorbonne_u.components.cyphy.plugins.devs.ports.SimulatorPluginManagementOutboundPort;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.events.AbstractAtomicSinkReference;
import fr.sorbonne_u.devs_simulation.models.events.CallableEventAtomicSink;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.CoordinatorI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.exceptions.InvariantException;

// -----------------------------------------------------------------------------
/**
 * The class <code>CoordinatorPlugin</code> implements a BCM plug-in for
 * the coordination role in DEVS simulations.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * In DEVS, coupled simulation models are meant to coordinate the submodels
 * they compose. This plug-in gathers the necessary code to make a BCM component
 * able to hold and execute a coupled simulation model.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	{@code (architecture == null || architecture.isCoupledModel(getPluginURI()))}
 * invariant	{@code (smops == null || coordinatedPorts == null) || (smops.size() == coordinatedPorts.size())}
 * </pre>
 * 
 * <p>Created on : 2018-06-14</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			CoordinatorPlugin
extends		AbstractSimulatorPlugin
implements	CoordinatorPluginI
{
	// -------------------------------------------------------------------------
	// Plug-in internal constants and variables
	// -------------------------------------------------------------------------

	private static final long							serialVersionUID = 1L;
	/** simulation architecture to which the coupled model held by the
	 *  plug-in belongs.													*/
	protected ComponentModelArchitecture					architecture;
	/** maps from URIs of the submodels to the outbound ports allowing this
	 *  plug-in's coupled model to manage their simulations.				*/
	protected Map<String,SimulatorPluginManagementOutboundPort>	smops;
	/** maps from URIs of the submodels to the outbound ports allowing this
	 *  plug-in's coupled model to coordinate their simulations.			*/
	protected Map<String,SimulatorOutboundPort>				coordinatedPorts;
	/** inbound port through which this plug-in's coupled model receives
	 *  notifications from its submodels.									*/
	protected ParentNotificationInboundPort					pnip;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	/**
	 * create a coordinator plug-in instance.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	{@code CoordinatorPlugin.checkInvariant(this)}
	 * </pre>
	 *
	 */
	public				CoordinatorPlugin()
	{
		super();

		assert	CoordinatorPlugin.checkInvariant(this);
	}

	/**
	 * check the invariant for the given plug-in.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code p != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param p		the plug-in to be checked.
	 * @return		true if the plug-in obeys to its invariant.
	 */
	public static boolean	checkInvariant(CoordinatorPlugin p)
	{
		assert	p != null;

		boolean invariant = true;
		invariant &= (p.architecture == null ||
						p.architecture.isCoupledModel(p.getPluginURI()));
		assert	invariant :
					new InvariantException("architecture == null || " + 
							"architecture.isCoupledModel(getPluginURI())");
		invariant &= (p.smops == null || p.coordinatedPorts == null) ||
						(p.smops.size() == p.coordinatedPorts.size());
		assert	invariant :
					new InvariantException(
						"(p.smops == null || p.coordinatedPorts == null) || " + 
						"(p.smops.size() == p.coordinatedPorts.size())");

		return invariant;
	}

	// -------------------------------------------------------------------------
	// Plug-in life-cycle methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#installOn(fr.sorbonne_u.components.ComponentI)
	 */
	@Override
	public void			installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);

		// add the required and offered component interfaces to the owner
		// component
		this.addRequiredInterface(SimulatorPluginManagementCI.class);
		this.addRequiredInterface(SimulatorCI.class);
		this.addOfferedInterface(ParentNotificationCI.class);
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#initialise()
	 */
	@Override
	public void			initialise() throws Exception
	{
		super.initialise();

		// create an inbound port for parent notifications and publish it.
		this.pnip = new ParentNotificationInboundPort(
										this.getOwner(),
										this.getPluginURI(),
										this.getPreferredExecutionServiceURI());
		this.pnip.publishPort();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractPlugin#finalise()
	 */
	@Override
	public void			finalise() throws Exception
	{
		if (this.smops != null && !this.smops.isEmpty()) {
			for (SimulatorPluginManagementOutboundPort smop :
														this.smops.values()) {
				if (smop.connected()) {
					this.getOwner().doPortDisconnection(smop.getPortURI());
				}
			}
		}
		if (this.coordinatedPorts != null && !this.coordinatedPorts.isEmpty()) {
			for (SimulatorOutboundPort sop : this.coordinatedPorts.values()) {
				if (sop.connected()) {
					this.getOwner().doPortDisconnection(sop.getPortURI());
				}
			}
		}

		assert	this.smops == null ||
					this.smops.values().stream().
						map(smop -> {	try {
											return !smop.connected();
										} catch (Exception e) {
											throw new RuntimeException(e);
										}
									}).allMatch(b -> b);
		assert	this.coordinatedPorts == null ||
					this.coordinatedPorts.values().stream().
						map(sop -> {	try {
											return !sop.connected();
										} catch (Exception e) {
											throw new RuntimeException(e);
										}
									}).allMatch(b -> b);
		
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#uninstall()
	 */
	@Override
	public void			uninstall() throws Exception
	{
		// unpublish and destroy all ports; remove required and offered
		// interfaces.
		if (this.smops != null && !this.smops.isEmpty()) {
			for (SimulatorPluginManagementOutboundPort smop :
														this.smops.values()) {
				assert	!smop.connected();
				smop.unpublishPort();
				smop.destroyPort();
			}
		}
		this.smops.clear();
		if (this.coordinatedPorts != null) {
			for (SimulatorOutboundPort sop : this.coordinatedPorts.values()) {
				assert	!sop.connected();
				sop.unpublishPort();
				sop.destroyPort();
			}
			this.coordinatedPorts.clear();
		}

		assert	this.smops == null || this.smops.isEmpty();
		assert	this.coordinatedPorts == null ||
									this.coordinatedPorts.isEmpty();

		this.smops = null;
		this.removeRequiredInterface(SimulatorPluginManagementCI.class);

		this.coordinatedPorts = null;
		this.removeRequiredInterface(SimulatorCI.class);

		this.pnip.unpublishPort();
		this.pnip.destroyPort();
		this.pnip = null;
		this.removeOfferedInterface(ParentNotificationCI.class);

		super.uninstall();
	}

	// -------------------------------------------------------------------------
	// Model manipulation methods (e.g., description, composition, ...)
	// -------------------------------------------------------------------------

	/**
	 * .
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isSimulatorSet()}
	 * post	true		// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#findProxyAtomicEngineURI(java.lang.String)
	 */
	@Override
	public String		findProxyAtomicEngineURI(String modelURI)
	throws Exception
	{
		assert	this.isSimulatorSet();

		return this.simulator.findProxyAtomicEngineURI(modelURI);
	}

	/**
	 * .
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isSimulatorSet()}
	 * post	true		// no postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#getEventAtomicSinks(java.lang.Class)
	 */
	@Override
	public Set<CallableEventAtomicSink>	getEventAtomicSinks(
		Class<? extends EventI> ce
		) throws Exception
	{
		assert	this.isSimulatorSet();

		return this.simulator.getEventAtomicSinks(ce);
	}

	/**
	 * .
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isSimulatorSet()}
	 * post	true		// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.interfaces.ModelDescriptionI#addInfluencees(java.lang.String, java.lang.Class, java.util.Set)
	 */
	@Override
	public void			addInfluencees(
		String modelURI,
		Class<? extends EventI> ce,
		Set<CallableEventAtomicSink> influencees
		) throws Exception
	{
		assert	this.isSimulatorSet();
		assert	modelURI != null;
		assert	this.simulator.getURI().equals(modelURI) ||
								this.simulator.isDescendentModel(modelURI);
		assert	ce != null;
		assert	influencees != null && influencees.size() != 0;

		this.simulator.addInfluencees(modelURI, ce, influencees);
	}

	/**
	 * .
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isSimulatorSet()}
	 * post	true		// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.CoordinatorI#setCoordinatedEngines(fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI[])
	 */
	@Override
	public void			setCoordinatedEngines(
		SimulatorI[] coordinatedEngines
		) throws Exception
	{
		assert	this.isSimulatorSet();

		((CoordinatorI)this.simulator).
								setCoordinatedEngines(coordinatedEngines);
	}

	/**
	 * .
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isSimulatorSet()}
	 * post	true		// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.CoordinatorI#coordinatedEnginesSet()
	 */
	@Override
	public boolean		coordinatedEnginesSet() throws Exception
	{
		assert	this.isSimulatorSet();

		return ((CoordinatorI)this.simulator).coordinatedEnginesSet();
	}

	// -------------------------------------------------------------------------
	// Model simulation methods
	// -------------------------------------------------------------------------

	/**
	 * .
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isSimulatorSet()}
	 * post	true		// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI#getAtomicEngineReference(java.lang.String)
	 */
	@Override
	public AbstractAtomicSinkReference	getAtomicEngineReference(
		String atomicEngineURI
		) throws Exception
	{
		assert	this.isSimulatorSet();

		if (this.simulator.getURI().equals(atomicEngineURI) ||
					this.simulator.isDescendentModel(atomicEngineURI)) {
			return this.simulator.getAtomicEngineReference(atomicEngineURI);
		} else {
			return null;
		}
	}

	// -------------------------------------------------------------------------
	// DEVS Coordinator plug-in specific methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin#compose(fr.sorbonne_u.components.cyphy.plugins.devs.architectures.ComponentModelArchitectureI)
	 */
	@Override
	public String		compose(ComponentModelArchitectureI architecture)
	throws Exception
	{
		this.architecture = (ComponentModelArchitecture) architecture;
		this.simulator =
			(SimulatorI) architecture.compose(
										this.getPluginURI(),
										(AbstractComponent)this.getOwner(),
										this.pnip.getPortURI(),
										this);
		this.logMessage(this.simulator.simulatorAsString());

		return this.sip.getPortURI();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.interfaces.SimulatorPluginManagementI#reinitialise()
	 */
	@Override
	public void			reinitialise() throws Exception
	{
		if (this.smops != null) {
			for (SimulatorPluginManagementOutboundPort smop :
														this.smops.values()) {
				if (smop.connected()) {
					smop.reinitialise();
					this.getOwner().doPortDisconnection(smop.getPortURI());
				}
				smop.unpublishPort();
				smop.destroyPort();
			}
			this.smops.clear();
		}
		if (this.coordinatedPorts != null) {
			for (SimulatorOutboundPort sop : this.coordinatedPorts.values()) {
				if (sop.connected()) {
					this.getOwner().doPortDisconnection(sop.getPortURI());
				}
				sop.unpublishPort();
				sop.destroyPort();
			}
			this.coordinatedPorts.clear();
		}

		ReflectionOutboundPort rop = new ReflectionOutboundPort(this.getOwner());
		rop.publishPort();
		Set<String> childrenModelsURIs =
				this.architecture.getChildrenModelURIs(this.getPluginURI());
		for (String uri : childrenModelsURIs) {
			if (this.architecture.getModelDescriptor(uri) instanceof
													CoupledModelDescriptor) {
				this.getOwner().doPortConnection(
							rop.getPortURI(),
							this.architecture.getReflectionInboundPortURI(uri),
							ReflectionConnector.class.getCanonicalName());
				rop.uninstallPlugin(uri);
				this.getOwner().doPortDisconnection(rop.getPortURI());
			}
		}
		rop.unpublishPort();
		rop.destroyPort();
		rop = null;
		
		super.reinitialise();
	}

	@Override
	public SimulatorPluginManagementOutboundPort	connectSubmodel4Management(
		String modelURI
		)
	{
		assert	modelURI != null;

		ComponentModelDescriptorI d =
							this.architecture.getModelDescriptor(modelURI);
		SimulatorPluginManagementOutboundPort smop = null;
		if (this.architecture.isCoupledModel(modelURI)) {
			assert	d instanceof ComponentCoupledModelDescriptorI;
			smop = ComponentModelArchitectureHelper.
						connectCoupledModelComponent(
								(AbstractComponent)this.getOwner(),
								modelURI,
								d.getComponentReflectionInboundPortURI(),
								((ComponentCoupledModelDescriptorI)d).
												createCoordinatorPlugin());
		} else {
			assert	this.architecture.isAtomicModel(modelURI);
			smop = ComponentModelArchitectureHelper.
						connectAtomicModelComponent(
								(AbstractComponent)this.getOwner(),
								modelURI,
								d.getComponentReflectionInboundPortURI());
		}

		if (this.smops == null) {
			this.smops =
				new HashMap<String,SimulatorPluginManagementOutboundPort>();
		}
		this.smops.put(modelURI, smop);
		return smop;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.plugins.devs.CoordinatorPluginI#connectSubmodel4Simulation(java.lang.String, java.lang.String)
	 */
	@Override
	public SimulatorOutboundPort	connectSubmodel4Simulation(
		String modelURI,
		String sipURI
		)
	{
		assert	modelURI != null && sipURI != null;
		try {
			SimulatorOutboundPort sop =
				new SimulatorOutboundPort(SimulatorCI.class, this.getOwner());
			sop.publishPort();
			this.getOwner().doPortConnection(
							sop.getPortURI(),
							sipURI,
							SimulatorConnector.class.getCanonicalName());
			if (this.coordinatedPorts == null) {
				this.coordinatedPorts =
								new HashMap<String,SimulatorOutboundPort>();
			}
			this.coordinatedPorts.put(modelURI, sop);
			return sop;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
// -----------------------------------------------------------------------------

