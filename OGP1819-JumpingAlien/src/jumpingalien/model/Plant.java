package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import jumpingalien.util.Sprite;

public class Plant extends GameObject{
	
	private int[] boundaries;

	public Plant(int positionLeftX, int positionBottomY, int pixelSizeX, int pixelSizeY, double horizontalSpeed, int hitpoints, 
			double secondsToLive,  double maxHorizontalSpeedRunning, 
			double maxHorizontalSpeedDucking, double minHorizontalSpeed, double horizontalAcceleration, double verticalAcceleration, 
			Sprite... sprites){
		
		super (positionLeftX, positionBottomY, pixelSizeX, pixelSizeY, hitpoints,  maxHorizontalSpeedRunning, 
				maxHorizontalSpeedDucking, minHorizontalSpeed, 0, horizontalAcceleration, verticalAcceleration,false, sprites);
		
		setHorizontalSpeedMeters(-1*Math.abs(horizontalSpeed));
		setOrientation(-1);
		setSecondsToLive(secondsToLive);
		this.setBoundaries();
	} 
	
	private void setSecondsToLive(double secondsToLive) {
		this.secondsToLive = secondsToLive;
	}
	
	@Basic
	public double getSecondsToLive() {
		return this.secondsToLive;
	}
	
	private double secondsToLive;
	

	public int[] getBoundaries() {
		return boundaries;
	}


	public void setBoundaries() {
		this.boundaries = new int[] { (int) (getXPositionPixel() - 0.5 * Math.abs(getHorizontalSpeedPixels())) ,getXPositionPixel() };
	}
	
	/**
	 * Returns whether an array of sprites has valid dimensions
	 * @param sprites The array to check
	 */
	public boolean isValidSpriteArray(Sprite ... sprites) {
		return (sprites.length == 2);		
	}


	@Override
	public void advanceTime(double dt, double timeStep) {
		if (!isDead()) {
			while (dt >= timeStep && !isDead()) {
				if (getSecondsToLive() >= timeStep) {
					if (getOrientation() == -1) {
						if (getXPositionActual() - Math.abs(getHorizontalSpeedMeters()) * timeStep < (double) (getBoundaries()[0]) / 100) {
							double newPosX = getXPositionActual() - Math.abs(getHorizontalSpeedMeters()) * timeStep;
							double actualPosX = (double) getBoundaries()[0] / 100 + Math.abs(newPosX - (double) getBoundaries()[0] / 100);
							setXPositionActual(actualPosX);
							setOverlappingTiles();
							
							setOrientation(1);
							setSprite(getSpriteArray()[1]);
							dt -= timeStep;
							setSecondsToLive(getSecondsToLive() - timeStep);
						}
						else {
							dt -= timeStep;
							setSecondsToLive(getSecondsToLive() - timeStep);
							setXPositionActual(getXPositionActual() - Math.abs(getHorizontalSpeedMeters()) * timeStep);
							setOverlappingTiles();
						}
					}
					else {
						if (getXPositionActual() + Math.abs(getHorizontalSpeedMeters()) * timeStep > (double) getBoundaries()[1] / 100) {
							double newPosX = getXPositionActual() + Math.abs(getHorizontalSpeedMeters()) * timeStep;
							double actualPosX = (double) getBoundaries()[1] / 100 - Math.abs(newPosX - (double) getBoundaries()[1] / 100);
							setXPositionActual(actualPosX);
							setOverlappingTiles();
							
							setOrientation(-1);
							setSprite(getSpriteArray()[0]);
							dt -= timeStep;
							setSecondsToLive(getSecondsToLive() - timeStep);
							
					} 
						else {
							dt -= timeStep;
							setSecondsToLive(getSecondsToLive() - timeStep);
							if (getOrientation() == 1)
								setXPositionActual(getXPositionActual() + Math.abs(getHorizontalSpeedMeters()) * timeStep);
							else
								setXPositionActual(getXPositionActual() - Math.abs(getHorizontalSpeedMeters()) * timeStep);
							setOverlappingTiles();
						}
					}
					
						
				}
				else {
					dt = 0;
					setXPositionActual(getXPositionActual() + getHorizontalSpeedMeters() * getSecondsToLive());
					setOverlappingTiles();
					setSecondsToLive(0);
					this.isDead = true;
					timeSinceDeath = 0;
				}
			}
			if (getOrientation() == 1)
				setXPositionActual(getXPositionActual() + Math.abs(getHorizontalSpeedMeters()) * dt);
			else
				setXPositionActual(getXPositionActual() - Math.abs(getHorizontalSpeedMeters()) * dt);
			setSecondsToLive(getSecondsToLive() - dt);
			setOverlappingTiles();
			
		}
		else if (timeSinceDeath < 0.6){
			if (dt < 0.599 - timeSinceDeath) {
				timeSinceDeath += dt;
			}
			else {
				timeSinceDeath += dt;
				this.getWorld().removeObject(this);
				terminate();
			}
			
		}
		else {
			this.getWorld().removeObject(this);
			terminate();
		}
	}


	double timeSinceDeath;

}
