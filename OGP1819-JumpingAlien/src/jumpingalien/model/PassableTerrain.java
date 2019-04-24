package jumpingalien.model;

public enum PassableTerrain {

    AIR(0), WATER(2), MAGMA(3), GAS(5);

    private int value;

    PassableTerrain(int i) {
	value = i;
    }

    public int getValue() {
	return value;
    }

    public static boolean contains(int i) {
	for (final PassableTerrain feature : PassableTerrain.values())
	    if (feature.getValue() == i)
		return true;
	return false;
    }

}
