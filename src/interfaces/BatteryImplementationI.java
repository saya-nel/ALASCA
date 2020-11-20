package interfaces;

import java.time.Duration;
import java.time.LocalTime;

import utils.BatteryState;

/**
 * 
 * Battery services interface
 * 
 * @author Bello Memmi
 *
 */
public interface BatteryImplementationI {

	/**
	 * Return the actual battery charge
	 * 
	 * @return the actual battery charge
	 * @throws Exception
	 */
	public float getBatteryCharge() throws Exception;

	/*
	 * 
	 * STANDARD
	 * 
	 */

	public boolean upMode();

	public boolean downMode();

	public boolean setMode(int modeIndex);

	public int currentMode();
	
	/*
	 * 
	 * PLANNING
	 * 
	 */
	

	public boolean hasPlan();

	public LocalTime startTime();


	public Duration duration();


	public LocalTime deadline();

	public boolean postpone(Duration d);

	public boolean cancel();

}
