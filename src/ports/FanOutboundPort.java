package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.FanCI;
import utils.FanLevel;

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
	 * @param uri   uri of the port
	 * @param owner owner component
	 * @throws Exception
	 */
	public FanOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, FanCI.class, owner);
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
