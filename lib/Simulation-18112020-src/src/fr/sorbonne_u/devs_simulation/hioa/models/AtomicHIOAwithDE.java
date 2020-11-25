package fr.sorbonne_u.devs_simulation.hioa.models;

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

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

// -----------------------------------------------------------------------------
/**
 * The class <code>AtomicHIOAwithDE</code> implements hybrid input/output
 * automata where the trajectories of continuous variables are defined by
 * differential equations.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2018-09-06</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public abstract class	AtomicHIOAwithDE
extends		AtomicHIOA
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create an atomic hybrid input/output model based on a differential
	 * equations solver with the given URI (if null, one will be generated)
	 * and to be run by the given simulator (or by the one of an ancestor
	 * coupled model if null) using the given time unit for its clock.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code simulatedTimeUnit != null}
	 * pre	{@code simulationEngine == null || simulationEngine instanceof HIOA_AtomicEngine}
	 * post	{@code getURI() != null}
	 * post	{@code uri != null implies this.getURI().equals(uri)}
	 * post	{@code getSimulatedTimeUnit().equals(simulatedTimeUnit)}
	 * post	{@code simulationEngine != null implies getSimulationEngine().equals(simulationEngine)}
	 * post	{@code !isDebugModeOn()}
	 * </pre>
	 *
	 * @param uri					unique identifier of the model.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param simulationEngine		simulation engine enacting the model.
	 * @throws Exception   			<i>TODO</i>.
	 */
	public				AtomicHIOAwithDE(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	// -------------------------------------------------------------------------
	// DEVS standard protocol and related methods
	// -------------------------------------------------------------------------

	/**
	 * besides calling the superclass method, the method
	 * <code>initialiseDerivatives</code> is also called after and then becomes
	 * part of the simulation protocol.
	 * 
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			initialiseState(Time initialTime)
	{
		super.initialiseState(initialTime);
		this.initialiseDerivatives();
	}

	/**
	 * after calling the method <code>userDefinedInternalTransition</code>, the
	 * method <code>computeDerivatives</code> is called to reevaluate the
	 * derivatives at the current time, hence becoming part of the simulation
	 * protocol.
	 * 
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#internalTransition()
	 */
	@Override
	public void			internalTransition()
	{
		if (this.hasDebugLevel(2)) {
			this.logMessage(
					"AtomicHIOAwithDE>>internalTransition " + this.uri);
		}

		assert	this.getCurrentStateTime().
									lessThanOrEqual(this.timeOfNextEvent);

		Duration elapsedTime =
				this.getTimeOfNextEvent().subtract(this.getCurrentStateTime());
		this.currentStateTime = this.getTimeOfNextEvent();

		this.userDefinedInternalTransition(elapsedTime);
		this.computeDerivatives();

		this.nextTimeAdvance = this.timeAdvance();
		this.timeOfNextEvent =
						this.currentStateTime.add(this.nextTimeAdvance);

		// Postconditions
		if (!this.getTimeOfNextEvent().equals(Time.INFINITY)) {
			assert	this.getTimeOfNextEvent().subtract(
												this.getCurrentStateTime()).
									equals(this.getNextTimeAdvance());
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#produceOutput(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			produceOutput(Time current)
	{
		assert	current != null;

		if (this.getTimeOfNextEvent().equals(current)) {
			super.produceOutput(current);
		}
	}

	/**
	 * initialise the derivatives of the model, called immediately after
	 * initialising the state of the model.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
	protected abstract void	initialiseDerivatives();

	/**
	 * compute the derivatives of the model variables, called immediately after
	 * performing the user defined state transition.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
	protected abstract void	computeDerivatives();
}
// -----------------------------------------------------------------------------
