package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.Sprite;

/**
 * @author Warre Dreesen
 * @author Sander Prenen
 *
 */
public class Plant extends GameObject {

    /**
     * A list to store the boundaries in which the plant will move
     */
    private int[] boundaries;

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
     * @post ... | new.getSecondsToLive() == secondsToLive
     * @post ... | new.getBoundaries == int[] { (int) (getXPositionPixel() - 0.5 *
     *       Math.abs(getHorizontalSpeedPixels())), getXPositionPixel() }
     * @post ... | new.getOrientation() == -1
     */
    public Plant(int positionLeftX, int positionBottomY, int pixelSizeX, int pixelSizeY, double horizontalSpeed,
	    int hitpoints, double secondsToLive, double maxHorizontalSpeedRunning, double maxHorizontalSpeedDucking,
	    double minHorizontalSpeed, double horizontalAcceleration, double verticalAcceleration, Sprite... sprites) {

	super(positionLeftX, positionBottomY, pixelSizeX, pixelSizeY, hitpoints, 1, maxHorizontalSpeedRunning,
		maxHorizontalSpeedDucking, minHorizontalSpeed, 0, horizontalAcceleration, verticalAcceleration, false,
		sprites);

	setHorizontalSpeedMeters(-1 * Math.abs(horizontalSpeed));
	setOrientation(-1);
	setSecondsToLive(secondsToLive);
	setBoundaries();
    }

    /**
     * Sets how long the plant will live
     * 
     * @param secondsToLive The seconds to live
     * 
     * @post ... | new.secondsToLive = secondsToLive
     */
    private void setSecondsToLive(double secondsToLive) {
	this.secondsToLive = secondsToLive;
    }

    /**
     * Returns how many seconds the plant has left to live.
     */
    @Basic
    public double getSecondsToLive() {
	return secondsToLive;
    }

    /**
     * A timer to keep track how long the plant has left to live.
     */
    private double secondsToLive;

    /**
     * Returns the boundaries in which the plant will move.
     */
    public int[] getBoundaries() {
	return boundaries;
    }

    /**
     * Sets the boundaries in which the plant will move
     * 
     * @post ... | new.boundaries == int[] { (int) (getXPositionPixel() - 0.5 *
     *       Math.abs(getHorizontalSpeedPixels())), getXPositionPixel() }
     */
    public void setBoundaries() {
	boundaries = new int[] { (int) (getXPositionPixel() - 0.5 * Math.abs(getHorizontalSpeedPixels())),
		getXPositionPixel() };
    }

    /**
     * Returns whether an array of sprites has valid dimensions
     * 
     * @param sprites The array to check
     */
    public boolean isValidSpriteArray(Sprite... sprites) {
	return sprites.length == 2;
    }

    @Override
    public void advanceTime(double dt, double timeStep) {
	if (!isDead()) {
	    while (dt >= timeStep && !isDead())
		if (getSecondsToLive() >= timeStep) {
		    if (getOrientation() == -1) {
			if (getXPositionActual()
				- Math.abs(getHorizontalSpeedMeters()) * timeStep < (double) getBoundaries()[0] / 100) {
			    final double newPosX = getXPositionActual()
				    - Math.abs(getHorizontalSpeedMeters()) * timeStep;
			    final double actualPosX = (double) getBoundaries()[0] / 100
				    + Math.abs(newPosX - (double) getBoundaries()[0] / 100);
			    setXPositionActual(actualPosX);

			    setOrientation(1);
			    setSprite(getSpriteArray()[1]);
			    dt -= timeStep;
			    setSecondsToLive(getSecondsToLive() - timeStep);
			} else {
			    dt -= timeStep;
			    setSecondsToLive(getSecondsToLive() - timeStep);
			    setXPositionActual(getXPositionActual() - Math.abs(getHorizontalSpeedMeters()) * timeStep);
			}
		    } else if (getXPositionActual()
			    + Math.abs(getHorizontalSpeedMeters()) * timeStep > (double) getBoundaries()[1] / 100) {
			final double newPosX = getXPositionActual() + Math.abs(getHorizontalSpeedMeters()) * timeStep;
			final double actualPosX = (double) getBoundaries()[1] / 100
				- Math.abs(newPosX - (double) getBoundaries()[1] / 100);
			setXPositionActual(actualPosX);

			setOrientation(-1);
			setSprite(getSpriteArray()[0]);
			dt -= timeStep;
			setSecondsToLive(getSecondsToLive() - timeStep);

		    } else {
			dt -= timeStep;
			setSecondsToLive(getSecondsToLive() - timeStep);
			if (getOrientation() == 1)
			    setXPositionActual(getXPositionActual() + Math.abs(getHorizontalSpeedMeters()) * timeStep);
			else
			    setXPositionActual(getXPositionActual() - Math.abs(getHorizontalSpeedMeters()) * timeStep);
		    }

		} else {
		    dt = 0;
		    setXPositionActual(getXPositionActual() + getHorizontalSpeedMeters() * getSecondsToLive());
		    setSecondsToLive(0);
		    isDead = true;
		    setTimeSinceDeath(0.0);
		}
	    if (getOrientation() == 1)
		setXPositionActual(getXPositionActual() + Math.abs(getHorizontalSpeedMeters()) * dt);
	    else
		setXPositionActual(getXPositionActual() - Math.abs(getHorizontalSpeedMeters()) * dt);

	    setSecondsToLive(getSecondsToLive() - dt);

	    if (getWorld() != null)
		for (final Object object : getWorld().getAllObjects())
		    if (collidesWith((GameObject) object) && object instanceof Mazub
			    && ((GameObject) object).getHitpoints() != ((GameObject) object).getMaxHitpoints()) {
			terminate();
			((GameObject) object).changeHitPoints(50);
		    }
	} else if (getTimeSinceDeath() < 0.6) {
	    if (dt < 0.599 - getTimeSinceDeath())
		setTimeSinceDeath(dt + getTimeSinceDeath());
	    else {
		setTimeSinceDeath(dt + getTimeSinceDeath());
		getWorld().removeObject(this);
		terminate();
	    }

	} else {
	    getWorld().removeObject(this);
	    terminate();
	}

    }

    /**
     * A timer to keep track of how long ago the plant died.
     */
    double timeSinceDeath;

    /**
     * Sets the time since death to the given time
     * 
     * @param time The time to set
     * 
     * @post ... | new.timeSinceDeath == time
     */
    private void setTimeSinceDeath(double time) {
	timeSinceDeath = time;
    }

    /**
     * Returns the time since death
     */
    @Basic
    public double getTimeSinceDeath() {
	return timeSinceDeath;
    }

}
