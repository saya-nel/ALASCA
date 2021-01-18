package fr.sorbonne_u.devs_simulation.models;

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

import fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI;
import fr.sorbonne_u.devs_simulation.models.interfaces.RTModelFactoryI;
import fr.sorbonne_u.devs_simulation.simulators.AtomicRTEngine;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

// -----------------------------------------------------------------------------
/**
 * The class <code>StandardRTAtomicModelFactory</code> implements the standard
 * model factory for real time atomic models.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The difference with the standard atomic model factory are that the standard
 * simulation engine to be created is <code>AtomicRTEngine</code> and the
 * initialisation of the acceleration factor used during the simulation runs.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	{@code accelerationFactor > 0.0}
 * </pre>
 * 
 * <p>Created on : 2020-11-23</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			StandardRTAtomicModelFactory
extends		StandardAtomicModelFactory
implements	RTModelFactoryI
{
	// -------------------------------------------------------------------------
	// Constants and instance variables
	// -------------------------------------------------------------------------

	private static final long	serialVersionUID = 1L;
	/** the acceleration factor used to control the pace of the simulation
	 *  when converting the simulated time to real time.					*/
	protected double			accelerationFactor;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a standard real time atomic model factory for the given Java
	 * class representing the model.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code atomicModelClass != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param atomicModelClass		the class defining the model.
	 */
	public				StandardRTAtomicModelFactory(
		Class<? extends AtomicModelI> atomicModelClass
		)
	{
		super(atomicModelClass);

		this.accelerationFactor = AtomicRTEngine.DEFAULT_ACCELERATION_FACTOR;
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.RTModelFactoryI#setAccelerationFactor(double)
	 */
	@Override
	public void			setAccelerationFactor(double accelerationFactor)
	{
		assert	accelerationFactor > 0.0;
		this.accelerationFactor = accelerationFactor;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AbstractAtomicModelFactory#createAtomicEngine()
	 */
	@Override
	public SimulatorI	createAtomicEngine()
	{
		return new AtomicRTEngine(this.accelerationFactor);
	}
}
// -----------------------------------------------------------------------------
