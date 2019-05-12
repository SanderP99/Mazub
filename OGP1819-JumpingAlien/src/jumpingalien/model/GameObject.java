package jumpingalien.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import jumpingalien.util.Sprite;

/**
 * A class that implements a player character with the ability to jump, to run
 * to the left and to the right.
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 * @version 2
 * 
 * @invar The left most x coordinate of GameObject is a valid coordinate |
 *        isValidXPosition() == true
 * @invar The bottom most y coordinate of GameObject is a valid coordinate |
 *        isValidYPosition() == true
 * @invar The horizontal speed of a GameObject is a valid speed |
 *        isValidHorizontalSpeed() == true
 * @invar The vertical speed of a GameObject is a valid speed |
 *        isValidVerticalSpeed() == true
 * @invar The GameObject is valid | isValidGameObject() == true
 * @invar The GameObject has a valid orientation | isValidOrientation() == true
 * @invar The sprites are valid sprites | isValidSpriteArray() == true
 * @invar The sprites in an array are all valid | for (sprite :
 *        getSpriteArray()) | isValidSprite(sprite) == true
 */
public abstract class GameObject {

    /**
     * Creates a new GameObject
     * 
     * @param pixelLeftX                The leftmost pixel of the GameObject
     * @param pixelBottomY              The bottom-most pixel of the GameObject
     * @param pixelSizeX                The width of the GameObject in pixels
     * @param pixelSizeY                The height of the GameObject in pixels.
     * @param hitpoints                 The hitpoints of the GameObject
     * @param maxHorizontalSpeedRunning The maximal horizontal speed of GameObject
     *                                  while running given in meters/second
     * @param maxHorizontalSpeedDucking The maximal horizontal speed of GameObejct
     *                                  while ducking given in meters/second
     * @param minHorizontalSpeed        The minimal horizontal speed of GameObject
     *                                  given in meters/second
     * @param maxVerticalSpeed          The maximum vertical speed of a GameObject
     * @param horizontalAcceleration    The horizontal acceleration of a GameObject
     * @param verticalAcceleration      The vertical acceleration of a GameObject
     * @param tempObject                A boolean to store if the created object is
     *                                  temporary
     * @param sprites                   The sprites needed to represent the
     *                                  GameObject
     * 
     * @effect setYSize(pixelSizeY) && setXSize(pixelSizeX) &&
     *         setXPositionPixel(pixelLeftX) && setYPositionPixel(pixelBottomY) &&
     *         setSprite(sprites[0]) && setSpriteArray(sprites) &&
     *         setHitpoints(hitpoints) && setOrientation(0) &&
     *         setHorizontalSpeedMeters(0) && setVerticalAcceleration(0) &&
     *         setHorizontalAcceleration(0) && setOverlappingTiles()
     * 
     * @pre ... | sprites.length > 0
     * @pre ... |
     * 
     * @post ... | this.maxHorizontalAcceleration == horizontalAcceleration
     * @post ... | this.maxVerticalAcceleration == verticalAcceleration
     * @post ... | this.minSpeed == minHorizontalSpeed
     * @post ... | this.maxSpeedDucking == maxHorizontalSpeedDucking
     * @post ... | this.maxSpeedRunning == maxHorizontalSpeedRunning
     * @post ... | this.maxVerticalSpeed == maxVerticalSpeed
     * @post ... | new.xSize == pixelSizeX
     * @post ... | new.ySize == pixelSizeY
     * @post ... | new.xPosPixel == pixelLeftX
     * @post ... | new.yPosPixel == pixelBottomY
     * @post ... | new.spriteArray == sprites
     * @post ... | new.hitPoints == hitpoints
     * @post ... | new.orientation == 0
     * @post ... | new.horizontalSpeedMeters == 0.0
     * @post ... | new.horizontalAcceleration == 0.0
     * @post ... | new.verticalAcceleration == 0.0
     */
    GameObject(int pixelLeftX, int pixelBottomY, int pixelSizeX, int pixelSizeY, int hitpoints, int maxHitpoints,
	    double maxHorizontalSpeedRunning, double maxHorizontalSpeedDucking, double minHorizontalSpeed,
	    double maxVerticalSpeed, double horizontalAcceleration, double verticalAcceleration, boolean tempObject,
	    Sprite... sprites) {
	this.tempObject = tempObject;
	setYSize(pixelSizeY);
	setXSize(pixelSizeX);
	setXPositionActual((double) pixelLeftX / 100);
	xPosPixel = pixelLeftX;
	setYPositionActual((double) pixelBottomY / 100);
	yPosPixel = pixelBottomY;
	setSpriteArray(sprites);
	setMaxHitpoints(maxHitpoints);
	setHitpoints(hitpoints);
	setMaxVerticalSpeed(maxVerticalSpeed);
	setMaxSpeed();
	setOrientation(0);
	setHorizontalSpeedMeters(0);
	minSpeed = minHorizontalSpeed;
	maxSpeedDucking = maxHorizontalSpeedDucking;
	maxSpeedRunning = maxHorizontalSpeedRunning;
	setHorizontalAcceleration(0);
	setVerticalAcceleration(0);
	setSprite(sprites[0]);
    }

    /**
     * A boolean to store if a created object is temporary
     */
    public boolean tempObject;

    /**
     * Returns whether the given sprite is valid
     * 
     * @param sprite The sprite to check
     */
    public static boolean isValidSprite(Sprite sprite) {
	return sprite.canHaveAsHeight(sprite.getHeight()) && sprite.canHaveAsWidth(sprite.getWidth())
		&& sprite.canHaveAsName(sprite.getName());
    }

    /**
     * Returns the current sprite.
     */
    @Basic
    public Sprite getCurrentSprite() {
	return sprite;
    }

    /**
     * Sets the current sprite to the given sprite
     * 
     * @param sprite The sprite to set
     * 
     * @post The current sprite of this GameObject is the given sprite | this.sprite
     *       == sprite
     */
    protected void setSprite(Sprite sprite) {
	if (!isValidSprite(sprite))
	    throw new RuntimeException();
	if (getCurrentSprite() != null && (getCurrentSprite().getHeight() != sprite.getHeight()
		|| getCurrentSprite().getWidth() != sprite.getWidth())) {
	    final Mazub newMazub = new Mazub(getXPositionActual(), getYPositionActual(), sprite.getWidth(),
		    sprite.getHeight(), 0, getMinSpeedMeters(), getMaxSpeedRunningMeters(), getMaxSpeedDuckingMeters(),
		    true, getSpriteArray());
	    if (getWorld() != null) {
		if (getWorld().canPlaceMazubFullCheck(newMazub, this)) {
		    this.sprite = sprite;
		    setSizes();
		}
	    } else {
		this.sprite = sprite;
		setSizes();
	    }
	} else {
	    this.sprite = sprite;
	    setSizes();
	}

    }

    private void setSizes() {
	setYSize(getCurrentSprite().getHeight());
	setXSize(getCurrentSprite().getWidth());
    }

    /**
     * A variable to store the current sprite.
     */
    protected Sprite sprite;

    /**
     * Returns the sprite array of a GameObject
     */
    public Sprite[] getSpriteArray() {
	return spriteArray.clone();
    }

    /**
     * Sets the sprite array to a given array
     * 
     * @param sprites The array to which to set
     * 
     * @post The new spriteArray is equal to the given one | new.getSpriteArray ==
     *       sprites
     */
    private void setSpriteArray(Sprite... sprites) {
	spriteArray = sprites.clone();
    }

    /**
     * A variable to store all the sprites of a GameObject
     */
    protected Sprite[] spriteArray;

    /**
     * Terminates the GameObject
     * 
     * @post isTerminated()
     * @effect setHitpoints(0) && removeWorld()
     */
    public void terminate() {
	if (!isTerminated()) {
	    isTerminated = true;
	    isDead = true;
	    setHitpoints(0);
	    removeWorld();
	    if (this instanceof Slime) {
		((Slime) this).removeSchool();
		allIDs.remove(((Slime) this).getIdentification());
	    }

	}
    }

    /**
     * Check whether this object is terminated.
     */
    @Basic
    @Raw
    public boolean isTerminated() {
	return isTerminated;
    }

    /**
     * A boolean to store if the GameObject is terminated
     */
    private boolean isTerminated;

    /**
     * Check whether this object is dead.
     */
    @Basic
    @Raw
    public boolean isDead() {
	return isDead;
    }

    /**
     * A boolean to store if the GameObject is dead
     */
    protected boolean isDead;

    /**
     * Removes the world of the GameObject
     * 
     * @post this.world == null
     * @post !this.getWorld().getAllObjects().contains(this)
     */
    protected void removeWorld() {
	if (world != null) {
	    world.removeObject(this);
	    world = null;
	}
    }

    /**
     * Returns the y size of a given GameObject in pixels.
     */
    @Basic
    public int getYsize() {
	return ySize;
    }

    /**
     * Sets the y size of GameObject to the given amount of pixels.
     * 
     * @param Y_size The wanted amount of pixels for the y size of GameObject.
     * 
     * @post the new y size is equal to the specified amount of pixels | (this.ySize
     *       == Y_size)
     */
    protected void setYSize(int Y_size) {
	ySize = Y_size;
    }

    /**
     * A variable to store the y size of a GameObject in pixels.
     */
    private int ySize;

    /**
     * Returns the x size of a given GameObject in pixels.
     */
    @Basic
    public int getXsize() {
	return xSize;
    }

    /**
     * Sets the x size of GameObject to the given amount of pixels.
     * 
     * @param X_size The wanted amount of pixels for the x size of GameObject.
     * 
     * @post the new x size is equal to the specified amount of pixels | (this.xSize
     *       == X_size)
     */
    protected void setXSize(int X_size) {
	xSize = X_size;
    }

    /**
     * Sets the x position of the bottom left pixel of a given GameObject to the
     * specified x position.
     * 
     * @param X_pos The wanted x position.
     * 
     * @post this.xPosMeter == X_pos
     * @post this.xPosPixel == (int) (X_pos * 100)
     * @post if !isPositionInWorld(X_pos) then this.isTerminated
     */
    public void setXPositionActual(double X_pos) {
	xPosPixel = (int) (X_pos * 100);
	xPosMeter = X_pos;
	if (!tempObject) {
	    if (getWorld() != null && getWorld().getWorldSizeX() < getXPositionPixel()) {
		setHitpoints(0);
		terminate();
	    }
	    if (!isValidActualXPosition(xPosMeter))
		terminate();
	}

    }

    /**
     * A variable to store the x size of a GameObject in pixels.
     */
    private int xSize;

    /**
     * Returns whether the given coordinate is on the canvas.
     * 
     * @param X_pos The coordinate to check in pixels.
     */
    public static boolean isValidPixelXPosition(int X_pos) {
	return X_pos >= 0 && X_pos <= Double.POSITIVE_INFINITY;
    }

    /**
     * Returns whether the given coordinate is on the canvas.
     * 
     * @param X_pos The coordinate to check in meters.
     */
    public static boolean isValidActualXPosition(double X_pos) {
	return X_pos <= Double.POSITIVE_INFINITY && X_pos >= 0;
    }

    /**
     * Returns the x position on the canvas in pixels.
     */
    @Basic
    public int getXPositionPixel() {
	return xPosPixel;
    }

    /**
     * Returns the x position on the canvas in meters.
     */
    public double getXPositionActual() {
	return xPosMeter;
    }

    /**
     * A variable to store the x position of the GameObject in pixels
     */
    protected int xPosPixel;

    /**
     * A variable to store the x position of the GameObject in meters
     */
    protected double xPosMeter;

    /**
     * Returns whether a position is a coordinate in the world
     * 
     * @param pixelX The x position in pixels
     * @param pixelY The y position in pixels
     */
    public boolean isPositionInWorld(int pixelX, int pixelY) {
	return pixelX >= 0 && pixelX < getWorld().getWorldSizeX() && pixelY >= 0 && pixelY < getWorld().getWorldSizeY();
    }

    /**
     * Returns whether the given coordinate is on the canvas.
     * 
     * @param Y_pos The given coordinate to check in meters.
     */
    public static boolean isValidActualYPosition(double Y_pos) {
	return Y_pos <= Double.POSITIVE_INFINITY && Y_pos >= -0.01;
    }

    /**
     * Returns whether the given coordinate is on the canvas.
     * 
     * @param Y_pos The given coordinate to check in pixels.
     */
    public static boolean isValidPixelYPosition(int Y_pos) {
	return Y_pos >= 0 && Y_pos <= Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the maximal x position of the world in pixels.
     */
    @Basic
    public int getMaxXPosition() {
	return getWorld().getWorldSizeX();
    }

    /**
     * Returns the y position on the canvas in pixels.
     */
    @Basic
    public int getYPositionPixel() {
	return yPosPixel;
    }

    /**
     * Returns the y position on the canvas in meters.
     */
    public double getYPositionActual() {
	return yPosMeter;
    }

    /**
     * Sets the y position of the bottom left pixel of a given GameObject to the
     * specified y position.
     * 
     * @param Y_pos The wanted y position.
     * 
     * @post this.yPosMeter == Y_pos
     * @post this.yPosPixel == (int) (Y_pos * 100)
     */
    public void setYPositionActual(double Y_pos) {

	yPosPixel = (int) (Y_pos * 100);
	yPosMeter = Y_pos;
	if (!tempObject) {
	    if (!isValidActualYPosition(yPosMeter)) {
		terminate();
		setHitpoints(0);
	    }
	    if (getWorld() != null && getWorld().getWorldSizeY() < getYPositionPixel())
		terminate();
	}

    }

    /**
     * Returns the maximal y position of the world in pixels.
     */
    @Basic
    public int getMaxYPosition() {
	return world.getWorldSizeY();
    }

    /**
     * A variable to store the y position of the GameObject in pixels
     */
    protected int yPosPixel;

    /**
     * A variable to store the y position of the GameObject in meters
     */
    protected double yPosMeter;

    /**
     * Returns the hitpoints
     */
    @Basic
    public int getHitpoints() {
	return hitpoints;
    }

    /**
     * Sets the hitpoints of the GameObject
     * 
     * @param hitpoints The hitpoints to set
     * 
     * @post if hitpoints <= 0 then this.hitpoints == 0
     * @post if hitpoints >= 500 then this.hitpoints == 500
     * @post if 0 < hitpoints < 500 then this.hitpoints == hitpoints
     */
    protected void setHitpoints(int hitpoints) {
	if (hitpoints <= 0) {
	    this.hitpoints = 0;
	    isDead = true;
	} else if (hitpoints > getMaxHitpoints())
	    this.hitpoints = getMaxHitpoints();
	else
	    this.hitpoints = hitpoints;
    }

    /**
     * A variable to store the hitpoints
     */
    private int hitpoints;

    /**
     * A variable to store the maximum hitpoints
     */
    private int maxHitpoints;

    /**
     * Changes the hitpoints of the GameObject
     * 
     * @param change The value by which to change
     * 
     * @effect setHitpoints(getHitpoints() + change)
     */
    protected void changeHitPoints(int change) {
	setHitpoints(getHitpoints() + change);

    }

    /**
     * Returns the maximum hitpoints of a GameObject
     */
    public int getMaxHitpoints() {
	return maxHitpoints;
    }

    /**
     * Sets the maximum hitpoints of a GameObject
     * 
     * @param maxHitpoints The maximum hitpoints
     * 
     * @post new.maxHitpoints == maxHitpoints
     */
    private void setMaxHitpoints(int maxHitpoints) {
	this.maxHitpoints = maxHitpoints;
    }

    /**
     * Returns true when the given orientation is a valid orientation
     * 
     * @param orientation The orientation to check
     */
    public static boolean isValidOrientation(int orientation) {
	return orientation == -1 || orientation == 0 || orientation == 1;

    }

    /**
     * Returns the orientation of a GameObject.
     */
    @Basic
    public int getOrientation() {
	return orientation;
    }

    /**
     * Sets the orientation of a given GameObject.
     * 
     * @param orientation The orientation that needs to be set
     * 
     * @pre The entered orientation is valid | isValidOrientation(orientation)
     * 
     * @post The orientation of this GameObject is equal to the given orientation. |
     *       new.getOrientation() == orientation
     */
    public void setOrientation(int orientation) {
	assert isValidOrientation(orientation);
	this.orientation = orientation;
    }

    /**
     * A variable to store the orientation of a GameObject
     */
    protected int orientation;

    /**
     * Returns the horizontal speed of the GameObject given in meters per second.
     */
    @Basic
    public double getHorizontalSpeedMeters() {
	return horizontalSpeed;
    }

    /**
     * Returns the horizontal speed of the GameObject given in pixels per second
     */
    public int getHorizontalSpeedPixels() {
	return (int) (horizontalSpeed * 100);
    }

    /**
     * Sets the the horizontal speed of GameObject to the given speed.
     * 
     * @param speed The horizontal speed to give to GameObject
     * @post The new speed of GameObject is equal to the given speed if the given
     *       speed is 0. | if (speed == 0) then new.horizontalSpeed == 0
     * @post The new speed of GameObject is equal to the given speed if the speed an
     *       allowed speed. | if (Math.abs(speed) > getMinSpeedMeters() &&
     *       Math.abs(speed) < getMaxSpeedMeters()) then new.horizontalSpeed ==
     *       speed
     * @post The new speed of GameObject is equal to the minimal speed if the speed
     *       is positive and less than the allowed minimum speed. | if (speed <
     *       getMinSpeedMeters() && speed > 0) then new.horizontalSpeed ==
     *       this.getMinSpeedMeters()
     * @post The new speed of GameObject is equal to the minimal speed times -1 if
     *       the speed is negative and less than the allowed minimum speed in
     *       absolute value. | if (speed < getMinSpeedMeters() && speed < 0) then
     *       new.horizontalSpeed == (this.getMinSpeedMeters())*-1
     * @post The new speed of GameObject is equal to the maximal speed if the speed
     *       is positive and greater than the allowed maximum speed. | if (speed >
     *       getMaxSpeedMeters() && speed > 0) then new.horizontalSpeed ==
     *       this.getMaxSpeedMeters()
     * @post The new speed of GameObject is equal to the maximal speed times -1 if
     *       the speed is negative and greater than the allowed maximum speed in
     *       absolute value. | if (speed > getMaxSpeedMeters() && speed < 0) then
     *       new.horizontalSpeed == (this.getMaxSpeedMeters())*-1
     */
    public void setHorizontalSpeedMeters(double speed) {
	if (Math.abs(speed) >= getMinSpeedMeters() && Math.abs(speed) <= getMaxSpeed())
	    horizontalSpeed = speed;
	else if (Math.abs(speed) <= getMinSpeedMeters() && speed < 0)
	    horizontalSpeed = -1 * getMinSpeedMeters();
	else if (Math.abs(speed) <= getMinSpeedMeters() && speed > 0)
	    horizontalSpeed = getMinSpeedMeters();
	else if (speed == 0)
	    horizontalSpeed = 0;
	else if (Math.abs(speed) > getMaxSpeedRunningMeters() && speed > 0)
	    horizontalSpeed = getMaxSpeedRunningMeters();
	else
	    horizontalSpeed = -1 * getMaxSpeedRunningMeters();
    }

    /**
     * A variable to store the horizontal speed of the GameObject
     */
    private double horizontalSpeed;

    /**
     * Returns the minimum horizontal speed in meters per second.
     */
    @Basic
    @Immutable
    public double getMinSpeedMeters() {
	return minSpeed;
    }

    /**
     * Returns the minimal horizontal speed in pixels per second.
     */
    @Immutable
    public int getMinSpeedPixels() {
	return (int) (minSpeed * 100);
    }

    /**
     * Returns the maximal horizontal speed in meters per second while running.
     */
    @Basic
    @Immutable
    public double getMaxSpeedRunningMeters() {
	return maxSpeedRunning;
    }

    /**
     * Returns the maximal horizontal speed in pixels per second while running.
     */
    @Immutable
    public int getMaxSpeedRunningPixels() {
	return (int) (maxSpeedRunning * 100);
    }

    /**
     * Returns the maximal horizontal speed in meters per second while ducking.
     */
    @Basic
    @Immutable
    public double getMaxSpeedDuckingMeters() {
	return maxSpeedDucking;
    }

    /**
     * Returns the maximal horizontal speed in pixels per second while ducking.
     */
    @Immutable
    public int getMaxSpeedDuckingPixels() {
	return (int) (maxSpeedDucking * 100);
    }

    /**
     * A function that returns the maximum vertical speed of the GameObject in
     * meters per second
     */
    @Basic
    @Immutable
    public double getMaxVerticalSpeedMeters() {
	return maxVerticalSpeed;
    }

    public void setMaxVerticalSpeed(double maxVerticalSpeed) {
	this.maxVerticalSpeed = maxVerticalSpeed;

    }

    /**
     * Returns the vertical speed of a given GameObject in meters per second.
     */
    @Basic
    @Immutable
    public double getVerticalSpeedMeters() {
	return verticalSpeed;
    }

    /**
     * Sets the vertical speed to the given speed
     * 
     * @param verticalSpeedMeters The vertical speed that needs to be set
     * @post If the absolute value of the given speed is less than the maximum speed
     *       then the new speed is equal to the given speed | if
     *       Math.abs(verticalSpeedMeters) <= getMaxVerticalSpeedMeters() then
     *       new.verticalSpeed = verticalSpeedMeters
     * @post If the given speed is equal to the maximum speed in magnitude and the
     *       given speed is positive then the new speed is the maximum speed | if
     *       Math.abs(verticalSpeedMeters) = getMaxVerticalSpeedMeters() &&
     *       verticalSpeedMeters < 0 then new.verticalSpeed =
     *       getMaxVerticalSpeedMeters()
     * @post If the given speed is equal to the maximum speed in magnitude and the
     *       given speed is negative then the new speed is the negative maximum
     *       speed | if Math.abs(verticalSpeedMeters) = getMaxVerticalSpeedMeters()
     *       && verticalSpeedMeters > 0 then new.verticalSpeed = -1*
     *       getMaxVerticalSpeedMeters()
     */
    public void setVerticalSpeedMeters(double verticalSpeedMeters) {
	if (verticalSpeedMeters <= getMaxVerticalSpeedMeters())
	    verticalSpeed = verticalSpeedMeters;
	else
	    verticalSpeed = getMaxVerticalSpeedMeters();
    }

    /**
     * Initiate the maximum and minimum velocities
     */
    protected final double minSpeed;

    protected final double maxSpeedRunning;

    protected final double maxSpeedDucking;

    /**
     * Returns whether the given speed is a valid speed
     */
    public boolean isValidHorizontalSpeed() {
	if (getHorizontalSpeedMeters() != 0)
	    if (Math.abs(getHorizontalSpeedMeters()) < getMinSpeedMeters()
		    || Math.abs(getHorizontalSpeedMeters()) > getMaxSpeed())
		return false;
	return true;
    }

    /**
     * A variable to store the vertical speed of the GameObject
     */
    protected double verticalSpeed;

    /**
     * A variable to store the maximum vertical speed of the GameObject
     */
    protected double maxVerticalSpeed;

    /**
     * Returns whether the given speed is a valid vertical speed
     */
    public boolean isValidVerticalSpeed() {
	if (getVerticalSpeedMeters() > getMaxVerticalSpeedMeters())
	    return false;
	return true;
    }

    /**
     * A boolean to store if the GameObject is ducking
     */
    public boolean isDucking;

    /**
     * Returns the maximum horizontal speed for the given state of the GameObject
     * depending on the state
     */
    public double getMaxSpeed() {
	setMaxSpeed();
	return maxSpeed;
    }

    /**
     * Sets the maximum horizontal speed for the given state of GameObject
     * 
     * @post if GameObject is ducking the new maximum speed is equal to the maximum
     *       speed while ducking | if (isDucking()) then new.maxSpeed =
     *       this.maxSpeedDuckingMeters
     * @post if GameObject is not ducking the new maximum speed is equal to the
     *       maximum speed while running | if (!isDucking()) then new.maxSpeed =
     *       this.maxSpeedRunningMeters
     */
    protected void setMaxSpeed() {
	if (isDucking)
	    maxSpeed = maxSpeedDucking;
	else
	    maxSpeed = maxSpeedRunning;
    }

    /**
     * A variable to store the maximum speed of the GameObject depending on the
     * state (ducking or running)
     */
    private double maxSpeed;

    /**
     * Returns the horizontal acceleration in meters per second squared.
     */
    public double getHorizontalAcceleration() {
	return horizontalAcceleration;
    }

    /**
     * Sets the horizontal acceleration to the given acceleration in meters per
     * second squared.
     * 
     * @param horizontalAcceleration The acceleration to set in meters per second
     * 
     * @post The new horizontal acceleration is equal to the given horizontal
     *       acceleration | new.getHorizontalAcceleration() ==
     *       horizontalAcceleration
     */
    public void setHorizontalAcceleration(double horizontalAcceleration) {
//	if (Math.abs(horizontalAcceleration) == 0 || Math.abs(horizontalAcceleration) == maxHorizontalAcceleration)
//	    this.horizontalAcceleration = horizontalAcceleration;
	this.horizontalAcceleration = horizontalAcceleration;
    }

    /**
     * A variable to store the horizontal acceleration of the GameObject
     */
    private double horizontalAcceleration;

    /**
     * A variable to store the maximum horizontal acceleration of the GameObject
     */
    protected final static double maxHorizontalAcceleration = 0.9;

    /**
     * Returns the vertical acceleration in meters per second squared.
     */
    public double getVerticalAcceleration() {
	return verticalAcceleration;
    }

    /**
     * Sets the vertical acceleration to the given acceleration in meters per second
     * squared.
     * 
     * @param verticalAcceleration The acceleration to set in meters per second
     * 
     * @post The new vertical acceleration is equal to the given vertical
     *       acceleration | new.getVerticalAcceleration() == verticalAcceleration
     */
    public void setVerticalAcceleration(double verticalAcceleration) {
	if (verticalAcceleration == 0 || verticalAcceleration == maxVerticalAcceleration)
	    this.verticalAcceleration = verticalAcceleration;
    }

    /**
     * A variable to store the vertical acceleration of the GameObject
     */
    private double verticalAcceleration;

    /**
     * A variable to store the maximum vertical acceleration of the GameObject
     */
    protected final static double maxVerticalAcceleration = -10.0;

    /**
     * Returns whether the given alien is valid
     */
    public boolean isValidGameObject() {
	if (!isValidActualXPosition(xPosMeter) || !isValidActualYPosition(yPosMeter)
		|| !isValidPixelXPosition(xPosPixel) || !isValidPixelYPosition(yPosPixel))
	    return false;
	if (!isValidHorizontalSpeed() || !isValidVerticalSpeed())
	    return false;
	if (spriteArray == null)
	    return false;
	if (isTerminated())
	    return false;
	return true;
    }

    /**
     * Returns whether a given GameObeject collides with this GameObject
     * 
     * @param other The other GameObject
     * 
     * @return !(other.getXPositionPixel() + (other.getXsize() - 1) <
     *         this.getXPositionPixel()) && !(this.getXPositionPixel() +
     *         (this.getXsize() - 1) < other.getXPositionPixel()) &&
     *         !(this.getYPositionPixel() + (this.getYsize() - 1) <
     *         other.getYPositionPixel()) && !(other.getYPositionPixel() +
     *         (other.getYsize() - 1) < this.getYPositionPixel())
     */
    public boolean collidesWith(GameObject other) {

	if (other.getXPositionPixel() + other.getXsize() - 1 < getXPositionPixel())
	    return false;
	if (getXPositionPixel() + getXsize() - 1 < other.getXPositionPixel())
	    return false;
	if (getYPositionPixel() + getYsize() < other.getYPositionPixel())
	    return false;
	if (other.getYPositionPixel() + other.getYsize() - 1 < getYPositionPixel())
	    return false;
	return true;
    }

    /**
     * A variable to store the world of the GameObject
     */
    World world;

    /**
     * Returns the world of the GameObject
     */
    @Basic
    public World getWorld() {
	return world;
    }

    /**
     * Sets the world of the GameObject to the given world
     * 
     * @param world The world to set
     * 
     * @post this.world == world
     */
    public void setWorld(World world) {
	this.world = world;
	world.addGameObject(this);
    }

    public abstract void advanceTime(double dt, double timeStep);

    /**
     * Returns a list with all the tiles that are overlapping with the GameObject
     */
    public List<int[]> getAllOverlappingTiles() {
	final int xPosition = getXPositionPixel();
	final int yPosition = getYPositionPixel();
	final int xSize = getXsize();
	final int ySize = getYsize();
	final int tileLength = getWorld().getTileLength();

	final List<int[]> overlappingTiles = new ArrayList<>();

	final int[] tileBottomLeft = new int[] { xPosition / tileLength, yPosition / tileLength };
	final int[] tileTopRight = new int[] { (xPosition + xSize) / tileLength, (yPosition + ySize - 1) / tileLength };

	for (int i = tileBottomLeft[0]; i <= tileTopRight[0]; i++)
	    for (int j = tileBottomLeft[1]; j <= tileTopRight[1]; j++)
		overlappingTiles.add(new int[] { i, j });

	return overlappingTiles;
    }

    /**
     * Returns whether the GameObject is standing on impassable terrain
     */
    public boolean isStandingOnImpassableTerrain() {
	if (getWorld() == null)
	    return false;

	for (final ImpassableTerrain feature : ImpassableTerrain.values())
	    for (int x = getXPositionPixel(); x < getXPositionPixel() + getXsize(); x++) {
		if (getWorld().getGeologicalFeature(x, getYPositionPixel()) == feature.getValue())
		    return true;
		if (getWorld().getGeologicalFeature(x, getYPositionPixel() - 1) == feature.getValue())
		    return true;
	    }

	final GameObject newObject = new Mazub(getXPositionActual(), getYPositionActual() - 0.01, 1, 1, 0, 0, 0, 0,
		false, getSpriteArray());
	if (!getWorld().canPlaceGameObjectAdvanceTime(newObject, this))
	    return true;

	return false;
    }

    /**
     * Makes a GameObject fall
     * 
     * @post The new acceleration is equal to the given maximum vertical
     *       acceleration. | new.VerticalAcceleration == maxVerticalAcceleration
     */
    public void fall() {
	if (getYPositionActual() > 0 || getWorld() == null)
	    setVerticalAcceleration(maxVerticalAcceleration);
	if (getWorld() != null)
	    for (final ImpassableTerrain feature : ImpassableTerrain.values())
		for (int x = getXPositionPixel(); x < getXPositionPixel() + getXsize(); x++)
		    if (getWorld().getGeologicalFeature(x, getYPositionPixel()) == feature.getValue())
			setVerticalAcceleration(0);
	if (getYPositionActual() < 0 && getVerticalSpeedMeters() < 0)
	    setVerticalAcceleration(0);
    }

    /**
     * Returns whether a given ID has been taken
     * 
     * @param id The ID to check
     */
    public static boolean hasSlimeWithID(long id) {
	if (getAllIDs().contains(id))
	    return true;
	return false;
    }

    /**
     * A set to store all IDs that are taken.
     */
    protected static Set<Long> allIDs = new HashSet<Long>();

    /**
     * Returns a set with all IDs that are taken.
     */
    private static Set<Long> getAllIDs() {
	return allIDs;
    }

    /**
     * Checks if the array of sprites is valid for the GameObject
     * 
     * @param sprites The sprites of the GameObject
     * 
     * 
     * @post ... | sprites.length > 0
     * @post ... | for sprite in sprites : sprite.isValidSprite()
     */
    public boolean isValidSpriteArray(Sprite[] sprites) {
	if (sprites.length <= 0)
	    return false;
	for (final Sprite sprite : sprites)
	    if (!sprite.canHaveAsHeight(sprite.getHeight()) || !sprite.canHaveAsName(sprite.getName())
		    || !sprite.canHaveAsWidth(sprite.getWidth()))
		return false;
	return true;
    }

}
