package jumpingalien.model;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.som.annotate.Raw;
import jumpingalien.util.Sprite;

/**
 * A class that implements a player character with the ability to jump, to run
 * to the left and to the right.
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 * @version 3
 */
public class Mazub extends GameObject implements HorizontalMovement, VerticalMovement {

    private double timeBeforeSpriteChange;

//    private final static double frameRate = 0.075;

    /**
     * Initialize a new player with a given position (X_pos, Y_pos), a given size
     * (X_size, Y_size), speed and sprites. .
     * 
     * @param X_pos                 The x position of the bottom left pixel of
     *                              Mazub.
     * @param X_size                The x size of Mazub given in pixels.
     * @param Y_pos                 The y position of the bottom left pixel of
     *                              Mazub.
     * @param Y_size                The y size of Mazub given in pixels.
     * @param horizontalSpeedMeters The horizontal speed of Mazub given in
     *                              meters/second
     * @param sprites               The sprites needed to represent Mazub
     * 
     * @pre ... | sprites.length >= 10
     * @pre ... | sprites.length % 2 == 0
     * 
     * @post ... | isDucking == false
     * @post ... | timeInWater == 0
     * @post ... | timeInMagma == 0
     * @post ... | timeInGas == 0
     * @post ... | timeSinceDeath == 0
     * @post ... | getTimeBeforeSpriteChange() == frameRate
     * 
     * @effect ... | super((int) (X_pos * 100), (int) (Y_pos * 100), X_size, Y_size,
     *         Constants.mazubHitPoints, Constants.mazubMaxHitPoints,
     *         Constants.mazubMaxHorizontalSpeedRunning,
     *         Constants.mazubMaxHorizontalSpeedDucking,
     *         Constants.mazubMinHorizontalSpeed, Constants.mazubMaxVerticalSpeed,
     *         Constants.mazubHorizontalAcceleration,
     *         Constants.maxVerticalAcceleration, advanceTime, sprites)
     */
    public Mazub(double X_pos, double Y_pos, int X_size, int Y_size, double horizontalSpeedMeters, boolean advanceTime,
	    Sprite... sprites) {
	super((int) (X_pos * 100), (int) (Y_pos * 100), X_size, Y_size, Constants.mazubHitPoints,
		Constants.mazubMaxHitPoints, Constants.mazubMaxHorizontalSpeedRunning,
		Constants.mazubMaxHorizontalSpeedDucking, Constants.mazubMinHorizontalSpeed,
		Constants.mazubMaxVerticalSpeed, Constants.mazubHorizontalAcceleration,
		Constants.maxVerticalAcceleration, advanceTime, sprites);

	isDucking = false;
	timeInWater = 0;
	timeInMagma = 0;
	timeInGas = 0;
	timeSinceDeath = 0;
	setTimeBeforeSpriteChange(Constants.frameRate);
    }

    /**
     * Initialize a new player with a given position (X_pos, Y_pos), a given size
     * (X_size, Y_size) and speed .
     * 
     * @param X_pos                 The x position of the bottom left pixel of Mazub
     *                              in pixels.
     * @param X_size                The x size of Mazub given in pixels.
     * @param Y_pos                 The y position of the bottom left pixel of Mazub
     *                              in pixels.
     * @param Y_size                The y size of Mazub given in pixels.
     * @param horizontalSpeedMeters The horizontal speed of Mazub given in
     *                              meters/second
     * @param minSpeedMeters        The minimal horizontal speed of Mazub given in
     *                              meters/second
     * @param maxSpeedRunningMeters The maximal horizontal speed of Mazub while
     *                              running given in meters/second
     * @param maxSpeedDuckingMeters The maximal horizontal speed of Mazub while
     *                              ducking given in meters/second
     * @param sprites               The sprites needed to represent Mazub
     * 
     * @pre ... | sprites.length >= 10
     * @pre ... | sprites.length % 2 == 0
     * 
     * @post ... | isDucking == false
     * @post ... | timeInWater == 0
     * @post ... | timeInMagma == 0
     * @post ... | timeInGas == 0
     * @post ... | timeSinceDeath == 0
     * @post ... | getTimeBeforeSpriteChange() == frameRate
     * 
     * @effect ... | super(X_pos, Y_pos, X_size, Y_size, 100, 500,
     *         maxSpeedRunningMeters, maxSpeedDuckingMeters, minSpeedMeters, 8.0,
     *         0.9, -10.0, advanceTime, sprites)
     */
    public Mazub(int X_pos, int Y_pos, int X_size, int Y_size, double horizontalSpeedMeters, boolean advanceTime,
	    Sprite... sprites) {
	super(X_pos, Y_pos, X_size, Y_size, Constants.mazubHitPoints, Constants.mazubMaxHitPoints,
		Constants.mazubMaxHorizontalSpeedRunning, Constants.mazubMaxHorizontalSpeedDucking,
		Constants.mazubMinHorizontalSpeed, Constants.mazubMaxVerticalSpeed,
		Constants.mazubHorizontalAcceleration, Constants.maxVerticalAcceleration, advanceTime, sprites);
	isDucking = false;
	timeInWater = 0;
	timeInMagma = 0;
	timeInGas = 0;
	timeSinceDeath = 0;
	setTimeBeforeSpriteChange(Constants.frameRate);
    }

    /**
     * Starts the movement in a given direction
     * 
     * @param direction The direction in which to move. -1 to go left, 1 to go right
     * 
     * @pre The given direction is valid | direction == 1 || direction == -1
     * @pre The given alien is valid | this.isValidAlien()
     * @pre The alien is not moving | !isMoving()
     * 
     * @post ... | new.orientation == direction
     * @post ... | new.isMoving == true
     * @post ... | new.getHorizontalAcceleration() == direction *
     *       maxHorizontalAcceleration
     * @post ... | new.getXSize() == getCurrentSprite().getWidth()
     * @post ... | new.getYSize() == getCurrentSprite().getHeight()
     */
    public void startMove(int direction) {
	assert isValidGameObject();
	assert direction == -1 || direction == 1;
	setOrientation(direction);
	assert !isMoving;

	setHorizontalSpeedMeters(minSpeed * direction);
	if (!isDucking)
	    setHorizontalAcceleration(direction * maxHorizontalAcceleration);
	isMoving = true;

	if (direction == 1 && !isJumping && !isFalling && !isDucking) {
	    setSprite(spriteArray[8]);
	    setYSize(getCurrentSprite().getHeight());
	    setXSize(getCurrentSprite().getWidth());
	} else if (direction == -1 && !isJumping && !isFalling && !isDucking) {
	    setSprite(spriteArray[9 + getSpriteLoopSize(spriteArray)]);
	    setYSize(getCurrentSprite().getHeight());
	    setXSize(getCurrentSprite().getWidth());
	} else if (direction == 1 && !isJumping && isDucking && !isFalling) {
	    setSprite(spriteArray[6]);
	    setYSize(getCurrentSprite().getHeight());
	    setXSize(getCurrentSprite().getWidth());
	} else if (direction == -1 && !isJumping && isDucking && !isFalling) {
	    setSprite(spriteArray[7]);
	    setYSize(getCurrentSprite().getHeight());
	    setXSize(getCurrentSprite().getWidth());
	}

    }

    /**
     * Ends the movement of Mazub
     * 
     * @pre The given alien is valid | this.isValidAlien()
     * @pre The alien is moving | this.isMoving
     * 
     * @post The horizontal speed of Mazub is 0 | new.getHorizontalSpeedActual == 0
     * @post The horizontal acceleration of Mazub is 0 |
     *       new.getHorizontalAcceleration == 0
     */
    @Raw
    public void endMove() {
	assert isValidGameObject();
	assert isMoving;

	final int orientation = getOrientation();
	isMoving = false;
	setHorizontalSpeedMeters(0);
	setHorizontalAcceleration(0);
	if (orientation == -1 && !isJumping && !isDucking) {
	    setSprite(spriteArray[3]);
	    timeSinceLastMove = 0.0;
	    setYSize(getCurrentSprite().getHeight());
	    setXSize(getCurrentSprite().getWidth());
	} else if (orientation == 1 && !isJumping && !isDucking) {
	    setSprite(spriteArray[2]);
	    timeSinceLastMove = 0.0;
	    setYSize(getCurrentSprite().getHeight());
	    setXSize(getCurrentSprite().getWidth());
	}
	timeSinceLastMove = 0.0;
    }

    /**
     * A boolean to store if Mazub is standing still and is not ducking and not
     * jumping
     */
    public boolean notMoving;

    /**
     * A boolean to store if Mazub is moving
     */
    public boolean isMoving;

    /**
     * Starts a jump for a given Mazub
     * 
     * @throws RuntimeException when Mazub is not on the ground |(isJuming ||
     *                          isFalling)
     * 
     * @post The new speed is equal to the given maximum vertical speed. |
     *       new.verticalSpeed == maxVerticalSpeed
     * @post The new acceleration is equal to the given maximum vertical
     *       acceleration. | new.VerticalAcceleration == maxVerticalAcceleration
     * @post ... | new.isJumping == true
     * @post ... | new.getXSize() == getCurrentSprite().getWidth()
     * @post ... | new.getYSize() == getCurrentSprite().getHeight()
     * @post ... | new.getVerticalSpeedMeters() == maxVerticalSpeed
     * @post ... | new.getVerticalAcceleration() == maxVerticalAcceleration
     */
    public void startJump() throws RuntimeException {
	isFalling = false;
	setVerticalSpeedMeters(maxVerticalSpeed);
	setVerticalAcceleration(Constants.maxVerticalAcceleration);
	isJumping = true;
	setSprite(spriteArray[0]);
	setYSize(getCurrentSprite().getHeight());
	setXSize(getCurrentSprite().getWidth());

	if (getOrientation() < 0) {
	    setSprite(spriteArray[5]);
	    setYSize(getCurrentSprite().getHeight());
	    setXSize(getCurrentSprite().getWidth());
	} else if (getOrientation() > 0) {
	    setSprite(spriteArray[4]);
	    setYSize(getCurrentSprite().getHeight());
	    setXSize(getCurrentSprite().getWidth());
	}
    }

    /**
     * Ends a jump for a given Mazub
     * 
     * @throws RuntimeException when Mazub is on the ground |(!isJuming)
     * 
     * @post The new speed is equal to 0. | new.verticalSpeed == 0
     * @post The new acceleration is equal to the given maximum vertical
     *       acceleration. | new.VerticalAcceleration == maxVerticalAcceleration
     * @post ... | new.getVerticalAcceleration() == maxVerticalAcceleration
     * @post ... | new.getXSize() == getCurrentSprite().getWidth()
     * @post ... | new.getYSize() == getCurrentSprite().getHeight()
     * @post ... | new.isJumping == false
     * @post ... | new.getTimeBeforeSpriteChange() == frameRate
     */
    public void endJump() throws RuntimeException {
	if (isJumping) {
	    if (getVerticalSpeedMeters() > 0)
		setVerticalSpeedMeters(0);
	    setVerticalAcceleration(Constants.maxVerticalAcceleration);
	    isJumping = false;
	    if (getOrientation() == 1 && !isFalling) {
		setSprite(spriteArray[8]);
		setYSize(getCurrentSprite().getHeight());
		setXSize(getCurrentSprite().getWidth());
	    }
	    if (getOrientation() == -1 && !isFalling) {
		setSprite(spriteArray[9 + getSpriteLoopSize(spriteArray)]);
		setYSize(getCurrentSprite().getHeight());
		setXSize(getCurrentSprite().getWidth());
	    }
	    if (getOrientation() == 0) {
		setSprite(spriteArray[0]);
		setYSize(getCurrentSprite().getHeight());
		setXSize(getCurrentSprite().getWidth());
	    }
	    setTimeBeforeSpriteChange(Constants.frameRate);
	}

	else if (isFalling)
	    ;
	else
	    throw new RuntimeException();
    }

    /**
     * A boolean to store if Mazub is jumping
     */
    public boolean isJumping;

    /**
     * A boolean to store if Mazub is falling
     */
    public boolean isFalling;

    /**
     * Starts the duck move for a Mazub
     * 
     * @post Mazub is ducking | isDucking
     * @post The horizontal acceleration is 0 | new.horizontalAcceleration == 0
     */
    public void startDuck() {
	setSprite(spriteArray[1]);
	if (getHorizontalSpeedMeters() != 0) {
	    setHorizontalSpeedMeters(getMaxSpeedDuckingMeters() * getOrientation());
	    if (getOrientation() > 0)
		setSprite(spriteArray[6]);

	    else if (getOrientation() < 0)
		setSprite(spriteArray[7]);
	}

	setYSize(getCurrentSprite().getHeight());
	setXSize(getCurrentSprite().getWidth());
	setHorizontalAcceleration(0.0);
	isDucking = true;

    }

    /**
     * Ends the duck move for a Mazub
     * 
     * @post Mazub is not ducking if possible | !isDucking
     */
    public void endDuck() {
	isDucking = false;
	setMaxSpeed();
	if (isMoving) {

	    if (getOrientation() == 1) {

		final int newHeight = getSpriteArray()[8].getHeight();
		final int newWidth = getSpriteArray()[8].getWidth();

		final Mazub newMazub = new Mazub(getXPositionActual(), getYPositionActual(), newWidth, newHeight, 0,
			true, getSpriteArray());

		if (getWorld().canPlaceMazubFullCheck(newMazub, this)) {
		    setSprite(spriteArray[8]);
		    setYSize(getCurrentSprite().getHeight());
		    setXSize(getCurrentSprite().getWidth());
		    setHorizontalAcceleration(maxHorizontalAcceleration * getOrientation());
		} else
		    isDucking = true;

	    } else {

		final int newHeight = getSpriteArray()[9 + getSpriteLoopSize(getSpriteArray())].getHeight();
		final int newWidth = getSpriteArray()[9 + getSpriteLoopSize(getSpriteArray())].getWidth();

		final Mazub newMazub = new Mazub(getXPositionActual(), getYPositionActual(), newWidth, newHeight, 0,
			true, getSpriteArray());

		if (getWorld().canPlaceGameObjectAdvanceTime(newMazub, this)) {
		    setSprite(spriteArray[9 + getSpriteLoopSize(getSpriteArray())]);
		    setYSize(getCurrentSprite().getHeight());
		    setXSize(getCurrentSprite().getWidth());
		    setHorizontalAcceleration(maxHorizontalAcceleration * getOrientation());
		} else
		    isDucking = true;
	    }
	    setTimeBeforeSpriteChange(Constants.frameRate);
	} else {
	    final int newHeight = getSpriteArray()[0].getHeight();
	    final int newWidth = getSpriteArray()[0].getWidth();

	    final Mazub newMazub = new Mazub(getXPositionActual(), getYPositionActual(), newWidth, newHeight, 0, true,
		    getSpriteArray());

	    if (getWorld().canPlaceGameObjectAdvanceTime(newMazub, this)) {
		setSprite(spriteArray[0]);
		setYSize(getCurrentSprite().getHeight());
		setXSize(getCurrentSprite().getWidth());
	    } else
		isDucking = true;
	}
	timeSinceLastMove = 0.0;
    }

    /**
     * Returns whether Mazub is ducking
     */
    public boolean isDucking() {
	return isDucking;
    }

    /**
     * A boolean to store if the Mazub is ducking
     */
    private boolean isDucking;

    private boolean collidesWithMagma;
    private boolean collidesWithWater;
    private boolean collidesWithGas;
    private double timeInWater;
    private double timeInMagma;
    private double timeInGas;

    private int getNextSpriteIndex() {
	int indexCurrentSprite = 0;
	int nextIndex = 0;
	final Sprite currentSprite = getCurrentSprite();
	final int length = spriteArray.length;
	for (int i = 0; i < length; i++)
	    if (spriteArray[i] == currentSprite)
		indexCurrentSprite = i;

	if (indexCurrentSprite >= 8) {
	    if (indexCurrentSprite == 8 + getSpriteLoopSize(spriteArray))
		nextIndex = 8;
	    else if (indexCurrentSprite == 9 + 2 * getSpriteLoopSize(spriteArray))
		nextIndex = 9 + getSpriteLoopSize(spriteArray);
	    else
		nextIndex = indexCurrentSprite + 1;
	} else
	    nextIndex = indexCurrentSprite;
	return nextIndex;
    }

    /**
     * Advances the in-game time for a given interval dt.
     * 
     * @param dt The time to advance in seconds
     * @effect updatePosition(dt, timeStep)
     */
    @Override
    public void advanceTime(double dt, double timeStep) {
	tempObject = true;
	if (!isMoving && !isJumping && !isDucking && notMoving)
	    timeSinceLastMove += dt;
	else if (!isMoving && !isJumping && !isDucking && !notMoving) {
	    notMoving = true;
	    timeSinceLastMove += dt;
	} else {
	    timeSinceLastMove = 0.0;
	    notMoving = false;
	}
	if (!isDead()) {
	    if (getTimeBeforeSpriteChange() <= 0)
		setTimeBeforeSpriteChange(Constants.frameRate);
	    while (dt > timeStep && !isDead() && !isTerminated())
		if (getTimeBeforeSpriteChange() > timeStep) {
		    updatePosition(timeStep);
		    dt -= timeStep;
		    setTimeBeforeSpriteChange(getTimeBeforeSpriteChange() - timeStep);
		} else {
		    updatePosition(getTimeBeforeSpriteChange());
		    dt -= getTimeBeforeSpriteChange();
		    setTimeBeforeSpriteChange(Constants.frameRate);
		    setSprite(getSpriteArray()[getNextSpriteIndex()]);
		}
	    if (dt > getTimeBeforeSpriteChange()) {
		updatePosition(getTimeBeforeSpriteChange());
		setSprite(getSpriteArray()[getNextSpriteIndex()]);
		dt -= getTimeBeforeSpriteChange();
		setTimeBeforeSpriteChange(Constants.frameRate);
	    }
	    updatePosition(dt);
	    setTimeBeforeSpriteChange(getTimeBeforeSpriteChange() - dt);

	} else if (isTerminated())
	    dt = 0;
	else if (timeSinceDeath < 0.6) {
	    if (dt < 0.599 - timeSinceDeath)
		timeSinceDeath += dt;
	    else {
		timeSinceDeath += dt;
		getWorld().removeObject(this);
		terminate();
	    }
	} else if (getWorld() != null) {
	    getWorld().removeObject(this);
	    terminate();
	}

	if (timeSinceLastMove > 1) {
	    setSprite(getSpriteArray()[0]);
	    setOrientation(0);
	}
	tempObject = false;
	if (getWorld() != null)
	    if (!isPositionInWorld(getXPositionPixel(), getYPositionPixel()))
		terminate();
    }

    /**
     * Updates the position of Mazub over a given time interval
     * 
     * @param dt The time interval in seconds over which to update the position
     * @post The new position is valid | isValidActualXPosition(new.xPosMeter) &&
     *       isValidActualYPosition(new.yPosMeter)
     * @post The new x position is equal to the old position + dt*(horizontal speed)
     *       + dt*dt*(horizontal acceleration) | new.xPosMeter ==
     *       getXPositionActual() + getHorizontalSpeedMeters()*dt +
     *       0.5*getHorizontalAcceleration()*dt*dt
     * @post The new y position is equal to the old position + dt*(vertical speed) +
     *       dt*dt*(vertical acceleration) | new.yPosMeter == getYPositionActual() +
     *       getVerticalSpeedMeters()*dt + 0.5*getVerticalAcceleration()*dt*dt
     * @post The new x position is 0 if the actual new position is left of the
     *       canvas | if new.xPosMeter == 0 then new.xPosMeter ==
     *       getXPositionActual() + getHorizontalSpeedMeters()*dt +
     *       0.5*getHorizontalAcceleration()*dt*dt < 0
     * @post The new x position is the maximal x position if the actual new position
     *       is right of the canvas | if new.xPosMeter == this.getMaxXPosition()
     *       then new.xPosMeter == getXPositionActual() +
     *       getHorizontalSpeedMeters()*dt + 0.5*getHorizontalAcceleration()*dt*dt >
     *       getMaxXPosition()
     * @post The new y position is 0 if the actual new position is under the canvas
     *       | if new.yPosMeter == 0 then getYPositionActual() +
     *       getVerticalSpeedMeters()*dt + 0.5*getVerticalAcceleration()*dt*dt < 0
     * @post The new y position is the maximal y position if the actual new position
     *       is above the canvas | if new.yPosMeter == this.getMaxYPosition() then
     *       getYPositionActual() + getVerticalSpeedMeters()*dt +
     *       0.5*getVerticalAcceleration()*dt*dt > getMaxYPosition()
     */
    private void updatePosition(double dt) {
	if (!isStandingOnImpassableTerrain())
	    setVerticalAcceleration(Constants.maxVerticalAcceleration);

	final double newPosX = getXPositionActual() + getHorizontalSpeedMeters() * dt
		+ 0.5 * getHorizontalAcceleration() * dt * dt;
	final double newPosY = getYPositionActual() + getVerticalSpeedMeters() * dt
		+ 0.5 * getVerticalAcceleration() * dt * dt;
	final double newSpeedX = getHorizontalSpeedMeters() + getHorizontalAcceleration() * dt;
	final double newSpeedY = getVerticalSpeedMeters() + getVerticalAcceleration() * dt;
	final int xSize = getXsize();
	final int ySize = getYsize();

	if (getWorld() != null) {
	    final Mazub newMazub = new Mazub(newPosX, newPosY, xSize, ySize, newSpeedX, true, getCurrentSprite());
	    if (getWorld().canPlaceGameObjectAdvanceTime(newMazub, this)) {
		setXPositionActual(newPosX);
		setYPositionActual(newPosY);
		setHorizontalSpeedMeters(newSpeedX);
		setVerticalSpeedMeters(newSpeedY);
	    } else {
		setHorizontalSpeedMeters(0);
		setVerticalSpeedMeters(0);
		setHorizontalAcceleration(0);

		setCorrectSprite();

		isMoving = false;
		setHorizontalSpeedMeters(0);
		setVerticalSpeedMeters(0);
		setHorizontalAcceleration(0);

	    }

	    if (isStandingOnImpassableTerrain() && !isJumping)
		setVerticalAcceleration(0);
	    else
		fall();

	    updateHitpointsGeologicalFeatures(dt);
	    updateHitpointsContactWithOtherGameObjects(dt, newMazub);

	    if (getHitpoints() == 0)
		isDead = true;

	    newMazub.terminate();

	} else {
	    setXPositionActual(newPosX);
	    setYPositionActual(newPosY);
	    setHorizontalSpeedMeters(newSpeedX);
	    setVerticalSpeedMeters(newSpeedY);
	}
	setTimeBeforeNextHitpointsChange(getTimeBeforeNextHitpointsChange() - dt);

    }

    private void setCorrectSprite() {
	if (getOrientation() == -1 && !isDucking())
	    setSprite(getSpriteArray()[3]);
	else if (getOrientation() == 0 && !isDucking())
	    setSprite(getSpriteArray()[0]);
	else if (!isDucking())
	    setSprite(getSpriteArray()[2]);
	else if (getOrientation() == -1)
	    setSprite(getSpriteArray()[7]);
	else if (getOrientation() == 1)
	    setSprite(getSpriteArray()[6]);
	else
	    setSprite(getSpriteArray()[1]);
    }

    private void updateHitpointsGeologicalFeatures(double dt) {
	List<int[]> tiles = new ArrayList<int[]>();
	tiles = getAllOverlappingTiles();
	collidesWithMagma = false;
	collidesWithWater = false;
	collidesWithGas = false;
	for (final int[] tile : tiles) {
	    if (getWorld().getGeologicalFeatureTile(tile) == PassableTerrain.MAGMA.getValue())
		collidesWithMagma = true;
	    if (getWorld().getGeologicalFeatureTile(tile) == PassableTerrain.WATER.getValue())
		collidesWithWater = true;
	    if (getWorld().getGeologicalFeatureTile(tile) == PassableTerrain.GAS.getValue())
		collidesWithGas = true;
	}

	if (collidesWithWater)
	    timeInWater += dt;
	else
	    timeInWater = 0;

	if (timeInWater >= 0.2) {
	    timeInWater -= 0.2;
	    if (!collidesWithMagma)
		changeHitPoints(-2);
	}
	if (collidesWithMagma) {
	    timeInWater = 0;
	    timeInGas = 0;
	    if (timeInMagma == 0) {
		changeHitPoints(-50);
		timeInMagma += dt;
	    } else
		timeInMagma += dt;
	    if (timeInMagma >= 0.2) {
		changeHitPoints(-50);
		timeInMagma -= 0.2;
	    }
	} else
	    timeInMagma = 0;
	if (collidesWithGas) {
	    timeInWater = 0;
	    if (timeInGas == 0) {
		if (!collidesWithMagma) {
		    changeHitPoints(-4);
		    timeInGas += dt;
		}
	    } else
		timeInGas += dt;
	    if (timeInGas >= 0.2) {
		if (!collidesWithMagma)
		    changeHitPoints(-4);
		timeInGas -= 0.2;
	    }
	}
    }

    private void updateHitpointsContactWithOtherGameObjects(double dt, final Mazub mazub) {
	for (final Object object : getWorld().getAllObjects()) {
	    if (object instanceof Skullcab)
		((Skullcab) object)
			.setTimeSinceContactWithMazub(((Skullcab) object).getTimeSinceContactWithMazub() + dt);
	    if (mazub.collidesWith((GameObject) object) && object instanceof Sneezewort
		    && getHitpoints() != getMaxHitpoints() && !((GameObject) object).isDead()) {
		changeHitPoints(50);
		((GameObject) object).terminate();
	    } else if (mazub.collidesWith((GameObject) object) && object instanceof Plant
		    && ((GameObject) object).isDead()) {
		changeHitPoints(-20);
		((GameObject) object).terminate();
	    }
	    if (mazub.collidesWith((GameObject) object) && object instanceof Skullcab
		    && getHitpoints() != getMaxHitpoints() && !((GameObject) object).isDead()
		    && ((Skullcab) object).getTimeSinceContactWithMazub() >= 0.6) {
		changeHitPoints(50);
		((GameObject) object).changeHitPoints(-1);
		((Skullcab) object).setTimeSinceContactWithMazub(0.0);
	    }
	    if (mazub.collidesWith((GameObject) object) && object instanceof Slime && !((GameObject) object).isDead()
		    && getTimeBeforeNextHitpointsChange() <= 0) {
		changeHitPoints(-20);
		setTimeBeforeNextHitpointsChange(0.6);
	    }
	    if (mazub.collidesWith((GameObject) object) && object instanceof Shark && !((GameObject) object).isDead()
		    && getTimeBeforeNextHitpointsChange() <= 0) {
		changeHitPoints(-50);
		setTimeBeforeNextHitpointsChange(0.6);
	    }
	}
    }

    private double getTimeBeforeSpriteChange() {
	return timeBeforeSpriteChange;
    }

    private void setTimeBeforeSpriteChange(double time) {
	timeBeforeSpriteChange = time;

    }

    /**
     * @post sprites.length >= 10
     * @post sprites.length % 2 == 0
     */
    @Override
    public boolean isValidSpriteArray(Sprite[] sprites) {
	for (final Sprite sprite : sprites)
	    if (!sprite.canHaveAsHeight(sprite.getHeight()) || !sprite.canHaveAsName(sprite.getName())
		    || !sprite.canHaveAsWidth(sprite.getWidth()))
		return false;
	return sprites.length >= 10 && sprites.length % 2 == 0;
    }

    /**
     * Returns the loop length of a given sprite array (The amount of sprites to go
     * either right or left)
     * 
     * @param sprites The array to get the loop length from
     * 
     * @throws RuntimeException When the spriteArray is not valid |
     *                          !isValidSpriteArray(sprites)
     */
    private int getSpriteLoopSize(Sprite... sprites) throws RuntimeException {
	if (!isValidSpriteArray(sprites))
	    throw new RuntimeException();
	return (sprites.length - 10) / 2;
    }

    double timeSinceDeath = 0;

    /**
     * A timer to store how long the Mazub has been standing still
     */
    private double timeSinceLastMove;

    /**
     * A boolean to store if the Mazub is the player of its world
     */
    boolean isPlayer;

    private double timeBeforeNextHitpointsChange = 0;

    private double getTimeBeforeNextHitpointsChange() {
	return timeBeforeNextHitpointsChange;
    }

    public void setTimeBeforeNextHitpointsChange(double dt) {
	timeBeforeNextHitpointsChange = dt;

    }

}