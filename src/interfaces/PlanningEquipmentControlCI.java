package interfaces;

import java.time.Duration;
import java.time.LocalTime;

public interface PlanningEquipmentControlCI extends StandardEquipmentControlCI {
	/**
	 * return true if the equipment currently has a planned program.
	 * 
	 * @return true if the equipment currently has a planned program.
	 */
	public boolean hasPlan();

	/**
	 * return the time at which the current planned program will start or null if
	 * none has been planned.
	 *
	 * @return the time at which the current planned program will start or null if
	 *         none has been planned.
	 */
	public LocalTime startTime();

	/**
	 * return the time at which the current planned program will start or null if
	 * none has been planned.
	 *
	 * @return the time at which the current planned program will start or null if
	 *         none has been planned.
	 */
	public Duration duration();

	/**
	 * return the time at which the current planned program must finish or null if
	 * none has been planned.
	 *
	 * @return the time at which the current planned program must finish or null if
	 *         none has been planned.
	 */
	public LocalTime deadline();

	/**
	 * postpone the start time of the planned program by the given duration,
	 * returning true if the operation succeeded or false otherwise.
	 *
	 * @param d duration by which the program must be postponed.
	 * @return true if the operation succeeded or false otherwise.
	 */
	public boolean postpone(Duration d);

	/**
	 * cancel the planned program, returning true if the operation succeeded or
	 * false otherwise.
	 *
	 * @return true if the operation succeeded or false otherwise.
	 */
	public boolean cancel();
}
