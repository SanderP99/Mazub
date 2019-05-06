package jumpingalien.model;

import java.util.List;

import jumpingalien.util.Sprite;

/**
 * A class that implements sharks
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 * @version 1
 * 
 * @invar The sprites are valid sprites | spriteArray.lenght == 3 &&
 *        isValidSpriteArray()
 *
 */
public class Shark extends GameObject implements HorizontalMovement, VerticalMovement {

    private double timeSinceDeath;
    private double timeToRest;
    private double timeToMove;

    public Shark(int pixelLeftX, int pixelBottomY, int pixelSizeX, int pixelSizeY, int hitpoints, int maxHitpoints,
	    double maxHorizontalSpeedRunning, double maxHorizontalSpeedDucking, double minHorizontalSpeed,
	    double maxVerticalSpeed, double horizontalAcceleration, double verticalAcceleration, double horizontalSpeed,
	    double verticalSpeed, boolean tempObject, Sprite[] sprites) {
	super(pixelLeftX, pixelBottomY, pixelSizeX, pixelSizeY, hitpoints, maxHitpoints, maxHorizontalSpeedRunning,
		maxHorizontalSpeedDucking, minHorizontalSpeed, maxVerticalSpeed, horizontalAcceleration,
		verticalAcceleration, tempObject, sprites);
	setOrientation(-1);
	setTimeToMove(0.5);
	setTimeToRest(0);

    }

    @Override
    public void advanceTime(double dt, double timeStep) {
	if (!isDead())
	    while (dt >= timeStep && !isDead() && !isTerminated()) {
		if (getTimeToMove() > 0)
		    if (getTimeToMove() > timeStep) {
			updatePosition(timeStep);
			dt -= timeStep;
			setTimeToMove(getTimeToMove() - timeStep);
		    } else {
			updatePosition(getTimeToMove());
			dt -= getTimeToMove();
			setTimeToMove(0.0);
			setTimeToRest(1.0);
		    }
		if (getTimeToRest() > 0)
		    if (getTimeToRest() >= timeStep) {
			stayInPosition();
			dt -= timeStep;
			setTimeToRest(getTimeToRest() - timeStep);
		    } else {
			stayInPosition();
			dt -= getTimeToRest();
			setTimeToRest(0.0);
			setTimeToMove(0.5);
		    } // TODO nog resterende dt vooruitzetten?
	    }
	else if (getTimeSinceDeath() < 0.6)
	    if (getTimeSinceDeath() + dt < 0.6)
		timeSinceDeath += dt;
	    else {
		timeSinceDeath += dt;
		getWorld().removeObject(this);
		terminate();
	    }
	else if (getWorld() != null) {
	    getWorld().removeObject(this);
	    terminate();
	} else if (isTerminated())
	    dt = 0;

    }

    private void stayInPosition() {
	setSprite(getSpriteArray()[0]);

    }

    private double getTimeToRest() {
	return timeToRest;
    }

    private void setTimeToRest(double d) {
	timeToRest = d;

    }

    private void setTimeToMove(double d) {
	timeToMove = d;
    }

    private double getTimeToMove() {
	return timeToMove;
    }

    private void updatePosition(double dt) {
	if (getTimeToMove() == 0.5)
	    if (isInWater())
		startJump();
	final double newPosX = getXPositionActual() + getHorizontalSpeedMeters() * dt
		+ 0.5 * getHorizontalAcceleration() * dt * dt;
	final double newPosY = getYPositionActual() + getVerticalSpeedMeters() * dt
		+ 0.5 * getVerticalAcceleration() * dt * dt;
	final double newSpeedX = getHorizontalSpeedMeters() + getHorizontalAcceleration() * dt;
	final double newSpeedY = getVerticalSpeedMeters() + getVerticalAcceleration() * dt;
	final int xSize = getXsize();
	final int ySize = getYsize();

	new Shark((int) (newPosX * 100), (int) (newPosY * 100), xSize, ySize, 1, 1, 10, 10, 0, 2, 1.5, -10.0, newSpeedX,
		newSpeedY, true, getSpriteArray());
    }

    private double getTimeSinceDeath() {
	return timeSinceDeath;
    }

    private void startJump() {
	setMaxSpeed();
	setVerticalSpeedMeters(maxVerticalSpeed);
	setVerticalAcceleration(maxVerticalAcceleration);
	isJumping = true;
    }

    private void endJump() {
	if (isJumping()) {
	    if (getVerticalSpeedMeters() > 0)
		setVerticalSpeedMeters(0.0);
	    isJumping = false;
	}
    }

    private boolean isJumping() {
	return isJumping;
    }

    private boolean isJumping;

    private boolean isInWater() {
	if (getWorld() == null)
	    return false;

	final List<int[]> tiles = getAllOverlappingTiles();

	for (final int[] tile : tiles)
	    if (getWorld().getGeologicalFeatureTile(tile) == PassableTerrain.WATER.getValue())
		return true;

	return false;
    }
}
