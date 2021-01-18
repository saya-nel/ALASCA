package fr.sorbonne_u.components.cyphy.hem2020e2.equipments.boiler;

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
import java.util.concurrent.TimeUnit;
import fr.sorbonne_u.devs_simulation.hioa.annotations.ExportedVariable;
import fr.sorbonne_u.devs_simulation.hioa.annotations.InternalVariable;
import fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOAwithDE;
import fr.sorbonne_u.devs_simulation.hioa.models.vars.Value;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import fr.sorbonne_u.devs_simulation.utils.StandardLogger;

// -----------------------------------------------------------------------------
/**
 * The class <code>BoilerMILModel</code> defines a simple MIL simulation model
 * for a water boiler.
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
 * invariant		{@code EXTERNAL_TEMP < targetTemp}
 * invariant		{@code targetTemp < STANDARD_HEATING_TEMP}
 * </pre>
 * 
 * <p>Created on : 2020-11-18</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			BoilerMILModel
extends		AtomicHIOAwithDE
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	/** URI for an instance model; works as long as only one instance is
	 *  created.															*/
	public static final String	URI = BoilerMILModel.class.getSimpleName();
	/** tracing flag.														*/
	public static boolean			TRACE = true;

	/** integration step for the differential equation(assumed in seconds).	*/
	protected static final double	STEP = 1.0;
	/** integration step as a duration, including the time unit.			*/
	protected final Duration		integrationStep;
	/** temperature of the heating plate in the boiler.						*/
	protected final double			STANDARD_HEATING_TEMP = 65.0;
	/** temperature in the room where the boiler is, assumed constant.		*/
	protected final double			EXTERNAL_TEMP = 20.0;
	/** heating transfer constant in the differential equation.				*/
	protected final double			HEATING_TRANSFER_CONSTANT = 1000.0;
	/** insulation heat transfer constant in the differential equation.		*/
	protected final double 			INSULATION_TRANSFER_CONSTANT = 10000.0;
	/** power (in watts) of the boiler.										*/
	protected static final double	POWER = 2200.0; // in watts
	/** electrical tension for the boiler.									*/
	protected static final double	TENSION = 220.0; // in volts

	// -------------------------------------------------------------------------
	// HIOA model variables
	// -------------------------------------------------------------------------

	/** the current water temperature.										*/
	@InternalVariable(type = Double.class)
	protected final Value<Double>	waterTemp = new Value<Double>(this, 0.0, 0);
	/** the current derivative of the water temperature.					*/
	protected double				currentTempDerivative = 0.0;
	/** true when the boiler is heating and false otherwise.				*/
	protected boolean				isHeating = false;
	/** target water temperature.											*/
	protected double				targetTemp = 52.0;
	/** the tolerance on the target water temperature to get a control
	 *  with hysteresis.													*/
	protected double				targetTolerance = 1.0;

	/** the current intensity of electric consumption (in amps).			*/
	@ExportedVariable(type = Double.class)
	protected final Value<Double>	currentIntensity =
												new Value<Double>(this, 0.0, 0);
	
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
	public				BoilerMILModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine);
		this.integrationStep = new Duration(STEP, simulatedTimeUnit);
		this.setLogger(new StandardLogger());
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
		this.toggleDebugMode();
		this.logMessage("simulation begins for " + this.uri + ".\n");

		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.hioa.models.AtomicHIOA#initialiseVariables(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	protected void		initialiseVariables(Time startTime)
	{
		super.initialiseVariables(startTime);

		// we assume that the water in the boiler is at the room temperature,
		// which would be true if the boiler was switched off during holidays,
		// for example.
		this.waterTemp.v = EXTERNAL_TEMP;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI> output()
	{
		return null;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration		timeAdvance()
	{
		return this.integrationStep;
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
		this.currentTempDerivative = 0.0;
		if (this.isHeating) {
			// the heating contribution: temperature difference between the
			// plate and the water divided by the heat transfer constant
			this.currentTempDerivative =
					(STANDARD_HEATING_TEMP - this.waterTemp.v)/
												HEATING_TRANSFER_CONSTANT;
		}
		// the cooling contribution: temperature difference between the
		// room and the water divided by the heat transfer constant
		this.currentTempDerivative +=
					(this.EXTERNAL_TEMP - this.waterTemp.v)/
												INSULATION_TRANSFER_CONSTANT;
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
		this.waterTemp.v = this.waterTemp.v + this.currentTempDerivative*STEP;
		this.waterTemp.time = this.getCurrentStateTime();

		// Tracing
		String mark = this.isHeating ? " (h)" : " (-)";
		StringBuffer message = new StringBuffer();
		if (TRACE && this.getCurrentStateTime().getSimulatedTime() % 10.0 == 0.0) {
			message.append(this.waterTemp.time.getSimulatedTime());
			message.append(mark);
			message.append(" : ");
			message.append(this.waterTemp.v);
		}

		// control: decide either to heat or not
		if (this.waterTemp.v > this.targetTemp + this.targetTolerance) {
			this.isHeating = false;
		}
		if (this.waterTemp.v < this.targetTemp - this.targetTolerance) {
			this.isHeating = true;
		}
		// update the current electricity consumption
		if (this.isHeating) {
			this.currentIntensity.v = POWER/TENSION;
		} else {
			this.currentIntensity.v = 0.0;
		}
		this.currentIntensity.time = this.getCurrentStateTime();

		// Tracing
		if (TRACE && this.getCurrentStateTime().getSimulatedTime() % 10.0 == 0.0) {
			message.append(" -- ");
			message.append(this.currentIntensity.v);
			message.append("\n");
			this.logMessage(message.toString());
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			endSimulation(Time endTime) throws Exception
	{
		this.logMessage("simulation ends for " + this.uri + ".\n");
		super.endSimulation(endTime);
	}
}
// -----------------------------------------------------------------------------
