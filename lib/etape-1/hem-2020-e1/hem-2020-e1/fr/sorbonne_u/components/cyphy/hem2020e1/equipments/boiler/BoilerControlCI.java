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

import fr.sorbonne_u.components.interfaces.OfferedCI;

// -----------------------------------------------------------------------------
/**
 * The component interface <code>BoilerControlCI</code> defines the services
 * the boiler offers to control its operation and its operation mode. 
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The boiler has two modes of operation, a standard mode where it consumes
 * more energy but heats the water faster and an economy mode where it consumes
 * less energy but heats the water slower. It can also be activated to run
 * normally or passivated to preserve energy for a while (but does not heat
 * the water).
 * </p>
 * <p>
 * The mode of the boiler are defined as integer constants instead of enumerated
 * values. This is because in this project, code will be generated at run time
 * using the tool Javassist which compiler does not accept the Java syntax
 * introduced after v1.4. Hence, this restriction of the tool hurts the good
 * design a bit.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2020-09-24</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public interface		BoilerControlCI
extends		OfferedCI,
			BoilerControlImplementationI
{
	/** economy mode of operation for the boiler.							*/
	public static final int		ECO = 1;
	/** standard mode of operation for the boiler.							*/
	public static final int		STD = 2;

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#switchOn(int)
	 */
	@Override
	public boolean		switchOn(int initialMode) throws Exception;

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#switchOff()
	 */
	@Override
	public boolean		switchOff() throws Exception;
	
	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#eco()
	 */
	@Override
	public void			eco() throws Exception;

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#std()
	 */
	@Override
	public void			std() throws Exception;

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#mode()
	 */
	@Override
	public int			mode() throws Exception;

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#active()
	 */
	@Override
	public boolean		active() throws Exception;

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#passivate()
	 */
	@Override
	public boolean		passivate() throws Exception;

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#activate()
	 */
	@Override
	public boolean		activate() throws Exception;

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#degreeOfEmergency()
	 */
	@Override
	public double		degreeOfEmergency() throws Exception;
}
// -----------------------------------------------------------------------------
