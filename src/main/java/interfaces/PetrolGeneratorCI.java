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
	 * @see interfaces.PetrolGeneratorImplementationI#getMaxLevel()
	 */
	@Override
	public float getMaxLevel() throws Exception;

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#getPetrolLevel()
	 */
	@Override
	public float getPetrolLevel() throws Exception;

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#addPetrol(float)
	 */
	@Override
	public void addPetrol(float quantity) throws Exception;

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception;

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception;

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception;

}
