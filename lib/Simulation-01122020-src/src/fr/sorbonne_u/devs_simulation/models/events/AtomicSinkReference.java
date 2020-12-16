package fr.sorbonne_u.devs_simulation.models.events;

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

import fr.sorbonne_u.devs_simulation.interfaces.EventsExchangingI;

// -----------------------------------------------------------------------------
/**
 * The class <code>AtomicSinkReference</code> defines a reference to a
 * model consuming some events as a direct standard (Java) object reference.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2018-06-15</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			AtomicSinkReference
extends		AbstractAtomicSinkReference
{
	private static final long serialVersionUID = 1L;

	/** Standard object reference implementing the interface
	 *  <code>EventsExchangingI</code>									*/
	public final EventsExchangingI	ref;

	/**
	 * construct an atomic sink reference from a (Java) standard object
	 * reference implementing the interface <code>EventsExchangingI</code>.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code ref != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param ref	the reference on an event exchanging model or proxy.
	 */
	public 				AtomicSinkReference(
		EventsExchangingI ref
		)
	{
		super();

		assert	ref != null;

		this.ref = ref;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.events.AbstractAtomicSinkReference#isDirect()
	 */
	@Override
	public boolean		isDirect()
	{
		return true;
	}
}
// -----------------------------------------------------------------------------
