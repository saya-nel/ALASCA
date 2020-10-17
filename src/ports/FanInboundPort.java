package ports;

import components.Fan;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.FanCI;
import utils.FanLevel;

/**
 * 
 * Fan inbound port for Fan component interface
 * 
 * @author Bello Memmi
 *
 */
public class FanInboundPort extends AbstractInboundPort implements FanCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Construtor of the fan inbound port
	 * 
	 * @param uri   of the fan inbound port
	 * @param owner owner component
	 * @throws Exception
	 */
	public FanInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, FanCI.class, owner);
	}

	/**
	 * @see interfaces.FanImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Fan) owner).turnOn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see interfaces.FanImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Fan) owner).turnOff();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see interfaces.FanImplementationI#adjustPower(FanLevel)
	 */
	@Override
	public void adjustPower(FanLevel level) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((Fan) owner).adjustPower(level);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see interfaces.FanImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fan) owner).isTurnedOn());
	}

	/**
	 * @see interfaces.FanImplementationI#getFanLevel()
	 */
	@Override
	public FanLevel getFanLevel() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((Fan) owner).getFanLevel());
	}

}
