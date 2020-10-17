package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import utils.FanLevel;

/**
 * 
 * Fan component interface
 * 
 * @author Bello Memmy
 *
 */
public interface FanCI extends FanImplementationI, OfferedCI {

	/**
	 * @see interfaces.FanImplementationI#turnOn()
	 */
	@Override
	void turnOn() throws Exception;

	/**
	 * @see interfaces.FanImplementationI#turnOff()
	 */
	@Override
	void turnOff() throws Exception;

	/**
	 * @see interfaces.FanImplementationI#adjustPower(FanLevel)
	 */
	@Override
	void adjustPower(FanLevel level) throws Exception;

	/**
	 * @see interfaces.FanImplementationI#isTurnedOn()
	 */
	@Override
	boolean isTurnedOn() throws Exception;

	/**
	 * @see interfaces.FanImplementationI#getFanLevel()
	 */
	@Override
	FanLevel getFanLevel() throws Exception;
}
