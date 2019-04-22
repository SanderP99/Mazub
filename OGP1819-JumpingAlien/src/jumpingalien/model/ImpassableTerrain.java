package jumpingalien.model;

public enum ImpassableTerrain {

    SOLID_GROUND(1), ICE(4);

    private int value;

    ImpassableTerrain(int i) {
	value = i;
    }

    public int getValue() {
	return value;
    }

}
