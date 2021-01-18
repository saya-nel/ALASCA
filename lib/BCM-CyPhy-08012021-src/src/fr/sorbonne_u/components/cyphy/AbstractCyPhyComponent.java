package fr.sorbonne_u.components.cyphy;

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

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cyphy.interfaces.CyPhyComponentI;
import fr.sorbonne_u.components.cyphy.interfaces.CyPhyReflectionCI;
import fr.sorbonne_u.components.cyphy.plugins.devs.AbstractSimulatorPlugin;
import fr.sorbonne_u.components.cyphy.plugins.devs.architectures.ComponentAtomicModelDescriptor;
import fr.sorbonne_u.components.cyphy.ports.CyPhyReflectionInboundPort;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionCI;
import fr.sorbonne_u.devs_simulation.architectures.Architecture;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

// -----------------------------------------------------------------------------
/**
 * The class <code>AbstractCyPhyComponent</code> add the necessary properties
 * and methods required to turn a standard BCM component into a cyber-physical
 * one.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * <i>Work in progress...</i>
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2019-06-18</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public abstract class	AbstractCyPhyComponent
extends		AbstractComponent
implements	CyPhyComponentI
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** Map from root model URIs to the component atomic model descriptors
	 *  that presents the corresponding local simulation architectures with
	 *  the root models in the component as atomic models to be composed
	 *  in global simulation architectures.									*/
	protected Map<String,ComponentAtomicModelDescriptor> atomicModelDescriptors;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a cyber-physical component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code nbThreads >= 0 && nbSchedulableThreads >= 0}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param nbThreads				number of standard threads.
	 * @param nbSchedulableThreads	number of schedulable threads.
	 */
	protected			AbstractCyPhyComponent(
		int nbThreads,
		int nbSchedulableThreads
		)
	{
		super(nbThreads, nbSchedulableThreads);
	}

	/**
	 * create a cyber-physical component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code reflectionInboundPortURI != null}
	 * pre	{@code nbThreads >= 0 && nbSchedulableThreads >= 0}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param reflectionInboundPortURI	URI of the reflection inbound port of the created component.
	 * @param nbThreads					number of standard threads.
	 * @param nbSchedulableThreads		number of schedulable threads.
	 */
	protected			AbstractCyPhyComponent(
		String reflectionInboundPortURI,
		int nbThreads,
		int nbSchedulableThreads
		)
	{
		super(reflectionInboundPortURI, nbThreads, nbSchedulableThreads);
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#configureReflection(java.lang.String)
	 */
	@Override
	protected void		configureReflection(String reflectionInboundPortURI)
	throws Exception
	{
		assert	reflectionInboundPortURI != null;

		this.addOfferedInterface(CyPhyReflectionCI.class);
		try {
			CyPhyReflectionInboundPort rip =
				new CyPhyReflectionInboundPort(reflectionInboundPortURI, this);
			rip.publishPort();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		assert	this.isOfferedInterface(CyPhyReflectionCI.class);
		assert	this.findInboundPortURIsFromInterface(ReflectionCI.class) !=
																		null;
		assert	this.findInboundPortURIsFromInterface(ReflectionCI.class).length
																		== 1;
		assert	this.findInboundPortURIsFromInterface(ReflectionCI.class)[0].
											equals(reflectionInboundPortURI);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * create and return the local simulation architecture of this
	 * cyber-physical component to describe its behaviour; <code>modelURI</code>
	 * must designate uniquely the architecture to be created as the URI of its
	 * root model.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modelURI != null}
	 * pre	{@code !isInstalled(modelURI)}
	 * pre	{@code !atomicModelDescriptors.containsKey(modelURI)}
	 * post	{@code atomicModelDescriptors.containsKey(modelURI)}
	 * </pre>
	 *
	 * @param modelURI		URI of the root model in the simulation architecture to be created.
	 * @return				the local simulation architecture of this component.
	 * @throws Exception 	<i>to do</i>.
	 */
	protected Architecture		createLocalArchitecture(
		String modelURI
		) throws Exception
	{
		throw new Exception("this implementation of createLocalArchitecture"
												+ " must never be called!");
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#getExecutorServiceIndex(java.lang.String)
	 */
	@Override
	protected int		getExecutorServiceIndex(String uri)
	{
		return super.getExecutorServiceIndex(uri);
	}

	/**
	 * return the scheduled executor service of this component with the given
	 * index.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code index >= 0}
	 * pre	{@code isSchedulable(index)}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param index	index of a scheduled executor service of this component.
	 * @return		the scheduled executor service of this component with the given index.
	 */
	protected ScheduledExecutorService	getScheduledExecutorService(int index)
	{
		assert	this.isSchedulable(index);
		return (ScheduledExecutorService) super.getExecutorService(index);
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.interfaces.CyPhyComponentI#getSimulatorPluginManagementInboundPortURI(java.lang.String)
	 */
	@Override
	public String		getSimulatorPluginManagementInboundPortURI(
		String pluginURI
		) throws Exception
	{
		assert	this.isPluginFacilitiesConfigured();
		assert	pluginURI != null;

		return ((AbstractSimulatorPlugin)this.getPlugin(pluginURI)).
								getSimulatorPluginManagementInboundPortURI();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.interfaces.CyPhyComponentI#getSimulatorInboundPortURI(java.lang.String)
	 */
	@Override
	public String		getSimulatorInboundPortURI(String pluginURI)
	throws Exception
	{
		assert	this.isPluginFacilitiesConfigured();
		assert	pluginURI != null;

		return ((AbstractSimulatorPlugin)this.getPlugin(pluginURI)).
												getSimulatorInboundPortURI();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.interfaces.CyPhyComponentI#getAtomicModelDescriptor(java.lang.String)
	 */
	@Override
	public ComponentAtomicModelDescriptor	getAtomicModelDescriptor(
		String modelURI
		) throws Exception
	{
		assert	modelURI != null;

		return this.atomicModelDescriptors.get(modelURI);
	}
}
// -----------------------------------------------------------------------------
