package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
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
	setTimeToMove(0.5);
	setTimeToRest(0.0);
    }

    @Override
    public void advanceTime(double dt, double timeStep) {
	while (dt > timeStep && !isDead() && !isTerminated())
	    if (getTimeToRest() <= 0) {
		setTimeToRest(1.0);
		while (getTimeToMove() > 0.0)
		    if (getTimeToMove() > timeStep) {
			updatePosition(timeStep);
			dt -= timeStep;
			setTimeToMove(getTimeToMove() - timeStep);
		    } else if (getTimeToMove() == timeStep) {
			updatePosition(timeStep);
			dt -= timeStep;
			setTimeToMove(0.0);
		    } else {
			updatePosition(getTimeToMove());
			dt -= getTimeToMove();
			setTimeToMove(0.0);
		    }
	    } else {
		setTimeToMove(0.5);
		while (getTimeToRest() > 0.0)
		    if (getTimeToRest() > timeStep) {
			dt -= timeStep;
			setTimeToRest(getTimeToRest() - timeStep);
		    } else if (getTimeToRest() == timeStep) {
			dt -= timeStep;
			setTimeToRest(0.0);
		    } else {
			dt -= getTimeToRest();
			setTimeToRest(0.0);
		    }
	    }
    }

    private void updatePosition(double dt) {
	setXPositionActual(getXPositionActual() + 0.02 * dt);

    }

    private double timeMoving;
    private double timeResting;
    private double timeToMove;
    private double timeToRest;

    @Basic
    public double getTimeMoving() {
	return timeMoving;
    }

    private void setTimeMoving(double time) {
	timeMoving = time;
    }

    @Basic
    public double getTimeResting() {
	return timeResting;
    }

    private void setTimeResting(double time) {
	timeResting = time;
    }

    @Basic
    public double getTimeToMove() {
	return timeToMove;
    }

    private void setTimeToMove(double time) {
	timeToMove = time;
    }

    @Basic
    public double getTimeToRest() {
	return timeToRest;
    }

    private void setTimeToRest(double time) {
	timeToRest = time;
    }

}
