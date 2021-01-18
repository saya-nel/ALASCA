package fr.sorbonne_u.components.cyphy;

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

import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSchedulingI;

// -----------------------------------------------------------------------------
/**
 * The class <code>ComponentRTScheduler</code> implements a scheduler to be
 * used by real time DEVS atomic models to make the component execute the tasks
 * pertaining to a simulation.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * BCM components can have scheduled executor services, which can be used to
 * execute simulation tasks. This implementation simply takes a component
 * reference an one of its scheduled executor services and implements a proxy
 * to that executor.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant 	{@code owner != null}
 * invariant	{@code executorServiceURI != null}
 * invariant	{@code owner.validExecutorServiceURI(executorServiceURI)}
 * invariant	{@code owner.isSchedulable(executorServiceURI)}
 * invariant	{@code owner.getExecutorServiceIndex(executorServiceURI) == executorServiceIndex}
 * </pre>
 * 
 * <p>Created on : 2020-12-15</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			ComponentRTScheduler
implements		RTSchedulingI
{
	private static final long				serialVersionUID = 1L;
	protected final AbstractCyPhyComponent	owner;
	protected String						executorServiceURI;
	protected int							executorServiceIndex;

	/**
	 * create a component real time scheduler, proxy to a scheduled executor
	 * service owned by the owner component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code owner != null}
	 * pre	{@code executorServiceURI != null}
	 * pre	{@code owner.validExecutorServiceURI(executorServiceURI)}
	 * pre	{@code owner.isSchedulable(executorServiceURI)}
	 * post	{@code ComponentRTScheduler.checkInvariant(this)}
	 * </pre>
	 *
	 * @param owner					component owning the executor service that will execute event tasks.
	 * @param executorServiceURI	URI of the scheduled executor service that will execute event tasks.
	 */
	public				ComponentRTScheduler(
		AbstractCyPhyComponent owner,
		String executorServiceURI
		)
	{
		assert	owner != null;
		assert	executorServiceURI != null;
		assert	owner.validExecutorServiceURI(executorServiceURI);
		assert	owner.isSchedulable(executorServiceURI);

		this.owner = owner;
		this.executorServiceURI = executorServiceURI;
		this.executorServiceIndex =
							owner.getExecutorServiceIndex(executorServiceURI);

		assert	ComponentRTScheduler.checkInvariant(this);
	}

	protected static boolean	checkInvariant(ComponentRTScheduler s)
	{
		boolean invariant = true;
		invariant &= s.owner != null;
		invariant &= s.executorServiceURI != null;
		invariant &= s.owner.validExecutorServiceURI(s.executorServiceURI);
		invariant &= s.owner.isSchedulable(s.executorServiceURI);
		invariant &= s.owner.getExecutorServiceIndex(s.executorServiceURI)
													== s.executorServiceIndex;
		return invariant;
	}

	/**
	 * update the scheduled executor service used to execute event tasks.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code executorServiceURI != null}
	 * pre	{@code owner.validExecutorServiceURI(executorServiceURI)}
	 * pre	{@code owner.isSchedulable(executorServiceURI)}
	 * post	{@code ComponentRTScheduler.checkInvariant(this)}
	 * </pre>
	 *
	 * @param executorServiceURI	URI of the new executor service.
	 */
	public void			updateExecutorService(String executorServiceURI)
	{
		assert	executorServiceURI != null;
		assert	this.owner.validExecutorServiceURI(executorServiceURI);
		assert	this.owner.isSchedulable(executorServiceURI);

		this.executorServiceURI = executorServiceURI;
		this.executorServiceIndex =
					this.owner.getExecutorServiceIndex(executorServiceURI);

		assert	ComponentRTScheduler.checkInvariant(this);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSchedulingI#schedule(java.lang.Runnable, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public ScheduledFuture<?>	schedule(
		Runnable eventTask,
		long delay,
		TimeUnit tu
		) throws RejectedExecutionException, NullPointerException
	{
		assert	ComponentRTScheduler.checkInvariant(this);
		return this.owner.getScheduledExecutorService(
									this.executorServiceIndex).
												schedule(eventTask, delay, tu);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSchedulingI#scheduleImmediate(java.lang.Runnable)
	 */
	@Override
	public Future<?>	scheduleImmediate(Runnable eventTask)
	throws RejectedExecutionException, NullPointerException
	{
		assert	ComponentRTScheduler.checkInvariant(this);
		return this.owner.getScheduledExecutorService(
									this.executorServiceIndex).
												submit(eventTask);
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSchedulingI#isShutdown()
	 */
	@Override
	public boolean		isShutdown()
	{
		return this.owner.getScheduledExecutorService(
									this.executorServiceIndex).isShutdown();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSchedulingI#isTerminated()
	 */
	@Override
	public boolean		isTerminated()
	{
		return this.owner.getScheduledExecutorService(
									this.executorServiceIndex).isTerminated();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSchedulingI#shutdown()
	 */
	@Override
	public void			shutdown() throws SecurityException
	{
		this.owner.getScheduledExecutorService(
									this.executorServiceIndex).shutdown();
	}

	/**
	 * @see fr.sorbonne_u.devs_simulation.simulators.interfaces.RTSchedulingI#shutdownNow()
	 */
	@Override
	public void			shutdownNow() throws SecurityException
	{
		this.owner.getScheduledExecutorService(
									this.executorServiceIndex).shutdownNow();
	}
}
// -----------------------------------------------------------------------------
