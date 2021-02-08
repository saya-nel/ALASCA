package main.java.components.battery.interfaces;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import main.java.components.battery.Battery;

/**
 * 
 * The component interface <code>BatteryCI</code> defines the services a
 * {@link Battery} component offers and that can be required from it.
 * 
 * @author Bello Memmi
 *
 */
public interface BatteryCI extends BatteryImplementationI, OfferedCI, RequiredCI {

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	@Override
	public float getBatteryCharge() throws Exception;

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception;

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception;

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception;

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception;

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#hasPlan()
	 */
	@Override
	public boolean hasPlan() throws Exception;

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#startTime()
	 */
	@Override
	public LocalTime startTime() throws Exception;

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#duration()
	 */
	@Override
	public Duration duration() throws Exception;

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#deadline()
	 */
	@Override
	public LocalTime deadline() throws Exception;

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#postpone(Duration)
	 */
	@Override
	public boolean postpone(Duration d) throws Exception;

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#cancel()
	 */
	@Override
	public boolean cancel() throws Exception;

	/**
	 * @see main.java.components.battery.interfaces.BatteryImplementationI#planifyEvent(Duration,
	 *      LocalTime)
	 */
	@Override
	public boolean planifyEvent(LocalTime startTime, LocalTime endTime) throws Exception;
}
