package fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler;

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

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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
@OfferedInterfaces(offered= {BoilerControlCI.class})
// -----------------------------------------------------------------------------
public class			Boiler
extends		AbstractComponent
implements	BoilerControlImplementationI
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
	"    <body equipmentRef=\"boiler\">return boiler.active();</body>\n" +
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

	/** maximum time during which the boiler can be suspended.				*/
	protected static long	MAX_SUSPENSION = Duration.ofHours(12).toMillis() ;
	/** is the boiler passive or active.									*/
	protected final AtomicBoolean				passive;
	/** last time the boiler was suspended while it is still suspended.		*/
	protected final AtomicReference<LocalTime>	lastSuspensionTime;
	/** current mode of operation of the boiler (eco or standard).			*/
	protected final AtomicInteger				operatingMode;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a new boiler component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 */
	public				Boiler()
	{
		super(1, 0);

		this.passive = new AtomicBoolean(false);
		this.lastSuspensionTime = new AtomicReference<LocalTime>();
		this.operatingMode = new AtomicInteger(BoilerControlCI.STD);
	}

	/**
	 * create a new boiler component with the given reflection inbound port URI.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param reflectionInboundPortURI	URI of reflection inbound port URI.
	 */
	public				Boiler(String reflectionInboundPortURI)
	{
		super(reflectionInboundPortURI, 1, 0);

		this.passive = new AtomicBoolean(false);
		this.lastSuspensionTime = new AtomicReference<LocalTime>();
		this.operatingMode = new AtomicInteger(BoilerControlCI.STD);
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#switchOn(int)
	 */
	@Override
	public boolean		switchOn(int initialMode)
	{
		this.operatingMode.set(initialMode);
		return true;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#switchOff()
	 */
	@Override
	public boolean		switchOff()
	{
		return true;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#eco()
	 */
	@Override
	public void			eco()
	{
		this.operatingMode.set(BoilerControlCI.ECO);
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#std()
	 */
	@Override
	public void			std()
	{
		this.operatingMode.set(BoilerControlCI.STD);
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#mode()
	 */
	@Override
	public int			mode()
	{
		return this.operatingMode.get();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#active()
	 */
	@Override
	public boolean		active()
	{
		return !this.passive.get();
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#passivate()
	 */
	@Override
	public boolean		passivate()
	{
		boolean succeed = false;
		synchronized (this.passive) {
			succeed = this.passive.compareAndSet(false, true);
			if (succeed) {
				this.lastSuspensionTime.set(LocalTime.now());
			}
		}
		return succeed;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#activate()
	 */
	@Override
	public boolean		activate()
	{
		boolean succeed;
		synchronized (this.passive) {
			succeed = this.passive.compareAndSet(true, false);
			if (succeed) {
				this.lastSuspensionTime.set(null);
			}
		}
		return succeed;
	}

	/**
	 * @see fr.sorbonne_u.components.cyphy.hem2020e1.equipments.boiler.BoilerControlImplementationI#degreeOfEmergency()
	 */
	@Override
	public double		degreeOfEmergency()
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
}
// -----------------------------------------------------------------------------
