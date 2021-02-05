package main.java.components.fan.sil;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.components.fan.Fan;
import main.java.components.fan.utils.FanLevel;
import main.java.utils.FileLogger;

/**
 * 
 * @author Bello Memmi
 *
 */
public class FanUserSILModel extends AtomicModel {

	private static final long serialVersionUID = 1L;

	/**
	 * URI for an instance model; works as long as only one instance is created.
	 */
	public static final String URI = FanUserSILModel.class.getSimpleName();

	/** time interval between event outputs. */
	protected static final double STEP = 2.0;
	/** the current event being output. */
	protected Fan.Operations currentOperation;
	/** time interval between event outputs. */
	protected Duration time2next;

	/**
	 * name used to pass the owner component reference as simulation parameter.
	 */
	public static final String FAN_REFERENCE_NAME = URI + ":" + "FANRN";

	/** owner component. */
	protected Fan owner;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a fan user SIL model instance.
	 * 
	 * <p>
	 * <strong>Contract</strong>
	 * </p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param uri               URI of the model.
	 * @param simulatedTimeUnit time unit used for the simulation time.
	 * @param simulationEngine  simulation engine to which the model is attached.
	 * @throws Exception <i>to do</i>.
	 */
	public FanUserSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.setLogger(new FileLogger("fanUser.log"));
	}

	/**
	 * get the next operation to be performed; this method implements a simple
	 * simulation scenario just to test the model.
	 *
	 * @return the next event to be output.
	 */
	protected Fan.Operations getNextOperation() {
		Fan.Operations ret = null;
		if (this.currentOperation == null) {
			ret = Fan.Operations.TURN_ON;
		} else {
			if (this.currentOperation == Fan.Operations.TURN_ON) {
				ret = Fan.Operations.SET_LOW;
			} else if (this.currentOperation == Fan.Operations.SET_LOW) {
				ret = Fan.Operations.SET_MID;
			} else if (this.currentOperation == Fan.Operations.SET_MID) {
				ret = Fan.Operations.SET_HIGH;
			} else if (this.currentOperation == Fan.Operations.SET_HIGH) {
				ret = Fan.Operations.TURN_OFF;
			} else if (this.currentOperation == Fan.Operations.TURN_OFF) {
				ret = Fan.Operations.TURN_ON;
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
	public void setSimulationRunParameters(Map<String, Object> simParams) throws Exception {
		this.owner = (Fan) simParams.get(FAN_REFERENCE_NAME);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#initialiseState(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void initialiseState(Time initialTime) {
		this.time2next = new Duration(STEP, this.getSimulatedTimeUnit());
		this.currentOperation = null;
		this.toggleDebugMode();
		this.logMessage("simulation begins.\n");

		super.initialiseState(initialTime);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.AtomicModelI#output()
	 */
	@Override
	public ArrayList<EventI> output() {
		return null;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.interfaces.ModelI#timeAdvance()
	 */
	@Override
	public Duration timeAdvance() {
		if (this.currentOperation == null) {
			return new Duration(0, TimeUnit.SECONDS);
		}
		if (this.currentOperation != null && this.currentOperation == Fan.Operations.TURN_OFF)
			return new Duration(6 * 3600, TimeUnit.SECONDS);
		else {
			return this.time2next;
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#userDefinedInternalTransition(fr.sorbonne_u.devs_simulation.models.time.Duration)
	 */
	@Override
	public void userDefinedInternalTransition(Duration elapsedTime) {
		super.userDefinedInternalTransition(elapsedTime);

		this.currentOperation = this.getNextOperation();
		StringBuffer message = new StringBuffer(this.getCurrentStateTime() + " executes ");
		message.append(this.currentOperation);
		message.append(".\n");
		this.logMessage(message.toString());
		switch (this.currentOperation) {
		case TURN_ON:
			this.owner.runTask(o -> {
				try {
					((Fan) o).turnOn();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			break;
		case TURN_OFF:
			this.owner.runTask(o -> {
				try {
					((Fan) o).turnOff();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			break;
		case SET_HIGH:
			this.owner.runTask(o -> {
				try {
					((Fan) o).adjustPower(FanLevel.HIGH);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			break;
		case SET_MID:
			this.owner.runTask(o -> {
				try {
					((Fan) o).adjustPower(FanLevel.MID);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			break;
		case SET_LOW:
			this.owner.runTask(o -> {
				try {
					((Fan) o).adjustPower(FanLevel.LOW);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.models.AtomicModel#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void endSimulation(Time endTime) throws Exception {
		this.logMessage("simulation ends.\n");
		super.endSimulation(endTime);
	}

}
