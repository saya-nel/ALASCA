package interfaces;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 *
 * Washer component interface
 *
 * @author Bello Memmi
 *
 */
public interface WasherCI extends WasherImplementationI, RequiredCI, OfferedCI {
	@Override
	boolean isTurnedOn() throws Exception;

	@Override
	void setProgramTemperature(int temperature) throws Exception;

	@Override
	int getProgramTemperature() throws Exception;

	@Override
	void setProgramDuration(int duration) throws Exception;

	@Override
	int getProgramDuration() throws Exception;

	@Override
	boolean turnOn() throws Exception;

	@Override
	boolean turnOff() throws Exception;

	@Override
	boolean upMode() throws Exception;

	@Override
	boolean downMode() throws Exception;

	@Override
	boolean setMode(int modeIndex) throws Exception;

	@Override
	int currentMode() throws Exception;

	@Override
	boolean hasPlan() throws Exception;

	@Override
	LocalTime startTime() throws Exception;

	@Override
	Duration duration() throws Exception;

	@Override
	LocalTime deadline() throws Exception;

	@Override
	boolean postpone(Duration d) throws Exception;

	@Override
	boolean cancel() throws Exception;
}
