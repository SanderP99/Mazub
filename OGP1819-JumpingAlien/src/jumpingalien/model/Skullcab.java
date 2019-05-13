package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import jumpingalien.util.Sprite;

/**
 * A class that implements Skullcab
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 * @version 1
 *
 * @invar ... | this.getOrientation() == 1 || this.getOrientation() == -1
 * @invar ... | getHorizontalSpeed() == 0
 * @invar ... | getSecondsToLive() <= Constants.skullcabSecondsToLive
 *
 */
public class Skullcab extends Plant implements VerticalMovement {

    /**
     * Creates a new Skullcab
     * 
     * @param positionLeftX   The x position of the left most pixel of Skullcab
     * @param positionBottomY The y position of the bottom most pixel of Skullcab
     * @param pixelSizeX      The width in pixels of Skullcab
     * @param pixelSizeY      The height in pixels of Skullcab
     * @param sprites         The sprites of Skullcab
     * 
     * @post ... | new.getSecondsToLive() == Constants.skullcabSecondsToLive
     * @post ... | new.getBoundaries() == new int[] { positionBottomY), (int)
     *       (positionBottomY + 0.5 * (int) (100 *
     *       Math.abs(Constants.skullcabVerticalSpeed)))
     * @post ... | new.getOrientation() == 1
     * @post ... | new.getVerticalSpeedMeters() ==
     *       Math.abs(Constants.skullcabVerticalSpeed)
     * 
     * @effect super(positionLeftX, positionBottomY, pixelSizeX, pixelSizeY,
     *         Constants.skullcabVerticalSpeed, Constants.skullcabHitPoints,
     *         Constants.skullcabSecondsToLive, 0, 0, 0, 0, 0, sprites)
     */
    @Raw
    public Skullcab(int positionLeftX, int positionBottomY, int pixelSizeX, int pixelSizeY, Sprite[] sprites) {
	super(positionLeftX, positionBottomY, pixelSizeX, pixelSizeY, Constants.skullcabVerticalSpeed,
		Constants.skullcabHitPoints, Constants.skullcabSecondsToLive, 0, 0, 0, 0, 0, sprites);

	setVerticalSpeedMeters(Math.abs(Constants.skullcabVerticalSpeed));
	setOrientation(1);
	setSecondsToLive(Constants.skullcabSecondsToLive);
	boundaries = new int[] { getYPositionPixel(),
		(int) (getYPositionPixel() + 0.5 * (int) (100 * Math.abs(getVerticalSpeedMeters()))) };
    }

    /**
     * A list to store the boundaries in which the plant will move
     */
    final int[] boundaries;

    /**
     * A variable to store the time since the last contact with Mazub
     */
    private double timeSinceContactWithMazub = 0.7;

    /**
     * Returns the time since the last contact with Mazub
     */
    @Basic
    public double getTimeSinceContactWithMazub() {
	return timeSinceContactWithMazub;
    }

    /**
     * Sets the time since the last contact with Mazub to the given time
     * 
     * @param time The time to set
     */
    protected void setTimeSinceContactWithMazub(double time) {
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
			    updatePosition(timeStep, actualPosY);

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
			updatePosition(timeStep, actualPosY);

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

    private void updatePosition(double timeStep, final double actualPosY) {
	setYPositionActual(actualPosY);
	checkCollision(timeStep);
	setTimeSinceContactWithMazub(getTimeSinceContactWithMazub() + timeStep);
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
    @Immutable
    public int[] getBoundaries() {
	return boundaries;
    }

}
