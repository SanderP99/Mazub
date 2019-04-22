package jumpingalien.model;

import jumpingalien.util.Sprite;

public class Skullcab extends Plant {

    public Skullcab(int positionLeftX, int positionBottomY, int pixelSizeX, int pixelSizeY, double verticalSpeed,
	    int hitpoints, double secondsToLive, double maxHorizontalSpeedRunning, double maxHorizontalSpeedDucking,
	    double minHorizontalSpeed, double horizontalAcceleration, double verticalAcceleration, Sprite[] sprites) {
	super(positionLeftX, positionBottomY, pixelSizeX, pixelSizeY, verticalSpeed, hitpoints, secondsToLive,
		maxHorizontalSpeedRunning, maxHorizontalSpeedDucking, minHorizontalSpeed, horizontalAcceleration,
		verticalAcceleration, sprites);

	setVerticalSpeedMeters(Math.abs(verticalSpeed));
	setOrientation(1);
	setSecondsToLive(secondsToLive);
	setBoundaries();
    }

    @Override
    public void setBoundaries() {
	boundaries = new int[] { getYPositionPixel(),
		(int) (getYPositionPixel() + 0.5 * (int) (100 * Math.abs(getVerticalSpeedMeters()))) };

    }

    int[] boundaries;

    @Override
    public void advanceTime(double dt, double timeStep) {
	if (!isDead()) {
	    while (dt >= timeStep && !isDead())
		if (getSecondsToLive() >= timeStep) {
		    if (getOrientation() == -1) {
			if (getYPositionActual()
				- Math.abs(getVerticalSpeedMeters()) * timeStep < (double) getBoundaries()[0] / 100) {
			    final double newPosY = getYPositionActual() - Math.abs(getVerticalSpeedMeters()) * timeStep;
			    final double actualPosY = (double) getBoundaries()[0] / 100
				    + Math.abs(newPosY - (double) getBoundaries()[0] / 100);
			    setYPositionActual(actualPosY);

			    setOrientation(1);
			    setSprite(getSpriteArray()[0]);
			    dt -= timeStep;
			    setSecondsToLive(getSecondsToLive() - timeStep);
			} else {
			    dt -= timeStep;
			    setSecondsToLive(getSecondsToLive() - timeStep);
			    setYPositionActual(getYPositionActual() - Math.abs(getVerticalSpeedMeters()) * timeStep);
			}
		    } else if (getYPositionActual()
			    + Math.abs(getVerticalSpeedMeters()) * timeStep > (double) getBoundaries()[1] / 100) {
			final double newPosY = getYPositionActual() + Math.abs(getVerticalSpeedMeters()) * timeStep;
			final double actualPosY = (double) getBoundaries()[1] / 100
				- Math.abs(newPosY - (double) getBoundaries()[1] / 100);
			setYPositionActual(actualPosY);

			setOrientation(-1);
			setSprite(getSpriteArray()[1]);
			dt -= timeStep;
			setSecondsToLive(getSecondsToLive() - timeStep);

		    } else {
			dt -= timeStep;
			setSecondsToLive(getSecondsToLive() - timeStep);
			if (getOrientation() == 1)
			    setYPositionActual(getYPositionActual() + Math.abs(getVerticalSpeedMeters()) * timeStep);
			else
			    setYPositionActual(getYPositionActual() - Math.abs(getVerticalSpeedMeters()) * timeStep);
		    }

		} else {
		    dt = 0;
		    setYPositionActual(getYPositionActual() + getVerticalSpeedMeters() * getSecondsToLive());
		    setSecondsToLive(0);
		    isDead = true;
		    setTimeSinceDeath(0.0);
		}
	    if (getOrientation() == 1)
		setYPositionActual(getYPositionActual() + Math.abs(getVerticalSpeedMeters()) * dt);
	    else
		setYPositionActual(getYPositionActual() - Math.abs(getVerticalSpeedMeters()) * dt);

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
