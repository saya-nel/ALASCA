package utils;

/**
 * 
 * Represents the fridge modes
 * 
 * @author Bello Memmi
 *
 */
public enum FridgeMode{
	NORMAL(0), ECO(1);
	
	private int value;

    private FridgeMode(int value) {
		this.value = value;
	}

    public int getValue() {
        return value;
    }
}