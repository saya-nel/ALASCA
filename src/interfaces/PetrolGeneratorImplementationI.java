package interfaces;

/**
 * 
 * methods of Petrol Generator
 * 
 * @author Bello Memmi
 *
 */
public interface PetrolGeneratorImplementationI {

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
	 * Turn the generator on
	 */
	public void turnOn() throws Exception;

	/**
	 * Turn the generator off
	 */
	public void turnOff() throws Exception;
}
