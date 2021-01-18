package fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide an
// example of a cyber-physical system.
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

import java.util.HashMap;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.HairDryerSILCoupledModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.HairDryerStateSILModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SetHigh;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SetLow;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SwitchOff;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.hairdryer.sil.SwitchOn;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.exceptions.PreconditionException;

// -----------------------------------------------------------------------------
/**
 * The class <code>HairDryer</code> implements the hair dryer component.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * The hair dryer is an uncontrollable appliance, hence it does not connect
 * with the household energy manager. However, it will connect later to the
 * electric panel to take its (simulated) electricity consumption into account.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-10-02</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
// -----------------------------------------------------------------------------
@OfferedInterfaces(offered={HairDryerCI.class})
// -----------------------------------------------------------------------------
public class			HairDryer
extends		AbstractCyPhyComponent
implements	HairDryerImplementationI
{
	// -------------------------------------------------------------------------
	// Inner classes and types
	// -------------------------------------------------------------------------

	/**
	 * The enumeration <code>Operations</code>
	 *
	 * <p><strong>Description</strong></p>
	 * 
	 * <p><strong>Invariant</strong></p>
	 * 
	 * <pre>
	 * invariant	true
	 * </pre>
	 * 
	 * <p>Created on : 2020-12-17</p>
	 * 
	 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
	 */
	public enum			Operations
	{
		TURN_ON,
		TURN_OFF,
		SET_HIGH,
		SET_LOW
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** URI of the reflection inbound port of this component; works for
	 *  singleton.															*/
	public static final String		REFLECTION_INBOUND_PORT_URI =
														"Hair-dryer-ibp-uri";
	/** true if the component is executed in a SIL simulation mode.			*/
	protected boolean				isSILSimulated;
	/** true if the component is under unit test.							*/
	protected boolean				isUnitTest;

	protected HairDryerRTAtomicSimulatorPlugin	simulatorPlugin;
	protected static final String	SCHEDULED_EXECUTOR_SERVICE_URI = "ses";

	/** current state (on, off) of the hair dryer.							*/
	protected HairDryerState		currentState;
	/** current mode of operation (low, high) of the hair dryer.			*/
	protected HairDryerMode			currentMode;

	/** inbound port offering the <code>HairDryerCI</code> interface.		*/
	protected HairDryerInboundPort	hdip;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a hair dryer component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code hairDryerInboundPortURI != null}
	 * pre	{@code !hairDryerInboundPortURI.isEmpty()}
	 * post	{@code getState() == HairDryerState.OFF}
	 * post	{@code getMode() == HairDryerMode.LOW}
	 * </pre>
	 * 
	 * @param hairDryerInboundPortURI	URI of the hair dryer inbound port.
	 * @param isSILSimulated			true if the component is executed in a SIL simulation mode.
	 * @param isUnitTest				true if the component is under unit test.
	 * @throws Exception				<i>to do</i>.
	 */
	protected			HairDryer(
		String hairDryerInboundPortURI,
		boolean isSILSimulated,
		boolean isUnitTest
		) throws Exception
	{
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);
		this.initialise(hairDryerInboundPortURI, isSILSimulated, isUnitTest);
	}

	/**
	 * initialise the hair dryer component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code hairDryerInboundPortURI != null}
	 * pre	{@code !hairDryerInboundPortURI.isEmpty()}
	 * post	{@code getState() == HairDryerState.OFF}
	 * post	{@code getMode() == HairDryerMode.LOW}
	 * </pre>
	 * 
	 * @param hairDryerInboundPortURI	URI of the hair dryer inbound port.
	 * @param isSILSimulated			true if the component is executed in a SIL simulation mode.
	 * @param isUnitTest				true if the component is under unit test.
	 * @throws Exception				<i>to do</i>.
	 */
	protected void		initialise(
		String hairDryerInboundPortURI,
		boolean isSILSimulated,
		boolean isUnitTest
		) throws Exception
	{
		assert	hairDryerInboundPortURI != null :
					new PreconditionException(
										"hairDryerInboundPortURI != null");
		assert	!hairDryerInboundPortURI.isEmpty() :
					new PreconditionException(
										"!hairDryerInboundPortURI.isEmpty()");

		this.currentState = HairDryerState.OFF;
		this.currentMode = HairDryerMode.LOW;
		this.isSILSimulated = isSILSimulated;
		this.isUnitTest = isUnitTest;
		this.hdip = new HairDryerInboundPort(hairDryerInboundPortURI, this);
		this.hdip.publishPort();

		this.tracer.get().setTitle("Hair dryer component");
		this.tracer.get().setRelativePosition(2, 0);
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

		if (this.isSILSimulated) {
			try {
				// create the scheduled executor service that will run the
				// simulation tasks
				this.createNewExecutorService(
									SCHEDULED_EXECUTOR_SERVICE_URI, 1, true);
				// create and initialise the atomic simulator plug-in that will
				// hold and execute the SIL simulation models
				this.simulatorPlugin = new HairDryerRTAtomicSimulatorPlugin();
				this.simulatorPlugin.setPluginURI(HairDryerSILCoupledModel.URI);
				this.simulatorPlugin.
						setSimulationExecutorService(
											SCHEDULED_EXECUTOR_SERVICE_URI);
				this.simulatorPlugin.
							initialiseSimulationArchitecture(this.isUnitTest);
				this.installPlugin(this.simulatorPlugin);
			} catch (Exception e) {
				throw new ComponentStartException(e) ;
			}
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void	execute() throws Exception
	{
		super.execute();

		if (this.isSILSimulated && this.isUnitTest) {
			this.simulatorPlugin.setSimulationRunParameters(
											new HashMap<String, Object>());
			this.simulatorPlugin.startRTSimulation(
								System.currentTimeMillis() + 100, 0.0, 10.1);
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.hdip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hairdryer.HairDryerImplementationI#getState()
	 */
	@Override
	public HairDryerState	getState() throws Exception
	{
		return this.currentState;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hairdryer.HairDryerImplementationI#getMode()
	 */
	@Override
	public HairDryerMode	getMode() throws Exception
	{
		return this.currentMode;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hairdryer.HairDryerImplementationI#turnOn()
	 */
	@Override
	public void			turnOn() throws Exception
	{
		assert	this.getState() == HairDryerState.OFF :
					new PreconditionException(
										"getState() == HairDryerState.OFF");

		this.currentState = HairDryerState.ON;
		this.currentMode = HairDryerMode.LOW;

		this.logMessage("HairDryer: Hair dryer turned on");

		if (this.isSILSimulated) {
			this.simulateOperation(Operations.TURN_ON);
		}
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hairdryer.HairDryerImplementationI#turnOff()
	 */
	@Override
	public void			turnOff() throws Exception
	{
		assert	this.getState() == HairDryerState.ON :
					new PreconditionException(
										"getState() == HairDryerState.ON");

		this.currentState = HairDryerState.OFF;

		this.logMessage("HairDryer: Hair dryer turned off");

		if (this.isSILSimulated) {
			this.simulateOperation(Operations.TURN_OFF);
		}
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hairdryer.HairDryerImplementationI#setHigh()
	 */
	@Override
	public void			setHigh() throws Exception
	{
		assert	this.getState() == HairDryerState.ON :
					new PreconditionException(
										"getState() == HairDryerState.ON");
		assert	this.getMode() == HairDryerMode.LOW :
					new PreconditionException("getMode() == HairDryerMode.LOW");

		this.currentMode = HairDryerMode.HIGH;

		this.logMessage("HairDryer: Hair dryer set to high");

		if (this.isSILSimulated) {
			this.simulateOperation(Operations.SET_HIGH);
		}
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hairdryer.HairDryerImplementationI#setLow()
	 */
	@Override
	public void			setLow() throws Exception
	{
		assert	this.getState() == HairDryerState.ON :
					new PreconditionException(
										"getState() == HairDryerState.ON");
		assert	this.getMode() == HairDryerMode.HIGH :
					new PreconditionException(
										"getMode() == HairDryerMode.HIGH");

		this.currentMode = HairDryerMode.LOW;

		this.logMessage("HairDryer: Hair dryer set to low");

		if (this.isSILSimulated) {
			this.simulateOperation(Operations.SET_LOW);
		}
	}

	protected void		simulateOperation(Operations op) throws Exception
	{
		switch(op)
		{
		case TURN_ON: this.simulatorPlugin.triggerExternalEvent(
												HairDryerStateSILModel.URI,
												t -> new SwitchOn(t));
					  break;
		case TURN_OFF: this.simulatorPlugin.triggerExternalEvent(
												HairDryerStateSILModel.URI,
												t -> new SwitchOff(t));
					  break;
		case SET_HIGH: this.simulatorPlugin.triggerExternalEvent(
												HairDryerStateSILModel.URI,
												t -> new SetHigh(t));
					  break;
		case SET_LOW: this.simulatorPlugin.triggerExternalEvent(
												HairDryerStateSILModel.URI,
												t -> new SetLow(t));
		}
	}
}
// -----------------------------------------------------------------------------
