package fr.sorbonne_u.devs_simulation.architectures;

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

// -----------------------------------------------------------------------------
/**
 * The enumeration <code>SimulationEngineCreationMode</code> defines the
 * different simulation engine creation modes when creating models.
 *
 * <p><strong>Description</strong></p>
 * 
 * <pre>
 *     NO_ENGINE              the model is created without a simulation engine.
 *     ATOMIC_ENGINE          the model is created with an atomic simulation engine.
 *     ATOMIC_RT_ENGINE       the model is created with a  real time atomic simulation engine.
 *     COORDINATION_ENGINE    the model is created with a coordination engine.
 *     COORDINATION_RT_ENGINE the model is created with a real time coordination engine.
 * </pre>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2018-06-26</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public enum				SimulationEngineCreationMode
{
	NO_ENGINE,				// the model is created without a simulation engine.
	ATOMIC_ENGINE,			// the model is created with an atomic simulation
							// engine.
	ATOMIC_RT_ENGINE,		// the model is created with a  real time atomic
							// simulation engine.
	COORDINATION_ENGINE,	// the model is created with a coordination engine.
	COORDINATION_RT_ENGINE;	// the model is created with a real time
							// coordination engine.

	/**
	 * return true if the value is suitable as an atomic engine.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @return	true if the value corresponds to an atomic engine.
	 */
	public boolean		isAtomic()
	{
		return this == NO_ENGINE || this == ATOMIC_ENGINE ||
													this == ATOMIC_RT_ENGINE;
	}

	/**
	 * return true if the value is suitable as a coordination engine.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @return	true if the value is suitable as a coordination engine.
	 */
	public boolean		isCoordination()
	{
		return this == NO_ENGINE || this == COORDINATION_ENGINE ||
												this == COORDINATION_RT_ENGINE;
	}

	/**
	 * return true if the mode is suitable for a real time model.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @return	true if the mode is suitable for a real time model.
	 */
	public boolean		isRealTime()
	{
		return this == NO_ENGINE || this == ATOMIC_RT_ENGINE ||
												this == COORDINATION_RT_ENGINE;
	}
}
// -----------------------------------------------------------------------------
