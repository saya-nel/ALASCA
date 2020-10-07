package fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide an
// example of a cyber-physical system.
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
 * The interface <code>BoilerControlImplementationI</code> defines the
 * operations to be implemented to control the boiler and its operation mode. 
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2020-10-02</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public interface		BoilerControlImplementationI
{
	/**
	 * switch the boiler on.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code initialMode == BoilerControlCI.STD || initialMode == BoilerControlCI.ECO}
	 * post	{@code mode() == initialMode}
	 * </pre>
	 *
	 * @param initialMode	mode in which the boiler is put initially.
	 * @return				true if the boiler has been switched on, false otherwise.
	 * @throws Exception	<i>to do</i>.
	 */
	public boolean		switchOn(int initialMode) throws Exception;

	/**
	 * switch the boiler off.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @return				true if the boiler has been switched off, false otherwise.
	 * @throws Exception	<i>to do</i>.
	 */
	public boolean		switchOff() throws Exception;
	
	/**
	 * pass the boiler on the economy mode of operation.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code mode() == BoilerControlCI.STD}
	 * post	{@code mode() == BoilerControlCI.ECO}
	 * </pre>
	 *
	 * @throws Exception	<i>to do</i>.
	 */
	public void			eco() throws Exception;

	/**
	 * pass the boiler on the standard mode of operation.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code mode() == BoilerControlCI.ECO}
	 * post	{@code mode() == BoilerControlCI.STD}
	 * </pre>
	 *
	 * @throws Exception	<i>to do</i>.
	 */
	public void			std() throws Exception;

	/**
	 * get the current mode of operation of the boiler.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	{@code return == BoilerControlCI.STD || return == BoilerControlCI.ECO}
	 * </pre>
	 *
	 * @return				the current mode of operation of the boiler.
	 * @throws Exception	<i>to do</i>.
	 */
	public int			mode() throws Exception;

	/**
	 * return true if the boiler is currently active (not suspended).
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @return				true if the boiler is currently running (not suspended).
	 * @throws Exception	<i>to do</i>.
	 */
	public boolean		active() throws Exception;

	/**
	 * pass the boiler in the passive (suspended) state.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code active()}
	 * post	{@code mode() == mode().pre}
	 * post	{@code !active()}
	 * </pre>
	 *
	 * @return				true if the boiler has been successfully passivated, false otherwise.
	 * @throws Exception	<i>to do</i>.
	 */
	public boolean		passivate() throws Exception;

	/**
	 * pass the boiler in the passive (active) state.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code !active()}
	 * post	{@code mode() == mode().pre}
	 * post	{@code active()}
	 * </pre>
	 *
	 * @return				true if the boiler has been successfully activated, false otherwise.
	 * @throws Exception	<i>to do</i>.
	 */
	public boolean		activate() throws Exception;

	/**
	 * return the degree of emergency to reactivate the boiler after passivating it.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code !active()}
	 * post	{@code return >= 0.0 && return <= 1.0}
	 * </pre>
	 *
	 * @return				the degree of emergency to reactivate the boiler after passivating it.
	 * @throws Exception	<i>to do</i>.
	 */
	public double		degreeOfEmergency() throws Exception;

}
// -----------------------------------------------------------------------------
