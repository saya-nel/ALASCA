package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import interfaces.HeaterCI;
import interfaces.HeaterImplementationI;

/**
 * Class representing the Heater component
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { HeaterCI.class })
public class Heater extends AbstractComponent implements HeaterImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	protected float requestedTemperature;

	protected boolean isOn;


	/**
	 * Constructor of the heater
	 * 
	 * @param uri of the Heater component
	 */
	public Heater(String uri) {
		super(uri, 1, 0);
		myUri = uri;
		this.requestedTemperature = 20;
		this.isOn = false;
	}
	/**
	 * @see HeaterCI
	 */
	public void turnOff() throws Exception
	{
		this.isOn = false;
	}

	/**
	 * @see HeaterCI
	 */
	public void turnOn() throws Exception
	{
		this.isOn = true;
	}
	/**
	 * @see HeaterCI
	 */
	public boolean heaterIsOn() throws Exception
	{
		return this.isOn;
	}
	/**
	 * @see HeaterCI
	 */
	public float getRequestedTemperature() throws Exception
	{
		return this.requestedTemperature;
	}

}
