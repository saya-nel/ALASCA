package ports;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

import components.Washer;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.WasherCI;

/**
 * Class representing the inbound port of the component Washer
 *
 * @author Bello Memmi
 *
 */
public class WasherInboundPort extends AbstractInboundPort implements WasherCI {

	private static final long serialVersionUID = 1L;

	public WasherInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, WasherCI.class, owner);
	}


	@Override
	public boolean isTurnedOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).isTurnedOn());
	}

	@Override
	public void setProgramTemperature(int temperature) throws Exception {
		this.getOwner().runTask(owner -> {
			try{
				((Washer) owner).setProgramTemperature(temperature);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	@Override
	public int getProgramTemperature() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).getProgramTemperature());
	}

	@Override
	public void setProgramDuration(int duration) throws Exception {
		this.getOwner().runTask(owner -> {
			try{
				((Washer) owner).setProgramDuration(duration);
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	@Override
	public int getProgramDuration() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).getProgramDuration());
	}

	@Override
	public boolean turnOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).turnOn());
	}

	@Override
	public boolean turnOff() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).turnOff());
	}

	@Override
	public boolean upMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).upMode());
	}

	@Override
	public boolean downMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).downMode());
	}

	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).setMode(modeIndex));
	}

	@Override
	public int currentMode() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).currentMode());
	}

	@Override
	public boolean hasPlan() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).hasPlan());
	}

	@Override
	public LocalTime startTime() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).startTime());
	}

	@Override
	public Duration duration() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).duration());
	}

	@Override
	public LocalTime deadline() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).deadline());
	}

	@Override
	public boolean postpone(Duration d) throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).postpone(d));
	}

	@Override
	public boolean cancel() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Washer) owner).cancel());
	}
}
