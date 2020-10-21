package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import utils.FanLevel;

/**
 * 
 * Fan component interface
 * 
 * @author Bello Memmi
 *
 */
public interface FanCI extends FanImplementationI, OfferedCI, RequiredCI {

	/**
	 * @see interfaces.FanImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception;

	/**
	 * @see interfaces.FanImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception;

	/**
	 * @see interfaces.FanImplementationI#adjustPower(FanLevel)
	 */
	@Override
	public void adjustPower(FanLevel level) throws Exception;

	/**
	 * @see interfaces.FanImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception;

	/**
	 * @see interfaces.FanImplementationI#getFanLevel()
	 */
	@Override
	public FanLevel getFanLevel() throws Exception;
}
