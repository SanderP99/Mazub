package jumpingalien.model;

/**
 * A class that implements a player character with the abaility to jump, to run to the left and to the right.
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 *
 */
public class Mazub {

	/**
	 * Initialize a new player with a given position (X_pos, Y_pos) and a given size (X_size, Y_size).
	 * 
	 * @param X_pos 
	 * 			The x position of the bottom left pixel of Mazub.
	 * @param Y_pos
	 * 			The y position of the bottom left pixel of Mazub.
	 * @param X_size
	 * 			The x size of Mazub given in pixels.
	 * @param Y_size
	 * 			The y size of Mazub given in pixels.
	 */
	public Mazub(double X_pos, double Y_pos, int X_size, int Y_size, double horizontalSpeed, double minSpeed, double maxSpeed) {
		setYSize(Y_size);
		setXSize(X_size);
		setXPosition(X_pos);
		setYPosition(Y_pos);
		setHorizontalSpeed(horizontalSpeed);
		setMinSpeed(minSpeed);
		setMaxSpeed(maxSpeed);
	}
	
	public Mazub() {
		setYSize(2);
		setXSize(1);
		setXPosition(0);
		setYPosition(0);
		setHorizontalSpeed(0);
		setMinSpeed(1);
		setMaxSpeed(3);
	}
	
	private int maxXPosition = 1024;
	private int maxYPosition = 768;
	
	private void setYSize(int Y_size) {
		this.ySize = Y_size;
	}
	
	public int getYsize() {
		return this.ySize;
	}
	
	private int ySize;
	
	private void setXSize(int X_size) {
		this.xSize = X_size;
	}
	
	public int getXsize() {
		return this.xSize;
	}
	
	private int xSize;
	
	private void setXPosition(double X_pos) throws RuntimeException{
		if (!isValidXPosition(X_pos))
			throw new RuntimeException();
		this.xPos = (int) X_pos * 100;
	}
	
	private int getMaxXPosition() {
		return maxXPosition;
	}
	
	public int getXPosition() {
		return this.xPos;
	}
	
	private boolean isValidXPosition(double X_pos) {
		return (X_pos < (getMaxXPosition() - 1) &&  X_pos >= 0);		
	}
	
	
	
	private int xPos;
	
	private void setYPosition(double Y_pos) throws RuntimeException{
		if (!isValidYPosition(Y_pos)) 
			throw new RuntimeException();
		this.yPos = (int) Y_pos * 100;
	}
	
	public int getYPosition() {
		return this.yPos;
	}
	
	
	private boolean isValidYPosition(double Y_pos) {
		return (Y_pos < (getMaxYPosition() - 1) &&  Y_pos >= 0);
	}
	
	private int getMaxYPosition() {
		return maxYPosition;
	}
	
	private int yPos;
	
	public String getOrientation() {
		return this.orientation;
	}
	
	private void setOrientation(String orientation) {
		
	}
	
	private String orientation;
	
	private double horizontalSpeed;
	
	public double getHorizontalSpeed() {
		return this.horizontalSpeed;
	}
	
	private void setHorizontalSpeed(double speed) {
		if (Math.abs(speed) < getMinSpeed() && speed < 0)
			this.horizontalSpeed = -1*getMinSpeed();
		else if (Math.abs(speed) < getMinSpeed() && speed > 0)
			this.horizontalSpeed = getMinSpeed();
		else if (speed == 0)
			this.horizontalSpeed = 0;
		else if (Math.abs(speed) > getMaxSpeed() && speed > 0)
			this.horizontalSpeed = getMaxSpeed();
		else
			this.horizontalSpeed = -1*getMaxSpeed();
	}
	
	private double minSpeed;
	private double maxSpeed;
	
	public double getMinSpeed() {
		return this.minSpeed;
	}
	
	private void setMinSpeed(double speed) {
		this.minSpeed = Math.abs(speed);
	}
	
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	
	private void setMaxSpeed(double speed) {
		this.maxSpeed = Math.abs(speed);
	}
}
