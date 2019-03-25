package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import jumpingalien.util.Sprite;

/**
 * A class that implements a player character with the ability to jump, to run to the left and to the right.
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 * @invar The left most x coordinate of GameObject is a valid coordinate
 * 			| isValidXPosition() == true
 * @invar The bottom most y coordinate of GameObject is a valid coordinate
 * 			| isValidYPosition() == true
 * @invar The horizontal speed of a GameObject is a valid speed
 * 			| isValidHorizontalSpeed() == true
 * @invar The vertical speed of a GameObject is a valid speed
 * 			| isValidVerticalSpeed() == true
 * @invar The GameObject is valid
 * 			| isValidGameObject() == true
 * @invar The GameObject has a valid orientation
 * 			| isValidOrientation() == true
 * @invar The sprites are valid sprites
 * 			| isValidSpriteArray() == true
 * @invar The sprites in an array are all valid
 * 			| isValidSprite() == true
 */
public abstract class GameObject {
	
	/**
	 * Creates a new GameObject
	 * 
	 * @param pixelLeftX
	 * 			The leftmost pixel of the GameObject
	 * @param pixelBottomY
	 * 			The bottom-most pixel of the GameObject
	 * @param pixelSizeX
	 * 			The width of the GameObject in pixels
	 * @param pixelSizeY
	 * 			The height of the GameObject in pixels.
	 * @param hitpoints
	 * 			The hitpoints of the GameObject
	 * @param maxHorizontalSpeedRunning
	 * 			The maximal horizontal speed of GameObject while running given in meters/second
	 * @param maxHorizontalSpeedDucking
	 * 			The maximal horizontal speed of GameObejct while ducking given in meters/second
	 * @param minHorizontalSpeed
	 * 			The minimal horizontal speed of GameObject given in meters/second
	 * @param maxVerticalSpeed
	 * 			The maximum vertical speed of a GameObject
	 * @param horizontalAcceleration
	 * 			The horizontal acceleration of a GameObject
	 * @param verticalAcceleration
	 * 			The vertical acceleration of a GameObject
	 * @param sprites
	 * 			The sprites needed to represent the GameObject
	 * 
	 * @effect setYSize(pixelSizeY) && setXSize(pixelSizeX) && setXPositionPixel(pixelLeftX) && setYPositionPixel(pixelBottomY)
	 * 			&& setSprite(sprites[0]) && setSpriteArray(sprites) && setHitpoints(hitpoints) && setOrientation(0)
	 * 			&& setHorizontalSpeedMeters(0) && setVerticalAcceleration(0) && setHorizontalAcceleration(0)
	 * 
	 * @post ...
	 * 		| this.maxHorizontalAcceleration == horizontalAcceleration
	 * @post ...
	 * 		| this.maxVerticalAcceleration == verticalAcceleration
	 * @post ...
	 * 		| this.minSpeed == minHorizontalSpeed
	 * @post ...
	 * 		| this.maxSpeedDucking == maxHorizontalSpeedDucking
	 * @post ...
	 * 		| this.maxSpeedRunning == maxHorizontalSpeedRunning
	 * @post ...
	 * 		| this.maxVerticalSpeed == maxVerticalSpeed
	 */
	GameObject(int pixelLeftX, int pixelBottomY, int pixelSizeX, int pixelSizeY, int hitpoints, double maxHorizontalSpeedRunning, 
			double maxHorizontalSpeedDucking, double minHorizontalSpeed, double maxVerticalSpeed, double horizontalAcceleration, double verticalAcceleration, 
			Sprite... sprites){
		setYSize(pixelSizeY);
		setXSize(pixelSizeX);
		setXPositionActual((double)pixelLeftX/100);
		setYPositionActual((double)pixelBottomY/100);
		setSprite(sprites[0]);
		setSpriteArray(sprites);
		setHitpoints(hitpoints);
		setMaxSpeed();
		setOrientation(0);
		setHorizontalSpeedMeters(0);
		this.maxHorizontalAcceleration = horizontalAcceleration;
		this.maxVerticalAcceleration = verticalAcceleration;
		this.minSpeed = minHorizontalSpeed;
		this.maxSpeedDucking = maxHorizontalSpeedDucking;
		this.maxSpeedRunning = maxHorizontalSpeedRunning;
		this.maxVerticalSpeed = maxVerticalSpeed;
		setHorizontalAcceleration(0);
		setVerticalAcceleration(0);
		
	}
	
	/**
	 * Sets the current sprite to the given sprite
	 * @param sprite The sprite to set
	 * 
	 * @post The current sprite of this GameObject is the given sprite
	 * 		| this.sprite == sprite
	 */
	protected void setSprite(Sprite sprite) {
		if (!isValidSprite(sprite))
			throw new RuntimeException();	
		this.sprite = sprite;
		
	}
	
	/**
	 * Sets the sprite array to a given array
	 * @param sprites The array to which to set
	 * 
	 * @post The new spriteArray is equal to the given one
	 * 		| new.getSpriteArray == sprites
	 */
	private void setSpriteArray (Sprite ... sprites) {
		this.spriteArray = sprites.clone();
	}
	
	/**
	 * Returns the sprite array of a GameObject
	 */
	public Sprite[] getSpriteArray() {
		return this.spriteArray.clone();
	}
	
	/**
	 * A variable to store all the sprites of a GameObject
	 */
	protected Sprite[] spriteArray;
 	
	/**
	 * Returns the current sprite.
	 */
	@Basic
	public Sprite getCurrentSprite() {
		return this.sprite;	
	}
	
	/**
	 * Returns whether the given sprite is valid
	 * 
	 * @param sprite The sprite to check
	 */
	public boolean isValidSprite(Sprite sprite) {
		return (sprite.canHaveAsHeight(sprite.getHeight()) &&
				sprite.canHaveAsWidth(sprite.getWidth()) &&
				sprite.canHaveAsName(sprite.getName()));
	}

	/**
	 * A variable to store the current sprite.
	 */
	protected Sprite sprite;
	
	/**
	 * Check whether this object is terminated.
	 */
	@Basic
	@Raw
	public boolean isTerminated() {
		return this.isTerminated;
	}
	
	/**
	 * Check whether this object is dead.
	 */
	@Basic
	@Raw
	public boolean isDead() {
		return this.isDead;
	}
	
	/**
	 * Terminates the GameObject
	 * 
	 * @post isTerminated()
	 * @effect setHitpoints(0) && removeWorld()
	 */
	public void terminate() {
		if (!isTerminated()) {
			this.isTerminated = true;
			this.setHitpoints(0);
			this.removeWorld();
			
		}
	}
	
	/**
	 * Removes the world of the GameObject
	 * 
	 * @post this.world == null
	 */
	private void removeWorld() {
		if (this.world != null) {
			this.world.removeObject(this);
			this.world = null;
		}
		
	}
	
	/**
	 * A boolean to store if the GameObject is terminated
	 */
	private boolean isTerminated;
	
	/**
	 * A boolean to store if the GameObject is dead
	 */
	private boolean isDead;
	
	
	/**
	 * Sets the y size of GameObject to the given amount of pixels.
	 * @param Y_size
	 * 			The wanted amount of pixels for the y size of GameObject.
	 * @post the new y size is equal to the specified amount of pixels
	 * 		| (this.ySize == Y_size)
	 */
	private void setYSize(int Y_size) {
		this.ySize = Y_size;
	}
	
	/**
	 * Returns the y size of a given GameObject in pixels.
	 */
	@Basic
	public int getYsize() {
		return this.ySize;
	}
	
	/**
	 * A variable to store the y size of a GameObject in pixels.
	 */
	private int ySize;
	
	/**
	 * Sets the x size of GameObject to the given amount of pixels.
	 * @param X_size
	 * 			The wanted amount of pixels for the x size of GameObject.
	 * @post the new x size is equal to the specified amount of pixels
	 * 		| (this.xSize == X_size)
	 */
	private void setXSize(int X_size) {
		this.xSize = X_size;
	}
	
	/**
	 * Returns the x size of a given GameObject in pixels.
	 */
	@Basic
	public int getXsize() {
		return this.xSize;
	}
	
	/**
	 * A variable to store the x size of a GameObject in pixels.
	 */
	private int xSize;
	
	/**
	 * Sets the x position of the bottom left pixel of a given GameObject to the specified x position.
	 * @param X_pos
	 * 			The wanted x position.
	 * 
	 * @post this.xPosMeter == X_pos
	 * @post this.xPosPixel == (int) (X_pos * 100)
	 */
	public void setXPositionActual(double X_pos) {
		this.xPosPixel = (int) (X_pos * 100);
		this.xPosMeter = X_pos;
		if (!isValidActualXPosition(this.xPosMeter))
			terminate();
		if (this.getWorld() != null && this.getWorld().getWorldSizeX() < this.getXPositionPixel())
			terminate();
	}
	
	/**
	 * Returns whether the given coordinate is on the canvas.
	 * @param X_pos
	 * 		The coordinate to check in pixels.
	 */
	public boolean isValidPixelXPosition(int X_pos) {
		return (X_pos >= 0 && X_pos <= Double.POSITIVE_INFINITY);
	}
	
	/**
	 * Returns whether the given coordinate is on the canvas.
	 * @param X_pos
	 * 			The coordinate to check in meters.
	 */
	public boolean isValidActualXPosition(double X_pos) {
		return (X_pos <= Double.POSITIVE_INFINITY &&  X_pos >= 0);		
	}
	
	/**
	 * Returns the x position on the canvas in pixels.
	 */
	@Basic 
	public int getXPositionPixel() {
		return this.xPosPixel;
	}
	
	/**
	 * Returns the maximal x position of the world in pixels.
	 */
	@Basic 
	public int getMaxXPosition() {
		return this.getWorld().getWorldSizeX();
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
	 * Sets the y position of the bottom left pixel of a given GameObject to the specified y position.
	 * @param Y_pos
	 * 			The wanted y position.
	 * 
	 * @post this.yPosMeter == Y_pos
	 * @post this.yPosPixel == (int) (Y_pos * 100)
	 */
	public void setYPositionActual(double Y_pos){
		this.yPosPixel = (int) (Y_pos * 100);
		this.yPosMeter = Y_pos;
		if (!isValidActualYPosition(this.xPosMeter)) 
			terminate();
		if (this.getWorld() != null && this.getWorld().getWorldSizeY() < this.getYPositionPixel())
			terminate();

	}
	
	/**
	 * Returns whether the given coordinate is on the canvas.
	 * @param Y_pos
	 * 			The given coordinate to check in meters.
	 */
	public boolean isValidActualYPosition(double Y_pos) {
		return (Y_pos <= Double.POSITIVE_INFINITY &&  Y_pos >= 0);
	}
	
	/**
	 * Returns whether the given coordinate is on the canvas.
	 * @param Y_pos
	 * 			The given coordinate to check in pixels.
	 */
	public boolean isValidPixelYPosition(int Y_pos) {
		return (Y_pos >= 0 && Y_pos <= Double.POSITIVE_INFINITY);
	}
	
	/**
	 * Returns the maximal y position of the world in pixels.
	 */
	@Basic
	public int getMaxYPosition() {
		return this.world.getWorldSizeY();
	}
	
	/**
	 * Returns the y position on the canvas in pixels.
	 */
	@Basic 
	public int getYPositionPixel() {
		return this.yPosPixel;
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
	 * Sets the hitpoints of the GameObject
	 * 
	 * @param hitpoints The hitpoints to set
	 * 
	 * @post if hitpoints < 0 then this.hitpoints == 0
	 * @post if hitpoints > 500 then this.hitpoints == 500
	 * @post if 0 < hitpoints < 500 then this.hitpoints == hitpoints
	 */
	protected void setHitpoints(int hitpoints) {
		if (hitpoints < 0)
			this.hitpoints = 0;
		if (hitpoints > 500)
			this.hitpoints = 500;
		else
			this.hitpoints = hitpoints;	
	}
	
	/**
	 * Returns the x position on the canvas in meters.
	 */
	public double getXPositionActual() {
		return this.xPosMeter;
	}
	
	/**
	 * Returns the y position on the canvas in meters.
	 */
	public double getYPositionActual() {
		return this.yPosMeter;
	}
	
	/**
	 * Returns the hitpoints
	 */
	@Basic
	public int getHitpoints() {
		return this.hitpoints;
	}
	
	/**
	 * A variable to store the hitpoints
	 */
	private int hitpoints;
	
	private void changeHitPoints(int change) {
		setHitpoints(getHitpoints() + change);
	}
	
	/**
	 * Sets the orientation of a given GameObject.
	 * 
	 * @param orientation
	 * 			The orientation that needs to be set
	 * 
	 * @pre The entered orientation is valid
	 * 		| isValidOrientation(orientation)
	 * 
	 * @post The orientation of this GameObject is equal to the given
	 *       orientation.
 	 *       | new.getOrientation() == orientation
	 */
	public void setOrientation(int orientation) {
		assert isValidOrientation();
		this.orientation = orientation;
	}
	
	/**
	 * Returns true when the given orientation is a valid orientation
	 * 
	 * @param orientation
	 * 			The orientation to check
	 */
	public boolean isValidOrientation() {
		return getOrientation() == -1 || getOrientation() == 0 || getOrientation() == 1;
		
	}
	
	/**
	 * Returns the orientation of a GameObject.
	 */
	@Basic
	public int getOrientation() {
		return this.orientation;
	}
	 /**
	  * Returns the orientation of the GameObject. -1 if the GameObject is oriented to the left, 0 if the GameObject is oriented to the front, 1 if the GameObject is oriented to the right
	  */
	protected int orientation;
	
	/**
	 * Returns the horizontal speed of the GameObject given in meters per second.
	 */
	@Basic
	public double getHorizontalSpeedMeters() {
		return this.horizontalSpeed;
	}
	/**
	 * Returns the horizontal speed of the GameObject given in pixels per second
	 */
	public int getHorizontalSpeedPixels() {
		return (int) (this.horizontalSpeed * 100);
	}
	
	
	/**
	 * A variable to store the horizontal speed of the GameObject
	 */
	private double horizontalSpeed;
	
	/**
	 *  Sets the the horizontal speed of GameObject to the given speed.
	 *  
	 * @param speed
	 * 			The horizontal speed to give to GameObject
	 * @post The new speed of GameObject is equal to the given speed if the given speed is 0.
	 * 			| if (speed == 0) then new.horizontalSpeed == 0
	 * @post The new speed of GameObject is equal to the given speed if the speed an allowed speed.
	 * 			| if (Math.abs(speed) > getMinSpeedMeters() && Math.abs(speed) < getMaxSpeedMeters()) then new.horizontalSpeed == speed 
	 * @post The new speed of GameObject is equal to the minimal speed if the speed is positive and less than the allowed minimum speed.
	 * 			| if (speed < getMinSpeedMeters() && speed > 0) then new.horizontalSpeed == this.getMinSpeedMeters() 
	 * @post The new speed of GameObject is equal to the minimal speed times -1 if the speed is negative and less than the allowed minimum speed in absolute value.
	 * 			| if (speed < getMinSpeedMeters() && speed < 0) then new.horizontalSpeed == (this.getMinSpeedMeters())*-1
	 * @post The new speed of GameObject is equal to the maximal speed if the speed is positive and greater than the allowed maximum speed.
	 * 			| if (speed > getMaxSpeedMeters() && speed > 0) then new.horizontalSpeed == this.getMaxSpeedMeters() 
	 * @post The new speed of GameObject is equal to the maximal speed times -1 if the speed is negative and greater than the allowed maximum speed in absolute value.
	 * 			| if (speed > getMaxSpeedMeters() && speed < 0) then new.horizontalSpeed == (this.getMaxSpeedMeters())*-1
	 */
	protected void setHorizontalSpeedMeters(double speed) {
		if(Math.abs(speed)>= getMinSpeedMeters() && Math.abs(speed)<= getMaxSpeed())
			this.horizontalSpeed = speed;
		else if (Math.abs(speed) <= getMinSpeedMeters() && speed < 0)
			this.horizontalSpeed = -1*getMinSpeedMeters();
		else if (Math.abs(speed) <= getMinSpeedMeters() && speed > 0)
			this.horizontalSpeed = getMinSpeedMeters();
		else if (speed == 0)
			this.horizontalSpeed = 0;
		else if (Math.abs(speed) > getMaxSpeedRunningMeters() && speed > 0)
			this.horizontalSpeed = getMaxSpeedRunningMeters();
		else
			this.horizontalSpeed = -1*getMaxSpeedRunningMeters();
	}
	
	/**
	 * Initiate the maximum  and minimum velocities
	 */
	protected final double minSpeed;
	protected final double maxSpeedRunning;
	protected final double maxSpeedDucking;
	
	
	/**
	 * Returns the minimum horizontal speed in meters per second.
	 */
	@Basic @Immutable
	public double getMinSpeedMeters() {
		return minSpeed;
	}
	
	/**
	 * Returns the minimal horizontal speed in pixels per second.
	 */
	@Immutable
	public int getMinSpeedPixels() {
		return (int) (this.minSpeed * 100);
	}
	
	/**
	 * Returns the maximal horizontal speed in meters per second while running.
	 */
	@Basic @Immutable	
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
	@Basic @Immutable
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
	 * Returns whether the given speed is a valid speed
	 */
	protected boolean isValidHorizontalSpeed() {
		if (this.getHorizontalSpeedMeters() != 0) 
				if ( Math.abs(this.getHorizontalSpeedMeters()) < this.getMinSpeedMeters()
				 || Math.abs(this.getHorizontalSpeedMeters()) > this.getMaxSpeed()) {
			return false;}
		return true;
	}
	
	/**
	 * A variable to store the vertical speed of the GameObject
	 */
	protected double verticalSpeed;
	
	/**
	 * Sets the vertical speed to the given speed
	 * @param verticalSpeedMeters
	 * 			The vertical speed that needs to be set
	 * @post If the absolute value of the given speed is less than the maximum speed then the new speed is equal to the given speed
	 * 		| if Math.abs(verticalSpeedMeters) <= getMaxVerticalSpeedMeters() then new.verticalSpeed = verticalSpeedMeters
	 * @post If the given speed is equal to the maximum speed in magnitude  and the given speed is positive then the new speed is the maximum speed
	 * 		| if Math.abs(verticalSpeedMeters) = getMaxVerticalSpeedMeters() && verticalSpeedMeters < 0 then new.verticalSpeed = getMaxVerticalSpeedMeters()
	 * @post If the given speed is equal to the maximum speed in magnitude  and the given speed is negative then the new speed is the negative maximum speed 
	 * 		| if Math.abs(verticalSpeedMeters) = getMaxVerticalSpeedMeters() && verticalSpeedMeters > 0 then new.verticalSpeed = -1* getMaxVerticalSpeedMeters()
	 */
	protected void setVerticalSpeedMeters(double verticalSpeedMeters) {
		if( verticalSpeedMeters <= getMaxVerticalSpeedMeters())
			this.verticalSpeed = verticalSpeedMeters;
		else this.verticalSpeed = getMaxVerticalSpeedMeters();
	}
	
	/**
	 * A variable to store the maximum vertical speed of the GameObject
	 */
	protected final double maxVerticalSpeed;
	
	/**
	 * A function that returns the maximum vertical speed of the GameObject in meters per second
	 */
	@Basic @Immutable
	public double getMaxVerticalSpeedMeters() {
		return maxVerticalSpeed;
	}
	
	/**
	 * Returns the vertical speed of a given GameObject in meters per second.
	 */
	@Basic @Immutable
	public double getVerticalSpeedMeters() {
		return this.verticalSpeed;
	}
	
	/**
	 * Returns whether the given speed is a valid vertical speed
	 */
	protected boolean isValidVerticalSpeed() {
		if (this.getVerticalSpeedMeters() > this.getMaxVerticalSpeedMeters())
			return false;
		return true;
	}
	
	/**
	 * A boolean to store if the GameObject is ducking
	 */
	public boolean isDucking;
	
	/**
	 * Sets the maximum horizontal speed for the given state of GameObject
	 * 
	 * @post if GameObject is ducking the new maximum speed is equal to the maximum speed while ducking
	 * 		| if (isDucking()) then new.maxSpeed = this.maxSpeedDuckingMeters
	 * @post if GameObject is not ducking the new maximum speed is equal to the maximum speed while running
	 * 		| if (!isDucking()) then new.maxSpeed = this.maxSpeedRunningMeters
	 */
	protected void setMaxSpeed() {
		if (this.isDucking)
			maxSpeed = maxSpeedDucking;
		else
			maxSpeed = maxSpeedRunning;
	}
	
	/**
	 * A variable to store the maximum speed of the GameObject depending on the state (ducking or running)
	 */
	private double maxSpeed;
	
	/**
	 * Returns the maximum horizontal speed for the given state of the GameObject depending on the state
	 */
	public double getMaxSpeed() {
		this.setMaxSpeed();
		return maxSpeed;
	}
	
	/**
	 * Returns the horizontal acceleration in meters per second squared.
	 */
	public double getHorizontalAcceleration() {
		return horizontalAcceleration;
	}
	
	
	
	/**
	 * Sets the horizontal acceleration to the given acceleration in meters per second squared.
	 * 
	 * @param horizontalAcceleration The acceleration to set in meters per second
	 * 
	 * @post The new horizontal acceleration is equal to the given horizontal acceleration
	 * 		| new.getHorizontalAcceleration() == horizontalAcceleration
	 */
	protected void setHorizontalAcceleration(double horizontalAcceleration) {
		if (Math.abs(horizontalAcceleration) == 0 || Math.abs(horizontalAcceleration) == maxHorizontalAcceleration)
			this.horizontalAcceleration =  horizontalAcceleration;	
	}
	
	/**
	 * A variable to store the horizontal acceleration of the GameObject
	 */
	private double horizontalAcceleration;
	
	/**
	 * A variable to store the maximum horizontal acceleration of the GameObject
	 */
	protected final double maxHorizontalAcceleration;
	
	/**
	 * Returns the vertical acceleration in meters per second squared.
	 */
	public double getVerticalAcceleration() {
		return verticalAcceleration;
	}
	
	/**
	 * Sets the vertical acceleration to the given acceleration in meters per second squared.
	 * 
	 * @param verticalAcceleration The acceleration to set in meters per second
	 * 
	 * @post The new vertical acceleration is equal to the given vertical acceleration
	 * 		| new.getVerticalAcceleration() == verticalAcceleration
	 */
	protected void setVerticalAcceleration(double verticalAcceleration) {
		if (verticalAcceleration == 0 || verticalAcceleration == maxVerticalAcceleration)
			this.verticalAcceleration =  verticalAcceleration;
	}
	
	/**
	 * A variable to store the vertical acceleration of the GameObject
	 */
	private double verticalAcceleration;
	
	/**
	 * A variable to store the maximum vertical accelration of the GameObject
	 */
	protected final double maxVerticalAcceleration;
	
	/**
	 * Returns whether the given alien is valid
	 */
	public boolean isValidGameObject() {
		if (!isValidActualXPosition(this.xPosMeter) || !isValidActualYPosition(this.yPosMeter)
				|| !isValidPixelXPosition(this.xPosPixel) || !isValidPixelYPosition(this.yPosPixel))
			return false;
		if (!isValidHorizontalSpeed() || !isValidVerticalSpeed())
			return false;
		if (this.spriteArray == null)
			return false;
		if (this.isDead())
			return false;
		if (this.isTerminated())
			return false;
		
		return true;
	}
	
	/**
	 * Returns whether a given GameObeject collides with this GameObject
	 * @param other The other GameObject
	 * 
	 * @return ((this.getXPositionPixel() + (this.getXsize() - 1) < other.getXPositionPixel()) 
	 * 			&& (this.getYPositionPixel() + (this.getYsize() - 1) < other.getYPositionPixel())
	 * 			&& (other.getXPositionPixel() + (other.getXsize() - 1) < this.getXPositionPixel())
	 * 			&& (other.getYPositionPixel() + (other.getYsize() - 1) < this.getYPositionPixel()))
	 */
	protected boolean collidesWith(GameObject other) {
//		if (!(this.getXPositionPixel() + (this.getXsize() - 1) < other.getXPositionPixel()))
//			return false;
//		if (!(this.getYPositionPixel() + (this.getYsize() - 1) < other.getYPositionPixel()))
//			return false;
//		if (!(other.getXPositionPixel() + (other.getXsize() - 1) < this.getXPositionPixel()))
//			return false;
//		if (!(other.getYPositionPixel() + (other.getYsize() - 1) < this.getYPositionPixel()))
//			return false;
//		return true;
		
		if (!(this.getXPositionPixel() + (this.getXsize() - 1) < other.getXPositionPixel())
				&& !(this.getYPositionPixel() + (this.getYsize() - 1) < other.getYPositionPixel())
						&& !(other.getXPositionPixel() + (other.getXsize() - 1) < this.getXPositionPixel())
							&& !(other.getYPositionPixel() + (other.getYsize() - 1) < this.getYPositionPixel()))
			return true;
		return false;
	}
	
	/**
	 * A variable to store the world of the GameObject
	 */
	World world;
	
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

	/**
	 * Returns the world of the GameObject
	 */
	@Basic
	public World getWorld() {
		return world;
	}

	protected abstract void advanceTime(double dt, double timeStep);


}
