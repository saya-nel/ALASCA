package main.java.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.interfaces.FanCI;
import main.java.utils.FanLevel;

/**
 * Outbound port of Fan component
 * 
 * @author Bello Memmi
 *
 */
public class FanOutboundPort extends AbstractOutboundPort implements FanCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of FanOutboundPort
	 * 
	 * @param owner owner component
	 * @throws Exception
	 */
	public FanOutboundPort(ComponentI owner) throws Exception {
		super(FanCI.class, owner);
	}

	/**
	 * @see interfaces.FanImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((FanCI) this.getConnector()).turnOn();
	}

	/**
	 * @see interfaces.FanImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((FanCI) this.getConnector()).turnOff();
	}

	/**
	 * @see interfaces.FanImplementationI#adjustPower(FanLevel)
	 */
	@Override
	public void adjustPower(FanLevel level) throws Exception {
		((FanCI) this.getConnector()).adjustPower(level);
	}

	/**
	 * @see interfaces.FanImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return ((FanCI) this.getConnector()).isTurnedOn();
	}

	/**
	 * @see interfaces.FanImplementationI#getFanLevel()
	 */
	@Override
	public FanLevel getFanLevel() throws Exception {
		return ((FanCI) this.getConnector()).getFanLevel();
	}

}
