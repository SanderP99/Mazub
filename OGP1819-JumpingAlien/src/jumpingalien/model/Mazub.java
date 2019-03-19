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
 * @invar The left most x coordinate of Mazub is a valid coordinate
 * 			| isValidXPosition() == true
 * @invar The bottom most y coordinate of Mazub is a valid coordinate
 * 			| isValidYPosition() == true
 * @invar The horizontal speed of a Mazub is a valid speed
 * 			| isValidHorizontalSpeed() == true
 * @invar The vertical speed of a Mazub is a valid speed
 * 			| isValidVerticalSpeed() == true
 * @invar The Mazub is valid
 * 			| isValidAlien == true
 * @invar The Mazub has a valid orientation
 * 			| isValidOrientation() == true
 * @invar The sprites are valid sprites
 * 			| isValidSpriteArray() == true
 * @invar The sprites in an array are all valid
 * 			| isValidSprite() == true
 */
public class Mazub{
	
	/**
	 * Initialize a new player with a given position (X_pos, Y_pos), a given size (X_size, Y_size), speed and sprites.
	 * .
	 * @param X_pos
	 * 			The x position of the bottom left pixel of Mazub.
	 * @param X_size
	 * 			The x size of Mazub given in pixels.
	 * @param Y_pos
	 * 			The y position of the bottom left pixel of Mazub.
	 * @param Y_size
	 * 			The y size of Mazub given in pixels.
	 * @param horizontalSpeedMeters
	 * 			The horizontal speed of Mazub given in meters/second
	 * @param minSpeedMeters
	 * 			The minimal horizontal speed of Mazub given in meters/second
	 * @param maxSpeedRunningMeters
	 * 			The maximal horizontal speed of Mazub while running given in meters/second
	 * @param maxSpeedDuckingMeters
	 * 			The maximal horizontal speed of Mazub while ducking given in meters/second
	 * @param sprites
	 * 			The sprites needed to represent Mazub
	 */
	public Mazub(double X_pos, double Y_pos, int X_size, int Y_size, double horizontalSpeedMeters, 
			double minSpeedMeters, double maxSpeedRunningMeters, double maxSpeedDuckingMeters, Sprite ... sprites) {
//		super (X_pos, Y_pos, X_size, Y_size);
		setYSize(Y_size);
		setXSize(X_size);
		setXPositionActual(X_pos);
		setYPositionActual(Y_pos);
		setHorizontalSpeedMeters(horizontalSpeedMeters);
		this.minSpeed = minSpeedMeters;
		this.maxSpeedDucking = maxSpeedDuckingMeters;
		this.maxSpeedRunning = maxSpeedRunningMeters;
		this.setSpriteArray(sprites);
		setSprite(sprites[0]);
		this.isDucking = false;
		setHitpoints(100);
	}
	
	/**
	 * Initialize a new player with a given position (X_pos, Y_pos), a given size (X_size, Y_size) and speed
	 * .
	 * @param X_pos
	 * 			The x position of the bottom left pixel of Mazub in pixels.
	 * @param X_size
	 * 			The x size of Mazub given in pixels.
	 * @param Y_pos
	 * 			The y position of the bottom left pixel of Mazub in pixels.
	 * @param Y_size
	 * 			The y size of Mazub given in pixels.
	 * @param horizontalSpeedMeters
	 * 			The horizontal speed of Mazub given in meters/second
	 * @param minSpeedMeters
	 * 			The minimal horizontal speed of Mazub given in meters/second
	 * @param maxSpeedRunningMeters
	 * 			The maximal horizontal speed of Mazub while running given in meters/second
	 * @param maxSpeedDuckingMeters
	 * 			The maximal horizontal speed of Mazub while ducking given in meters/second
	 * @param sprites
	 * 			The sprites needed to represent Mazub
	 */
	public Mazub(int X_pos, int Y_pos, int X_size, int Y_size, double horizontalSpeedMeters, double minSpeedMeters, double maxSpeedRunningMeters, double maxSpeedDuckingMeters, Sprite...sprites) {
		setYSize(Y_size);
		setXSize(X_size);
		setXPositionPixel(X_pos);
		setYPositionPixel(Y_pos);
		setHorizontalSpeedMeters(horizontalSpeedMeters);
		this.minSpeed = minSpeedMeters;
		this.maxSpeedDucking = maxSpeedDuckingMeters;
		this.maxSpeedRunning = maxSpeedRunningMeters;
		setSprite(sprites[0]);
		setOrientation(0);
		setSpriteArray(sprites);
		setHitpoints(100);
	}
	

	/**
	 * Initiate the size of the canvas in pixels
	 */
	private int maxXPosition = 1024;
	private int maxYPosition = 768;
	
	/**
	 * Sets the y size of Mazub to the given amount of pixels.
	 * @param Y_size
	 * 			The wanted amount of pixels for the y size of Mazub.
	 * @post the new y size is equal to the specified amount of pixels
	 * 		| (this.ySize == Y_size)
	 */
	private void setYSize(int Y_size) {
		this.ySize = Y_size;
	}
	
	/**
	 * Returns the y size of a given Mazub.
	 */
	@Basic
	public int getYsize() {
		return this.ySize;
	}
	
	private int ySize;
	
	/**
	 * Sets the x size of Mazub to the given amount of pixels.
	 * @param X_size
	 * 			The wanted amount of pixels for the x size of Mazub.
	 * @post the new x size is equal to the specified amount of pixels
	 * 		| (this.xSize == X_size)
	 */
	private void setXSize(int X_size) {
		this.xSize = X_size;
	}
	
	/**
	 * Sets the x position of the bottom left pixel of a given Mazub to the specified x pixel.
	 * @param X_pos
	 * 			The wanted x position in pixels.
	 * @throws RuntimeException
	 * 			Throws an exception when the wanted position isn't on the canvas.
	 * 			| !isValidXPosition(X_pos/100)
	 */
	public void setXPositionPixel(int X_pos) throws RuntimeException{
		if (!isValidPixelXPosition())
			throw new RuntimeException();
		this.xPosPixel = X_pos;
		this.xPosMeter = ((double) X_pos)/100;
	}
	
	/**
	 * Returns the x size of a given Mazub.
	 */
	@Basic
	public int getXsize() {
		return this.xSize;
	}
	
	private int xSize;
	
	/**
	 * Sets the x position of the bottom left pixel of a given Mazub to the specified x position.
	 * @param X_pos
	 * 			The wanted x position.
	 * @throws RuntimeException
	 * 			Throws an exception when the wanted position isn't on the canvas.
	 * 			| !isValidXPosition(X_pos)
	 */
	public void setXPositionActual(double X_pos) throws RuntimeException{
		if (!isValidActualXPosition(X_pos))
			throw new RuntimeException();
		this.xPosPixel = (int) (X_pos * 100);
		this.xPosMeter = X_pos;
	}
	
	/**
	 * Returns the maximal x position on the canvas in pixels.
	 */
	@Basic 
	public int getMaxXPosition() {
		return maxXPosition;
	}
	
	/**
	 * Returns the x position on the canvas in pixels.
	 */
	@Basic 
	public int getXPositionPixel() {
		return this.xPosPixel;
	}
	
	/**
	 * Returns the x position on the canvas in meters.
	 */
	public double getXPositionActual() {
		return this.xPosMeter;
	}
	
	/**
	 * Returns whether the given coordinate is on the canvas.
	 * @param X_pos
	 * 			The coordinate to check in meters.
	 */
	public boolean isValidActualXPosition(double X_pos) {
		return (X_pos*100 <= (getMaxXPosition() ) &&  X_pos >= 0);		
	}
	
	/**
	 * Returns whether the given coordinate is on the canvas.
	 */
	public boolean isValidPixelXPosition() {
		return (getXPositionPixel() >= 0 && getXPositionPixel() <= getMaxXPosition());
	}
	
	private int xPosPixel;
	private double xPosMeter;
	
	/**
	 * Sets the y position of the bottom left pixel of a given Mazub to the specified y position.
	 * @param Y_pos
	 * 			The wanted y position.
	 * @throws RuntimeException
	 * 			Throws an exception when the wanted position isn't on the canvas.
	 * 			| !isValidYPosition(Y_pos)
	 */
	public void setYPositionActual(double Y_pos) throws RuntimeException{
		if (!isValidActualYPosition(Y_pos)) 
			throw new RuntimeException();
		this.yPosPixel = (int) (Y_pos * 100);
		this.yPosMeter = Y_pos;
	}
	
	/**
	 * Sets the y position of the bottom left pixel of a given Mazub to the specified y pixel.
	 * @param Y_pos
	 * 			The wanted y position in pixels.
	 * @throws RuntimeException
	 * 			Throws an exception when the wanted position isn't on the canvas.
	 * 			| !isValidXPosition(Y_pos/100)
	 */
	public void setYPositionPixel(int Y_pos) throws RuntimeException{
		if (!isValidPixelYPosition(Y_pos))
			throw new RuntimeException();
		this.yPosPixel = Y_pos;
		this.yPosMeter = ((double) Y_pos)/100;
	}	
		
	/**
	 * Returns the y position on the canvas in pixels.
	 */
	@Basic
	public int getYPositionPixel() {
		return this.yPosPixel;
	}
	
	/**
	 * Returns the y position on the canvas in meters.
	 */
	public double getYPositionActual() {
		return this.yPosMeter;
	}
	
	/**
	 * Returns whether the given coordinate is on the canvas.
	 * @param Y_pos
	 * 			The given coordinate to check in meters.
	 */
	public boolean isValidActualYPosition(double Y_pos) {
		return (Y_pos*100 <= (getMaxYPosition()) &&  Y_pos >= 0);
	}
	
	/**
	 * Returns whether the given coordinate is on the canvas.
	 * @param X_pos
	 * 			The given coordinate to check in pixels.
	 */
	public boolean isValidPixelYPosition(int Y_pos) {
		return (Y_pos >= 0 && Y_pos <= getMaxYPosition());
	}
	
	/**
	 * Returns the maximal y position on the canvas in pixels.
	 */
	@Basic
	public int getMaxYPosition() {
		return maxYPosition;
	}
	
	private int yPosPixel;
	private double yPosMeter;
	
	/**
	 * Returns the orientation of a Mazub.
	 */
	@Basic
	public int getOrientation() {
		return this.orientation;
	}
	
	/**
	 * Sets the orientation of a given Mazub.
	 * @param orientation
	 * 			The orientation that needs to be set
	 * @pre The entered orientation is valid
	 * 		| isValidOrientation(orientation)
	 * @post The orientation of this Mazub is equal to the given
	 *       orientation.
 	 *       | new.getOrientation() == orientation
	 */
	private void setOrientation(int orientation) {
		assert isValidOrientation();
		this.orientation = orientation;
	}
	
	/**
	 * Returns true when the given orientation is a valid orientation
	 * @param orientation
	 * 			The orientation to check
	 */
	public boolean isValidOrientation() {
		return getOrientation() == -1 || getOrientation() == 0 || getOrientation() == 1;
		
	}
	
	private int orientation;
	
	
	private double horizontalSpeed;
	
	/**
	 * Returns the horizontal speed of a Mazub given in meters per second.
	 */
	@Basic
	public double getHorizontalSpeedMeters() {
		return this.horizontalSpeed;
	}
	/**
	 * Returns the horizontal speed of Mazub given in pixels per second
	 */
	public double getHorizontalSpeedPixels() {
		return (this.horizontalSpeed * 100);
	}
	
	/**
	 *  Sets the the horizontal speed of Mazub to the given speed.
	 *  
	 * @param speed
	 * 			The horizontal speed to give to Mazub
	 * @post The new speed of Mazub is equal to the given speed if the given speed is 0.
	 * 			| if (speed == 0) then new.horizontalSpeed == 0
	 * @post The new speed of Mazub is equal to the given speed if the speed an allowed speed.
	 * 			| if (Math.abs(speed) > getMinSpeedMeters() && Math.abs(speed) < getMaxSpeedMeters()) then new.horizontalSpeed == speed 
	 * @post The new speed of Mazub is equal to the minimal speed if the speed is positive and less than the allowed minimum speed.
	 * 			| if (speed < getMinSpeedMeters() && speed > 0) then new.horizontalSpeed == this.getMinSpeedMeters() 
	 * @post The new speed of Mazub is equal to the minimal speed times -1 if the speed is negative and less than the allowed minimum speed in absolute value.
	 * 			| if (speed < getMinSpeedMeters() && speed < 0) then new.horizontalSpeed == (this.getMinSpeedMeters())*-1
	 * @post The new speed of Mazub is equal to the maximal speed if the speed is positive and greater than the allowed maximum speed.
	 * 			| if (speed > getMaxSpeedMeters() && speed > 0) then new.horizontalSpeed == this.getMaxSpeedMeters() 
	 * @post The new speed of Mazub is equal to the maximal speed times -1 if the speed is negative and greater than the allowed maximum speed in absolute value.
	 * 			| if (speed > getMaxSpeedMeters() && speed < 0) then new.horizontalSpeed == (this.getMaxSpeedMeters())*-1
	 */
	private void setHorizontalSpeedMeters(double speed) {
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
	private final double minSpeed;
	private final double maxSpeedRunning;
	private final double maxSpeedDucking;
	
	
	/**
	 * Returns the minimum horizontal speed in meters per second.
	 */
	@Basic @Immutable
	public double getMinSpeedMeters() {
		return minSpeed;
	}
	
	/**
	 * Returns the minimal horizontal speed in pixels per second0.
	 */
	@Immutable
	public double getMinSpeedPixels() {
		return (this.minSpeed * 100);
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
	public double getMaxSpeedRunningPixels() {
		return maxSpeedRunning * 100;
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
	public double getMaxSpeedDuckingPixels() {
		return maxSpeedDucking * 100;
	}
	
	/**
	 * Returns whether the given speed is a valid speed
	 */
	private boolean isValidHorizontalSpeed() {
		if (this.getHorizontalSpeedMeters() != 0) 
				if ( Math.abs(this.getHorizontalSpeedMeters()) < this.getMinSpeedMeters()
				 || Math.abs(this.getHorizontalSpeedMeters()) > this.getMaxSpeed()) {
			return false;}
		return true;
	}
	
	private double verticalSpeed;
	
	/**
	 * Returns the vertical speed of a given Mazub in meters per second.
	 */
	@Basic @Immutable
	public double getVerticalSpeedMeters() {
		return this.verticalSpeed;
	}
	
	/**
	 * Returns the vertical speed of a given Mazub in pixels per second.
	 */
	@Immutable
	public double getVerticalSpeedPixels() {
		return this.verticalSpeed * 100;
	}
	
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
	private void setVerticalSpeedMeters(double verticalSpeedMeters) {
		if( verticalSpeedMeters <= getMaxVerticalSpeedMeters())
			this.verticalSpeed = verticalSpeedMeters;
		else this.verticalSpeed = getMaxVerticalSpeedMeters();
	}
	private final double maxVerticalSpeed = 8.0;
	
	@Basic @Immutable
	public double getMaxVerticalSpeedMeters() {
		return maxVerticalSpeed;
	}
	
	/**
	 * Returns whether the given speed is a valid vertical speed
	 */
	private boolean isValidVerticalSpeed() {
		if (this.getVerticalSpeedMeters() > this.getMaxVerticalSpeedMeters())
			return false;
		return true;
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
	 * @post The new horizontal acceleration is equal to the given horizontal acceleration
	 * 		| new.getHorizontalAcceleration() == horizontalAcceleration
	 */
	private void setHorizontalAcceleration(double horizontalAcceleration) {
		if (Math.abs(horizontalAcceleration)== 0 || Math.abs(horizontalAcceleration) == maxHorizontalAcceleration)
			this.horizontalAcceleration =  horizontalAcceleration;	
	}
	
	private double horizontalAcceleration;
	private final double maxHorizontalAcceleration = 0.9;
	
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
	 * @post The new vertical acceleration is equal to the given vertical acceleration
	 * 		| new.getVerticalAcceleration() == verticalAcceleration
	 */
	private void setVerticalAcceleration(double verticalAcceleration) {
		this.verticalAcceleration =  verticalAcceleration;
	}
	
	private double verticalAcceleration;
	private final double maxVerticalAcceleration = -10.0;
	
	/**
	 * Starts the movement in a given direction
	 * @param direction
	 * 			The direction in which to move. -1 to go left, 1 to go right
	 * @pre The given direction is valid
	 * 		| direction == 1 || direction == -1
	 * @pre The given alien is valid
	 * 		| this.isValidAlien()
	 * @pre The alien is not moving
	 * 		| !isMoving()
	 */
	public void startMove(int  direction) {
		assert this.isValidAlien();
		assert (direction == -1 || direction == 1);
		setOrientation(direction);
		assert !this.isMoving;
		
		this.setHorizontalSpeedMeters(minSpeed * direction);
		if (!isDucking)
			this.setHorizontalAcceleration(direction * maxHorizontalAcceleration);
		this.isMoving = true;
		
		if (direction == 1 && (!isJumping && !isFalling && !isDucking)) 
			setSprite(spriteArray[8]);
		else if (direction == -1 && (!isJumping && !isFalling && !isDucking))
			setSprite(spriteArray[9 + getSpriteLoopSize(spriteArray)]);
		else if (direction == 1 && !isJumping && isDucking && !isFalling)
			setSprite(spriteArray[6]);
		else if (direction == -1 && !isJumping && isDucking && !isFalling)
			setSprite(spriteArray[7]);
		
		}

	/**
	 * Sets the maximum horizontal speed for the given state of Mazub
	 * 
	 * @post if Mazub is ducking the new maximum speed is equal to the maximum speed while ducking
	 * 		| if (isDucking()) then new.maxSpeed = this.maxSpeedDuckingMeters
	 * @post if Mazub is not ducking the new maximum speed is equal to the maximum speed while running
	 * 		| if (!isDucking()) then new.maxSpeed = this.maxSpeedRunningMeters
	 */
	private void setMaxSpeed() {
		if (this.isDucking)
			maxSpeed = maxSpeedDucking;
		else
			maxSpeed = maxSpeedRunning;
	}
	
	private double maxSpeed;
	
	/**
	 * Returns the maximum horizontal speed for the given state of the Mazub
	 */
	public double getMaxSpeed() {
		this.setMaxSpeed();
		return maxSpeed;
	}
	
	/**
	 * Ends the movement of Mazub
	 * @pre The given alien is valid
	 * 		| this.isValidAlien()
	 * @pre The alien is moving
	 * 		| this.isMoving
	 * @post The horizontal speed of Mazub is 0
	 * 		| new.getHorizontalSpeedActual == 0
	 * @post The horizontal acceleration of Mazub is 0
	 * 		| new.getHorizontalAcceleration == 0
	 */
	@Raw
	public void endMove() {
		assert this.isValidAlien();
		assert this.isMoving;
		
		int orientation = getOrientation();
		this.isMoving = false;
		this.setHorizontalSpeedMeters(0);
		this.setHorizontalAcceleration(0);
		if (orientation == -1 && !isJumping && !isDucking) { 
			this.setSprite(this.spriteArray[3]);
			timeSinceLastMove = 0.0;
		}
		else if (orientation == 1 && !isJumping && !isDucking ) {
			setSprite(spriteArray[2]);
			timeSinceLastMove = 0.0;
		}

	}
	
	public boolean isMoving; 
	
	/**
	 * Returns whether the given alien is valid
	 */
	public boolean isValidAlien() {
		if (!isValidActualXPosition(this.xPosMeter) || !isValidActualYPosition(this.yPosMeter)
				|| !isValidPixelXPosition() || !isValidPixelYPosition(this.yPosPixel))
			return false;
		if (!isValidHorizontalSpeed() || !isValidVerticalSpeed())
			return false;
		if (this.spriteArray == null)
			return false;
		
		return true;
	}
	
	/**
	 * Starts a jump for a given Mazub
	 * @throws RuntimeException when Mazub is not on the ground
	 * 			|(isJuming || isFalling)
	 * @post The new speed is equal to the given maximum vertical speed.
	 * 		| new.verticalSpeed == maxVerticalSpeed
	 * @post The new acceleration is equal to the given maximum vertical acceleration.
	 * 		| new.VerticalAcceleration == maxVerticalAcceleration
	 */
	public void startJump() throws RuntimeException {
		if (this.isJumping)
			throw new RuntimeException();
		else if (this.isFalling);
		else {
			this.isFalling = false;
			this.setVerticalSpeedMeters(maxVerticalSpeed);
			this.setVerticalAcceleration(maxVerticalAcceleration);
			this.isJumping = true;
			setSprite(this.spriteArray[0]);
			
			if (getOrientation() < 0)
				setSprite(this.spriteArray[5]);
			else if (getOrientation() > 0)
				setSprite(this.spriteArray[4]);
			
		}
	}
	
	/**
	 * Ends a jump for a given Mazub
	 * @throws RuntimeException when Mazub is on the ground
	 * 			|(!isJuming)
	 * @post The new speed is equal to 0.
	 * 		| new.verticalSpeed == 0
	 * @post The new acceleration is equal to the given maximum vertical acceleration.
	 * 		| new.VerticalAcceleration == maxVerticalAcceleration
	 */
	public void endJump() throws RuntimeException {
		if (this.isJumping) {
			this.setVerticalSpeedMeters(0);
			this.setVerticalAcceleration(maxVerticalAcceleration);
			this.isJumping = false;
			if (getOrientation() == 1 && !isFalling)
				setSprite(spriteArray[8 + getSpriteLoopSize(spriteArray)]);
			if (getOrientation() == -1 && !isFalling)
				setSprite(spriteArray[9 + 2*getSpriteLoopSize(spriteArray)]);
			if (getOrientation() == 0)
				setSprite(spriteArray[0]);
		}
		else if( this.isFalling);
		else {
			throw new RuntimeException();
		}
	}
	
	public boolean isJumping;
	
	/**
	 * Makes a Mazub fall
	 * @post The new acceleration is equal to the given maximum vertical acceleration.
	 * 		| new.VerticalAcceleration == maxVerticalAcceleration
	 */
	private void fall() {
		if (this.getYPositionActual() > 0)
			this.setVerticalAcceleration(maxVerticalAcceleration);
		if (this.getYPositionActual() < 0  && this.getVerticalSpeedMeters() < 0)
			this.setVerticalAcceleration(0);
	}
	
	public boolean isFalling;
	
	/**
	 * Updates the position of Mazub over a given time interval
	 * @param dt The time interval in seconds over which to update the position
	 * @post The new position is valid
	 * 		| isValidActualXPosition(new.xPosMeter) && isValidActualYPosition(new.yPosMeter)
	 * @post The new x position is equal to the old position + dt*(horizontal speed) + dt*dt*(horizontal acceleration)
	 * 		| new.xPosMeter == getXPositionActual() + getHorizontalSpeedMeters()*dt + 0.5*getHorizontalAcceleration()*dt*dt
	 * @post The new y position is equal to the old position + dt*(vertical speed) + dt*dt*(vertical acceleration)
	 * 		| new.yPosMeter == getYPositionActual() + getVerticalSpeedMeters()*dt + 0.5*getVerticalAcceleration()*dt*dt
	 * @post The new x position is 0 if the actual new position is left of the canvas
	 * 		| if new.xPosMeter == 0 then new.xPosMeter == getXPositionActual() + getHorizontalSpeedMeters()*dt + 0.5*getHorizontalAcceleration()*dt*dt < 0
	 * @post The new x position is the maximal x position if the actual new position is right of the canvas
	 * 		| if new.xPosMeter == this.getMaxXPosition() then new.xPosMeter == getXPositionActual() + getHorizontalSpeedMeters()*dt + 0.5*getHorizontalAcceleration()*dt*dt > getMaxXPosition()
	 * @post The new y position is 0 if the actual new position is under the canvas
	 * 		| if new.yPosMeter == 0 then getYPositionActual() + getVerticalSpeedMeters()*dt + 0.5*getVerticalAcceleration()*dt*dt < 0
	 * @post The new y position is the maximal y position if the actual new position is above the canvas
	 * 		| if new.yPosMeter == this.getMaxYPosition() then getYPositionActual() + getVerticalSpeedMeters()*dt + 0.5*getVerticalAcceleration()*dt*dt > getMaxYPosition()
	 */
	private void updatePosition(double dt) {
		this.setMaxSpeed();
		fall();
		double newPosX = getXPositionActual() + getHorizontalSpeedMeters()*dt + 0.5*getHorizontalAcceleration()*dt*dt;
		if(!isValidActualXPosition(newPosX)) {
			if( newPosX <0) {
				newPosX = 0;
			}
			else newPosX = ((double) this.getMaxXPosition())/100;
		}
		setXPositionActual(newPosX);
		double newSpeedX = getHorizontalSpeedMeters() + getHorizontalAcceleration()*dt;
		setHorizontalSpeedMeters(newSpeedX);
		double newPosY = getYPositionActual() + getVerticalSpeedMeters()*dt + 0.5*getVerticalAcceleration()*dt*dt;
		if(!isValidActualYPosition(newPosY)) {
			if( newPosY <0) {
				newPosY = 0;
			}
			else newPosY = (((double) this.getMaxYPosition()))/100;
			}
		setYPositionActual(newPosY);
		double newSpeedY = getVerticalSpeedMeters() + getVerticalAcceleration()*dt;

		this.setVerticalSpeedMeters(newSpeedY);
		if(newPosY <= 0)
			this.isFalling=false;
		if (newSpeedY < 0 && newPosY > 0) {
			this.isJumping = false;
			this.isFalling = true;
			}
		if(newPosY == 0)
			this.isFalling=false;
		}
	
	/**
	 * Updates the position of Mazub over a given time interval
	 * @param dt The time interval in seconds over which to update the position
	 * @param index The index to which to set the new sprite
	 * @post The new position is valid
	 * 		| isValidActualXPosition(new.xPosMeter) && isValidActualYPosition(new.yPosMeter)
	 * @post The new x position is equal to the old position + dt*(horizontal speed) + dt*dt*(horizontal acceleration)
	 * 		| new.xPosMeter == getXPositionActual() + getHorizontalSpeedMeters()*dt + 0.5*getHorizontalAcceleration()*dt*dt
	 * @post The new y position is equal to the old position + dt*(vertical speed) + dt*dt*(vertical acceleration)
	 * 		| new.yPosMeter == getYPositionActual() + getVerticalSpeedMeters()*dt + 0.5*getVerticalAcceleration()*dt*dt
	 * @post The new x position is 0 if the actual new position is left of the canvas
	 * 		| if new.xPosMeter == 0 then new.xPosMeter == getXPositionActual() + getHorizontalSpeedMeters()*dt + 0.5*getHorizontalAcceleration()*dt*dt < 0
	 * @post The new x position is the maximal x position if the actual new position is right of the canvas
	 * 		| if new.xPosMeter == this.getMaxXPosition() then new.xPosMeter == getXPositionActual() + getHorizontalSpeedMeters()*dt + 0.5*getHorizontalAcceleration()*dt*dt > getMaxXPosition()
	 * @post The new y position is 0 if the actual new position is under the canvas
	 * 		| if new.yPosMeter == 0 then getYPositionActual() + getVerticalSpeedMeters()*dt + 0.5*getVerticalAcceleration()*dt*dt < 0
	 * @post The new y position is the maximal y position if the actual new position is above the canvas
	 * 		| if new.yPosMeter == this.getMaxYPosition() then getYPositionActual() + getVerticalSpeedMeters()*dt + 0.5*getVerticalAcceleration()*dt*dt > getMaxYPosition()
	 * @post The new sprite is the sprite with the next index
	 * 		| new.sprite == spriteArray[index]
	 */
	private void updatePositionAndSprite(double dt, int index) {
		this.setMaxSpeed();
		fall();
		double newPosX = getXPositionActual() + getHorizontalSpeedMeters()*dt + 0.5*getHorizontalAcceleration()*dt*dt;
		if(!isValidActualXPosition(newPosX)) {
			if( newPosX <0) {
				newPosX = 0;
			}
			else newPosX = ((double) this.getMaxXPosition())/100;
		}
		setXPositionActual(newPosX);
		double newSpeedX = getHorizontalSpeedMeters() + getHorizontalAcceleration()*dt;
		setHorizontalSpeedMeters(newSpeedX);
		double newPosY = getYPositionActual() + getVerticalSpeedMeters()*dt + 0.5*getVerticalAcceleration()*dt*dt;
		if(!isValidActualYPosition(newPosY)) {
			if( newPosY <0) {
				newPosY = 0;
			}
			else newPosY = (((double) this.getMaxYPosition()))/100;
			}
		setYPositionActual(newPosY);
		double newSpeedY = getVerticalSpeedMeters() + getVerticalAcceleration()*dt;

		this.setVerticalSpeedMeters(newSpeedY);
		if(newPosY <= 0)
			this.isFalling=false;
		if (newSpeedY < 0) {
			this.isJumping = false;
			this.isFalling = true;
			}
		if(newPosY == 0)
			this.isFalling=false;
		setSprite(spriteArray[index]);

		}
	
	
	private int getNextSpriteIndex() {
		int indexCurrentSprite = 0;
		int nextIndex = 0;
		Sprite currentSprite = getCurrentSprite();
		int length = spriteArray.length;
		for (int i = 0; i < length; i++) {
			if (spriteArray[i] == currentSprite)
				indexCurrentSprite = i;
		}
		
		if (indexCurrentSprite >= 8) {
			if (indexCurrentSprite == 8 + getSpriteLoopSize(spriteArray))
				nextIndex = 8;
			else if (indexCurrentSprite == 9 + 2*getSpriteLoopSize(spriteArray))
				nextIndex = 9 + getSpriteLoopSize(spriteArray);
			else
				nextIndex = indexCurrentSprite + 1;			
		}
		else
			nextIndex = indexCurrentSprite;
		return nextIndex;
	}
	
	/**
	 * Advances the in-game time for a given interval dt.
	 * @param dt The time to advance in seconds
	 * @effect updatePositionAndSprite(dt, index)
	 */
	public void advanceTime(double dt) {
		if (dt != dt)
			dt = 0.0;
		if (dt > maxTimeFrame)
			dt = maxTimeFrame;
		if (dt < 0)
			dt = 0.0;	
		
		int fullUpdates = (int) (dt / frameRate);
		for (int i=0; i < fullUpdates; i++) {
			updatePositionAndSprite(frameRate, getNextSpriteIndex());
			if (!isMoving && !isJumping && !isDucking) {
				timeSinceLastMove += frameRate;
			}
			else {
				timeSinceLastMove = 0.0;
			}
		}
		updatePosition(dt % frameRate);
		timeToSpare += dt % frameRate;
		
		if (!isMoving && !isJumping && !isDucking ) {
			timeSinceLastMove += dt % frameRate;
		}
		else {
			timeSinceLastMove = 0.0;
		}
		
		
		if (timeToSpare > frameRate) {
			updatePositionAndSprite(0, getNextSpriteIndex());
			timeToSpare -= frameRate;
		}
		
		if (timeSinceLastMove >= 1) {
			updatePositionAndSprite(0, 0);
			timeSinceLastMove = 0.0;
			setVerticalSpeedMeters(0.0);
			setVerticalAcceleration(0.0);
		}
	}
	
	private double maxTimeFrame = 0.2;
	private double frameRate = 0.075;
	
	/**
	 * Returns the current sprite.
	 */
	public Sprite getCurrentSprite() {
		return this.sprite;	
	}
	
	/**
	 * Sets the sprite of Mazub to the given sprite
	 * @param sprite The sprite to set
	 * @throws RuntimeException If the sprite is not valid
	 * 			| !isValidSprite(sprite)
	 * @post The new sprite is equal to the given sprite
	 * 		| new.sprite == sprite
	 */
	private void setSprite(Sprite sprite) throws RuntimeException {
		if (! isValidSprite(sprite))
			throw new RuntimeException();	
		this.sprite = sprite;	
	}
	
	/**
	 * Returns whether the given sprite is valid
	 * @param sprite The sprite to check
	 */
	public boolean isValidSprite(Sprite sprite) {
		return (sprite.canHaveAsHeight(sprite.getHeight()) &&
				sprite.canHaveAsWidth(sprite.getWidth()) &&
				sprite.canHaveAsName(sprite.getName()));
	}
	
	/**
	 * Returns whether an array of sprites has valid dimensions
	 * @param sprites The array to check
	 */
	public boolean isValidSpriteArray(Sprite ... sprites) {
		return (sprites.length >= 10 && sprites.length % 2 == 0);		
	}
	
	/**
	 * Sets the sprite array to a given array
	 * @param sprites The array to which to set
	 * @post The new spriteArray is equal to the given one
	 * 		| new.getSpriteArray == sprites
	 */
	private void setSpriteArray (Sprite ... sprites) {
		this.spriteArray = sprites.clone();
	}
	
	/**
	 * Returns the sprite array of a Mazub
	 */
	public Sprite[] getSpriteArray() {
		return this.spriteArray.clone();
	}
	
	private Sprite[] spriteArray;
	
	/**
	 * Returns the loop length of a given sprite array (The amount of sprites to go either right or left)
	 * @param sprites The array to get the loop length from
	 * @throws RuntimeException When the spriteArray is not valid
	 * 			| !isValidSpriteArray(sprites)
	 */
	private int getSpriteLoopSize(Sprite ... sprites) throws RuntimeException {
		if(! isValidSpriteArray(sprites))
			throw new RuntimeException();
		return ((sprites.length - 10)/2);
	}
	
	private Sprite sprite;
	
	
	/**
	 * Starts the duck move for a Mazub
	 * @post Mazub is ducking
	 * 		| isDucking
	 * @post The horizontal acceleration is 0
	 * 		| new.horizontalAcceleration == 0
	 */
	public void startDuck() {
		this.setSprite(this.spriteArray[1]);
		if (this.getHorizontalSpeedMeters() != 0) {
			this.setHorizontalSpeedMeters(this.getMaxSpeedDuckingMeters()*this.getOrientation());
			if(this.getOrientation() > 0)
				this.setSprite(this.spriteArray[6]);
			else if (getOrientation() < 0)
				this.setSprite(this.spriteArray[7]);}

		
		this.setHorizontalAcceleration(0.0);
		this.isDucking = true;

	}
	
	/**
	 * Ends the duck move for a Mazub
	 * @post Mazub is not ducking
	 * 		| !isDucking
	 */
	public void endDuck() {
		this.setSprite(this.spriteArray[0]);
		this.isDucking = false;
		this.setMaxSpeed();
		if (this.isMoving) {
			this.setHorizontalAcceleration(maxHorizontalAcceleration*this.getOrientation());
			if (getOrientation() == 1)
				setSprite(spriteArray[8]);
			else
				setSprite(spriteArray[9 + getSpriteLoopSize(getSpriteArray())]);
		}
		
	}
	
	public boolean isDucking; 
	
	private double timeToSpare = 0.0;
	
	private double timeSinceLastMove;
	
	private void setHitpoints(int hitpoints) {
		if (hitpoints < 0)
			this.hitpoints = 0;
		if (hitpoints > 500)
			this.hitpoints = 500;
		else
			this.hitpoints = hitpoints;
		
	}
	
	public int getHitpoints() {
		return this.hitpoints;
	}
	
	private int hitpoints;
	
	private void changeHitPoints(int change) {
		setHitpoints(getHitpoints() + change);
	}
}
 
