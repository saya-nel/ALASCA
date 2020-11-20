package connectors;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.WasherCI;
import interfaces.WasherImplementationI;

public class WasherConnector extends AbstractConnector implements WasherCI {

	@Override
	public boolean isTurnedOn() throws Exception {
		return ((WasherCI) this.offering).isTurnedOn();
	}

	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		((WasherCI) this.offering).setProgramTemperature(temperature);
	}

	@Override
	public int getProgramTemperature() throws Exception {
		return ((WasherCI) this.offering).getProgramTemperature();
	}

	@Override
	public void setProgramDuration(int duration) throws Exception {
		((WasherCI) this.offering).setProgramDuration(duration);
	}

	@Override
	public int getProgramDuration() throws Exception {
		return ((WasherCI) this.offering).getProgramDuration();
	}

	@Override
	public boolean turnOn() throws Exception {
		return ((WasherCI) this.offering).turnOn();
	}

	@Override
	public boolean turnOff() throws Exception {
		return ((WasherCI) this.offering).turnOff();
	}

	@Override
	public boolean upMode() throws Exception {
		return ((WasherCI) this.offering).upMode();
	}

	@Override
	public boolean downMode() throws Exception{
		return ((WasherCI) this.offering).downMode();
	}

	@Override
	public boolean setMode(int modeIndex) throws Exception{
		return ((WasherCI) this.offering).setMode(modeIndex);
	}

	@Override
	public int currentMode() throws Exception{
		return ((WasherCI) this.offering).currentMode();
	}

	@Override
	public boolean hasPlan() throws Exception{
		return ((WasherCI) this.offering).hasPlan();
	}

	@Override
	public LocalTime startTime() throws Exception{
		return ((WasherCI) this.offering).startTime();
	}

	@Override
	public Duration duration() throws Exception{
		return ((WasherCI) this.offering).duration();
	}

	@Override
	public LocalTime deadline() throws Exception{
		return ((WasherCI) this.offering).deadline();
	}

	@Override
	public boolean postpone(Duration d) throws Exception{
		return ((WasherCI) this.offering).postpone(d);
	}

	@Override
	public boolean cancel() throws Exception{
		return ((WasherCI) this.offering).cancel();
	}
}
