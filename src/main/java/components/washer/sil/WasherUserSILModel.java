package main.java.components.washer.sil;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.models.AtomicModel;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.SimulatorI;
import main.java.components.washer.Washer;
import main.java.components.washer.utils.WasherModes;
import main.java.utils.FileLogger;

/**
 * 
 * The class <code>WasherUserSILModel</code> defines a user model for the washer
 * 
 * @author Bello Memmi
 *
 */
public class WasherUserSILModel extends AtomicModel {

	private static final long serialVersionUID = 1L;

	/**
	 * URI for an instance model; works as long as only one instance is created.
	 */
	public static final String URI = WasherUserSILModel.class.getSimpleName();

	/**
	 * time interval between event outputs.
	 */
	protected static final double STEP = 60 * 60 * 4; // 4 hours
	/**
	 * the current event being output.
	 */
	protected Washer.Operations currentOperation;
	/**
	 * time interval between event outputs.
	 */
	protected Duration time2next;

	/**
	 * name used to pass the owner component reference as simulation parameter.
	 */
	public static final String WASHER_REFERENCE_NAME = URI + ":" + "WASHERRN";

	/** owner component. */
	protected Washer owner;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a washer user SIL model instance.
	 *
	 * @param uri               URI of the model.
	 * @param simulatedTimeUnit time unit used for the simulation time.
	 * @param simulationEngine  simulation engine to which the model is attached.
	 * @throws Exception <i>to do</i>.
	 */
	public WasherUserSILModel(String uri, TimeUnit simulatedTimeUnit, SimulatorI simulationEngine) throws Exception {
		super(uri, simulatedTimeUnit, simulationEngine);
		this.setLogger(new FileLogger("washerUser.log"));
	}

	/**
	 * get the next operation to be performed; this method implements a simple
	 * simulation scenario just to test the model.
	 *
	 * @return the next event to be output.
	 */
	protected Washer.Operations getNextOperation() {
		Washer.Operations ret = null;
		ret = Washer.Operations.TURN_ON;
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
		this.owner = (Washer) simParams.get(WASHER_REFERENCE_NAME);
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
			return new Duration(60 * 1.5, TimeUnit.SECONDS);
		} else {
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
					((Washer) o).turnOn();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			break;
		case TURN_OFF:
			this.owner.runTask(o -> {
				try {
					((Washer) o).turnOff();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			break;
		case SET_ECO:
			this.owner.runTask(o -> {
				try {
					((Washer) o).setMode(WasherModes.ECO.ordinal());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			break;
		case SET_STD:
			this.owner.runTask(o -> {
				try {
					((Washer) o).setMode(WasherModes.STD.ordinal());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			break;
		case SET_PERFORMANCE:
			this.owner.runTask(o -> {
				try {
					((Washer) o).setMode(WasherModes.PERFORMANCE.ordinal());
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
