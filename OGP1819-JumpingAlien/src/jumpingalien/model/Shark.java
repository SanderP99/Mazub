package jumpingalien.model;

import jumpingalien.util.Sprite;

public class Shark extends GameObject {

    public Shark(int pixelLeftX, int pixelBottomY, int pixelSizeX, int pixelSizeY, int hitpoints, int maxHitpoints,
	    double maxHorizontalSpeedRunning, double maxHorizontalSpeedDucking, double minHorizontalSpeed,
	    double maxVerticalSpeed, double horizontalAcceleration, double verticalAcceleration, boolean tempObject,
	    Sprite[] sprites) {
	super(pixelLeftX, pixelBottomY, pixelSizeX, pixelSizeY, hitpoints, maxHitpoints, maxHorizontalSpeedRunning,
		maxHorizontalSpeedDucking, minHorizontalSpeed, maxVerticalSpeed, horizontalAcceleration,
		verticalAcceleration, tempObject, sprites);
	setSprite(getSpriteArray()[0]);
    }

    @Override
    public void advanceTime(double dt, double timeStep) {
	// TODO Auto-generated method stub

    }

}
