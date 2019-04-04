package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.Sprite;

/**
 * @author Warre Dreesen
 * @author Sander Prenen
 *
 */
public class Plant extends GameObject {

    private int[] boundaries;

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

    private void setSecondsToLive(double secondsToLive) {
	this.secondsToLive = secondsToLive;
    }

    @Basic
    public double getSecondsToLive() {
	return secondsToLive;
    }

    private double secondsToLive;

    public int[] getBoundaries() {
	return boundaries;
    }

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
		    timeSinceDeath = 0.0;
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
	} else if (timeSinceDeath < 0.6) {
	    if (dt < 0.599 - timeSinceDeath)
		timeSinceDeath += dt;
	    else {
		timeSinceDeath += dt;
		getWorld().removeObject(this);
		terminate();
	    }

	} else {
	    getWorld().removeObject(this);
	    terminate();
	}

    }

    double timeSinceDeath;

}
