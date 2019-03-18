package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;
import jdk.nashorn.internal.ir.SetSplitState;
import jumpingalien.util.Sprite;

public abstract class GameObject {

	GameObject(int pixelLeftX, int pixelBottomY, int pixelSizeX, int pixelSizeY, int hitpoints, Sprite... sprites){
		setYSize(pixelSizeY);
		setXSize(pixelSizeX);
		setXPositionPixel(pixelLeftX);
		setYPositionPixel(pixelBottomY);
		setSprite(sprites[0]);
		setSprites(sprites);
		setHitpoints(hitpoints);
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
	 * Returns whether the given coordinate is on the canvas.
	 */
	public boolean isValidPixelXPosition(int X_pos) {
		return (X_pos >= 0 && X_pos <= getMaxXPosition());
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
	
	public int getHitpoints() {
		return this.hitpoints;
	}
	
	private int hitpoints;
	
	private void changeHitPoints(int change) {
		setHitpoints(getHitpoints() + change);
	}
}
