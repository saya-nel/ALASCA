package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.PetrolGeneratorCI;

/**
 * 
 * PetrolGenerator inbound port
 * 
 * @author Bello Memmi
 *
 */
public class PetrolGeneratorInboundPort extends AbstractInboundPort implements PetrolGeneratorCI {

	private static final long serialVersionUID = 1L;

	public PetrolGeneratorInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, PetrolGeneratorCI.class, owner);
	}

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#getMaxLevel()
	 */
	@Override
	public float getMaxLevel() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((PetrolGeneratorCI) owner).getMaxLevel());
	}

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#getPetrolLevel()
	 */
	@Override
	public float getPetrolLevel() throws Exception {
		return this.getOwner().handleRequestSync(owner -> ((PetrolGeneratorCI) owner).getPetrolLevel());
	}

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#addPetrol(float)
	 */
	@Override
	public void addPetrol(float quantity) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((PetrolGeneratorCI) owner).addPetrol(quantity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#turnOn()
	 */
	@Override
	public void turnOn() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((PetrolGeneratorCI) owner).turnOn();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see interfaces.PetrolGeneratorImplementationI#turnOff()
	 */
	@Override
	public void turnOff() throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((PetrolGeneratorCI) owner).turnOff();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
