package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.Sprite;

public class Plant extends GameObject{
	
	public Plant(int positionLeftX, int positionBottomY, int pixelSizeX, int pixelSizeY, double horizontalSpeed, int hitpoints, 
			int secondsToLive,  double maxHorizontalSpeedRunning, 
			double maxHorizontalSpeedDucking, double minHorizontalSpeed, double horizontalAcceleration, double verticalAcceleration, 
			Sprite... sprites){
		
		super (positionLeftX, positionBottomY, pixelSizeX, pixelSizeY, hitpoints,  maxHorizontalSpeedRunning, 
				maxHorizontalSpeedDucking, minHorizontalSpeed, 0, horizontalAcceleration, verticalAcceleration, sprites);
		
		setHorizontalSpeedMeters(-1*Math.abs(horizontalSpeed));
		setOrientation(-1);
		setSecondsToLive(secondsToLive);
	} 

	
	@Basic
	public double getVelocity() {
		return this.getHorizontalSpeedMeters();
	}
	
	private void setSecondsToLive(int secondsToLive) {
		this.secondsToLive = secondsToLive;
	}
	
	@Basic
	public int getSecondsToLive() {
		return this.secondsToLive;
	}
	
	private int secondsToLive;
	
	/**
	 * Returns whether an array of sprites has valid dimensions
	 * @param sprites The array to check
	 */
	public boolean isValidSpriteArray(Sprite ... sprites) {
		return (sprites.length == 2);		
	}
	
	
}
