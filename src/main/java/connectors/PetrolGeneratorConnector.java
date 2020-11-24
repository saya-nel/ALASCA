package main.java.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import main.java.interfaces.PetrolGeneratorCI;

/**
 * Connector for the PetrolGeneratorCI component interface
 * 
 * @author Bello Memmi
 *
 */
public class PetrolGeneratorConnector extends AbstractConnector implements PetrolGeneratorCI {

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#getMaxLevel()
	 */
	@Override
	public float getMaxLevel() throws Exception {
		return ((PetrolGeneratorCI) this.offering).getMaxLevel();
	}

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#getPetrolLevel()
	 */
	@Override
	public float getPetrolLevel() throws Exception {
		return ((PetrolGeneratorCI) this.offering).getPetrolLevel();
	}

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#addPetrol(float)
	 */
	@Override
	public void addPetrol(float quantity) throws Exception {
		((PetrolGeneratorCI) this.offering).addPetrol(quantity);
	}

	/**
	 * @see PetrolGeneratorCI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((PetrolGeneratorCI) this.offering).turnOn();
	}

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#turnOff()
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

}
