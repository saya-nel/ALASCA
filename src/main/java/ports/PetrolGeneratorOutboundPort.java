package main.java.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.interfaces.PetrolGeneratorCI;

/**
 * Outbound port for PetrolGenerator component interface
 * 
 * @author Bello Memmi
 *
 */
public class PetrolGeneratorOutboundPort extends AbstractOutboundPort implements PetrolGeneratorCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of FanOutboundPort
	 * 
	 * @param owner owner component
	 * @throws Exception
	 */
	public PetrolGeneratorOutboundPort(ComponentI owner) throws Exception {
		super(PetrolGeneratorCI.class, owner);
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#getMaxLevel()
	 */
	@Override
	public float getMaxLevel() throws Exception {
		return ((PetrolGeneratorCI) this.getConnector()).getMaxLevel();
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#getPetrolLevel()
	 */
	@Override
	public float getPetrolLevel() throws Exception {
		return ((PetrolGeneratorCI) this.getConnector()).getPetrolLevel();
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#addPetrol(float)
	 */
	@Override
	public void addPetrol(float quantity) throws Exception {
		((PetrolGeneratorCI) this.getConnector()).addPetrol(quantity);
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((PetrolGeneratorCI) this.getConnector()).turnOn();
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((PetrolGeneratorCI) this.getConnector()).turnOff();
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return ((PetrolGeneratorCI) this.getConnector()).isTurnedOn();
	}

	@Override
	public void fillAll() throws Exception {
		((PetrolGeneratorCI) this.getConnector()).fillAll();
	}
}
