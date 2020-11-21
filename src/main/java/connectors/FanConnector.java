package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.FanCI;
import utils.FanLevel;

/**
 * Connector for the FanCI component interface
 * 
 * @author Bello Memmi
 *
 */
public class FanConnector extends AbstractConnector implements FanCI {

	/**
	 * @see interfaces.FanImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((FanCI) this.offering).turnOn();
	}

	/**
	 * @see interfaces.FanImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((FanCI) this.offering).turnOff();
	}

	/**
	 * @see interfaces.FanImplementationI#adjustPower(FanLevel)
	 */
	@Override
	public void adjustPower(FanLevel level) throws Exception {
		((FanCI) this.offering).adjustPower(level);
	}

	/**
	 * @see interfaces.FanImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return ((FanCI) this.offering).isTurnedOn();
	}

	/**
	 * @see interfaces.FanImplementationI#getFanLevel()
	 */
	@Override
	public FanLevel getFanLevel() throws Exception {
		return ((FanCI) this.offering).getFanLevel();
	}

}
