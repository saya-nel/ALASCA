package main.java.components.petrolGenerator.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import main.java.components.petrolGenerator.interfaces.PetrolGeneratorCI;

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
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#getMaxLevel()
	 */
	@Override
	public float getMaxLevel() throws Exception {
		return ((PetrolGeneratorCI) this.getConnector()).getMaxLevel();
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#getPetrolLevel()
	 */
	@Override
	public float getPetrolLevel() throws Exception {
		return ((PetrolGeneratorCI) this.getConnector()).getPetrolLevel();
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#addPetrol(float)
	 */
	@Override
	public void addPetrol(float quantity) throws Exception {
		((PetrolGeneratorCI) this.getConnector()).addPetrol(quantity);
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((PetrolGeneratorCI) this.getConnector()).turnOn();
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((PetrolGeneratorCI) this.getConnector()).turnOff();
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#isTurnedOn()
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
