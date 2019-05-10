package jumpingalien.model;

import java.util.ArrayList;
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
	if (!isValidSpriteArray(sprites))
	    throw new RuntimeException();
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
			rest(timeStep);
			dt -= timeStep;
			setTimeToRest(getTimeToRest() - timeStep);
		    } else {
			rest(getTimeToRest());
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
		rest(dt);
		setTimeToRest(getTimeToRest() - dt);
		dt = 0;

	    }

	    else if (getTimeToRest() > dt) {
		rest(dt);
		setTimeToRest(getTimeToRest() - dt);
		dt = 0;
	    } else {
		rest(dt);
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

    private void rest(double dt) {
	setSprite(getSpriteArray()[0]);
	setHorizontalSpeedMeters(0);
	setHorizontalAcceleration(0);
	if (!isStandingOnImpassableTerrain() && !isUnderWater()) {
	    setVerticalAcceleration(maxVerticalAcceleration);
	    final double newPosY = getYPositionActual() + getVerticalSpeedMeters() * dt
		    + 0.5 * getVerticalAcceleration() * dt * dt;
	    final double newSpeedY = getVerticalSpeedMeters() + getVerticalAcceleration() * dt;
	    setYPositionActual(newPosY);
	    setVerticalSpeedMeters(newSpeedY);
	} else {
	    setVerticalSpeedMeters(0);
	    setVerticalAcceleration(0);
	}
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

    private double getTimeSinceDeath() {
	return timeSinceDeath;
    }

    private boolean collidesWithWater;
    private double timeOutOfWater = 0;
    private double timeBeforeNextHitpointsChange = 0;

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

    private void updatePosition(double dt) {
	if (getOrientation() == -1)
	    setSprite(getSpriteArray()[1]);
	else if (getOrientation() == 1)
	    setSprite(getSpriteArray()[2]);
	else
	    setSprite(getSpriteArray()[0]);

	if (getTimeToMove() == 0.5) {
	    if (isInWater() || isStandingOnImpassableTerrain())
		setVerticalSpeedMeters(2.0);
	    if (isUnderWater()) {
		setVerticalAcceleration(0);
		if (getVerticalSpeedMeters() < 0)
		    setVerticalSpeedMeters(0);
	    } else
		setVerticalAcceleration(maxVerticalAcceleration);
	} else if (isUnderWater()) {
	    setVerticalAcceleration(0);
	    if (getVerticalSpeedMeters() < 0)
		setVerticalSpeedMeters(0);
	} else
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

	final Shark newSharkBoth = new Shark((int) (newPosX * 100), (int) (newPosY * 100), xSize, ySize, 1, 1, 10, 10,
		0, 10, 0, 0, newSpeedX, newSpeedY, true, getSpriteArray());

	if (getWorld().canPlaceGameObjectAdvanceTime(newSharkBoth, this)) {
	    setXPositionActual(newPosX);
	    setYPositionActual(newPosY);
	    setHorizontalSpeedMeters(newSpeedX);
	    setVerticalSpeedMeters(newSpeedY);
	} else {
	    setHorizontalSpeedMeters(0);
	    setVerticalSpeedMeters(0);
	    setHorizontalAcceleration(0);
	}

	List<int[]> tiles = new ArrayList<int[]>();
	tiles = getAllOverlappingTiles();

	collidesWithWater = false;

	for (final int[] tile : tiles)
	    if (getWorld().getGeologicalFeatureTile(tile) == PassableTerrain.WATER.getValue())
		collidesWithWater = true;

	if (!collidesWithWater)
	    timeOutOfWater += dt;
	else
	    timeOutOfWater = 0;

	if (timeOutOfWater >= 0.2) {
	    timeOutOfWater -= 0.2;
	    changeHitPoints(-6);
	}

	if (getWorld() != null)
	    for (final Object gameObject : getWorld().getAllObjects()) {
		if (newSharkBoth.collidesWith((GameObject) gameObject) && gameObject instanceof Slime)
		    changeHitPoints(10);

		if (newSharkBoth.collidesWith((GameObject) gameObject) && gameObject instanceof Mazub)
		    if (getTimeBeforeNextHitpointsChange() <= 0) {
			changeHitPoints(-50);
			setTimeBeforeNextHitpointsChange(0.6);
		    }
	    }

	setTimeBeforeNextHitpointsChange(getTimeBeforeNextHitpointsChange() - dt);
	newSharkBoth.terminate();
    }

    private double getTimeBeforeNextHitpointsChange() {
	return timeBeforeNextHitpointsChange;
    }

    private void setTimeBeforeNextHitpointsChange(double dt) {
	timeBeforeNextHitpointsChange = dt;
    }

    /**
     * @post sprites.lenght == 3
     */
    @Override
    public boolean isValidSpriteArray(Sprite[] sprites) {
	return sprites.length == 3;
    }
}
