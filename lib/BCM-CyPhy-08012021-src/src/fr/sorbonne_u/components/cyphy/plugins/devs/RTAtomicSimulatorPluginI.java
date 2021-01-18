package fr.sorbonne_u.components.cyphy.plugins.devs;

import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Time;

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

import fr.sorbonne_u.devs_simulation.simulators.interfaces.RTAtomicSimulatorI;

// -----------------------------------------------------------------------------
/**
 * The interface <code>RTAtomicSimulatorPluginI</code> declares elements
 * required in an atomic simulator plug-in for real time simulations.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * In real time SIL simulations, communication between the simulation and the
 * running software must be put in place. Here, the method
 * <code>triggerExternalEvent</code> allows the software to send an external
 * event to an atomic model for execution. Hence, it enables the capability for
 * an operation in the software to trigger the execution of an event transition
 * in the simulation model.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2020-12-16</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public interface		RTAtomicSimulatorPluginI
extends		AtomicSimulatorPluginI,
			RTSimulatorPluginI,
			RTAtomicSimulatorI
{
	/**
	 * The functional interface  <code>EventFactoryFI</code> allows to create
	 * event factories for DEVS simulations in BCMCyPhy.
	 *
	 * <p><strong>Description</strong></p>
	 * 
	 * <p><strong>Invariant</strong></p>
	 * 
	 * <pre>
	 * invariant	true
	 * </pre>
	 * 
	 * <p>Created on : 2020-12-17</p>
	 * 
	 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
	 */
	@FunctionalInterface
	public static interface	EventFactoryFI
	{
		/**
		 * create an event with the given time of occurrence.
		 * 
		 * <p><strong>Contract</strong></p>
		 * 
		 * <pre>
		 * pre	true			// no precondition.
		 * post	true			// no postcondition.
		 * </pre>
		 *
		 * @param occurrence	simulation time of occurrence of the event ot be created.
		 * @return				an event with the given time of occurrence.
		 */
		public EventI	createEvent(Time occurrence);
	}

	/**
	 * trigger an external event at the current simulation time on a simulation
	 * model managed by this plug-in.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code destinationModelURI != null && !destinationModelURI.isEmpty()}
	 * pre	{@code ef != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param destinationModelURI	URI of the simulation model on which the event must be triggered.
	 * @param ef					event factory that will create an external event with occurrence time passed as parameter.
	 * @throws Exception			<i>to do</i>.
	 */
	public void			triggerExternalEvent(
		String destinationModelURI,
		EventFactoryFI ef
		) throws Exception;
}
// -----------------------------------------------------------------------------
