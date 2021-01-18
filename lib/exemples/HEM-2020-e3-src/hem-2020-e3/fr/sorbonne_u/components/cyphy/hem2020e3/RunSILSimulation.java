package fr.sorbonne_u.components.cyphy.hem2020e3;

import java.util.concurrent.TimeUnit;

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

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.Boiler;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.BoilerReactiveController;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.HairDryer;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hem.HEM;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.meter.ElectricMeter;

// -----------------------------------------------------------------------------
/**
 * The class <code>RunSILSimulation</code> executes the SIL simulaiton of the
 * HEM example using one JVM.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-12-23</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			RunSILSimulation
extends		AbstractCVM
{
	/** acceleration factor for the real time execution.					*/
	public static final double		ACC_FACTOR = 2.0;
	/** delay to start the real time simulations on every model at the
	 *  same moment (the order is delivered to the models during this
	 *  delay.																*/
	public static final long		DELAY_TO_START_SIMULATION = 1000L;
	/** duration  of the simulation.										*/
	public static final double		SIMULATION_DURATION = 10.5;
	/** URI of the simulation architecture used in the execution.			*/
	public static final String		SIM_ARCHITECTURE_URI = "sil";
	/** URI of the inbound port of the hair dryer component.				*/
	protected final static String	HAIR_DRYER_INBOUND_PORT_URI = "hdip-URI";

	public				RunSILSimulation() throws Exception
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
				new Object[]{HAIR_DRYER_INBOUND_PORT_URI, true, false});
		AbstractComponent.createComponent(
				Boiler.class.getCanonicalName(),
				new Object[]{true});
		AbstractComponent.createComponent(
				BoilerReactiveController.class.getCanonicalName(), 
				new Object[]{});
		AbstractComponent.createComponent(
				ElectricMeter.class.getCanonicalName(), new Object[]{false});
		AbstractComponent.createComponent(
				HEM.class.getCanonicalName(),
				new Object[]{});

		AbstractComponent.createComponent(
				HEMSimulationCoordinator.class.getCanonicalName(),
				new Object[]{});
		AbstractComponent.createComponent(
				HEMSimulationSupervisor.class.getCanonicalName(),
				new Object[]{});

		super.deploy();
	}

	public static void	main(String[] args)
	{
		try {
			RunSILSimulation r = new RunSILSimulation();
			long d = TimeUnit.SECONDS.toMillis(
									(long)(SIMULATION_DURATION/ACC_FACTOR));
			r.startStandardLifeCycle(d + 5000L);
			Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
// -----------------------------------------------------------------------------
