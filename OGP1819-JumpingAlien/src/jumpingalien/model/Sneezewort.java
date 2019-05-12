package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import jumpingalien.util.Sprite;

/**
 * A class that implements Sneezewort
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 * @version 1
 * 
 * @invar The sprites are valid sprites | spriteArray.lenght == 2 &&
 *        isValidSpriteArray()
 * @invar ... | this.getVerticalSpeed() == 0
 */
public class Sneezewort extends Plant implements HorizontalMovement {

    /**
     * Creates a new Sneezewort
     * 
     * @param positionLeftX   The x position of the left most pixel of Sneezewort
     * @param positionBottomY The y position of the bottom most pixel of Sneezewort
     * @param pixelSizeX      The width in pixels of Sneezewort
     * @param pixelSizeY      The height in pixels of Sneezewort
     * @param sprites         The sprites of Sneezewort
     * 
     * @post ... | new.getSecondsToLive() == Constants.sneezewortSecondsToLive
     * @post ... | new.getBoundaries() == new int[] { positionLeftX), (int)
     *       (positionLeftX + 0.5 * (int) (100 *
     *       Math.abs(Constants.sneezewortHorizontalSpeed)))
     * @post ... | new.getOrientation() == -1
     * @post ... | new.getVerticalSpeedMeters() ==
     *       Math.abs(Constants.sneezewortHorizontalSpeed)
     * 
     * @effect super(positionLeftX, positionBottomY, pixelSizeX, pixelSizeY,
     *         Constants.sneezewortHorizontalSpeed, Constants.sneezewortHitPoints,
     *         Constants.sneezewortSecondsToLive, 0.5, 0, 0, 0, 0, sprites)
     */
    @Raw
    public Sneezewort(int positionLeftX, int positionBottomY, int pixelSizeX, int pixelSizeY, Sprite[] sprites) {
	super(positionLeftX, positionBottomY, pixelSizeX, pixelSizeY, Constants.sneezewortHorizontalSpeed,
		Constants.sneezewortHitPoints, Constants.sneezewortSecondsToLive, 0.5, 0, 0, 0, 0, sprites);

	setHorizontalSpeedMeters(-1 * Math.abs(Constants.sneezewortHorizontalSpeed));
	setOrientation(-1);
	setSecondsToLive(Constants.sneezewortSecondsToLive);
	boundaries = new int[] { (int) (getXPositionPixel() - 0.5 * Math.abs(getHorizontalSpeedPixels())),
		getXPositionPixel() };
    }

    /**
     * A list to store the boundaries in which the plant will move
     */
    final int[] boundaries;

    /**
     * Returns the boundaries in which the plant will move.
     */
    @Override
    @Immutable
    public int[] getBoundaries() {
	return boundaries;
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
}
