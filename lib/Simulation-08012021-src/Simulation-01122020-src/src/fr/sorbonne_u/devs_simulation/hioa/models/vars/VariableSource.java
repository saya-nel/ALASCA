package fr.sorbonne_u.devs_simulation.hioa.models.vars;

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
 * The class <code>VariableSource</code> describes exported variables of
 * submodels either to define the connections among submodels when
 * creating a coupled model or to define how a coupled model reexports
 * the exported variables of its submodels.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	{@code name != null}
 * invariant	{@code type != null}
 * invariant	{@code exportingModelURI != null}
 * </pre>
 * 
 * <p>Created on : 2018-06-29</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			VariableSource
{
	/** name of the exported variable.										*/
	public final String		name;
	/** type of the exported variable.										*/
	public final Class<?>	type;
	/** URI of the model exporting the variable.							*/
	public final String		exportingModelURI;

	/**
	 * create a variable source description.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code name != null}
	 * pre	{@code type != null}
	 * pre	{@code exportingModelURI != null}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param name				name of the exported variable.
	 * @param type				type of the exported variable.
	 * @param exportingModelURI	URI of the model exporting the variable.
	 */
	public				VariableSource(
		String name,
		Class<?> type,
		String exportingModelURI
		)
	{
		super();

		assert	name != null;
		assert	type != null;
		assert	exportingModelURI != null;

		this.name = name;
		this.type = type;
		this.exportingModelURI = exportingModelURI;
	}
}
// -----------------------------------------------------------------------------
