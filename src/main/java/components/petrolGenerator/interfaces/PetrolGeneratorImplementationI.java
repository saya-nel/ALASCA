package main.java.components.petrolGenerator.interfaces;

import main.java.components.petrolGenerator.PetrolGenerator;

/**
 * 
 * The interface <code>PetrolGeneratorImplementationI</code> defines the service
 * that must be implemented by the {@link PetrolGenerator} component.
 * 
 * @author Bello Memmi
 *
 */
public interface PetrolGeneratorImplementationI {

	// -------------------------------------------------------------------------
	// Component services signatures
	// -------------------------------------------------------------------------

	/**
	 * Return the maximum level of petrol of the generator
	 * 
	 * @return the maximum level of petrol of the generator
	 */
	public float getMaxLevel() throws Exception;

	/**
	 * Return the current petrol level of the generator
	 * 
	 * @return the petrol level of the generator
	 */
	public float getPetrolLevel() throws Exception;

	/**
	 * Add petrol to the generator
	 * 
	 * @param quantity quantity to add
	 */
	public void addPetrol(float quantity) throws Exception;

	/**
	 * Fill all the petrol generator
	 * 
	 * @throws Exception
	 */
	public void fillAll() throws Exception;

	/**
	 * Turn the generator on
	 */
	public void turnOn() throws Exception;

	/**
	 * Turn the generator off
	 */
	public void turnOff() throws Exception;

	/**
	 * Return true if the generator is turned on, false else
	 * 
	 * @return true if the generator is turned on, false else
	 * @throws Exception
	 */
	public boolean isTurnedOn() throws Exception;
}
