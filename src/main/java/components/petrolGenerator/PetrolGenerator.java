package main.java.components.petrolGenerator;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import main.java.interfaces.PetrolGeneratorCI;
import main.java.interfaces.PetrolGeneratorImplementationI;
import main.java.ports.PetrolGeneratorInboundPort;

/**
 * Class representing the petrol generator component
 * 
 * @author Bello Memmi
 *
 */
@OfferedInterfaces(offered = { PetrolGeneratorCI.class })
public class PetrolGenerator extends AbstractComponent implements PetrolGeneratorImplementationI {

	/**
	 * Component URI
	 */
	protected String myUri;

	/**
	 * Maximum petrol level
	 */
	protected float maximumPetrolLevel;

	/**
	 * generator petrol level
	 */
	protected float petrolLevel;

	/**
	 * True if the generator is running
	 */
	protected boolean isTurnedOn;

	/**
	 * Inbound port of the petrol generator
	 */
	protected PetrolGeneratorInboundPort pgip;

	/**
	 * Constructor of petrol generator
	 * 
	 * @param reflectionPortURI URI of the component
	 * @param bipURI            URI of the petrol generator inbound port
	 * @throws Exception
	 */
	protected PetrolGenerator(String reflectionPortURI, String pgipURI, float maxLevel) throws Exception {
		super(1, 0);
		myUri = reflectionPortURI;
		this.initialise(pgipURI, maxLevel);
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * Initialize the petrol generator component
	 * 
	 * @param batteryInboundPortURI
	 * @throws Exception
	 */
	protected void initialise(String pgipURI, float maximumLevel) throws Exception {
		this.isTurnedOn = false;
		this.petrolLevel = 0;
		this.maximumPetrolLevel = maximumLevel;
		this.pgip = new PetrolGeneratorInboundPort(pgipURI, this);
		this.pgip.publishPort();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.pgip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#getMaxLevel()
	 */
	@Override
	public float getMaxLevel() throws Exception {
		return maximumPetrolLevel;
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#getPetrolLevel()
	 */
	@Override
	public float getPetrolLevel() throws Exception {
		return petrolLevel;
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#addPetrol(float)
	 */
	@Override
	public void addPetrol(float quantity) throws Exception {
		if (petrolLevel + quantity <= maximumPetrolLevel)
			petrolLevel = petrolLevel + quantity;
		else
			petrolLevel = maximumPetrolLevel;
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		isTurnedOn = true;
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		isTurnedOn = false;
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return isTurnedOn;
	}

	/**
	 *
	 */
	public void fillAll() throws Exception {
		this.petrolLevel=this.maximumPetrolLevel;
	}

	public void emptyGenerator() throws Exception {
		this.petrolLevel = 0;
	}
}
