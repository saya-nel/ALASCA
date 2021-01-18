package fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil;

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

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.Boiler;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.StandardComponentLogger;
import fr.sorbonne_u.devs_simulation.hioa.annotations.InternalVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.annotations.ModelExternalEvents;
import fr.sorbonne_u.devs_simulation.models.events.Event;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

// -----------------------------------------------------------------------------
/**
 * The class <code>BoilerElectricitySILModel</code> defines a simple SIL
 * simulation model for a water boiler.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * A water boiler contains some amount of water that it must maintain at a
 * target temperature. The electricity consumption depends upon the fact that
 * the boiler is currently heating water or not. This model assumes that the
 * electricity consumption is either some constant when heating and zero when
 * not heating. But to model the changes from heating to non heating, we need
 * to also model the current water temperature in the boiler such as to define
 * a target temperature over which heating is turned off and under which it is
 * turned on (with some tolerance to have an inertia in the control).
 * </p>
 * <p>
 * The differential equation used to model the temperature has several
 * components, one for each of the contributing factor: heating plate
 * temperature and external temperature. For each of them, the derivative of
 * the water temperature is assumed proportional to the difference between the
 * water temperature and the plate temperature (resp. external temperature).
 * The heating contribution is either positive or zero as the temperature of
 * the plate is higher than the one of the water. The external room temperature
 * contribution is negative, as this temperature is lower than the temperature
 * of the water.
 * </p>
 * <p>
 * Another contribution not modelled here but that would be needed in a
 * faithful model: the temperature of the cold water poured in the boiler when
 * someone uses the hot water.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-11-18</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
// -----------------------------------------------------------------------------
@ModelExternalEvents(imported={Heat.class,DoNotHeat.class,
							   Passivate.class,Activate.class},
					 exported={Heat.class,DoNotHeat.class,
							   Passivate.class,Activate.class})
// -----------------------------------------------------------------------------
public class			BoilerWaterSILModel
extends		AtomicHIOAwithDE
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long		serialVersionUID = 1L;
	/** URI for an instance model; works as long as only one instance is
	 *  created.															*/
	public static final String		URI = BoilerWaterSILModel.class.
															getSimpleName();
	/** tracing flag.														*/
	public static boolean			TRACE = true;

	/** integration step for the differential equation(assumed in seconds).	*/
	protected static final double	STEP = 1.0;
	/** integration step as a duration, including the time unit.			*/
	protected final Duration		integrationStep;

	/** last received external event.										*/
	protected Event					lastReceivedEvent;

	/** URI of the variable pointing to the boiler component.				*/
	public static final String		BOILER_REFERENCE_NAME = URI + ":BRN";
	/** owner component.													*/
	protected Boiler				owner;

	// -------------------------------------------------------------------------
	// HIOA model variables
	// -------------------------------------------------------------------------

	/** the current water temperature.										*/
	@InternalVariable(type = Double.class)
	protected final Value<Double>	waterTemp = new Value<Double>(this, 0.0, 0);
	/** the current derivative of the water temperature.					*/
	protected double				currentTempDerivative = 0.0;
	
	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * create an instance of the boiler model.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param uri					unique identifier of the model.
	 * @param simulatedTimeUnit		time unit used for the simulation clock.
	 * @param simulationEngine		simulation engine enacting the model.
	 * @throws Exception			<i>to do</i>.
	 */
	public				BoilerWaterSILModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine);
		this.integrationStep = new Duration(STEP, simulatedTimeUnit);
	}

	// -------------------------------------------------------------------------
	// Instance methods
	// -------------------------------------------------------------------------

	/**
	 * return the current water temperature.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @return	the current water temperature.
	 */
	public double		getWaterTemperature()
	{
		return this.waterTemp.v;
	}

	// -------------------------------------------------------------------------
	// Simulation protocol and related methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			initialiseState(Time initialTime)
	{
		this.lastReceivedEvent = null;

		this.toggleDebugMode();
		this.logMessage("simulation begins.\n");

		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void		initialiseVariables(Time startTime)
	{
		// we assume that the water in the boiler is at the room temperature,
		// which would be true if the boiler was switched off during holidays,
		// for example.
		this.waterTemp.v = Boiler.EXTERNAL_TEMP;
		super.initialiseVariables(startTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void			setSimulationRunParameters(
		Map<String, Object> simParams
		) throws Exception
	{
		this.owner = (Boiler)simParams.get(BOILER_REFERENCE_NAME);
		this.setLogger(new StandardComponentLogger(this.owner));
		super.setSimulationRunParameters(simParams);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI> output()
	{
		if (this.lastReceivedEvent != null) {
			ArrayList<EventI> ret = new ArrayList<EventI>();
			ret.add(this.lastReceivedEvent);
			this.lastReceivedEvent = null;
			return ret;
		} else {
			return null;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration		timeAdvance()
	{
		if (this.lastReceivedEvent == null) {
			return this.integrationStep;
		} else {
			return Duration.zero(this.getSimulatedTimeUnit());
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE#initialiseDerivatives()
	 */
	@Override
	protected void		initialiseDerivatives()
	{
		this.computeDerivatives();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE#computeDerivatives()
	 */
	@Override
	protected void		computeDerivatives()
	{
		// this method is called at each internal transition, just after
		// executing the method userDefinedInternalTransition.
		this.currentTempDerivative = 0.0;
		try {
			if (this.owner.active() && this.owner.isHeating()) {
				// the heating contribution: temperature difference between the
				// plate and the water divided by the heat transfer constant
				this.currentTempDerivative =
						(Boiler.STANDARD_HEATING_TEMP - this.waterTemp.v)/
											Boiler.HEATING_TRANSFER_CONSTANT;
			}
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
		// the cooling contribution: temperature difference between the
		// room and the water divided by the insulation transfer constant
		this.currentTempDerivative +=
					(Boiler.EXTERNAL_TEMP - this.waterTemp.v)/
											Boiler.INSULATION_TRANSFER_CONSTANT;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			userDefinedInternalTransition(Duration elapsedTime)
	{
		super.userDefinedInternalTransition(elapsedTime);

		// update the water temperature using the Euler integration of the
		// differential equation
		Duration d = this.getCurrentStateTime().subtract(this.waterTemp.time);
		this.waterTemp.v =
				this.waterTemp.v +
						this.currentTempDerivative*d.getSimulatedDuration();
		this.waterTemp.time = this.getCurrentStateTime();

		// Tracing
		if (TRACE) {
			String mark = "";
			try {
				mark = (this.owner.active() && this.owner.isHeating()) ?
							" (h)" 		// heating
					   :	" (-)";		// not heating
			} catch (Exception e) {
				throw new RuntimeException(e) ;
			}
			StringBuffer message = new StringBuffer();
			message.append(this.waterTemp.time.getSimulatedTime());
			message.append(mark);
			message.append(": ");
			message.append(this.waterTemp.v);
			message.append("\n");
			this.logMessage(message.toString());
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedExternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			userDefinedExternalTransition(Duration elapsedTime)
	{
		// get the vector of current external events
		ArrayList<EventI> currentEvents = this.getStoredEventAndReset();
		// when this method is called, there is at least one external event,
		// and for the hair dryer model, there will be exactly one by
		// construction.
		assert	currentEvents != null && currentEvents.size() == 1;

		this.lastReceivedEvent = (Event) currentEvents.get(0);
		StringBuffer message =
				new StringBuffer("executes the external event ");
		message.append(this.lastReceivedEvent.getClass().getSimpleName());
		message.append("(");
		message.append(
			this.lastReceivedEvent.getTimeOfOccurrence().getSimulatedTime());
		message.append(")\n");
		this.logMessage(message.toString());

		super.userDefinedExternalTransition(elapsedTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			endSimulation(Time endTime) throws Exception
	{
		this.logMessage("simulation ends.\n");
		super.endSimulation(endTime);
	}
}
// -----------------------------------------------------------------------------
