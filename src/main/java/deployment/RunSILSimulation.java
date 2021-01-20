package main.java.deployment;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import main.java.components.electricMeter.ElectricMeter;
import main.java.components.fan.Fan;

/**
 * 
 * @author Bello Memmi
 *
 */
public class RunSILSimulation extends AbstractCVM {
	/** acceleration factor for the real time execution. */
	public static final double ACC_FACTOR = 2.0;
	/**
	 * delay to start the real time simulations on every model at the same moment
	 * (the order is delivered to the models during this delay.
	 */
	public static final long DELAY_TO_START_SIMULATION = 1000L;
	/** duration of the simulation. */
	public static final double SIMULATION_DURATION = 10.5;
	/** URI of the simulation architecture used in the execution. */
	public static final String SIM_ARCHITECTURE_URI = "sil";
	/** URI of the inbound port of the fan component. */
	protected final static String FAN_INBOUND_PORT_URI = "fip-URI";

	public RunSILSimulation() throws Exception {
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {
		AbstractComponent.createComponent(Fan.class.getCanonicalName(),
				new Object[] { FAN_INBOUND_PORT_URI, true, false });
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
