package main.java.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.interfaces.SuspensionEquipmentControlCI;

/**
 * outbound port for the suspension equipment control component interface
 * 
 * @author Bello Memmi
 *
 */
public class SuspensionEquipmentControlOutboundPort extends AbstractOutboundPort
		implements SuspensionEquipmentControlCI {

	private static final long serialVersionUID = 5349182540590991024L;

	/**
	 * Constructor of the SuspensionEquipmentControlOutboundPort
	 * 
	 * @param owner owner component
	 * @throws Exception
	 */
	public SuspensionEquipmentControlOutboundPort(ComponentI owner) throws Exception {
		super(SuspensionEquipmentControlCI.class, owner);
	}

	/**
	 * @see main.java.interfaces.SuspensionEquipmentControlCI#suspended()
	 */
	@Override
	public boolean suspended() throws Exception {
		return ((SuspensionEquipmentControlCI) this.getConnector()).suspended();
	}

	/**
	 * @see main.java.interfaces.SuspensionEquipmentControlCI#suspend()
	 */
	@Override
	public boolean suspend() throws Exception {
		return ((SuspensionEquipmentControlCI) this.getConnector()).suspend();
	}

	/**
	 * @see main.java.interfaces.SuspensionEquipmentControlCI#resume()
	 */
	@Override
	public boolean resume() throws Exception {
		return ((SuspensionEquipmentControlCI) this.getConnector()).resume();
	}

	/**
	 * @see main.java.interfaces.SuspensionEquipmentControlCI#emergency()
	 */
	@Override
	public double emergency() throws Exception {
		return ((SuspensionEquipmentControlCI) this.getConnector()).emergency();
	}

	/**
	 * @see main.java.interfaces.SuspensionEquipmentControlCI#on()
	 */
	@Override
	public boolean on() throws Exception {
		return ((SuspensionEquipmentControlCI) this.getConnector()).on();
	}

	/**
	 * @see main.java.interfaces.SuspensionEquipmentControlCI#off()
	 */
	@Override
	public boolean off() throws Exception {
		return ((SuspensionEquipmentControlCI) this.getConnector()).off();
	}

	/**
	 * @see main.java.interfaces.SuspensionEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((SuspensionEquipmentControlCI) this.getConnector()).upMode();
	}

	/**
	 * @see main.java.interfaces.SuspensionEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((SuspensionEquipmentControlCI) this.getConnector()).downMode();
	}

	/**
	 * @see main.java.interfaces.SuspensionEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((SuspensionEquipmentControlCI) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see main.java.interfaces.SuspensionEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((SuspensionEquipmentControlCI) this.getConnector()).currentMode();
	}
}
