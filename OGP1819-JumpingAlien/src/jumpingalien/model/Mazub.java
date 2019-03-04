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
 */
public class Mazub {
	
	/**
	 * Initialize a new player with a given position (X_pos, Y_pos), a given size (X_size, Y_size) and speed
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
		setYSize(Y_size);
		setXSize(X_size);
		setXPosition(X_pos);
		setYPosition(Y_pos);
		setHorizontalSpeedMeters(horizontalSpeedMeters);
		this.minSpeed = minSpeedMeters;
		this.maxSpeedDucking = maxSpeedDuckingMeters;
		this.maxSpeedRunning = maxSpeedRunningMeters;
		this.setSpriteArray(sprites);
		setSprite(sprites[0]);
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
	}
	
	/**
	 * Create a new player in the origin with size 1x2.
	 */
	public Mazub() {
		setYSize(2);
		setXSize(1);
		setXPosition(0);
		setYPosition(0);
		setHorizontalSpeedMeters(0);
		this.minSpeed = 1;
		this.maxSpeedDucking = 1;
		this.maxSpeedRunning = 3;
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
		if (!isValidXPosition(X_pos/100))
			throw new RuntimeException();
		this.xPosPixel = X_pos;
		this.xPosMeter = (double) X_pos/100;
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
	public void setXPosition(double X_pos) throws RuntimeException{
		if (!isValidXPosition(X_pos))
			throw new RuntimeException();
		this.xPosPixel = (int) X_pos * 100;
		this.xPosMeter = X_pos;
	}
	
	/**
	 * Returns the maximal x position on the canvas in pixels.
	 */
	@Basic
	private int getMaxXPosition() {
		return maxXPosition;
	}
	
	/**
	 * Returns the x position on the canvas in pixels.
	 */
	@Basic
	public int getXPositionPixel() {
		return this.xPosPixel;
	}
	
	public double getXPositionActual() {
		return this.xPosMeter;
	}
	
	/**
	 * Returns whether the given coordinate is on the canvas.
	 * @param X_pos
	 * 			The coordinate to check.
	 */
	public boolean isValidXPosition(double X_pos) {
		return (X_pos < (getMaxXPosition() - 1) &&  X_pos >= 0);		
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
	public void setYPosition(double Y_pos) throws RuntimeException{
		if (!isValidYPosition(Y_pos)) 
			throw new RuntimeException();
		this.yPosPixel = (int) Y_pos * 100;
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
		if (!isValidXPosition(Y_pos))
			throw new RuntimeException();
		this.yPosPixel = Y_pos;
		this.yPosMeter = (double) Y_pos/100;
	}	
		
	/**
	 * Returns the y position on the canvas.
	 */
	@Basic
	public int getYPositionPixel() {
		return this.yPosPixel;
	}
	
	public double getYPositionActual() {
		return this.yPosMeter;
	}
	
	/**
	 * Returns whether the given coordinate is on the canvas.
	 * @param X_pos
	 * 			The given coordinate to check.
	 */
	public boolean isValidYPosition(double Y_pos) {
		return (Y_pos < (getMaxYPosition() - 1) &&  Y_pos >= 0);
	}
	
	/**
	 * Returns the maximal y position on the canvas.
	 */
	@Basic
	private int getMaxYPosition() {
		return maxYPosition;
	}
	
	private int yPosPixel;
	private double yPosMeter;
	
	/**
	 * Returns the orientation of a Mazub
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
	 * 
	 */
	private void setOrientation(int orientation) {
		assert isValidOrientation(orientation);
		this.orientation = orientation;
	}
	
	/**
	 * Returns true when the given orientation is a valid orientation
	 * @param orientation
	 * 			The orientation to check
	 */
	public Boolean isValidOrientation(int orientation) {
		return orientation == -1 || orientation == 0 || orientation == 1;
		
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
		if (Math.abs(speed) < getMinSpeedMeters() && speed < 0)
			this.horizontalSpeed = -1*getMinSpeedMeters();
		else if (Math.abs(speed) < getMinSpeedMeters() && speed > 0)
			this.horizontalSpeed = getMinSpeedMeters();
		else if (speed == 0)
			this.horizontalSpeed = 0;
		else if (Math.abs(speed) > getMaxSpeedRunningMeters() && speed > 0)
			this.horizontalSpeed = getMaxSpeedRunningMeters();
		else
			this.horizontalSpeed = -1*getMaxSpeedRunningMeters();
	}
	
	/**
	 * Sets the horizontal speed of a given Mazub to the given speed in pixels per second
	 * @param speed 
	 * 			The speed in pixels per second
	 * @effect Has the same effect as setting the speed in meters to the given speed/100
	 * 		| setHorizontalSpeedMeters(speed/100)
	 */
	private void setHorizontalSpeedPixels(double speed) {
		setHorizontalSpeedMeters(speed / 100);
	}
	
	/**
	 * Initiate the maximum velocities
	 */
	public final double minSpeed;
	public final double maxSpeedRunning;
	public final double maxSpeedDucking;
	
	
	/**
	 * Returns the minimum horizontal speed in meters per second.
	 */
	@Basic @Immutable
	public double getMinSpeedMeters() {
		return this.minSpeed;
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
		if (this.getHorizontalSpeedMeters() != 0 &&( this.getHorizontalSpeedMeters() < this.getMinSpeedMeters()
				 || this.getHorizontalSpeedMeters() > this.getMaxSpeedRunningMeters()))
			return false;
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
		if( Math.abs(verticalSpeedMeters) <= getMaxVerticalSpeedMeters())
			this.verticalSpeed = verticalSpeedMeters;
		else if (verticalSpeedMeters > 0)
			this.verticalSpeed = getMaxVerticalSpeedMeters();
		else
			this.verticalSpeed = -1* getMaxVerticalSpeedMeters();
	}
	private final double maxVerticalSpeed = 8;
	
	@Basic @Immutable
	public double getMaxVerticalSpeedMeters() {
		return maxVerticalSpeed;
	}
	
	private boolean isValidVerticalSpeed() {
		if (this.getVerticalSpeedMeters() != 0 || Math.abs(this.getVerticalSpeedMeters()) > this.getMaxVerticalSpeedMeters())
			return false;
		return true;
	}
	public double getHorizontalAcceleration() {
		return horizontalAcceleration;
	}
	private void setHorizontalAcceleration(double horizontalAcceleration) {
		if (Math.abs(horizontalAcceleration)== 0 || Math.abs(horizontalAcceleration) == maxHorizontalAcceleration)
			this.horizontalAcceleration =  horizontalAcceleration;
		
	}
	private double horizontalAcceleration;
	private final double maxHorizontalAcceleration = 0.9;
	
	public double getVerticalAcceleration() {
		return verticalAcceleration;
	}
	private void setVerticalAcceleration(double verticalAcceleration) {
		this.verticalAcceleration =  verticalAcceleration;
	}
	private double verticalAcceleration;
	private final double maxVerticalAcceleration = 10;
	
	
	
	/**
	 * Starts the movement in a given direction
	 * @param direction
	 * 			The direction in which to move. -1 to go left, 1 to go right
	 * @pre The given direction is valid
	 * 		| direction == 1 || direction == -1
	 * @pre The given alien is valid
	 * 		| this.isValidAlien()
	 * @pre The alien is not moving
	 * 		| this.getHorizontalSpeedMeters() == 0
	 */
	public void startMove(int  direction) {
		assert this.isValidAlien();
		assert (direction == -1 || direction == 1);
		assert this.getHorizontalSpeedMeters() == 0;
		

		this.setHorizontalSpeedMeters(minSpeed * direction);
		this.setHorizontalAcceleration(direction * maxHorizontalAcceleration);

	}
	
	/**
	 * Ends the movement of Mazub
	 * @pre The given alien is valid
	 * 		| this.isValidAlien()
	 * @pre The alien is moving
	 * 		| this.getHorizontalSpeedMeters() != 0
	 */
	public void endMove() {
		assert this.isValidAlien();
		assert this.getHorizontalSpeedMeters() != 0;
		
		if (this.getHorizontalSpeedMeters() != 0)
			this.setHorizontalSpeedMeters(0);
			this.setHorizontalAcceleration(0);
	}
	
	/**
	 * Returns whether the given alien is valid
	 */
	public boolean isValidAlien() {
		if (!isValidXPosition(this.xPosPixel) || !isValidYPosition(this.yPosPixel))
			return false;
		if (!isValidHorizontalSpeed() || !isValidVerticalSpeed())
			return false;
		if (this.spriteArray == null)
			return false;
		
		return true;
	}
	
	public void startJump() throws RuntimeException {
		if (this.yPosPixel != 0)
			throw new RuntimeException();
		else {
			this.setVerticalSpeedMeters(maxVerticalSpeed);
			this.setVerticalAcceleration(maxVerticalAcceleration);
		}
	}
	
	public void endJump() throws RuntimeException {
		if(this.getVerticalSpeedMeters() < 0)
			throw new RuntimeException();
		if (this.yPosPixel == 0)
			throw new RuntimeException();
		else {
			this.setVerticalSpeedMeters(0);
			this.setVerticalAcceleration(maxVerticalAcceleration);
		}
	}
	
	public void advanceTime(double t) {
		if (t > maxTimeFrame )
			t = maxTimeFrame;
		if (t <0) {
			t = 0;}
		
			if (this.getHorizontalSpeedMeters() >= getMaxSpeedRunningMeters())
				this.setHorizontalSpeedMeters(getMaxSpeedRunningMeters());
				this.setHorizontalAcceleration(0);
			double newPosX = getXPositionPixel() + getHorizontalSpeedMeters()*100*t + 0.5*getHorizontalAcceleration()*t*t*100;
			if (isValidXPosition(newPosX/100)) {
				this.setXPosition(newPosX/100);
				double newSpeedX = getHorizontalSpeedMeters() + getHorizontalAcceleration()*t;
				this.setHorizontalSpeedMeters(newSpeedX);}
			if (this.getYPositionPixel() ==0 && this.getVerticalSpeedMeters()!= 0) {
				this.setVerticalAcceleration(0);
				this.setVerticalSpeedMeters(0);
				}
			else {
				double newPosY = getYPositionPixel() + getVerticalSpeedMeters()*100*t + 0.5*getVerticalAcceleration()*t*t*100;
				if (isValidYPosition(newPosY/100)) {
					this.setYPosition(newPosY/100);
					double newSpeedY = getVerticalSpeedMeters() + getVerticalAcceleration()*t;
					this.setVerticalSpeedMeters(newSpeedY);
					}
				else{
					this.setVerticalAcceleration(0);
					this.setVerticalSpeedMeters(0);
					this.setYPosition(0);
				}	
		}	
	}
	
	private double maxTimeFrame = 0.2;
	
	public Sprite getCurrentSprite() {
		return this.sprite;
		
	}
	
	public void setSprite(Sprite sprite) throws RuntimeException {
		if (! isValidSprite(sprite))
			throw new RuntimeException();	
		this.sprite = sprite;	
	}
	
	public boolean isValidSprite(Sprite sprite) {
		return (sprite.canHaveAsHeight(sprite.getHeight()) &&
				sprite.canHaveAsWidth(sprite.getWidth()) &&
				sprite.canHaveAsName(sprite.getName()));
	}
	
	public boolean isValidSpriteArray(Sprite ... sprites) {
		return (sprites.length >= 10 && sprites.length%2 == 0);		
	}
	
	public void setSpriteArray (Sprite ... sprites) {
		this.spriteArray = sprites;
	}
	
	public Sprite[] getSpriteArray() {
		return this.spriteArray;
	}
	
	private Sprite[] spriteArray;
	
	public int getSpriteLoopSize(Sprite ... sprites) throws RuntimeException {
		if(! isValidSpriteArray(sprites))
			throw new RuntimeException();
		return ((sprites.length -8)/2);
	}
	
	public Sprite sprite;
	/**
	 * iets bijschrijven
	 */
	
	
	public void startDuck() {
		this.setSprite(this.spriteArray[1]);
		if (this.getHorizontalSpeedMeters() != 0)
			this.setHorizontalSpeedMeters(this.getMaxSpeedDuckingMeters());

	}
	
	public void endDuck() {
		this.setSprite(this.spriteArray[0]);
	}
	
	public boolean isDucking() {
		return false;
		
	}
	
}
 
