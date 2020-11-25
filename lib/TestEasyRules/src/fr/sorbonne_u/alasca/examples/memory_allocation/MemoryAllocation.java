package fr.sorbonne_u.alasca.examples.memory_allocation;

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

import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import fr.sorbonne_u.alasca.easyrules.extensions.FactsChanged;

// -----------------------------------------------------------------------------
/**
 * The class <code>MemoryAllocation</code> illustrates the use of the Easy Rules
 * library on an example where decisions are made about allocating more memory
 * to a process when its memory consumption reaches thresholds in terms of
 * percentage of the currently allocated memory that is actually used by the
 * process.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2019-12-09</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			MemoryAllocation
{
	public static final String	MEMORY_CONSUMPTION_PERCENTAGE =
										"memory consumption percentage" ;
	public static final String	HIGH_MEMORY_CONSUMPTION_PERCENTAGE =
										"high memory consumption percentage" ;
	public static final String	MEDIUM_MEMORY_CONSUMPTION_PERCENTAGE =
										"medium memory consumption percentage" ;
	public static final String	AVAILABLE_MEMORY = "available memory" ;
	public static final String	CURRENTLY_ALLOCATED_MEMORY =
										"currently allocated memory" ;
	public static final String	MEMORY_ALLOCATION_DECISION =
										"memory allocation decision" ;

	public static void	main(String[] args)
	{
		// Creating a new default rule engine
		RulesEngine re = new DefaultRulesEngine() ;
		// Show the parameters of the rule engine i.e., controlling its
		// inference behaviour.
		// System.out.println(re.getParameters()) ;

		// Creating a new rule set
		Rules rules = new Rules() ;
		// Rules in Easy Rules are defined as classes annotated by @Rule and
		// they must be registered in a rule before being applied by a rule
		// engine
		rules.register(new HighMemoryConsumption()) ;
		rules.register(new MediumMemoryConsumption()) ;
		rules.register(new LowMemoryConsumption()) ;
		rules.register(new HighMemoryAllocationDecision()) ;
		rules.register(new LowMemoryAllocationDecision()) ;
		rules.register(new NoMemoryAllocationDecision()) ;

		// Facts in Easy Rules are defined as a mapping of fact URIs to fact
		// values and put in a fact set.
		FactsChanged facts = new FactsChanged() ;
		facts.put(MEMORY_CONSUMPTION_PERCENTAGE, 60.0) ;
		facts.put(AVAILABLE_MEMORY, 1000.0) ;
		facts.put(CURRENTLY_ALLOCATED_MEMORY, 500.0) ;
		// Show the current facts
		System.out.println(facts.toString()) ;

		// Fire the rules until the set of facts do not change anymore i.e.,
		// knowledge base is "saturated".
		while (facts.hasChanged()) {
			facts.reset() ;
			re.fire(rules, facts) ;
			System.out.println(facts.toString()) ;
		}

		System.out.println("\nmemory allocation decision is " +
									facts.get(MEMORY_ALLOCATION_DECISION)) ;
	}
}
// -----------------------------------------------------------------------------
