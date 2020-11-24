package main.java.ports;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.interfaces.PlanningEquipmentControlCI;

public class PlanningEquipmentControlOutboundPort extends AbstractOutboundPort implements PlanningEquipmentControlCI {

	private static final long serialVersionUID = 2167115130128418684L;

	public PlanningEquipmentControlOutboundPort(ComponentI owner) throws Exception {
		super(PlanningEquipmentControlCI.class, owner);
	}

	public PlanningEquipmentControlOutboundPort(String inbound_uri, ComponentI owner) throws Exception {
		super(inbound_uri, PlanningEquipmentControlCI.class, owner);
	}

	@Override
	public boolean hasPlan() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).hasPlan();
	}

	@Override
	public LocalTime startTime() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).startTime();
	}

	@Override
	public Duration duration() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).duration();
	}

	@Override
	public LocalTime deadline() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).deadline();
	}

	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).postpone(d);
	}

	@Override
	public boolean cancel() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).cancel();
	}

	@Override
	public boolean on() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).on();
	}

	@Override
	public boolean off() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).off();
	}

	@Override
	public boolean upMode() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).upMode();
	}

	@Override
	public boolean downMode() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).downMode();
	}

	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).setMode(modeIndex);
	}

	@Override
	public int currentMode() throws Exception {
		return ((PlanningEquipmentControlCI) this.getConnector()).currentMode();
	}
}
