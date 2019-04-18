package jumpingalien.model;

import jumpingalien.util.Sprite;

public class Sneezewort extends Plant {



    public Sneezewort(int positionLeftX, int positionBottomY, int pixelSizeX, int pixelSizeY, double horizontalSpeed,
	    int hitpoints, double secondsToLive, double maxHorizontalSpeedRunning, double maxHorizontalSpeedDucking,
	    double minHorizontalSpeed, double horizontalAcceleration, double verticalAcceleration, Sprite[] sprites) {
	super(positionLeftX, positionBottomY, pixelSizeX, pixelSizeY, horizontalSpeed, hitpoints, secondsToLive,
		maxHorizontalSpeedRunning, maxHorizontalSpeedDucking, minHorizontalSpeed, horizontalAcceleration,
		verticalAcceleration, sprites);

    }

}
