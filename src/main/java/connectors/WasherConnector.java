package main.java.connectors;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.interfaces.WasherCI;

/**
 * Connector for the WasherCI component interface
 * 
 * @author Bello Memmi
 *
 */
public class WasherConnector extends AbstractConnector implements WasherCI {

	/**
	 * @see main.java.interfaces.WasherImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return ((WasherCI) this.offering).isTurnedOn();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#setProgramTemperature(int)
	 */
	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		((WasherCI) this.offering).setProgramTemperature(temperature);
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#getProgramTemperature()
	 */
	@Override
	public int getProgramTemperature() throws Exception {
		return ((WasherCI) this.offering).getProgramTemperature();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#setProgramDuration(int)
	 */
	@Override
	public void setProgramDuration(int duration) throws Exception {
		((WasherCI) this.offering).setProgramDuration(duration);
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#getProgramDuration()
	 */
	@Override
	public int getProgramDuration() throws Exception {
		return ((WasherCI) this.offering).getProgramDuration();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#turnOn()
	 */
	@Override
	public boolean turnOn() throws Exception {
		return ((WasherCI) this.offering).turnOn();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#turnOff()
	 */
	@Override
	public boolean turnOff() throws Exception {
		return ((WasherCI) this.offering).turnOff();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((WasherCI) this.offering).upMode();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((WasherCI) this.offering).downMode();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((WasherCI) this.offering).setMode(modeIndex);
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((WasherCI) this.offering).currentMode();
	}

	/**
	 * @see main.java.interfaces.PlanningEquipmentControlCI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception {
		return ((WasherCI) this.offering).hasPlan();
	}

	/**
	 * @see main.java.interfaces.PlanningEquipmentControlCI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception {
		return ((WasherCI) this.offering).startTime();
	}

	/**
	 * @see main.java.interfaces.PlanningEquipmentControlCI#duration()
	 */
	@Override
	public Duration duration() throws Exception {
		return ((WasherCI) this.offering).duration();
	}

	/**
	 * @see main.java.interfaces.PlanningEquipmentControlCI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception {
		return ((WasherCI) this.offering).deadline();
	}

	/**
	 * @see main.java.interfaces.PlanningEquipmentControlCI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception {
		return ((WasherCI) this.offering).postpone(d);
	}

	/**
	 * @see main.java.interfaces.PlanningEquipmentControlCI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception {
		return ((WasherCI) this.offering).cancel();
	}

	/**
	 * @see main.java.interfaces.WasherImplementationI#planifyEvent(Duration,
	 *      LocalTime)
	 */
	@Override
	public boolean planifyEvent(Duration durationLastPlanned, LocalTime deadline) throws Exception {
		return ((WasherCI) this.offering).planifyEvent(durationLastPlanned, deadline);
	}
}