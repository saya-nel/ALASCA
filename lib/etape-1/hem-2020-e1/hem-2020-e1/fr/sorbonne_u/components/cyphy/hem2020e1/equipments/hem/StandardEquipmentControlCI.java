package fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem;

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

import fr.sorbonne_u.components.interfaces.RequiredCI;

// -----------------------------------------------------------------------------
/**
 * The component interface <code>StandardEquipmentControlCI</code> defines
 * the operations that can be performed by a controller on a controllable
 * equipment.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-09-24</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public interface		StandardEquipmentControlCI
extends		RequiredCI
{
	/**
	 * switch on the equipment, returning true if the operation succeeded or
	 * false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @return	true if the operation succeeded or false otherwise.
	 */
	public boolean		on();

	/**
	 * switch off the equipment, returning true if the operation succeeded or
	 * false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @return	true if the operation succeeded or false otherwise.
	 */
	public boolean		off();

	/**
	 * force the equipment to the next more energy consuming mode of operation,
	 * returning true if the operation succeeded or false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @return	true if the operation succeeded or false otherwise.
	 */
	public boolean		upMode();

	/**
	 * force the equipment to the next less energy consuming mode of operation,
	 * returning true if the operation succeeded or false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @return	true if the operation succeeded or false otherwise.
	 */
	public boolean		downMode();

	/**
	 * set the equipment to the given mode of operation, returning true if the
	 * operation succeeded or false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code modeIndex > 0}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param modeIndex	index of the new mode of operation.
	 * @return			true if the operation succeeded or false otherwise.
	 */
	public boolean		setMode(int modeIndex);

	/**
	 * return the current mode of operation of the equipment.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	{@code return > 0}
	 * </pre>
	 *
	 * @return	the current mode of operation of the equipment.
	 */
	public int			currentMode();
}
// -----------------------------------------------------------------------------
