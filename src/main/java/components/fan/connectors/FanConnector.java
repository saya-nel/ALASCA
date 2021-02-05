package main.java.components.fan.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.components.fan.interfaces.FanCI;
import main.java.components.fan.utils.FanLevel;

/**
 * Connector for the FanCI component interface
 * 
 * @author Bello Memmi
 *
 */
public class FanConnector extends AbstractConnector implements FanCI {

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((FanCI) this.offering).turnOn();
	}

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((FanCI) this.offering).turnOff();
	}

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#adjustPower(FanLevel)
	 */
	@Override
	public void adjustPower(FanLevel level) throws Exception {
		((FanCI) this.offering).adjustPower(level);
	}

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return ((FanCI) this.offering).isTurnedOn();
	}

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#getFanLevel()
	 */
	@Override
	public FanLevel getFanLevel() throws Exception {
		return ((FanCI) this.offering).getFanLevel();
	}

}
