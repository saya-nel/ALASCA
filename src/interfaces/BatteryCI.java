package interfaces;

import java.time.Duration;
import java.time.LocalTime;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * 
 * Battery component interface
 * 
 * @author Bello Memmi
 *
 */
public interface BatteryCI extends BatteryImplementationI, OfferedCI, RequiredCI {

	/**
	 * @see interfaces.BatteryImplementationI#getBatteryCharge()
	 */
	public float getBatteryCharge() throws Exception;

	/**
	 * @see interfaces.BatteryImplementationI#upMode()
	 */
	public boolean upMode();

	/**
	 * @see interfaces.BatteryImplementationI#downMode()
	 */
	public boolean downMode();

	/**
	 * @see interfaces.BatteryImplementationI#setMode(int)
	 */
	public boolean setMode(int modeIndex);

	/**
	 * @see interfaces.BatteryImplementationI#currentMode()
	 */
	public int currentMode();
	
	/**
	 * @see interfaces.BatteryImplementationI#hasPlan()
	 */
	public boolean hasPlan();

	/**
	 * @see interfaces.BatteryImplementationI#startTime()
	 */
	public LocalTime startTime();

	/**
	 * @see interfaces.BatteryImplementationI#duration()
	 */
	public Duration duration();

	/**
	 * @see interfaces.BatteryImplementationI#deadline()
	 */
	public LocalTime deadline();

	/**
	 * @see interfaces.BatteryImplementationI#postpone(Duration)
	 */
	public boolean postpone(Duration d);

	/**
	 * @see interfaces.BatteryImplementationI#cancel()
	 */
	public boolean cancel();

}
