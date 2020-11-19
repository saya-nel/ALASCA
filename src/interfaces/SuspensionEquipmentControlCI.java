package interfaces;

public interface SuspensionEquipmentControlCI extends StandardEquipmentControlCI {
	/**
	 * return true if the equipment has been suspended.
	 *
	 * @return true if the equipment has been suspended.
	 */
	public boolean suspended();

	/**
	 * suspend the equipment, returning true if the suspension succeeded or false
	 * otherwise.
	 *
	 * @return true if the suspension succeeded or false otherwise.
	 */
	public boolean suspend();

	/**
	 * resume the previously suspended equipment, returning true if the resumption
	 * succeeded or false otherwise.
	 *
	 * @return true if the resumption succeeded or false otherwise.
	 */
	public boolean resume();

	/**
	 * return the degree of emergency of a resumption for the previously suspended
	 * equipment.
	 *
	 * @return the degree of emergency of a resumption for the previously suspended
	 *         equipment.
	 */
	public double emergency();
}