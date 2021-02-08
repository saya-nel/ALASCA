package main.java.components.fan.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import main.java.components.fan.Fan;
import main.java.components.fan.utils.FanLevel;

/**
 * 
 * The component interface <code>FanCI</code> defines the services a {@link Fan}
 * component offers and that can be required from it.
 * 
 * @author Bello Memmi
 *
 */
public interface FanCI extends FanImplementationI, OfferedCI, RequiredCI {

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception;

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception;

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#adjustPower(FanLevel)
	 */
	@Override
	public void adjustPower(FanLevel level) throws Exception;

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception;

	/**
	 * @see main.java.components.fan.interfaces.FanImplementationI#getFanLevel()
	 */
	@Override
	public FanLevel getFanLevel() throws Exception;
}
