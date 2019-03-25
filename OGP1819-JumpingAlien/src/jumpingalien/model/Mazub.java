package jumpingalien.model;

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
public class Mazub extends GameObject {
	
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
		super((int)(X_pos * 100), (int)(Y_pos * 100), X_size, Y_size, 100, maxSpeedRunningMeters, maxSpeedDuckingMeters, minSpeedMeters, 8.0, 0.9, -10.0, sprites);
		this.isDucking = false;
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
		super(X_pos, Y_pos, X_size, Y_size, 100, maxSpeedRunningMeters, maxSpeedDuckingMeters, minSpeedMeters, 8.0, 0.9, -10.0, sprites);
		this.isDucking = false;
	}
	
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
		assert this.isValidGameObject();
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
		assert this.isValidGameObject();
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
	@Override
	public void advanceTime(double dt, double timeStep) {
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
//			setVerticalSpeedMeters(0.0);
//			setVerticalAcceleration(0.0);
		}
	}
	
	private double maxTimeFrame = 0.2;
	private double frameRate = 0.075;
	
	/**
	 * Returns whether an array of sprites has valid dimensions
	 * @param sprites The array to check
	 */
	public boolean isValidSpriteArray(Sprite ... sprites) {
		return (sprites.length >= 10 && sprites.length % 2 == 0);		
	}
	
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
	
//	private Sprite sprite;
	
	
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
	
	boolean isPlayer;

	public void setPlayer() {
		this.isPlayer = true;
		this.getWorld().player = this;
	}
	
	public boolean isPlayer() {
		return this.isPlayer;
	}
	
}
 
