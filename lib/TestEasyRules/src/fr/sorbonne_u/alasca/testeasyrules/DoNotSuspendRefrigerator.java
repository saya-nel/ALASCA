package fr.sorbonne_u.alasca.testeasyrules;

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

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

// -----------------------------------------------------------------------------
/**
 * The class <code>DoNotSuspendRefrigerator</code> represents an inference rule
 * for the Easy Rules inference engine.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The <code>DoNotSuspendRefrigerator</code> establishes that when  the energy
 * production margin (the value of a fact with URI
 * <code>TestEasyRules.CURRENT_PRODUCTION_MARGIN</code>) is greater than or
 * equal to the threshold <code>PRODUCTION_MARGIN_THRESHOLD</code> then
 * the fact with URI <code>TestEasyRules.SUSPEND_REFRIGERATOR</code>
 * with the value <code>NO</code> is asserted.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2019-12-10</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
@Rule(name="do not suspend refrigerator",
	  description="produces a short delay decision for refrigerator suspension"
		      + " when the indoor temperature is high and the energy "
		      + "production margin is low",
	  // lower priority for rules that first infer from other inferences
	  priority=2)
public class			DoNotSuspendRefrigerator
{
	@Condition
	public boolean		when(
		@Fact(RefrigeratorSuspensionDecision.CURRENT_PRODUCTION_MARGIN)
																double margin
		)
	{
		return margin >= SuspendRefrigerator.PRODUCTION_MARGIN_THRESHOLD ;
	}

	@Action
	public void			establish(Facts facts)
	{
		facts.put(RefrigeratorSuspensionDecision.CAN_SUSPEND_REFRIGERATOR,
								SuspendRefrigerator.SuspensionDecision.NO) ;
	}
}
// -----------------------------------------------------------------------------
