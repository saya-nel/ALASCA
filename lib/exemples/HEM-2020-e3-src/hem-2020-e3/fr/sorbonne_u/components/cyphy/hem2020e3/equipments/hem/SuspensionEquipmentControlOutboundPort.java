package fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hem;

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

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.SuspensionEquipmentControlCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

// -----------------------------------------------------------------------------
/**
 * The class <code>SuspensionEquipmentControlOutboundPort</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-12-30</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			SuspensionEquipmentControlOutboundPort
extends		AbstractOutboundPort
implements	SuspensionEquipmentControlCI
{
	private static final long serialVersionUID = 1L;

	public				SuspensionEquipmentControlOutboundPort(
		ComponentI owner
		) throws Exception
	{
		super(SuspensionEquipmentControlCI.class, owner);
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.StandardEquipmentControlCI#on()
	 */
	@Override
	public boolean		on() throws Exception
	{
		return ((SuspensionEquipmentControlCI)this.getConnector()).on();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.StandardEquipmentControlCI#off()
	 */
	@Override
	public boolean		off() throws Exception
	{
		return ((SuspensionEquipmentControlCI)this.getConnector()).off();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.StandardEquipmentControlCI#upMode()
	 */
	@Override
	public boolean		upMode() throws Exception
	{
		return ((SuspensionEquipmentControlCI)this.getConnector()).upMode();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.StandardEquipmentControlCI#downMode()
	 */
	@Override
	public boolean		downMode() throws Exception
	{
		return ((SuspensionEquipmentControlCI)this.getConnector()).downMode();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.StandardEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean		setMode(int modeIndex) throws Exception
	{
		return ((SuspensionEquipmentControlCI)this.getConnector()).
														setMode(modeIndex);
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.StandardEquipmentControlCI#currentMode()
	 */
	@Override
	public int			currentMode() throws Exception
	{
		return ((SuspensionEquipmentControlCI)this.getConnector()).
														currentMode();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.SuspensionEquipmentControlCI#suspended()
	 */
	@Override
	public boolean		suspended() throws Exception
	{
		return ((SuspensionEquipmentControlCI)this.getConnector()).suspended();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.SuspensionEquipmentControlCI#suspend()
	 */
	@Override
	public boolean		suspend() throws Exception
	{
		return ((SuspensionEquipmentControlCI)this.getConnector()).suspend();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.SuspensionEquipmentControlCI#resume()
	 */
	@Override
	public boolean		resume() throws Exception
	{
		return ((SuspensionEquipmentControlCI)this.getConnector()).resume();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.SuspensionEquipmentControlCI#emergency()
	 */
	@Override
	public double		emergency() throws Exception
	{
		return ((SuspensionEquipmentControlCI)this.getConnector()).emergency();
	}
}
// -----------------------------------------------------------------------------
