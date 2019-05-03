package jumpingalien.model;

import jumpingalien.util.Sprite;

public class Sneezewort extends Plant {

    public Sneezewort(int positionLeftX, int positionBottomY, int pixelSizeX, int pixelSizeY, double horizontalSpeed,
	    int hitpoints, double secondsToLive, double maxHorizontalSpeedRunning, double maxHorizontalSpeedDucking,
	    double minHorizontalSpeed, double horizontalAcceleration, double verticalAcceleration, Sprite[] sprites) {
	super(positionLeftX, positionBottomY, pixelSizeX, pixelSizeY, horizontalSpeed, hitpoints, secondsToLive,
		maxHorizontalSpeedRunning, maxHorizontalSpeedDucking, minHorizontalSpeed, horizontalAcceleration,
		verticalAcceleration, sprites);

	setHorizontalSpeedMeters(-1 * Math.abs(horizontalSpeed));
	setOrientation(-1);
	setSecondsToLive(secondsToLive);
	setBoundaries();
    }

    /**
     * Sets the boundaries in which the plant will move
     * 
     * @post ... | new.boundaries == int[] { (int) (getXPositionPixel() - 0.5 *
     *       Math.abs(getHorizontalSpeedPixels())), getXPositionPixel() }
     */
    @Override
    protected void setBoundaries() {
	boundaries = new int[] { (int) (getXPositionPixel() - 0.5 * Math.abs(getHorizontalSpeedPixels())),
		getXPositionPixel() };
    }

    /**
     * A list to store the boundaries in which the plant will move
     */
    int[] boundaries;

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
     * Returns the boundaries in which the plant will move.
     */
    @Override
    public int[] getBoundaries() {
	return boundaries;
    }
}
