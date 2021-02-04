package main.java.interfaces;

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
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#getMaxLevel()
	 */
	@Override
	public float getMaxLevel() throws Exception;

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#getPetrolLevel()
	 */
	@Override
	public float getPetrolLevel() throws Exception;

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#addPetrol(float)
	 */
	@Override
	public void addPetrol(float quantity) throws Exception;

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception;

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception;

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception;

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#fillAll()
	 */
	@Override
	public void fillAll() throws Exception;

}
