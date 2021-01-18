package fr.sorbonne_u.components.cyphy.interfaces;

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

// -----------------------------------------------------------------------------
/**
 * The interface <code>EmbeddingComponentAccess</code> defines how a
 * simulation model can access its embedding component.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * When performing software-in-the-loop (SIL) simulations, the simulation may
 * have to access values and methods that belongs to the components. Typically,
 * a controller may change the state of a component which impacts the simulation
 * by changing the behavioural mode of the entity it simulates.
 * </p>
 * <p>
 * This interface is meant to be implemented by components that would be called
 * typically by the internal transition method in DEVS atomic models. If the
 * simulation model needs to be able to call methods on its component, an
 * interface extending this one should be defined to add the required method
 * signatures.
 * </p>
 * <p>
 * Using this facility as is currently only provides the possibility for
 * a simulation model to poll the component for inputs during the simulation.
 * As polling is a costly way to do this, in the near future a better way of
 * communication from the component to the simulation model to avoid polling
 * must be added, but it is complex as it cannot interfere negatively with the
 * DEVS protocol.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2019-10-29</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public interface		EmbeddingComponentAccessI
{
	/**
	 * get the value the embedding component associates to <code>name</code>.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code name != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param name			the name of a value that the simulation model can access on its embedding component.
	 * @return				the value the embedding component associates to <code>name</code>.
	 * @throws Exception	<i>to do</i>.
	 */
	default Object		getEmbeddingComponentStateValue(
		String name
		) throws Exception
	{
		assert	name != null;
		// by default, return null
		return null;
	}

	/**
	 * set the value the embedding component associates to <code>name</code>.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code name != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param name			the name of a value that the simulation model can access on its embedding component.
	 * @param value			the value associated to the name by its embedding component.
	 * @throws Exception	<i>to do</i>.
	 */
	default void		setEmbeddingComponentStateValue(
		String name,
		Object value
		) throws Exception 
	{
		assert	name != null;
		// by default, do nothing
	}
}
// -----------------------------------------------------------------------------
