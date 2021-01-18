package fr.sorbonne_u.devs_simulation.hioa.models.vars;

import java.util.concurrent.ScheduledFuture;

import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA;
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

// -----------------------------------------------------------------------------
/**
 * The class <code>CausalValue</code> implements value holder for HIOA models
 * that allows depending models to trigger a causal transition on models upon
 * which it depends before execution its own internal transition.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * This class is meant to be used to implement simulation models which update
 * their state in a lazy evaluation manner. It is particularly interesting
 * when a HIOA model is implemented using deterministic equations which can
 * be evaluated only when an, updated value is needed rather than regularly as
 * models with differential equations.
 * </p>
 * <p>
 * However, mixing in one model causal and internal transitions can be tricky,
 * hence it is probably a good practice to either have a model that is evaluated
 * by internal transitions only or by causal transitions only.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-11-30</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			CausalValue<Type>
extends		Value<Type>
{
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a causal value instance for the given variable and the given owner
	 * with an initial value but without history.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code owner != null}
	 * pre	{@code initialTime.getTimeUnit().equals(owner.getSimulatedTimeUnit())}
	 * pre	{@code initialTime.greaterThanOrEqual(Time.zero(timeUnit))}
	 * pre	{@code historySize >= 0}
	 * post	{@code this.owner.equals(owner)}
	 * post {@code v.equals(initialValue)}
	 * post	{@code time.equals(initialTime)}
	 * post	{@code Value.checkInvariant(this)}
	 * </pre>
	 *
	 * @param owner			model owning the variable.
	 * @param initialValue	the initial value has been computed/assigned.
	 * @throws Exception	<i>to do</i>.
	 */
	public				CausalValue(
		AtomicHIOA owner,
		Type initialValue
		) throws Exception
	{
		super(owner, initialValue);
	}

	/**
	 * create a causal value instance for the given variable and the given owner
	 * with an initial value computed at the given initial time and with
	 * the given history size.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code owner != null}
	 * pre	{@code initialTime.getTimeUnit().equals(owner.getSimulatedTimeUnit())}
	 * pre	{@code initialTime.greaterThanOrEqual(Time.zero(timeUnit))}
	 * pre	{@code historySize >= 0}
	 * post	{@code this.owner.equals(owner)}
	 * post {@code v.equals(initialValue)}
	 * post	{@code time.equals(initialTime)}
	 * post	{@code Value.checkInvariant(this)}
	 * </pre>
	 *
	 * @param owner			model owning the variable.
	 * @param initialValue	initial value of the variable.
	 * @param historySize	predefined size of the history.
	 * @throws Exception	<i>to do</i>.
	 */
	public				CausalValue(
		AtomicHIOA owner,
		Type initialValue,
		int historySize
		) throws Exception
	{
		super(owner, initialValue, historySize);
	}


	/**
	 * create a value instance for the given variable and the given owner
	 * and the given variable descriptor with an initial value computed at
	 * the given initial time and with the given history size.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code owner != null}
	 * pre	{@code descriptor != null}
	 * pre	{@code initialTime.getTimeUnit().equals(owner.getSimulatedTimeUnit())}
	 * pre	{@code initialTime.greaterThanOrEqual(Time.zero(timeUnit))}
	 * pre	{@code historySize >= 0}
	 * post	{@code this.owner.equals(owner)}
	 * post	{@code this.descriptor.equals(descriptor)}
	 * post {@code v.equals(initialValue)}
	 * post	{@code time.equals(initialTime)}
	 * post	{@code Value.checkInvariant(this)}
	 * </pre>
	 *
	 * @param owner			model owning the variable.
	 * @param descriptor	descriptor of the variable.
	 * @param initialValue	initial value of the variable.
	 * @param initialTime	the time at which the initial value has been computed/assigned.
	 * @param historySize	predefined size of the history.
	 * @throws Exception	<i>to do</i>.
	 */
	public				CausalValue(
		AtomicHIOA owner,
		VariableDescriptor descriptor,
		Type initialValue,
		Time initialTime,
		int historySize
		) throws Exception
	{
		super(owner, descriptor, initialValue, initialTime, historySize);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * give the possibility to the model which produces this value to update
	 * its state immediately.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param currentTime	simulated time at which the update is mandated.
	 * @return				a scheduled future allowing to synchronise the caller with the end of the execution of this update.
	 */
	public ScheduledFuture<?>	update(Time currentTime)
	{
		try {
			return this.owner.update(currentTime);
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
// -----------------------------------------------------------------------------
