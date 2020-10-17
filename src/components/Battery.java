package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import interfaces.BatteryCI;
import interfaces.BatteryImplementationI;
import ports.BatteryInboundPort;
import utils.BatteryState;

/**
 * Class representing the Battery component
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { BatteryCI.class })
public class Battery extends AbstractComponent implements BatteryImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	/**
	 * Actual charge of battery in mA/h
	 */
	protected float batteryCharge;

	/**
	 * Actual state of battery
	 */
	protected BatteryState stateBattery;

	/**
	 * Inbound port of the battery
	 */
	protected BatteryInboundPort fip;

	/**
	 * Constructor of the battery
	 * 
	 * @param uri of the Battery component
	 */
	public Battery(String uri, String fipURI) throws Exception{
		super(uri, 1, 0);
		myUri = uri;
		this.stateBattery = BatteryState.SLEEPING;
		this.batteryCharge = 0;
		fip = new BatteryInboundPort(fipURI, this);
		fip.publishPort();
	}

	public float readBatteryCharge() throws Exception
	{
		return this.batteryCharge;
	}

	public BatteryState getBatteryState() throws Exception
	{
		return this.stateBattery;
	}
	public void setBatteryState(BatteryState state) throws Exception
	{
		this.stateBattery = state;
	}


}
