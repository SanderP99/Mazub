package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import be.kuleuven.cs.som.annotate.Value;

/**
 * An enumeration introducing different types of impassable terrain.
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 * @version 1
 *
 */
@Value
public enum ImpassableTerrain {

    SOLID_GROUND(1), ICE(4);

    /**
     * Variable storing the integer for this feature
     */
    private final int value;

    /**
     * Initialize the feature with the given integer
     * 
     * @param i The integer for the new feature
     * 
     * @post ... | new.getValue() == i
     */
    @Raw
    ImpassableTerrain(int i) {
	value = i;
    }

    /**
     * Return the value for this feature
     */
    @Basic
    @Raw
    @Immutable
    public int getValue() {
	return value;
    }

    /**
     * Checks whether the given geological feature is a impassable feature
     * 
     * @param geologicalFeature The feature to check
     */
    public static boolean contains(int geologicalFeature) {
	for (final ImpassableTerrain feature : ImpassableTerrain.values())
	    if (feature.getValue() == geologicalFeature)
		return true;
	return false;
    }
}
