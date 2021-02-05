package main.java.components.petrolGenerator.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * Component interface for the petrol generator
 * 
 * @author Bello Memmi
 *
 */
public interface PetrolGeneratorCI extends PetrolGeneratorImplementationI, RequiredCI, OfferedCI {

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#getMaxLevel()
	 */
	@Override
	public float getMaxLevel() throws Exception;

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#getPetrolLevel()
	 */
	@Override
	public float getPetrolLevel() throws Exception;

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#addPetrol(float)
	 */
	@Override
	public void addPetrol(float quantity) throws Exception;

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception;

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception;

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception;

	/**
	 * @see main.java.components.petrolGenerator.interfaces.PetrolGeneratorImplementationI#fillAll()
	 */
	@Override
	public void fillAll() throws Exception;

}
