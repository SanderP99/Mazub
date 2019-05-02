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

    /**
     * A list to store the boundaries in which the plant will move
     */
    int[] boundaries;

    private double timeSinceContactWithMazub = 0.6;

    public double getTimeSinceContactWithMazub() {
	return timeSinceContactWithMazub;
    }

    void setTimeSinceContactWithMazub(double time) {
	timeSinceContactWithMazub = time;
    }

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
			    checkCollision(timeStep);
			    setTimeSinceContactWithMazub(getTimeSinceContactWithMazub() + timeStep);

			    setOrientation(1);
			    setSprite(getSpriteArray()[0]);
			    dt -= timeStep;
			    setSecondsToLive(getSecondsToLive() - timeStep);
			} else {
			    dt -= timeStep;
			    setSecondsToLive(getSecondsToLive() - timeStep);
			    setYPositionActual(getYPositionActual() - Math.abs(getVerticalSpeedMeters()) * timeStep);
			    checkCollision(timeStep);
			    setTimeSinceContactWithMazub(getTimeSinceContactWithMazub() + timeStep);

			}
		    } else if (getYPositionActual()
			    + Math.abs(getVerticalSpeedMeters()) * timeStep > (double) getBoundaries()[1] / 100) {
			final double newPosY = getYPositionActual() + Math.abs(getVerticalSpeedMeters()) * timeStep;
			final double actualPosY = (double) getBoundaries()[1] / 100
				- Math.abs(newPosY - (double) getBoundaries()[1] / 100);
			setYPositionActual(actualPosY);
			checkCollision(timeStep);
			setTimeSinceContactWithMazub(getTimeSinceContactWithMazub() + timeStep);

			setOrientation(-1);
			setSprite(getSpriteArray()[1]);
			dt -= timeStep;
			setSecondsToLive(getSecondsToLive() - timeStep);

		    } else {
			dt -= timeStep;
			setSecondsToLive(getSecondsToLive() - timeStep);
			if (getOrientation() == 1) {
			    setYPositionActual(getYPositionActual() + Math.abs(getVerticalSpeedMeters()) * timeStep);
			    checkCollision(timeStep);
			    setTimeSinceContactWithMazub(getTimeSinceContactWithMazub() + timeStep);

			} else {
			    setYPositionActual(getYPositionActual() - Math.abs(getVerticalSpeedMeters()) * timeStep);
			    checkCollision(timeStep);
			    setTimeSinceContactWithMazub(getTimeSinceContactWithMazub() + timeStep);

			}
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

//	    if (getTimeSinceContactWithMazub() >= 0.6) {
//		changeHitPoints(-1);
//		getWorld().getPlayer().changeHitPoints(50);
//		setTimeSinceContactWithMazub(getTimeSinceContactWithMazub() - 0.6);
//	    }
	    if (getWorld() != null && getWorld().getPlayer() != null)
		if (collidesWith(getWorld().getPlayer())
			&& getWorld().getPlayer().getHitpoints() != getWorld().getPlayer().getMaxHitpoints()
			&& getTimeSinceContactWithMazub() >= 0.6) {
		    changeHitPoints(-1);
		    getWorld().getPlayer().changeHitPoints(50);
		    setTimeSinceContactWithMazub(0.0);
		}
//		else
//		    setTimeSinceContactWithMazub(getTimeSinceContactWithMazub() + dt);

	} else if (getTimeSinceDeath() < 0.6) {
	    if (dt < 0.599 - getTimeSinceDeath())
		setTimeSinceDeath(dt + getTimeSinceDeath());
	    else {
		setTimeSinceDeath(dt + getTimeSinceDeath());
		getWorld().removeObject(this);
		terminate();
	    }

	} else if (dt < 0.599 - getTimeSinceDeath())
	    setTimeSinceDeath(dt + getTimeSinceDeath());
	else {
	    setTimeSinceDeath(dt + getTimeSinceDeath());
	    if (getWorld() != null)
		getWorld().removeObject(this);
	    terminate();
	}

    }

    private void checkCollision(double timeStep) {
	if (getWorld() != null && getWorld().getPlayer() != null)
	    if (collidesWith(getWorld().getPlayer()))
		if (getTimeSinceContactWithMazub() == 0)
		    setTimeSinceContactWithMazub(timeStep);
		else if (getTimeSinceContactWithMazub() >= 0.6) {
		    setTimeSinceContactWithMazub(getTimeSinceContactWithMazub() - 0.6);
		    changeHitPoints(-1);
		    getWorld().getPlayer().changeHitPoints(50);
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
