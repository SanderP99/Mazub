package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.Sprite;

/**
 * A class that implements a plant
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 * @version 1
 *
 */
public abstract class Plant extends GameObject {

    /**
     * Create a plant
     * 
     * @param positionLeftX             The left-most pixel of the plant
     * @param positionBottomY           The bottom-most pixel of the plant
     * @param pixelSizeX                The width of the plant in pixels
     * @param pixelSizeY                The height of the plant in pixels
     * @param horizontalSpeed           The horizontal speed of the plant in meters
     *                                  per second
     * @param hitpoints                 The hitpoints of the plant
     * @param secondsToLive             The seconds the plant will live
     * @param maxHorizontalSpeedRunning The maximum horizontal speed of the plant
     *                                  while running
     * @param maxHorizontalSpeedDucking The maximum horizontal speed of the plant
     *                                  while ducking
     * @param minHorizontalSpeed        The minimum horizontal speed of the plant
     * @param horizontalAcceleration    The horizontal acceleration of the plant
     * @param verticalAcceleration      The vertical acceleration of the plant
     * @param sprites                   The sprites of the plant
     * 
     * @effect super(positionLeftX, positionBottomY, pixelSizeX, pixelSizeY,
     *         hitpoints, 1, maxHorizontalSpeedRunning, maxHorizontalSpeedDucking,
     *         minHorizontalSpeed, 0, horizontalAcceleration, verticalAcceleration,
     *         false, sprites)
     * @effect setHorizontalSpeedMeters(-1 * Math.abs(horizontalSpeed))
     * @effect setOrientation(-1);
     * @effect setBoundaries();
     * @effect setSecondsToLive(secondsToLive);
     * 
     * @pre ... | sprites.length == 2
     * 
     * @post ... | new.getSecondsToLive() == secondsToLive
     * @post ... | new.getBoundaries == int[] { (int) (getXPositionPixel() - 0.5 *
     *       Math.abs(getHorizontalSpeedPixels())), getXPositionPixel() }
     * @post ... | new.getOrientation() == -1
     */
    public Plant(int positionLeftX, int positionBottomY, int pixelSizeX, int pixelSizeY, double horizontalSpeed,
	    int hitpoints, double secondsToLive, double maxHorizontalSpeedRunning, double maxHorizontalSpeedDucking,
	    double minHorizontalSpeed, double horizontalAcceleration, double verticalAcceleration, Sprite... sprites) {

	super(positionLeftX, positionBottomY, pixelSizeX, pixelSizeY, hitpoints, hitpoints, maxHorizontalSpeedRunning,
		maxHorizontalSpeedDucking, minHorizontalSpeed, 0.5, horizontalAcceleration, verticalAcceleration, false,
		sprites);

	if (!isValidSpriteArray(sprites))
	    throw new RuntimeException();
    }

    /**
     * A timer to keep track how long the plant has left to live.
     */
    private double secondsToLive;

    /**
     * Returns how many seconds the plant has left to live.
     */
    @Basic
    public double getSecondsToLive() {
	return secondsToLive;
    }

    /**
     * Sets how long the plant will live
     * 
     * @param secondsToLive The seconds to live
     * 
     * @post ... | new.secondsToLive = secondsToLive
     */
    protected void setSecondsToLive(double secondsToLive) {
	this.secondsToLive = secondsToLive;
    }

    /**
     * Returns the boundaries in which the plant will move.
     */
    public abstract int[] getBoundaries();

    /**
     * @post sprites.length == 2
     */
    @Override
    public boolean isValidSpriteArray(Sprite[] sprites) {
	for (final Sprite sprite : sprites)
	    if (!sprite.canHaveAsHeight(sprite.getHeight()) || !sprite.canHaveAsName(sprite.getName())
		    || !sprite.canHaveAsWidth(sprite.getWidth()))
		return false;
	return sprites.length == 2;
    }

    /**
     * A timer to keep track of how long ago the plant died.
     */
    double timeSinceDeath;

    /**
     * Returns the time since death
     */
    @Basic
    public double getTimeSinceDeath() {
	return timeSinceDeath;
    }

    /**
     * Sets the time since death to the given time
     * 
     * @param time The time to set
     * 
     * @post ... | new.timeSinceDeath == time
     */
    protected void setTimeSinceDeath(double time) {
	timeSinceDeath = time;
    }

    @Override
    public abstract void advanceTime(double dt, double timeStep);

}
