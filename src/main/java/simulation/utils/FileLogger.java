package main.java.simulation.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fr.sorbonne_u.devs_simulation.utils.StandardLogger;

public class FileLogger extends StandardLogger {

	private FileWriter writer;

	public FileLogger(String filename) {
		super();
		try {
			// check if the directory "simulationLogs" exists, if not, create it
			File directory = new File("simulationLogs");
			if (!directory.exists())
				directory.mkdir();
			writer = new FileWriter("simulationLogs/" + filename, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void logMessage(String modelURI, String message) {
		assert modelURI != null;
		assert !modelURI.contains(separator);

		String toWrite = System.currentTimeMillis() + this.separator + modelURI + this.separator + message + "\n";
		System.out.print(toWrite);
		try {
			writer.append(toWrite);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
