package fr.sorbonne_u.components.cyphy.hem2020e3.equipments.meter;

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

import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.meter.sil.ElectricMeterSILCoupledModel;
import fr.sorbonne_u.components.exceptions.ComponentStartException;

// -----------------------------------------------------------------------------
/**
 * The class <code>ElectricMeter</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * TODO: add a service allowing to get the current total electricity
 * consumption.
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2020-12-18</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			ElectricMeter
extends		AbstractCyPhyComponent
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	public static final String		REFLECTION_INBOUHD_PORT_URI ="EMRIB-URI";
	/** true if the component is executed for unit testing.					*/
	protected boolean				isUnitTesting;
	/** the atomic simulator plug-in of the component.						*/
	protected ElectricMeterRTAtomicSimulatorPlugin	simulatorPlugin;
	/**	URI of the executor service used to perform the simulation.			*/
	protected static final String	SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create the electric meter component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param isUnitTesting	true if the component is executed in unit testing.
	 */
	protected			ElectricMeter(boolean isUnitTesting)
	{
		super(REFLECTION_INBOUHD_PORT_URI, 1, 0);

		this.isUnitTesting = isUnitTesting;

		this.tracer.get().setTitle("Electric meter component");
		this.tracer.get().setRelativePosition(3, 0);
		this.toggleTracing();		
	}

	// -------------------------------------------------------------------------
	// Component life-cycle.
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();

		try {
			// create the scheduled executor service that will run the
			// simulation tasks
			this.createNewExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
			// create and initialise the atomic simulator plug-in that will
			// hold and execute the SIL simulation models
			this.simulatorPlugin = new ElectricMeterRTAtomicSimulatorPlugin();
			this.simulatorPlugin.setPluginURI(ElectricMeterSILCoupledModel.URI);
			this.simulatorPlugin.
				setSimulationExecutorService(SCHEDULED_EXECUTOR_SERVICE_URI);
			this.simulatorPlugin.initialiseSimulationArchitecture();
			this.installPlugin(this.simulatorPlugin);
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
// -----------------------------------------------------------------------------
