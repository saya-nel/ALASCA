package fr.sorbonne_u.devs_simulation.simulators;

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

import fr.sorbonne_u.devs_simulation.simulators.interfaces.RTAtomicSimulatorI;
import fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSchedulingI;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import fr.sorbonne_u.devs_simulation.models.events.EventI;
import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;

// -----------------------------------------------------------------------------
/**
 * The class <code>AtomicRTEngine</code> implements a simulation engine which
 * performs simulation in real time aka. the simulation clock follows the
 * real physical time, eventually accelerated (or decelerated).
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * For atomic models, the atomic real time engine does almost all the work as
 * the internal and external events are executed by tasks scheduled on the
 * computer (here through Java executor services) using the computer clock.
 * </p>
 * <p>
 * As atomic models execute independently in real time, the way to start a
 * real time simulation for a coupled model is to set a start time in the
 * real physical time (using the preferred scheduling time unit) at which all
 * atomic models will start. For simulations involving many models in a large
 * simulation architectures, it is preferable to set this start time
 * sufficiently in the future to let all models schedule their first internal
 * transition before this start time.
 * </p>
 * <p>
 * The simulation will force the start time in simulated time to coincide with
 * this start time in the real physical time, and then the simulation clock
 * will follow the real physical time (with the acceleration factor taken into
 * account).
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true	// TODO
 * </pre>
 * 
 * <p>Created on : 2020-11-20</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			AtomicRTEngine
extends		AtomicEngine
implements	RTAtomicSimulatorI
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	private static final long				serialVersionUID = 1L;
	/** default acceleration factor, used when it is not set by the user.	*/
	public static final double				DEFAULT_ACCELERATION_FACTOR = 1.0;
	/** the scheduling time unit used when scheduling tasks on Java
	 *  executor services.													*/
	public static final TimeUnit			PREFFERED_SCHEDULING_TIME_UNIT =
														TimeUnit.MILLISECONDS;
	/** tolerance on the cutoff time in the preferred scheduling
	 *  time unit.															*/
	public static final long				END_TIME_TOLERANCE = 100;

	/** lock used to force one event execution at a time.					*/
	protected final ReentrantLock			eventSerialisationLock;
	/** the real time scheduler used to schedule the event processing
	 *  tasks.																*/
	protected RTSchedulingI					rtScheduler;
	/** a scheduled future protected by atomic reference that allows to
	 *  cancel the task scheduled for the next internal event when an
	 *  external event is received before the execution of the next
	 *  internal event (another internal event is usually planned
	 *  after the external event in the DEVS simulation  protocol).			*/
	protected final AtomicReference<ScheduledFuture<?>>
											nextInternalEventFuture;
	/** the task that is scheduled to execute internal events.				*/
	protected Runnable						internalEventTask;
	/** the acceleration/deceleration factor that adjusts the pace of
	 *  the simulation upon the real physical time; a value greater than
	 *  1 will force the simulation to run faster in real time than the
	 *  simulated time, while a value under 1 forces it to run slower.		*/
	protected double						accelerationFactor;

	/** the real time of start in the preferred scheduling time unit.		*/
	protected long							realTimeOfStart;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create an atomic real time simulation engine with the given acceleration
	 * factor.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code accelerationFactor > 0.0}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param accelerationFactor	the acceleration/deceleration factor that adjusts the pace of the simulation upon the real physical time.
	 */
	public				AtomicRTEngine(double accelerationFactor)
	{
		assert	accelerationFactor > 0.0;

		this.eventSerialisationLock = new ReentrantLock(true);
		this.nextInternalEventFuture =
								new AtomicReference<ScheduledFuture<?>>();
		this.initialise(accelerationFactor);
	}

	/**
	 * create an atomic real time simulation engine with the default
	 * acceleration factor.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
	public				AtomicRTEngine()
	{
		this(DEFAULT_ACCELERATION_FACTOR);
	}

	/**
	 * initialise the atomic real time simulation engine.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code accelerationFactor > 0.0}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param accelerationFactor	for real time engines, the acceleration factor between the simulation clock and the real time.
	 */
	protected void		initialise(double accelerationFactor)
	{
		assert	accelerationFactor > 0.0;

		this.accelerationFactor = accelerationFactor;		
		final AtomicRTEngine rte = this;
		this.internalEventTask =
				new Runnable() {
					@Override
					public void run() {
						try {
							rte.rtInternalEventTask();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				};
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTAtomicSimulatorI#isRTSchedulerSet()
	 */
	@Override
	public boolean		isRTSchedulerSet()
	{
		return (this.rtScheduler != null);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTAtomicSimulatorI#setRTScheduler(fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSchedulingI)
	 */
	@Override
	public void			setRTScheduler(RTSchedulingI scheduler)
	{
		assert	scheduler != null;
		assert	!scheduler.isShutdown();

		this.rtScheduler = scheduler;
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTAtomicSimulatorI#getRTScheduler()
	 */
	@Override
	public RTSchedulingI	getRTScheduler()
	{
		assert	this.isRTSchedulerSet();
		return this.rtScheduler;
	}

	// -------------------------------------------------------------------------
	// Simulation related methods
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSimulatorI#startRTSimulation(long, double, double)
	 */
	@Override
	public void			startRTSimulation(
		long realTimeOfStart,
		double simulationStartTime,
		double simulationDuration
		) throws Exception
	{
		assert	realTimeOfStart > System.currentTimeMillis();
		assert	realTimeOfStart < Long.MAX_VALUE;
		assert	simulationStartTime >= 0.0;
		assert	simulationStartTime < Double.POSITIVE_INFINITY;
		assert	simulationDuration > 0.0;
		assert	simulationDuration <= Double.POSITIVE_INFINITY;
		assert	isRTSchedulerSet();

		this.realTimeOfStart = realTimeOfStart;
		TimeUnit tu = this.simulatedModel.getSimulatedTimeUnit();
		// initialise the simulation with its start time and its duration
		// in simulated time (with its proper time unit).
		this.initialiseSimulation(new Time(simulationStartTime, tu),
								  new Duration(simulationDuration, tu));

		if (this.hasDebugLevel(1)) {
			this.simulatedModel.logMessage(
					"simulation begins for " + this.getURI() + ".\n");
		}

		// plan the first internal transition
		this.planNextInternalEventTask();

		// for real time simulations, an atomic model that has no planned
		// internal events after some point would not know when to stop,
		// hence we schedule a forced stop in case; it is has been already
		// stop, it will do nothing.
		final AtomicRTEngine rte = this;
		this.rtScheduler.schedule(
				new Runnable() {
					@Override
					public void run() {
						try {
							rte.endSimulation(simulationEndTime);
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}					
				},
				this.computeRealTimeDelayToNextEvent(
								this.simulationEndTime) + END_TIME_TOLERANCE,
				PREFFERED_SCHEDULING_TIME_UNIT);
	}

	/**
	 * return the delay in real time from now corresponding to the time
	 * <code>t</code> in simulated time.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code t != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param t				the time in simulated time to be converted.
	 * @return				the time converted into a real time delay from now in milliseconds.
	 * @throws Exception	<i>to do</i>.
	 */
	protected long		computeRealTimeDelayToNextEvent(
		Time t
		) throws Exception
	{
		assert	t != null;

		double fromStart = t.getSimulatedTime() -
								this.simulationStartTime.getSimulatedTime();

		// in order to be as precise as possible when computing the scheduling
		// delay in the preferred scheduling time unit and avoid drifts by
		// accumulating errors made on each delay, the accelerated delay is
		// computed in double precision from the simulation start time. Then,
		// the accelerated delay is converted in the preferred scheduling time
		// unit in two phases: first it is converted from the simulation time
		// unit to nanoseconds, then it is converted to the preferred scheduling
		// time unit. Finally the result is cast to a long after rounding it.

		long cf1 = TimeUnit.NANOSECONDS.convert(
							1,
							this.getNextTimeAdvance().getTimeUnit());
		long cf2 = TimeUnit.NANOSECONDS.convert(
							1,
							AtomicRTEngine.PREFFERED_SCHEDULING_TIME_UNIT);
		long f = (long) Math.round(
							((fromStart/this.accelerationFactor)*cf1)/cf2);
		long s = this.realTimeOfStart + f;
		long c = System.currentTimeMillis();
		long ret = s-c;
		if (ret < 0) {
			this.simulatedModel.logMessage(
				"Warning: negative delay to next event " + ret
													+ " corrected to 0!\n");
			ret = 0;
		}
		return ret;
	}

	/**
	 * the task that will perform an internal transition when needed; to be
	 * scheduled on the executor service of the simulation engine.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isModelSet()}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @throws Exception	<i>to do</i>.
	 */
	protected void		rtInternalEventTask() throws Exception
	{
		assert	this.isModelSet();

		this.eventSerialisationLock.lock();
		try {
			this.produceOutput(this.timeOfNextEvent);
			this.internalEventStep();
			this.planNextInternalEventTask();
		} finally {
			this.eventSerialisationLock.unlock();
		}
	}

	/**
	 * plan the next internal event if any and the simulation did not reach
	 * its end time (in simulated time).
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @throws Exception	<i>to do</i>.
	 */
	protected void		planNextInternalEventTask() throws Exception
	{
		if (this.isRunning && !this.stoppedSimulation && this.timeOfNextEvent != null &&
				 this.timeOfNextEvent.lessThanOrEqual(this.simulationEndTime)) {
			this.nextInternalEventFuture.set(
						this.rtScheduler.schedule(
								this.internalEventTask,
								this.computeRealTimeDelayToNextEvent(
														this.timeOfNextEvent),
								PREFFERED_SCHEDULING_TIME_UNIT));
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.AtomicEngine#endSimulation(fr.sorbonne_u.devs_simulation.models.time.Time)
	 */
	@Override
	public void			endSimulation(Time endTime) throws Exception
	{
		if (this.isRunning) {
			if (this.hasDebugLevel(1)) {
				this.simulatedModel.logMessage(
						"simulation ends for " + this.getURI() + ".\n");
			}
			this.rtScheduler.shutdown();
			super.endSimulation(endTime);
		}
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.AtomicEngine#storeInput(java.lang.String, java.util.ArrayList)
	 */
	@Override
	public void			storeInput(
		String destinationURI,
		ArrayList<EventI> es
		) throws Exception
	{
		assert	this.simulatedModel.getURI().equals(destinationURI);
		assert	es != null;

		// schedule a task to execute the external events in es
		this.rtScheduler.scheduleImmediate(
							this.createExternalEventTask(destinationURI, es));
	}

	/**
	 * call the inherited method <code>storeInput</code> to actually store
	 * the received events in the local simulation models, bypassing the
	 * redefinition to avoid an infinite loop.
	 * 
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code destinationURI != null}
	 * pre	{@code getURI().equals(destinationURI) || isDescendentModel(destinationURI)}
	 * pre	{@code es != null && !es.isEmpty()}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param destinationURI	URI of the destination model.
	 * @param es				received events.
	 * @throws Exception		<i>to do</i>.
	 */
	private void		inheritedStoreInput(
		String destinationURI,
		ArrayList<EventI> es
		) throws Exception
	{
		super.storeInput(destinationURI, es);
	}

	/**
	 * create the task that will execute the next external transition when a
	 * set of events has been received.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code destinationURI != null}
	 * pre	{@code getURI().equals(destinationURI) || isDescendentModel(destinationURI)}
	 * pre	{@code es != null && !es.isEmpty()}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param destinationURI	URI of the atomic model that must receive the events.
	 * @param es				set of events just received.
	 * @return					the task to be scheduled.
	 */
	protected Runnable	createExternalEventTask(
		final String destinationURI,
		final ArrayList<EventI> es
		)
	{
		assert	destinationURI != null;
		try {
			assert	getURI().equals(destinationURI) ||
											isDescendentModel(destinationURI);
		} catch (Exception e1) {
			throw new RuntimeException(e1) ;
		}
		assert	es != null;

		final AtomicRTEngine rte = this;
		return new Runnable() {
					@Override
					public void run() {
						rte.eventSerialisationLock.lock();
						try {
							if (rte.nextInternalEventFuture.get() != null) {
								rte.nextInternalEventFuture.get().cancel(false);
							}
							rte.inheritedStoreInput(destinationURI, es);
							Duration elapsedTime =
								es.get(0).getTimeOfOccurrence().
											subtract(rte.getTimeOfLastEvent());
							rte.externalEventStep(elapsedTime);
							rte.planNextInternalEventTask();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						} finally {
							rte.eventSerialisationLock.unlock();
						}
					}
				};
	}
}
// -----------------------------------------------------------------------------
