package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * A class that implements a player character with the ability to jump, to run to the left and to the right.
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 *
 */
public class Mazub {
	
	/**
	 * Initialize a new player with a given position (X_pos, Y_pos), a given size (X_size, Y_size) and speed
	 * .
	 * @param X_pos
	 * 			The x position of the bottom left pixel of Mazub.
	 * @param Y_pos
	 * 			The x size of Mazub given in pixels.
	 * @param X_size
	 * 			The x position of the bottom left pixel of Mazub.
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
	 */
	public Mazub(double X_pos, double Y_pos, int X_size, int Y_size, double horizontalSpeedMeters, double minSpeedMeters, double maxSpeedRunningMeters, double maxSpeedDuckingMeters) {
		setYSize(Y_size);
		setXSize(X_size);
		setXPosition(X_pos);
		setYPosition(Y_pos);
		setHorizontalSpeedMeters(horizontalSpeedMeters);
		this.minSpeed = minSpeedMeters;
		this.maxSpeedDucking = maxSpeedDuckingMeters;
		this.maxSpeedRunning = maxSpeedRunningMeters;
	}
	
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
	private void setXPosition(double X_pos) throws RuntimeException{
		if (!isValidXPosition(X_pos))
			throw new RuntimeException();
		this.xPos = (int) X_pos * 100;
	}
	
	/**
	 * Returns the maximal x position on the canvas.
	 */
	@Basic
	private int getMaxXPosition() {
		return maxXPosition;
	}
	
	/**
	 * Returns the x position on the canvas.
	 */
	@Basic
	public int getXPosition() {
		return this.xPos;
	}
	
	/**
	 * Returns whether the given coordinate is on the canvas.
	 * @param X_pos
	 * 			The given coordinate to check.
	 */
	private boolean isValidXPosition(double X_pos) {
		return (X_pos < (getMaxXPosition() - 1) &&  X_pos >= 0);		
	}
	
	
	
	private int xPos;
	
	/**
	 * Sets the y position of the bottom left pixel of a given Mazub to the specified y position.
	 * @param Y_pos
	 * 			The wanted y position.
	 * @throws RuntimeException
	 * 			Throws an exception when the wanted position isn't on the canvas.
	 * 			| !isValidYPosition(Y_pos)
	 */
	private void setYPosition(double Y_pos) throws RuntimeException{
		if (!isValidYPosition(Y_pos)) 
			throw new RuntimeException();
		this.yPos = (int) Y_pos * 100;
	}
	
	/**
	 * Returns the y position on the canvas.
	 */
	@Basic
	public int getYPosition() {
		return this.yPos;
	}
	
	/**
	 * Returns whether the given coordinate is on the canvas.
	 * @param X_pos
	 * 			The given coordinate to check.
	 */
	private boolean isValidYPosition(double Y_pos) {
		return (Y_pos < (getMaxYPosition() - 1) &&  Y_pos >= 0);
	}
	
	/**
	 * Returns the maximal y position on the canvas.
	 */
	@Basic
	private int getMaxYPosition() {
		return maxYPosition;
	}
	
	private int yPos;
	
	@Basic
	public String getOrientation() {
		return this.orientation;
	}
	
	private void setOrientation(String orientation) {
		
	}
	
	private String orientation;
	
	private double horizontalSpeed;
	
	@Basic
	public double getHorizontalSpeedMeters() {
		return this.horizontalSpeed;
	}
	
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
	
	public final double minSpeed;
	public final double maxSpeedRunning;
	public final double maxSpeedDucking;
	
	
	/**
	 * Returns the minimum horizontal speed.
	 */
	@Basic @Immutable
	public double getMinSpeedMeters() {
		return this.minSpeed;
	}
	
	@Basic @Immutable	
	public double getMaxSpeedRunningMeters() {
		return maxSpeedRunning;
	}
	@Basic @Immutable
	public double getMaxSpeedDuckingMeters() {
		return maxSpeedDucking;
	}
	
	private boolean isValidHorizontalSpeed() {
		if (this.getHorizontalSpeedMeters() != 0 || this.getHorizontalSpeedMeters() < this.getMinSpeedMeters()
				 || this.getHorizontalSpeedMeters() > this.getMaxSpeedRunningMeters())
			return false;
		return true;
	}
	
	private double verticalSpeed;
	
	@Basic
	public double getVerticalSpeedMeters() {
		return this.verticalSpeed;
	}
	
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
	
	public void startMove(Mazub alien) {
		assert alien.isValidAlien();
		
		if (alien.getHorizontalSpeedMeters() == 0)
			while (alien.getHorizontalSpeedMeters() < alien.getMaxSpeedRunningMeters())
				alien.setHorizontalSpeedMeters(alien.getHorizontalSpeedMeters() + acceleration*t);
		//TODO AdvanceTime
	}
	
	public void endMove(Mazub alien) {
		assert alien.isValidAlien();
		
		if (alien.getHorizontalSpeedMeters() != 0)
			alien.setHorizontalSpeedMeters(0);
	}
	
	private final double acceleration = 0.9;
	
	private boolean isValidAlien() {
		if (!isValidXPosition(this.xPos) || !isValidYPosition(this.yPos))
			return false;
		if (!isValidHorizontalSpeed() || !isValidVerticalSpeed())
			return false;
		return true;
	}
	
	public void startJump() throws RuntimeException {
		if (this.yPos != 0)
			throw new RuntimeException();
		else {
			this.setVerticalSpeedMeters(maxVerticalSpeed);
			this.fall();
		}
	}
	
	public void endJump() throws RuntimeException {
		if (this.yPos == 0)
			throw new RuntimeException();
		else {
			this.setVerticalSpeedMeters(0);
			this.fall();
		}
	}
	
	private void fall() {
		while (this.yPos != 0)
			this.setVerticalSpeedMeters(this.getVerticalSpeedMeters() - 10 * t);
	}
}
