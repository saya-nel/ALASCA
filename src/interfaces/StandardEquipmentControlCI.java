package interfaces;

import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface StandardEquipmentControlCI extends RequiredCI {
	/**
	 * switch on the equipment, returning true if the operation succeeded or false
	 * otherwise.
	 *
	 * @return true if the operation succeeded or false otherwise.
	 */
	public boolean on();

	/**
	 * switch off the equipment, returning true if the operation succeeded or false
	 * otherwise.
	 *
	 * @return true if the operation succeeded or false otherwise.
	 */
	public boolean off();

	/**
	 * force the equipment to the next more energy consuming mode of operation,
	 * returning true if the operation succeeded or false otherwise.
	 *
	 * @return true if the operation succeeded or false otherwise.
	 */
	public boolean upMode();

	/**
	 * force the equipment to the next less energy consuming mode of operation,
	 * returning true if the operation succeeded or false otherwise.
	 *
	 * @return true if the operation succeeded or false otherwise.
	 */
	public boolean downMode();

	/**
	 * set the equipment to the given mode of operation, returning true if the
	 * operation succeeded or false otherwise.
	 *
	 * @param modeIndex index of the new mode of operation.
	 * @return true if the operation succeeded or false otherwise.
	 */
	public boolean setMode(int modeIndex);

	/**
	 * return the current mode of operation of the equipment.
	 *
	 * @return the current mode of operation of the equipment.
	 */
	public int currentMode();
}
