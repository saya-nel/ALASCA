package fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler;

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

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cyphy.AbstractCyPhyComponent;
import fr.sorbonne_u.components.cyphy.hem2020e1.equipments.hem.EquipmentRegistrationCI;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.Activate;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.BoilerWaterSILModel;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.DoNotHeat;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.Heat;
import fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.sil.Passivate;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

// -----------------------------------------------------------------------------
/**
 * The class <code>Boiler</code> implements a boiler component for the
 * household energy management example.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * In this version, the class is incomplete as it does not implement neither
 * the connection between components nor the component life-cycle.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-09-22</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
// -----------------------------------------------------------------------------
@OfferedInterfaces(offered={BoilerControlCI.class,BoilerSensorCI.class,
							BoilerActuatorCI.class})
@RequiredInterfaces(required={EquipmentRegistrationCI.class})
// -----------------------------------------------------------------------------
public class			Boiler
extends		AbstractCyPhyComponent
implements	BoilerControlImplementationI,
			BoilerReactiveControlImplementationI
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** The description in XML of the link between the required interface of
	 *  the home energy manager and the actual component interface of the
	 *  boiler (<code>BoilerCI</code>)).									*/
	public static final String CONTROL_INTERFACE_DESCRIPTOR =
	"<control-adapter\n" +
	"    xmlns=\"http://www.sorbonne-universite.fr/alasca/control-adapter\"\n" +
	"    type=\"suspension\"\n" +
	"    uid=\"1A10000\"\n" +
	"    offered=\"fr.sorbonne_u.components.cyphy.hem.equipments.boiler.BoilerControlCI\">\n" +
	"  <consumption nominal=\"2000\"/>\n" +
	"  <on>\n" +
	"    <required>fr.sorbonne_u.components.cyphy.hem.equipments.boiler.BoilerControlCI</required>\n" +
	"    <body equipmentRef=\"boiler\">\n" +
	"      return boiler.switchOn(fr.sorbonne_u.components.cyphy.hem.equipments.boiler.BoilerControlCI.STD);\n" +
	"    </body>\n" +
	"  </on>\n" +
	"  <off>\n" +
	"    <body equipmentRef=\"boiler\">return boiler.switchOff();</body>\n" +
	"  </off>\n" +
	"  <mode-control numberOfModes=\"2\">\n" +
	"    <upMode>\n" +
	"      <required>fr.sorbonne_u.components.cyphy.hem.equipments.boiler.BoilerControlCI</required>\n" +
	"      <body equipmentRef=\"boiler\">\n" +
	"        int m = boiler.mode();\n" +
	"        if (m == fr.sorbonne_u.components.cyphy.hem.equipments.boiler.BoilerControlCI.ECO) {\n" +
	"          boiler.std();\n" +
	"          return true;\n" +
	"        } else {\n" +
	"          return false;\n" +
	"        }\n" +
	"      </body>\n" +
	"    </upMode>\n" +
	"    <downMode>\n" +
	"      <required>fr.sorbonne_u.components.cyphy.hem.equipments.boiler.BoilerControlCI</required>\n" +
	"      <body equipmentRef=\"boiler\">\n" +
	"        int m = boiler.mode();\n" +
	"        if (m == fr.sorbonne_u.components.cyphy.hem.equipments.boiler.BoilerControlCI.STD) {\n" +
	"          boiler.eco();\n" +
	"          return true;\n" +
	"        } else {\n" +
	"          return false;\n" +
	"        }\n" +
	"      </body>\n" +
	"    </downMode>\n" +
	"    <setMode>\n" +
	"      <required>fr.sorbonne_u.components.cyphy.hem.equipments.boiler.BoilerControlCI</required>\n" +
	"      <parameter name=\"newMode\"/>\n" +
	"      <body equipmentRef=\"boiler\">\n" +
	"        boolean ret = false;\n" +
	"        int m = boiler.mode();\n" +
	"        if (newMode == 1 &amp;&amp; m == fr.sorbonne_u.components.cyphy.hem.equipments.boiler.BoilerControlCI.STD) {\n" +
	"          boiler.eco();\n" +
	"          ret = true;\n" +
	"        } else if (newMode == 2 &amp;&amp; m == fr.sorbonne_u.components.cyphy.hem.equipments.boiler.BoilerControlCI.ECO) {\n" +
	"          boiler.std();\n" +
	"          ret = true;\n" +
	"        }\n" +
	"        return ret;  \n" +
	"      </body>\n" +
	"    </setMode>\n" +
	"    <currentMode>\n" +
	"      <required>fr.sorbonne_u.components.cyphy.hem.equipments.boiler.BoilerControlCI</required>\n" +
	"      <body equipmentRef=\"boiler\">\n" +
	"        int m = boiler.mode();\n" +
	"        if (m == fr.sorbonne_u.components.cyphy.hem.equipments.boiler.BoilerControlCI.ECO) {\n" +
	"          return 1;\n" +
	"        } else {\n" +
	"          return 2;\n" +
	"        }\n" +
	"      </body>\n" +
	"    </currentMode>\n" +
	"  </mode-control>\n" +
	"  <suspended>\n" +
	"    <body equipmentRef=\"boiler\">return !boiler.active();</body>\n" +
	"  </suspended>\n" +
	"  <suspend>\n" +
	"    <body equipmentRef=\"boiler\">return boiler.passivate();</body>\n" +
	"  </suspend>\n" +
	"  <resume>\n" +
	"    <body equipmentRef=\"boiler\">return boiler.activate();</body>\n" +
	"  </resume>\n" +
	"  <emergency>\n" +
	"    <body equipmentRef=\"boiler\">return boiler.degreeOfEmergency();</body>\n" +
	"  </emergency>\n" +
	"</control-adapter>";

	public static final String		REFLECTION_INBOUND_PORT_URI =
												Boiler.class.getSimpleName();

	/** true if the component is executed in a SIL simulation mode.			*/
	protected boolean				isSILSimulated;
	/** URI of the executor service used for real time simulation.			*/
	protected static final String	SCHEDULED_EXECUTOR_SERVICE_URI = "ses";
	/** plug-in for the real time atomic simulator of the boiler.			*/
	protected BoilerRTAtomicSimulatorPlugin		simulatorPlugin;

	/** URI of the inbound port offering the control interface.				*/
	public static final String			CONTROL_INBOUND_PORT_URI =
													"BOILER-CONTROL-IBP-URI";
	/** the inbound port offering the control interface.					*/
	protected BoilerControlInboundPort	controlIBP;
	public static final String			SENSOR_INBOUND_PORT_URI =
													"BOILER-SENSOR-IBP-URI";
	protected BoilerSensorInboundPort	sensorIBP;
	public static final String			ACTUATOR_INBOUND_PORT_URI =
													"BOILER-ACTUATOR-IBP-URI";
	protected BoilerActuatorInboundPort	actuatorIBP;

	/** true if the boiler should be currently heating the water.			*/
	protected final AtomicBoolean	isHeating;
	/** maximum time during which the boiler can be suspended.				*/
	protected static long	MAX_SUSPENSION = Duration.ofHours(12).toMillis() ;
	/** is the boiler passive or active.									*/
	protected final AtomicBoolean	passive;
	/** last time the boiler was suspended while it is still suspended.		*/
	protected final AtomicReference<LocalTime>	lastSuspensionTime;

	/** temperature of the heating plate in the boiler.						*/
	public final static double		STANDARD_HEATING_TEMP = 65.0;
	/** temperature in the room where the boiler is, assumed constant.		*/
	public final static double		EXTERNAL_TEMP = 20.0;
	/** heating transfer constant in the differential equation.				*/
	public final static double		HEATING_TRANSFER_CONSTANT = 1000.0;
	/** insulation heat transfer constant in the differential equation.		*/
	public final static double 		INSULATION_TRANSFER_CONSTANT = 10000.0;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a boiler component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param isSILSimulated	true if the component is executed in a SIL simulation mode.
	 * @throws Exception		<i>to do</i>.
	 */
	protected			Boiler(boolean isSILSimulated) throws Exception
	{
		super(REFLECTION_INBOUND_PORT_URI, 1, 0);

		this.isHeating	= new AtomicBoolean(false);
		this.passive = new AtomicBoolean(false);
		this.lastSuspensionTime = new AtomicReference<LocalTime>();
		this.initialise(isSILSimulated);
	}

	/**
	 * initialise a boiler component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param isSILSimulated	true if the component is executed in a SIL simulation mode.
	 * @throws Exception		<i>to do</i>.
	 */
	protected void		initialise(boolean isSILSimulated) throws Exception
	{
		this.isSILSimulated = isSILSimulated;
		this.controlIBP =
				new BoilerControlInboundPort(CONTROL_INBOUND_PORT_URI, this);
		this.controlIBP.publishPort();
		this.sensorIBP = new BoilerSensorInboundPort(this);
		this.sensorIBP.publishPort();
		this.actuatorIBP = new BoilerActuatorInboundPort(this);
		this.actuatorIBP.publishPort();

		this.tracer.get().setTitle("Boiler component");
		this.tracer.get().setRelativePosition(1, 0);
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
				this.simulatorPlugin = new BoilerRTAtomicSimulatorPlugin();
				this.simulatorPlugin.setPluginURI(BoilerWaterSILModel.URI);
				this.simulatorPlugin.setSimulationExecutorService(
											SCHEDULED_EXECUTOR_SERVICE_URI);
				this.simulatorPlugin.initialiseSimulationArchitecture();
				this.installPlugin(this.simulatorPlugin);
			} catch (Exception e) {
				throw new ComponentStartException(e) ;
			}
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.controlIBP.unpublishPort();
			this.sensorIBP.unpublishPort();
			this.actuatorIBP.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#switchOn(int)
	 */
	@Override
	public boolean		switchOn(int initialMode) throws Exception
	{
		// Boiler operating mode not yet implemented!
		return true;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#switchOff()
	 */
	@Override
	public boolean		switchOff() throws Exception
	{
		// Boiler operating mode not yet implemented!
		return true;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#eco()
	 */
	@Override
	public void			eco() throws Exception
	{
		throw new RuntimeException(
						"Boiler operating mode not yet implemented!");
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#std()
	 */
	@Override
	public void			std() throws Exception
	{
		throw new RuntimeException(
						"Boiler operating mode not yet implemented!");
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#mode()
	 */
	@Override
	public int			mode() throws Exception
	{
		throw new RuntimeException(
						"Boiler operating mode not yet implemented!");
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#active()
	 */
	@Override
	public boolean		active() throws Exception
	{
		return !this.passive.get();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#passivate()
	 */
	@Override
	public boolean		passivate() throws Exception
	{
		this.logMessage("Boiler: passivate");
		boolean succeed = false;
		synchronized (this.passive) {
			succeed = this.passive.compareAndSet(false, true);
			if (succeed) {
				this.lastSuspensionTime.set(LocalTime.now());
			}
		}

		if (this.isSILSimulated) {
			this.simulatorPlugin.triggerExternalEvent(
											BoilerWaterSILModel.URI,
											t -> new Passivate(t));
		}

		return succeed;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#activate()
	 */
	@Override
	public boolean		activate() throws Exception
	{
		this.logMessage("Boiler: activate");
		boolean succeed;
		synchronized (this.passive) {
			succeed = this.passive.compareAndSet(true, false);
			if (succeed) {
				this.lastSuspensionTime.set(null);
			}
		}

		if (this.isSILSimulated) {
			this.simulatorPlugin.triggerExternalEvent(
										BoilerWaterSILModel.URI,
										t -> new Activate(t));
		}

		return succeed;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#degreeOfEmergency()
	 */
	@Override
	public double		degreeOfEmergency() throws Exception
	{
		synchronized (this.passive) {
			if (!this.passive.get()) {
				return 0.0;
			} else {
				Duration d = Duration.between(this.lastSuspensionTime.get(),
											  LocalTime.now());
				long inMillis = d.toMillis();
				if (inMillis > MAX_SUSPENSION) {
					return 1.0;
				} else {
					return ((double)inMillis)/((double)MAX_SUSPENSION);
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.BoilerReactiveControlImplementationI#isHeating()
	 */
	@Override
	public boolean		isHeating()
	{
		return this.isHeating.get();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.BoilerReactiveControlImplementationI#heatingSwitch(boolean)
	 */
	@Override
	public void			heatingSwitch(boolean heat)
	{
		this.isHeating.set(heat);

		if (this.isSILSimulated) {
			try {
				// trigger the event Heat or DoNotHeat on the simulation
				// model to drive its state and make it simulate the
				// corresponding boiler behaviour
				if (heat) {
					this.simulatorPlugin.triggerExternalEvent(
											BoilerWaterSILModel.URI,
											t -> new Heat(t));
				} else {
					this.simulatorPlugin.triggerExternalEvent(
											BoilerWaterSILModel.URI,
											t -> new DoNotHeat(t));
				}
			} catch (Exception e) {
				throw new RuntimeException(e) ;
			}
		}
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e3.equipments.boiler.BoilerReactiveControlImplementationI#waterTemperatureSensor()
	 */
	@Override
	public double		waterTemperatureSensor()
	{
		if (this.isSILSimulated) {
			try {
				// get the water temperature from the simulation model
				return (double) this.simulatorPlugin.getModelStateValue(
									BoilerWaterSILModel.URI,
									BoilerRTAtomicSimulatorPlugin.
											WATER_TEMPERATURE_VARIABLE_NAME);
			} catch (Exception e) {
				throw new RuntimeException(e) ;
			}
		} else {
			throw new RuntimeException(
						"Actuel water temperature sensor not implemented yet!");
		}
	}
}
// -----------------------------------------------------------------------------
