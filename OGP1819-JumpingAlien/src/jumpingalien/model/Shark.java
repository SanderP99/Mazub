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
    private boolean switched;

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
	if (!isDead()) {
	    switched = false;
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
			stayInPosition(timeStep);
			dt -= timeStep;
			setTimeToRest(getTimeToRest() - timeStep);
		    } else {
			stayInPosition(getTimeToRest());
			dt -= getTimeToRest();
			setTimeToRest(0.0);
			setTimeToMove(0.5);
			setOrientation(getOrientation() * -1);
			switched = true;
		    }
	    }
	    if (getTimeToMove() > dt) {
		updatePosition(dt);
		setTimeToMove(getTimeToMove() - dt);
		dt = 0;

	    } else if (getTimeToMove() > 0) {
		updatePosition(getTimeToMove());
		dt -= getTimeToMove();
		setTimeToMove(0);
		setTimeToRest(1);
		stayInPosition(dt);
		setTimeToRest(getTimeToRest() - dt);
		dt = 0;

	    }

	    else if (getTimeToRest() > dt) {
		stayInPosition(dt);
		setTimeToRest(getTimeToRest() - dt);
		dt = 0;
	    } else {
		stayInPosition(dt);
		dt -= getTimeToRest();
		setTimeToMove(0.5);
		if (!switched)
		    setOrientation(getOrientation() * -1);
		setTimeToRest(0);
		updatePosition(dt);
		setTimeToMove(getTimeToMove() - dt);
		dt = 0;
	    }
	} else if (getTimeSinceDeath() < 0.6)
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

    private void stayInPosition(double dt) {
	setSprite(getSpriteArray()[0]);
	if (!isStandingOnImpassableTerrain() && !isUnderWater()) {
	    final double newPosY = getYPositionActual() + getVerticalSpeedMeters() * dt
		    + 0.5 * getVerticalAcceleration() * dt * dt;
	    final double newSpeedY = getVerticalSpeedMeters() + getVerticalAcceleration() * dt;
	    setYPositionActual(newPosY);
	    setVerticalSpeedMeters(newSpeedY);
	} else
	    setVerticalSpeedMeters(0);
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
	if (getOrientation() == -1)
	    setSprite(getSpriteArray()[1]);
	else if (getOrientation() == 1)
	    setSprite(getSpriteArray()[2]);
	else
	    setSprite(getSpriteArray()[0]);
	if (getTimeToMove() == 0.5 && (isInWater() || isStandingOnImpassableTerrain())) {
	    // setMaxSpeed();
	    setVerticalSpeedMeters(getMaxVerticalSpeedMeters());
	    if (isUnderWater()) {
		setVerticalAcceleration(0.0);
		setVerticalSpeedMeters(0);
	    }

	    else
		setVerticalAcceleration(maxVerticalAcceleration);
	} else if (isInWater())
	    if (isUnderWater())
		setVerticalAcceleration(0.0);
	    else
		setVerticalAcceleration(maxVerticalAcceleration);
	else if (getTimeToMove() == 0.5 && !isUnderWater())
	    setVerticalAcceleration(maxVerticalAcceleration);
	else
	    setVerticalAcceleration(maxVerticalAcceleration);

	setHorizontalAcceleration(1.5 * getOrientation());

	final double newPosX = getXPositionActual() + Math.abs(getHorizontalSpeedMeters()) * getOrientation() * dt
		+ 0.5 * getHorizontalAcceleration() * dt * dt;
	final double newPosY = getYPositionActual() + getVerticalSpeedMeters() * dt
		+ 0.5 * getVerticalAcceleration() * dt * dt;
	final double newSpeedX = Math.abs(getHorizontalSpeedMeters()) * getOrientation()
		+ getHorizontalAcceleration() * dt;
	final double newSpeedY = getVerticalSpeedMeters() + getVerticalAcceleration() * dt;
	final int xSize = getXsize();
	final int ySize = getYsize();

	new Shark((int) (newPosX * 100), getYPositionPixel(), xSize, ySize, 1, 1, 10, 10, 0, 10, 0, 0, newSpeedX,
		newSpeedY, true, getSpriteArray());
	new Shark(getXPositionPixel(), (int) (newPosY * 100), xSize, ySize, 1, 1, 10, 10, 0, 10, 0, 0, newSpeedX,
		newSpeedY, true, getSpriteArray());
	final Shark newSharkBoth = new Shark((int) (newPosX * 100), (int) (newPosY * 100), xSize, ySize, 1, 1, 10, 10,
		0, 10, 0, 0, newSpeedX, newSpeedY, true, getSpriteArray());

	final boolean canPlaceBothShark = getWorld().canPlaceGameObjectAdvanceTime(newSharkBoth, this);

	if (canPlaceBothShark) {
	    setXPositionActual(newPosX);
	    setYPositionActual(newPosY);
	    setHorizontalSpeedMeters(newSpeedX);
	    setVerticalSpeedMeters(newSpeedY);
	} else {
	    setHorizontalSpeedMeters(0);
	    setVerticalSpeedMeters(0);
	    setHorizontalAcceleration(0);
	}
//	else if (canPlaceXShark) {
//	    setHorizontalSpeedMeters(newSpeedX);
//	    setXPositionActual(newPosX);
//	    setVerticalSpeedMeters(0);
//	} else if (canPlaceYShark) {
//	    setVerticalSpeedMeters(newSpeedY);
//	    setYPositionActual(newPosY);
//	    setHorizontalSpeedMeters(0);
//	} else {
//	    setHorizontalSpeedMeters(0);
//	    setVerticalSpeedMeters(0);
//	    setHorizontalAcceleration(0);
//	}
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

    private boolean isUnderWater() {
	if (getWorld() == null)
	    return false;

	for (int x = 0; x < getXsize(); x++)
	    if (getWorld().getGeologicalFeature(getXPositionPixel() + x,
		    getYPositionPixel() + getYsize() - 1) == PassableTerrain.WATER.getValue())
		return true;

	return false;
    }

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
