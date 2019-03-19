package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import jumpingalien.util.Sprite;

public abstract class GameObject {

	GameObject(int pixelLeftX, int pixelBottomY, int pixelSizeX, int pixelSizeY, int hitpoints, double maxHorizontalSpeedRunning, 
			double maxHorizontalSpeedDucking, double minHorizontalSpeed, double horizontalAcceleration, double verticalAcceleration, 
			Sprite... sprites){
		setYSize(pixelSizeY);
		setXSize(pixelSizeX);
		setXPositionPixel(pixelLeftX);
		setYPositionPixel(pixelBottomY);
		setSprite(sprites[0]);
		setSprites(sprites);
		setHitpoints(hitpoints);
		setMaxSpeed();
		setHorizontalSpeedMeters(0);
		this.minSpeed = minHorizontalSpeed;
		this.maxSpeedDucking = maxHorizontalSpeedDucking;
		this.maxSpeedRunning = maxHorizontalSpeedRunning;
		setHorizontalAcceleration(horizontalAcceleration);
		setVerticalAcceleration(verticalAcceleration);
	}
	

	private void setSprite(Sprite sprite) {
		if (! isValidSprite(sprite))
			throw new RuntimeException();	
		this.sprite = sprite;
		
	}
	
	private void setSprites(Sprite... sprites) {
		this.sprites = sprites;
	}
	
	public Sprite[] getSprites() {
		return this.sprites;
	}
	
	private Sprite[] sprites;
 	
	/**
	 * Returns the current sprite.
	 */
	public Sprite getCurrentSprite() {
		return this.sprite;	
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

	private Sprite sprite;

	/**
	 * Initiate the size of the canvas in pixels
	 */
	private int maxXPosition = 1024;
	private int maxYPosition = 768;
	
	/**
	 * Check whether this object is terminated.
	 */
	@Basic
	@Raw
	public boolean isTerminated() {
		return this.isTerminated;
	}
	
	public boolean isDead() {
		return this.isDead;
	}
	
	public void terminate() {
		if (!isTerminated()) {
			this.isTerminated = true;
			this.setHitpoints(0);
		}
	}
	
	public void kill() {
		if (!isDead()) {
			this.isDead = true;
			this.setHitpoints(0);
		}
	}
	
	private boolean isTerminated;
	private boolean isDead;
	
	private int hitPoints;
	
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
	 * Returns the x size of a given Mazub.
	 */
	@Basic
	public int getXsize() {
		return this.xSize;
	}
	
	private int xSize;
	
	/**
	 * Sets the x position of the bottom left pixel of a given Mazub to the specified x pixel.
	 * @param X_pos
	 * 			The wanted x position in pixels.
	 * @throws RuntimeException
	 * 			Throws an exception when the wanted position isn't on the canvas.
	 * 			| !isValidXPosition(X_pos/100)
	 */
	public void setXPositionPixel(int X_pos) throws RuntimeException{
		if (!isValidPixelXPosition(X_pos))
			throw new RuntimeException();
		this.xPosPixel = X_pos;
		this.xPosMeter = ((double) X_pos)/100;
	}
	
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
	 * Returns whether the given coordinate is on the canvas.
	 */
	public boolean isValidPixelXPosition(int X_pos) {
		return (X_pos >= 0 && X_pos <= getMaxXPosition());
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
	 * Returns the x position on the canvas in pixels.
	 */
	@Basic 
	public int getXPositionPixel() {
		return this.xPosPixel;
	}
	
	/**
	 * Returns the maximal x position on the canvas in pixels.
	 */
	@Basic 
	public int getMaxXPosition() {
		return maxXPosition;
	}
	
	
	
	private int xPosPixel;
	private double xPosMeter;
	
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
	 * Returns whether the given coordinate is on the canvas.
	 * @param X_pos
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
	
	/**
	 * Returns the y position on the canvas in pixels.
	 */
	@Basic 
	public int getYPositionPixel() {
		return this.yPosPixel;
	}
	
	private int yPosPixel;
	private double yPosMeter;
	
	private void setHitpoints(int hitpoints) {
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
	
	public int getHitpoints() {
		return this.hitpoints;
	}
	
	private int hitpoints;
	
	private void changeHitPoints(int change) {
		setHitpoints(getHitpoints() + change);
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
	
	/**
	 * Returns the orientation of a Mazub.
	 */
	@Basic
	public int getOrientation() {
		return this.orientation;
	}
	
	private int orientation;
	
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
	public int getHorizontalSpeedPixels() {
		return (int) (this.horizontalSpeed * 100);
	}
	
	private double horizontalSpeed;
	
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
	
	public boolean isDucking;
	
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
		if (Math.abs(horizontalAcceleration) == 0 || Math.abs(horizontalAcceleration) == maxHorizontalAcceleration)
			this.horizontalAcceleration =  horizontalAcceleration;	
	}
	
	private double horizontalAcceleration;
	private double maxHorizontalAcceleration;
	
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
		if (verticalAcceleration == 0 || verticalAcceleration == maxVerticalAcceleration)
			this.verticalAcceleration =  verticalAcceleration;
	}
	
	private double verticalAcceleration;
	private double maxVerticalAcceleration;
}
