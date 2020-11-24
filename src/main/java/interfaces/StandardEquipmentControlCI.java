package main.java.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface StandardEquipmentControlCI extends RequiredCI, OfferedCI {
	/**
	 * switch on the equipment, returning true if the operation succeeded or false
	 * otherwise.
	 *
	 * @return true if the operation succeeded or false otherwise.
	 */
	public boolean on() throws Exception;

	/**
	 * switch off the equipment, returning true if the operation succeeded or false
	 * otherwise.
	 *
	 * @return true if the operation succeeded or false otherwise.
	 */
	public boolean off() throws Exception;

	/**
	 * force the equipment to the next more energy consuming mode of operation,
	 * returning true if the operation succeeded or false otherwise.
	 *
	 * @return true if the operation succeeded or false otherwise.
	 */
	public boolean upMode() throws Exception;

	/**
	 * force the equipment to the next less energy consuming mode of operation,
	 * returning true if the operation succeeded or false otherwise.
	 *
	 * @return true if the operation succeeded or false otherwise.
	 */
	public boolean downMode() throws Exception;

	/**
	 * set the equipment to the given mode of operation, returning true if the
	 * operation succeeded or false otherwise.
	 *
	 * @param modeIndex index of the new mode of operation.
	 * @return true if the operation succeeded or false otherwise.
	 */
	public boolean setMode(int modeIndex) throws Exception;

	/**
	 * return the current mode of operation of the equipment.
	 *
	 * @return the current mode of operation of the equipment.
	 */
	public int currentMode() throws Exception;
}
