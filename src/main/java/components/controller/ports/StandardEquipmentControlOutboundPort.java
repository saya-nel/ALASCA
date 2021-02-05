package main.java.components.controller.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.components.controller.interfaces.StandardEquipmentControlCI;

/**
 * outbound port for the standard equipment control component interface
 * 
 * @author Bello Memmi
 *
 */
public class StandardEquipmentControlOutboundPort extends AbstractOutboundPort implements StandardEquipmentControlCI {

	private static final long serialVersionUID = 8035295470318061359L;

	/**
	 * Constructor of the StandardEquipmentControlOutboundPort
	 * 
	 * @param owner owner component
	 * @throws Exception
	 */
	public StandardEquipmentControlOutboundPort(ComponentI owner) throws Exception {
		super(StandardEquipmentControlCI.class, owner);
	}

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#on()
	 */
	@Override
	public boolean on() throws Exception {
		return ((StandardEquipmentControlCI) this.getConnector()).on();
	}

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#off()
	 */
	@Override
	public boolean off() throws Exception {
		return ((StandardEquipmentControlCI) this.getConnector()).off();
	}

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#upMode()
	 */
	@Override
	public boolean upMode() throws Exception {
		return ((StandardEquipmentControlCI) this.getConnector()).upMode();
	}

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#downMode()
	 */
	@Override
	public boolean downMode() throws Exception {
		return ((StandardEquipmentControlCI) this.getConnector()).downMode();
	}

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#setMode(int)
	 */
	@Override
	public boolean setMode(int modeIndex) throws Exception {
		return ((StandardEquipmentControlCI) this.getConnector()).setMode(modeIndex);
	}

	/**
	 * @see main.java.components.controller.interfaces.StandardEquipmentControlCI#currentMode()
	 */
	@Override
	public int currentMode() throws Exception {
		return ((StandardEquipmentControlCI) this.getConnector()).currentMode();
	}
}
