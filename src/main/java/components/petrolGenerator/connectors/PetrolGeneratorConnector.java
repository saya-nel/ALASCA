package main.java.components.petrolGenerator.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.components.petrolGenerator.interfaces.PetrolGeneratorCI;

/**
 * Connector for the PetrolGeneratorCI component interface
 * 
 * @author Bello Memmi
 *
 */
public class PetrolGeneratorConnector extends AbstractConnector implements PetrolGeneratorCI {

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#getMaxLevel()
	 */
	@Override
	public float getMaxLevel() throws Exception {
		return ((PetrolGeneratorCI) this.offering).getMaxLevel();
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#getPetrolLevel()
	 */
	@Override
	public float getPetrolLevel() throws Exception {
		return ((PetrolGeneratorCI) this.offering).getPetrolLevel();
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#addPetrol(float)
	 */
	@Override
	public void addPetrol(float quantity) throws Exception {
		((PetrolGeneratorCI) this.offering).addPetrol(quantity);
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((PetrolGeneratorCI) this.offering).turnOn();
	}

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		((PetrolGeneratorCI) this.offering).turnOff();
	}

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return ((PetrolGeneratorCI) this.offering).isTurnedOn();
	}

	@Override
	public void fillAll() throws Exception {
		((PetrolGeneratorCI) this.offering).fillAll();
	}

}
