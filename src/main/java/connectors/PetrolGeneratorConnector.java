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
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#getMaxLevel()
	 */
	@Override
	public float getMaxLevel() throws Exception {
		return ((PetrolGeneratorCI) this.offering).getMaxLevel();
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#getPetrolLevel()
	 */
	@Override
	public float getPetrolLevel() throws Exception {
		return ((PetrolGeneratorCI) this.offering).getPetrolLevel();
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#addPetrol(float)
	 */
	@Override
	public void addPetrol(float quantity) throws Exception {
		((PetrolGeneratorCI) this.offering).addPetrol(quantity);
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		((PetrolGeneratorCI) this.offering).turnOn();
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#turnOff()
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
