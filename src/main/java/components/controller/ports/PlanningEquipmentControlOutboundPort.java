package main.java.components.controller.ports;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.components.controller.interfaces.PlanningEquipmentControlCI;

/**
 * The class <code>PlanningEquipmentControlOutboundPort</code> implements an
 * outbound port for the component interface
 * <code>PlanningEquipmentControlCI</code>.
 * 
 * @author Bello Memmi
 *
 */
public class PlanningEquipmentControlOutboundPort extends AbstractOutboundPort implements PlanningEquipmentControlCI {

	private static final long serialVersionUID = 2167115130128418684L;

	/**
	 * Constructor of the PlanningEquipmentControlOutboundPort
	 * 
	 * @param owner owner component
	 * @throws Exception
	 */
	public PlanningEquipmentControlOutboundPort(ComponentI owner) throws Exception {
		super(PlanningEquipmentControlCI.class, owner);
	}

	/**
	 * Constructor of the PlanningEquipmentControlOutboundPort
	 * 
	 * @param inbound_uri reflexion port uri
	 * @param owner       owner component
	 * @throws Exception
	 */
	public PlanningEquipmentControlOutboundPort(String inbound_uri, ComponentI owner) throws Exception {
		super(inbound_uri, PlanningEquipmentControlCI.class, owner);
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).hasPlan();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).startTime();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).duration();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).deadline();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).postpone(d);
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).cancel();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#on()
	 */
	@Override
	public boolean on() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).on();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#off()
	 */
	@Override
	public boolean off() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).off();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).upMode();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).downMode();
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see main.java.components.controller.interfaces.PlanningEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).currentMode();
	}
}
