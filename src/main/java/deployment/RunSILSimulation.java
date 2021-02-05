package main.java.deployment;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import main.java.components.battery.Battery;
import main.java.components.controller.Controller;
import main.java.components.electricMeter.ElectricMeter;
import main.java.components.fan.Fan;
import main.java.components.petrolGenerator.PetrolGenerator;
import main.java.components.solarPanels.SolarPanels;
import main.java.components.washer.Washer;

/**
 * 
 * @author Bello Memmi
 *
 */
public class RunSILSimulation extends AbstractCVM {
	/** acceleration factor for the real time execution. */
	public static final double ACC_FACTOR = 10.0;
	/**
	 * delay to start the real time simulations on every model at the same moment
	 * (the order is delivered to the models during this delay.
	 */
	public static final long DELAY_TO_START_SIMULATION = 1000L;
	/** duration of the simulation. */
	public static final double SIMULATION_DURATION = 100;
	/** URI of the simulation architecture used in the execution. */
	public static final String SIM_ARCHITECTURE_URI = "sil";
	/** URI of the inbound port of the fan component. */
	protected final static String FAN_INBOUND_PORT_URI = "fip-URI";
	/** URI of the inbound port of the solar panels component. */
	protected final static String SOLARPANELS_INBOUND_PORT_URI = "spip-URI";
	/** URI of the inbound port of the petrol generator component. */
	protected final static String PETROL_GENERATOR_INBOUND_PORT_URI = "pgip-URI";
	/** URI of the inbound port of the battery component. */
	protected final static String BATTERY_INBOUND_PORT_URI = "bip-URI";
	/** URI of the inbound port of the washer component. */
	protected final static String WASHER_INBOUND_PORT_URI = "wip-URI";
	/** URI of the inbound port of the controller component. */
	protected final static String CONTROLLER_INBOUND_PORT_URI = "cip-URI";

	public RunSILSimulation() throws Exception {
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {
		AbstractComponent.createComponent(Controller.class.getCanonicalName(),
				new Object[] { CONTROLLER_INBOUND_PORT_URI });
		AbstractComponent.createComponent(Fan.class.getCanonicalName(),
				new Object[] { FAN_INBOUND_PORT_URI, true, false });
		AbstractComponent.createComponent(SolarPanels.class.getCanonicalName(),
				new Object[] { SOLARPANELS_INBOUND_PORT_URI, true, false });
		AbstractComponent.createComponent(PetrolGenerator.class.getCanonicalName(),
				new Object[] { PETROL_GENERATOR_INBOUND_PORT_URI, true, false });
		AbstractComponent.createComponent(Battery.class.getCanonicalName(),
				new Object[] { "batterySerial", BATTERY_INBOUND_PORT_URI, CONTROLLER_INBOUND_PORT_URI, true, false });
		AbstractComponent.createComponent(Washer.class.getCanonicalName(),
				new Object[] { "washerSerial", WASHER_INBOUND_PORT_URI, CONTROLLER_INBOUND_PORT_URI, true, false });
		AbstractComponent.createComponent(ElectricMeter.class.getCanonicalName(), new Object[] { false });

		AbstractComponent.createComponent(HEMSimulationCoordinator.class.getCanonicalName(), new Object[] {});
		AbstractComponent.createComponent(HEMSimulationSupervisor.class.getCanonicalName(), new Object[] {});

		super.deploy();
	}

	public static void main(String[] args) {
		try {
			RunSILSimulation r = new RunSILSimulation();
			long d = TimeUnit.SECONDS.toMillis((long) (SIMULATION_DURATION / ACC_FACTOR));
			r.startStandardLifeCycle(d + 5000L);
			Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
// -----------------------------------------------------------------------------
