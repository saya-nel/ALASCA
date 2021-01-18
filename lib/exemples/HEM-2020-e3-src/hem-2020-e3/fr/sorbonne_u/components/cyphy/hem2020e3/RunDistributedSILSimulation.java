package fr.sorbonne_u.components.cyphy.hem2020e3;

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

import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.Boiler;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.BoilerReactiveController;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.HairDryer;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hem.HEM;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.meter.ElectricMeter;

// -----------------------------------------------------------------------------
/**
 * The class <code>RunDistributedSILSimulation</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2021-01-07</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			RunDistributedSILSimulation
extends		AbstractDistributedCVM
{
	public static final String	HEM_JVM_URI = "HEM";
	public static final String	ELECTRIC_METER_JVM_URI = "ElectricMeter";
	public static final String	HAIR_DRYER_JVM_URI = "Hairdryer";
	public static final String	BOILER_JVM_URI = "Boiler";

	public				RunDistributedSILSimulation(String[] args)
	throws Exception
	{
		super(args);
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#instantiateAndPublish()
	 */
	@Override
	public void			instantiateAndPublish() throws Exception
	{
		if (thisJVMURI.equals(HEM_JVM_URI)) {
			AbstractComponent.createComponent(
					HEM.class.getCanonicalName(),
					new Object[]{});
			AbstractComponent.createComponent(
					HEMSimulationCoordinator.class.getCanonicalName(),
					new Object[]{});
			AbstractComponent.createComponent(
					HEMSimulationSupervisor.class.getCanonicalName(),
					new Object[]{});
		} else if (thisJVMURI.equals(ELECTRIC_METER_JVM_URI)) {
			AbstractComponent.createComponent(
					ElectricMeter.class.getCanonicalName(),
					new Object[]{false});
		} else if (thisJVMURI.equals(HAIR_DRYER_JVM_URI)) {
			AbstractComponent.createComponent(
					HairDryer.class.getCanonicalName(),
					new Object[]{RunSILSimulation.HAIR_DRYER_INBOUND_PORT_URI,
								 true, false});
		} else if (thisJVMURI.equals(BOILER_JVM_URI)) {
			AbstractComponent.createComponent(
					Boiler.class.getCanonicalName(),
					new Object[]{true});
			AbstractComponent.createComponent(
					BoilerReactiveController.class.getCanonicalName(), 
					new Object[]{});
		} else {
			System.out.println("Unknown JVM URI... " + thisJVMURI);
		}
		super.instantiateAndPublish();
	}

	public static void	main(String[] args)
	{
		try {
			RunDistributedSILSimulation cvm =
									new RunDistributedSILSimulation(args);
			long d = TimeUnit.SECONDS.toMillis(
					(long)(RunSILSimulation.SIMULATION_DURATION/
											RunSILSimulation.ACC_FACTOR));
			cvm.startStandardLifeCycle(d + 600000L);
			Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
// -----------------------------------------------------------------------------
