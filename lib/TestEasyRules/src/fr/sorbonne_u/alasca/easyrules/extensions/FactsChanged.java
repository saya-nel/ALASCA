package fr.sorbonne_u.alasca.easyrules.extensions;

// Copyright Jacques Malenfant, Sorbonne Universite.
//
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

import org.jeasy.rules.api.Facts;

// -----------------------------------------------------------------------------
/**
 * The class <code>FactsChanged</code> extends the class <code>Facts</code>
 * from the Easy Rules library to record in a more efficient way the facts
 * that have been changed after firing the rules on the set of facts; this
 * eases programming inferences until no more facts are changed or added to
 * the set of facts.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2019-11-27</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			FactsChanged
extends		Facts
{
	protected boolean	hasChanged ;

	public				FactsChanged()
	{
		super() ;
		this.hasChanged = false ;
	}

	public boolean 		hasChanged()
	{
		return this.hasChanged ;
	}

	public void			reset()
	{
		this.hasChanged = false ;
	}

	/**
	 * @see org.jeasy.rules.api.Facts#put(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object		put(String name, Object fact)
	{
		assert	name != null ;
		if (!this.asMap().containsKey(name) ||
										!this.asMap().get(name).equals(fact)) {
			this.hasChanged = true ;
		}
		return super.put(name, fact);
	}

	
}
// -----------------------------------------------------------------------------
