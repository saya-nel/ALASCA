package fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler;

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
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cyphy.hem2020e3.RunSILSimulation;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import java.util.concurrent.TimeUnit;

// -----------------------------------------------------------------------------
/**
 * The class <code>BoilerWaterTemperatureReactiveController</code> implements
 * a simple reactive controller with hysteresis for a boiler.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	{@code Boiler.EXTERNAL_TEMP < targetTemp}
 * invariant	{@code targetTemp < Boiler.STANDARD_HEATING_TEMP}
 * </pre>
 * 
 * <p>Created on : 2021-01-04</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
//-----------------------------------------------------------------------------
@RequiredInterfaces(required={BoilerSensorCI.class,BoilerActuatorCI.class})
//-----------------------------------------------------------------------------
public class			BoilerReactiveController
extends		AbstractComponent
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** sensor outbound port connected to the boiler component.				*/
	protected BoilerSensorOutboundPort		sensorOBP;
	/** actuator outbound port connected to the boiler component.			*/
	protected BoilerActuatorOutboundPort	actuatorOBP;
	/** true if the boiler is currently heating.							*/
	protected boolean						heating;
	/** period of the reactive controller.									*/
	protected static final long				CONTROL_PERIOD = 1000L;
	/** total number of control loop executions (for testing purposes).		*/
	protected static final int				TOTAL_LOOPS = 10;
	/** number of control loops executed so far.							*/
	protected int							numberOfLoops;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/** target water temperature (in celcius).								*/
	protected static final double			TARGET_TEMP = 52.0;
	/** the tolerance (in celcius) on the target water temperature to get
	 *  a control with hysteresis.											*/
	protected static final double			TARGET_TOLERANCE = 1.0;

	/**
	 * create a boiler reactive controller component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
	protected			BoilerReactiveController()
	{
		// one scheduled executor service used to execute the control loop
		super(1, 1);

		this.tracer.get().setTitle("Boiler reactive controller component");
		this.tracer.get().setRelativePosition(1, 1);
		this.toggleTracing();		
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void start() throws ComponentStartException
	{
		super.start();

		this.numberOfLoops = 0;
		try {
			this.sensorOBP = new BoilerSensorOutboundPort(this);
			this.sensorOBP.publishPort();
			this.doPortConnection(
					this.sensorOBP.getPortURI(),
					Boiler.SENSOR_INBOUND_PORT_URI,
					BoilerSensorConnector.class.getCanonicalName());
			this.actuatorOBP = new BoilerActuatorOutboundPort(this);
			this.actuatorOBP.publishPort();
			this.doPortConnection(
					this.actuatorOBP.getPortURI(),
					Boiler.ACTUATOR_INBOUND_PORT_URI,
					BoilerActuatorConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e) ;
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void execute() throws Exception
	{
		super.execute();

		this.heating = false;
		double delay = CONTROL_PERIOD/RunSILSimulation.ACC_FACTOR;
		this.scheduleTask(
				o ->	{ try {
							((BoilerReactiveController)o).controlLoop();
					   	  } catch (Exception e) {
					   		throw new RuntimeException(e);
					   	  }
					 	},
				RunSILSimulation.DELAY_TO_START_SIMULATION + (long)delay,
				TimeUnit.MILLISECONDS);
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.sensorOBP.getPortURI());
		this.doPortDisconnection(this.actuatorOBP.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.sensorOBP.unpublishPort();
			this.actuatorOBP.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component internal services
	// -------------------------------------------------------------------------

	/**
	 * water temperature control.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @throws Exception	<i>to do</i>.
	 */
	protected synchronized void		controlLoop() throws Exception
	{
		double currentTemp = this.sensorOBP.getWaterTemperatureInCeclsius();
		if (this.heating) {
			if (currentTemp > TARGET_TEMP + TARGET_TOLERANCE) {
				this.logMessage("Boiler reactive controller stops heating.");
				this.actuatorOBP.stopHeating();
				this.heating = false;
			} else {
				this.logMessage("Boiler reactive controller heats.");				
			}
		} else {
			if (currentTemp < TARGET_TEMP - TARGET_TOLERANCE) {
				this.logMessage("Boiler reactive controller starts heating.");
				this.actuatorOBP.startHeating();
				this.heating = true;
			} else {
				this.logMessage("Boiler reactive controller does not heat.");
			}
		}
		this.numberOfLoops++;
		if (this.numberOfLoops < TOTAL_LOOPS) {
			double delay = CONTROL_PERIOD/RunSILSimulation.ACC_FACTOR;
			this.scheduleTask(o -> { try {
										((BoilerReactiveController)o).
																controlLoop();
									 } catch (Exception e) {
										 throw new RuntimeException(e);
									 }
								   },
							  (long)delay, TimeUnit.MILLISECONDS);
		}
	}
}
// -----------------------------------------------------------------------------
