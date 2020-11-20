package ports;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.WasherCI;
import interfaces.WasherImplementationI;

/**
 *
 * Outbound port of Washer component
 *
 * @author Bello Memmi
 *
 */
public class WasherOutboundPort extends AbstractOutboundPort implements WasherCI {

	private static final long serialVersionUID = 1L;

	public WasherOutboundPort(ComponentI owner) throws Exception {
		super(WasherCI.class, owner);
	}


	@Override
	public boolean isTurnedOn() throws Exception {
		return ((WasherCI) this.getConnector()).isTurnedOn();
	}

	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		((WasherCI) this.getConnector()).setProgramTemperature(temperature);
	}

	@Override
	public int getProgramTemperature() throws Exception {
		return ((WasherCI) this.getConnector()).getProgramTemperature();
	}

	@Override
	public void setProgramDuration(int duration) throws Exception {
		((WasherCI) this.getConnector()).setProgramDuration(duration);
	}

	@Override
	public int getProgramDuration() throws Exception {
		return ((WasherCI) this.getConnector()).getProgramDuration();
	}

	@Override
	public boolean turnOn() throws Exception {
		return ((WasherCI) this.getConnector()).turnOn();
	}

	@Override
	public boolean turnOff() throws Exception {
		return ((WasherCI) this.getConnector()).turnOff();
	}

	@Override
	public boolean upMode() throws Exception{
		return ((WasherCI) this.getConnector()).upMode();
	}

	@Override
	public boolean downMode() throws Exception{
		return ((WasherCI) this.getConnector()).downMode();
	}

	@Override
	public boolean setMode(int modeIndex) throws Exception{
		return ((WasherCI) this.getConnector()).setMode(modeIndex);
	}

	@Override
	public int currentMode() throws Exception{
		return ((WasherCI) this.getConnector()).currentMode();
	}

	@Override
	public boolean hasPlan() throws Exception{
		return ((WasherCI) this.getConnector()).hasPlan();
	}

	@Override
	public LocalTime startTime() throws Exception{
		return ((WasherCI) this.getConnector()).startTime();
	}

	@Override
	public Duration duration() throws Exception{
		return ((WasherCI) this.getConnector()).duration();
	}

	@Override
	public LocalTime deadline() throws Exception{
		return ((WasherCI) this.getConnector()).deadline();
	}

	@Override
	public boolean postpone(Duration d) throws Exception{
		return ((WasherCI) this.getConnector()).postpone(d);
	}

	@Override
	public boolean cancel() throws Exception{
		return ((WasherCI) this.getConnector()).cancel();
	}
}
