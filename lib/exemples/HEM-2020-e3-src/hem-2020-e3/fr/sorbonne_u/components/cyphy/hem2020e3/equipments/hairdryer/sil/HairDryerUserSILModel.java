package fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide an example
// for the extension of the BCM component model that aims to define a components
// tailored for cyber-physical control systems (CPCS) for Java.
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
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.HairDryer;
import fr.sorbonne_u.components.cyphy.plugins.devs.utils.MemorisingComponentLogger;
import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;

// -----------------------------------------------------------------------------
/**
 * The class <code>HairDryerUser_MILModel</code> defines a very simple user
 * model for the hair dryer.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * This model is meant to illustrate how to program user MIL models, sending
 * events to other models to simulate user actions.
 * </p>
 * <p>
 * Here, we simply output events at a regularly rate and in a predefined
 * cycle to test all of the different modes in the hair dryer. More realistic
 * user models should be implemented to get better simulations.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2020-11-12</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			HairDryerUserSILModel
extends		AtomicModel
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long			serialVersionUID = 1L;
	/** URI for an instance model; works as long as only one instance is
	 *  created.															*/
	public static final String			URI = HairDryerUserSILModel.class.
																getSimpleName();

	/** time interval between event outputs.								*/
	protected static final double		STEP = 1.0;
	/** the current operation to be performed.								*/
	protected HairDryer.Operations		currentOperation;
	/** time interval between event outputs.								*/
	protected Duration					time2next;

	/** name used to pass the owner component reference as simulation
	 *  parameter.															*/
	public static final String			HAIR_DRYER_REFERENCE_NAME =
															URI + ":" + "HDCRN";
	/** owner component.													*/
	protected HairDryer					owner;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a hair dryer user MIL model instance.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param uri				URI of the model.
	 * @param simulatedTimeUnit	time unit used for the simulation time.
	 * @param simulationEngine	simulation engine to which the model is attached.
	 * @throws Exception		<i>to do</i>.
	 */
	public				HairDryerUserSILModel(
		String uri,
		TimeUnit simulatedTimeUnit,
		SimulatorI simulationEngine
		) throws Exception
	{
		super(uri, simulatedTimeUnit, simulationEngine);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * get the next operation to be performed; this method implements a simple
	 * simulation scenario just to test the model.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @return	the next event to be output.
	 */
	protected HairDryer.Operations	getNextOperation()
	{
		HairDryer.Operations ret = null;
		if (this.currentOperation == null) {
			// no previous event
			// first event is a turn on
			ret = HairDryer.Operations.TURN_ON;
		} else {
			// next event cycles through switch on, set high, set low and
			// switch off
			if (this.currentOperation == HairDryer.Operations.TURN_ON) {
				ret = HairDryer.Operations.SET_HIGH;
			} else if (this.currentOperation == HairDryer.Operations.TURN_OFF) {
				ret = HairDryer.Operations.TURN_ON;
			} else if (this.currentOperation == HairDryer.Operations.SET_LOW) {
				ret = HairDryer.Operations.TURN_OFF;
			} else {
				assert	this.currentOperation == HairDryer.Operations.SET_HIGH;
				ret = HairDryer.Operations.SET_LOW;
			}
		}
		return ret;
	}

	// -------------------------------------------------------------------------
	// DEVS simulation protocol
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.Model#setSimulationRunParameters(java.util.Map)
	 */
	@Override
	public void			setSimulationRunParameters(
		Map<String, Object> simParams
		) throws Exception
	{
		this.owner = (HairDryer)simParams.get(HAIR_DRYER_REFERENCE_NAME);
		// the memorising logger keeps all log messages until the end of the
		// simulation; they must explicitly be printed at the end to see them
		this.setLogger(new MemorisingComponentLogger(this.owner));
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			initialiseState(Time initialTime)
	{
		// initialise the duration between two events
		this.time2next = new Duration(STEP, this.getSimulatedTimeUnit());
		// initially, null which will trigger the first event in the series
		this.currentOperation = null;
		this.toggleDebugMode();
		this.logMessage("simulation begins.\n");

		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI>	output()
	{
		return null;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration		timeAdvance()
	{
		// for this simple example, we just force the model to execute at a
		// fixed rate given be the variable time2next
		return this.time2next;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void			userDefinedInternalTransition(Duration elapsedTime)
	{
		super.userDefinedInternalTransition(elapsedTime);

		this.currentOperation = this.getNextOperation();
		StringBuffer message = new StringBuffer("executes ");
		message.append(this.currentOperation);
		message.append(".\n");
		this.logMessage(message.toString());
		switch (this.currentOperation)
		{
		case TURN_ON:
			this.owner.runTask(o -> { try {
										((HairDryer)o).turnOn();
									  } catch (Exception e) {
										  throw new RuntimeException(e) ;
									  }
									});
			break;
		case TURN_OFF:
			this.owner.runTask(o -> { try {
										((HairDryer)o).turnOff();
									  } catch (Exception e) {
										  throw new RuntimeException(e) ;
									  }
									});
			break;
		case SET_HIGH:
			this.owner.runTask(o -> { try {
										((HairDryer)o).setHigh();
									  } catch (Exception e) {
										  throw new RuntimeException(e) ;
									  }
									});
			break;
		case SET_LOW:
			this.owner.runTask(o -> { try {
										((HairDryer)o).setLow();
									  } catch (Exception e) {
										  throw new RuntimeException(e) ;
									  }
									});
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			endSimulation(Time endTime) throws Exception
	{
		this.logMessage("simulation ends.\n");
		((MemorisingComponentLogger)this.logger).printLog();
		super.endSimulation(endTime);
	}
}
// -----------------------------------------------------------------------------
