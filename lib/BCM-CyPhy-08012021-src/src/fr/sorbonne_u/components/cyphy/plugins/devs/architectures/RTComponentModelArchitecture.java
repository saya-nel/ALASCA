package fr.sorbonne_u.components.cyphy.plugins.devs.architectures;

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

import java.util.Map;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.devs_simulation.architectures.RTArchitecture;
import fr.sorbonne_u.devs_simulation.models.architectures.AbstractAtomicModelDescriptor;
import fr.sorbonne_u.devs_simulation.models.architectures.CoupledModelDescriptor;

// -----------------------------------------------------------------------------
/**
 * The class <code>RTComponentModelArchitecture</code> implements real time
 * component model simulation architectures.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-12-29</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			RTComponentModelArchitecture
extends		ComponentModelArchitecture
{
	private static final long serialVersionUID = 1L;

	/**
	 * create a real time component model architecture.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code architectureURI != null && !architectureURI.isEmpty()}
	 * pre	{@code rootModelURI != null && !rootModelURI.isEmpty()}
	 * pre	{@code atomicModelDescriptors != null}
	 * pre	{@code coupledModelDescriptors != null}
	 * pre	{@code simulationTimeUnit != null}
	 * pre	{@code atomicModelDescriptors.containsKey(rootModelURI) || coupledModelDescriptors.containsKey(rootModelURI)}
	 * pre	{@code forall md in atomicModelDescriptors.values() : md instanceof ComponentAtomicModelDescriptor }
	 * pre	{@code forall md in coupledModelDescriptors.values() : md instanceof ComponentCoupledModelDescriptor }
	 * post	{@code !isComplete() || Architecture.checkInvariant(this)}
	 * post	{@code !isComplete() || RTArchitecture.checkInvariant(this)}
	 * post	{@code !isComplete() || ComponentModelArchitecture.checkInvariant(this)}
	 * </pre>
	 *
	 * @param architectureURI			URI of the architecture.
	 * @param rootModelURI				URI of the root model in the architecture.
	 * @param atomicModelDescriptors	map from atomic model URIs to their atomic model descriptors.
	 * @param coupledModelDescriptors	map from coupled model URIs to their coupled model descriptors.
	 * @param simulationTimeUnit		time unit for the simulation clocks.
	 * @throws Exception				<i>to do</i>.
	 */
	public				RTComponentModelArchitecture(
		String architectureURI,
		String rootModelURI,
		Map<String, AbstractAtomicModelDescriptor> atomicModelDescriptors,
		Map<String, CoupledModelDescriptor> coupledModelDescriptors,
		TimeUnit simulationTimeUnit
		) throws Exception
	{
		super(architectureURI, rootModelURI, atomicModelDescriptors,
			  coupledModelDescriptors, simulationTimeUnit);

		assert	!this.isComplete() || RTArchitecture.checkInvariant(this);
	}

	// -------------------------------------------------------------------------
	// Instance methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.architectures.Architecture#isRealTime()
	 */
	@Override
	public boolean		isRealTime()
	{
		return true;
	}
}
// -----------------------------------------------------------------------------
