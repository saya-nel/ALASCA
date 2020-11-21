package utils;

import fr.sorbonne_u.components.ComponentI;

/**
 * This class allows to display a message on the standard output and to log it
 * on a component.
 * 
 * @author Bello Memmi
 */
public class Log {

	/**
	 * Displays a message on the standard output and the log on a component
	 * 
	 * @param component component on which the message is to be displayed
	 * @param message   message to be displayed
	 */
	public static void printAndLog(ComponentI component, String message) {
		System.out.println(message);
		component.logMessage(message);
	}
}
