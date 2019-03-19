package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.Sprite;

public class Plant extends GameObject{
	
	public Plant(int positionLeftX, int positionBottomY, int pixelSizeX, int pixelSizeY, double horizontalSpeed, int hitpoints, 
			int secondsToLive,  double maxHorizontalSpeedRunning, 
			double maxHorizontalSpeedDucking, double minHorizontalSpeed, double horizontalAcceleration, double verticalAcceleration, 
			Sprite... sprites){
		
		super (positionLeftX, positionBottomY, pixelSizeX, pixelSizeY, hitpoints,  maxHorizontalSpeedRunning, 
				maxHorizontalSpeedDucking, minHorizontalSpeed, horizontalAcceleration, verticalAcceleration, sprites);
		
		setHorizontalSpeedMeters(-1*Math.abs(horizontalSpeed));
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
	
	
}
