package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.Sprite;

public class Plant extends GameObject{
	
	public Plant(int positionLeftX, int positionBottomY, int pixelSizeX, int pixelSizeY, double horizontalSpeed, int hitpoints, int secondsToLive,  Sprite... sprites){
		super (positionLeftX, positionBottomY, pixelSizeX, pixelSizeY, hitpoints,  sprites);
		setVelocity(-1* Math.abs(horizontalSpeed));
		setSecondsToLive(secondsToLive);
	} 

	private void setVelocity(double horizontalSpeed) {
		this.velocity = horizontalSpeed;
	}
	
	@Basic
	public double getVelocity() {
		return this.velocity;
	}
	
	private double velocity;
	
	private void setSecondsToLive(int secondsToLive) {
		this.secondsToLive = secondsToLive;
	}
	
	@Basic
	public int getSecondsToLive() {
		return this.secondsToLive;
	}
	
	private int secondsToLive;
	
	
}
