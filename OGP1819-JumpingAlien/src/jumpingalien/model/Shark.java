package jumpingalien.model;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.som.annotate.Basic;
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
 * @invar ... | getTimeToRest() <= 1.0
 * @invar ... | getTimeToMove() <= 0.5
 */
public class Shark extends GameObject implements HorizontalMovement, VerticalMovement {

    /**
     * Creates a new shark
     * 
     * @param pixelLeftX   The x coordinate of the most left pixel
     * @param pixelBottomY The y coordinate of the bottom most pixel
     * @param pixelSizeX   The width of the shark in pixels
     * @param pixelSizeY   The height of the shark in pixels
     * @param tempObject   A boolean to store if the object is a temporary object
     * @param sprites      The sprites of the shark
     * 
     * @effect super(pixelLeftX, pixelBottomY, pixelSizeX, pixelSizeY,
     *         Constants.sharkHitPoints, Constants.sharkMaxHitPoints,
     *         Constants.sharkMaxHorizontalSpeed, 0,
     *         Constants.sharkMinHorizontalSpeed, Constants.sharkMaxVerticalSpeed,
     *         Constants.sharkHorizontalAcceleration,
     *         Constants.maxVerticalAcceleration, tempObject, sprites)
     * @effect setOrientation(1)
     * @effect setTimeToMove(0.5)
     * @effect setTimeToRest(0.0)
     * 
     * @pre ... | sprites.length == 3
     * 
     * @throws RuntimeException If the spriteArray is not valid
     */
    public Shark(int pixelLeftX, int pixelBottomY, int pixelSizeX, int pixelSizeY, boolean tempObject, Sprite[] sprites)
	    throws RuntimeException {
	super(pixelLeftX, pixelBottomY, pixelSizeX, pixelSizeY, Constants.sharkHitPoints, Constants.sharkMaxHitPoints,
		Constants.sharkMaxHorizontalSpeed, 0, Constants.sharkMinHorizontalSpeed,
		Constants.sharkMaxVerticalSpeed, Constants.sharkHorizontalAcceleration,
		Constants.maxVerticalAcceleration, tempObject, sprites);
	if (!isValidSpriteArray(sprites))
	    throw new RuntimeException();
	setOrientation(-1);
	setTimeToMove(0.5);
	setTimeToRest(0);

    }

    @Override
    public void advanceTime(double dt, double timeStep) {
	if (!isDead()) {
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
		setVerticalAcceleration(Constants.maxVerticalAcceleration);
	} else if (isUnderWater()) {
	    setVerticalAcceleration(0);
	    if (getVerticalSpeedMeters() < 0)
		setVerticalSpeedMeters(0);
	} else
	    setVerticalAcceleration(Constants.maxVerticalAcceleration);
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

	final Shark newSharkBoth = new Shark((int) (newPosX * 100), (int) (newPosY * 100), xSize, ySize, true,
		getSpriteArray());

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

    private void rest(double dt) {
	setSprite(getSpriteArray()[0]);
	setHorizontalSpeedMeters(0);
	setHorizontalAcceleration(0);
	if (!isStandingOnImpassableTerrain() && !isUnderWater()) {
	    setVerticalAcceleration(Constants.maxVerticalAcceleration);
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

    /**
     * A variable to store how long the shark needs to rest
     */
    private double timeToRest;

    /**
     * Returns the time left to rest
     */
    @Basic
    private double getTimeToRest() {
	return timeToRest;
    }

    /**
     * Sets the time left to rest
     * 
     * @param d The time to set
     * 
     * @post ... | new.getTimeToRest() == d
     */
    private void setTimeToRest(double d) {
	timeToRest = d;

    }

    /**
     * A variable to store how long the shark needs to move
     */
    private double timeToMove;

    /**
     * Returns the time left to move
     */
    @Basic
    private double getTimeToMove() {
	return timeToMove;
    }

    /**
     * Sets the time left to move
     * 
     * @param d The time to set
     * 
     * @post ... | new.getTimeToMove() == d
     */
    private void setTimeToMove(double d) {
	timeToMove = d;
    }

    /**
     * A variable to store the time since the shark has died
     */
    private double timeSinceDeath;

    /**
     * Returns the time since the death of the shark
     */
    @Basic
    private double getTimeSinceDeath() {
	return timeSinceDeath;
    }

    /**
     * A boolean to store if the shark is in contact with water
     */
    private boolean collidesWithWater;

    /**
     * A timer to store the time since the shark has left the water
     */
    private double timeOutOfWater = 0;

    /**
     * Returns whether the shark is fully underwater
     */
    private boolean isUnderWater() {
	if (getWorld() == null)
	    return false;

	for (int x = 0; x < getXsize(); x++)
	    if (getWorld().getGeologicalFeature(getXPositionPixel() + x,
		    getYPositionPixel() + getYsize() - 1) == PassableTerrain.WATER.getValue())
		return true;

	return false;
    }

    /**
     * Returns if the shark is in contact with water
     */
    private boolean isInWater() {
	if (getWorld() == null)
	    return false;

	final List<int[]> tiles = getAllOverlappingTiles();

	for (final int[] tile : tiles)
	    if (getWorld().getGeologicalFeatureTile(tile) == PassableTerrain.WATER.getValue())
		return true;
	return false;
    }

    /*
     * A timer to record how long it will take before the shark can change it's
     * hitpoints again
     */
    private double timeBeforeNextHitpointsChange = 0;

    /**
     * Returns the time before the next hitpoints change
     */
    @Basic
    private double getTimeBeforeNextHitpointsChange() {
	return timeBeforeNextHitpointsChange;
    }

    /**
     * Sets the time before the next hitpoints change
     * 
     * @param dt The time to set
     * 
     * @post ... | new.getTimeBeforeNextHitpointsChange() == dt
     */
    private void setTimeBeforeNextHitpointsChange(double dt) {
	timeBeforeNextHitpointsChange = dt;
    }

    /**
     * @post sprites.length == 3
     */
    @Override
    public boolean isValidSpriteArray(Sprite[] sprites) {
	for (final Sprite sprite : sprites)
	    if (!sprite.canHaveAsHeight(sprite.getHeight()) || !sprite.canHaveAsName(sprite.getName())
		    || !sprite.canHaveAsWidth(sprite.getWidth()))
		return false;
	return sprites.length == 3;
    }
}
