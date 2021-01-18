package fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer;

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

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

// -----------------------------------------------------------------------------
/**
 * The class <code>CVMUnitTest</code> performs unit tests on the hair dryer
 * component.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The class can perform two types of unit tests: a standard unit test with
 * a tester component that calls the different services on the hair dryer
 * component and a SIL simulation unit test where the SIL simulation with a
 * user simulation model will trigger actions on the hair dryer component.
 * The latter allows to test both the software of the component and the models
 * for the simulation.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2020-10-05</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			CVMUnitTest
extends		AbstractCVM
{
	/** URI of the hair dryer inbound port offering the interface
	 *  <code>HairDryerCI</code>.											*/
	protected final static String	HAIR_DRYER_INBOUND_PORT_URI = "hdip-URI";
	/** if true, the unit test is driven by the SIL simulation, otherwise
	 *  it is driven by the component <code>HairDryerUnitTester</code>.		*/
	protected final static boolean	SIL_UNIT_TEST = true;

	public				CVMUnitTest() throws Exception
	{
		
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void			deploy() throws Exception
	{
		AbstractComponent.createComponent(
					HairDryer.class.getCanonicalName(),
					new Object[]{HAIR_DRYER_INBOUND_PORT_URI, SIL_UNIT_TEST,
								 true});
		if (!SIL_UNIT_TEST) {
			AbstractComponent.createComponent(
					HairDryerUnitTester.class.getCanonicalName(),
					new Object[]{HAIR_DRYER_INBOUND_PORT_URI});
		}

		super.deploy();
	}

	public static void		main(String[] args)
	{
		try {
			CVMUnitTest cvm = new CVMUnitTest();
			cvm.startStandardLifeCycle(SIL_UNIT_TEST ? 12000L : 2000L);
			Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
// -----------------------------------------------------------------------------
