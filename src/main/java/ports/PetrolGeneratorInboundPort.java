package main.java.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import main.java.components.PetrolGenerator;
import main.java.interfaces.PetrolGeneratorCI;

/**
 * 
 * PetrolGenerator inbound port for the petrol generator component interface
 * 
 * @author Bello Memmi
 *
 */
public class PetrolGeneratorInboundPort extends AbstractInboundPort implements PetrolGeneratorCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Constuctor of the PetrolGeneratorInboundPort
	 * 
	 * @param uri   reflexion uri of the port
	 * @param owner owner component
	 * @throws Exception
	 */
	public PetrolGeneratorInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, PetrolGeneratorCI.class, owner);
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#getMaxLevel()
	 */
	@Override
	public float getMaxLevel() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((PetrolGenerator) owner).getMaxLevel());
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#getPetrolLevel()
	 */
	@Override
	public float getPetrolLevel() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((PetrolGenerator) owner).getPetrolLevel());
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#addPetrol(float)
	 */
	@Override
	public void addPetrol(float quantity) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((PetrolGenerator) owner).addPetrol(quantity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((PetrolGenerator) owner).turnOn();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((PetrolGenerator) owner).turnOff();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see main.java.interfaces.PetrolGeneratorImplementationI#isTurnedOn()
	 */
	@Override
	public boolean isTurnedOn() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((PetrolGenerator) owner).isTurnedOn());
	}
}
