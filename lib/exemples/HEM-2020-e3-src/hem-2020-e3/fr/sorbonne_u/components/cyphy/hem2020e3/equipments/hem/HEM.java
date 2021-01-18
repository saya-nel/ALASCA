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

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.EquipmentRegistrationCI;
import fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.PlanningEquipmentControlCI;
import fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.SuspensionEquipmentControlCI;
import fr.sorbonne_u.components.cyphy.hem2020e3.RunSILSimulation;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.Boiler;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

// -----------------------------------------------------------------------------
/**
 * The class <code>HEM</code> prefigures the household energy manager component.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * Just a quick implementation for testing and show how to call a controlled
 * appliance.
 * </p>
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
//-----------------------------------------------------------------------------
@OfferedInterfaces(offered={EquipmentRegistrationCI.class})
@RequiredInterfaces(required={PlanningEquipmentControlCI.class,
							  SuspensionEquipmentControlCI.class})
//-----------------------------------------------------------------------------
public class			HEM
extends		AbstractCyPhyComponent
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** outbound port to the boiler; in the project, the exact appliance
	 *  would not be known, only its suspension capabilities.				*/
	protected SuspensionEquipmentControlOutboundPort	boilerControlOBP;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * creating the HEM component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
	protected			HEM()
	{
		super(1, 0);
		this.initialise();
	}

	/**
	 * create the HEM component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param reflectionInboundPortURI URI of the reflection inbound port.
	 */
	protected			HEM(String reflectionInboundPortURI)
	{
		super(reflectionInboundPortURI, 1, 0);
		this.initialise();
	}

	/**
	 * initialise the HEM component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
	protected void		initialise()
	{
		this.tracer.get().setTitle("Household energy manager component");
		this.tracer.get().setRelativePosition(3, 1);
		this.toggleTracing();
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();

		try {
			this.boilerControlOBP =
					new SuspensionEquipmentControlOutboundPort(this);
			this.boilerControlOBP.publishPort();
			this.doPortConnection(
					this.boilerControlOBP.getPortURI(),
					Boiler.CONTROL_INBOUND_PORT_URI,
					BoilerControlConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void	execute() throws Exception
	{
		super.execute();

		this.logMessage("HEM waiting for the simulation to begin.");
		Thread.sleep(RunSILSimulation.DELAY_TO_START_SIMULATION);;

		double delay = 2435L/RunSILSimulation.ACC_FACTOR;
		this.logMessage("HEM waiting before suspending the boiler.");
		Thread.sleep((long)delay);
		this.logMessage("HEM suspending the boiler.");
		this.boilerControlOBP.suspend();
		delay = 4000L/RunSILSimulation.ACC_FACTOR;
		this.logMessage("HEM waiting before resuming the boiler.");
		Thread.sleep((long)delay);
		this.logMessage("HEM resuming the boiler.");
		this.boilerControlOBP.resume();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.boilerControlOBP.getPortURI());
		this.boilerControlOBP.unpublishPort();
		super.finalise();
	}
}
// -----------------------------------------------------------------------------
