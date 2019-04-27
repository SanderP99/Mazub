package jumpingalien.model;

import jumpingalien.util.Sprite;

/**
 * A class that implements a slime
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 * @version 1
 *
 */
public class Slime extends GameObject implements Comparable<Slime> {

    private final long id;
    School school;

    public Slime(int pixelLeftX, int pixelBottomY, int pixelSizeX, int pixelSizeY, int hitpoints, int maxHitpoints,
	    double maxHorizontalSpeedRunning, double maxHorizontalSpeedDucking, double minHorizontalSpeed,
	    double maxVerticalSpeed, double horizontalAcceleration, double verticalAcceleration, boolean tempObject,
	    long id, School school, Sprite[] sprites) {
	super(pixelLeftX, pixelBottomY, pixelSizeX, pixelSizeY, hitpoints, maxHitpoints, maxHorizontalSpeedRunning,
		maxHorizontalSpeedDucking, minHorizontalSpeed, maxVerticalSpeed, horizontalAcceleration,
		verticalAcceleration, tempObject, sprites);
	this.id = id;
	setSchool(school);
	setOrientation(1);
	setHorizontalAcceleration(0.7);
    }

    void setSchool(School school) {
	school.addSlime(this);
	this.school = school;
//	setWorld(this.school.getWorld());
    }

    public long getIdentification() {
	return id;
    }

    public School getSchool() {
	return school;
    }

    public void removeSchool() {
	getSchool().removeSlime(this);
	school = null;
    }

    @Override
    public int compareTo(Slime other) {
	return Long.compare(getIdentification(), other.getIdentification());
    }

//    @Override
//    public void advanceTime(double dt, double timeStep) {
//	final double newPosX = getXPositionActual() + getHorizontalSpeedMeters() * dt
//		+ 0.5 * getHorizontalAcceleration() * dt * dt;
//	final double newPosY = getYPositionActual() + getVerticalSpeedMeters() * dt
//		+ 0.5 * getVerticalAcceleration() * dt * dt;
//	final double newSpeedX = getHorizontalSpeedMeters() + getHorizontalAcceleration() * dt;
//	final double newSpeedY = getVerticalSpeedMeters() + getVerticalAcceleration() * dt;
//	final int xSize = getXsize();
//	final int ySize = getYsize();
//
//	if (getWorld() != null) {
//	    final Slime newSlime = new Slime((int) (newPosX * 100), (int) (newPosY * 100), xSize, ySize, 100, 100, 0.0,
//		    0.0, 0.0, 0.0, 2.5, 0.7, true, getIdentification(), getSchool(), getSpriteArray());
//	    if (getWorld().canPlaceGameObjectAdvanceTime(newSlime, this)) {
//		setXPositionActual(newPosX);
//		setYPositionActual(newPosY);
//		setHorizontalSpeedMeters(newSpeedX);
//		setVerticalSpeedMeters(newSpeedY);
//	    } else {
//		setHorizontalSpeedMeters(0);
//		setVerticalSpeedMeters(0);
//		setHorizontalAcceleration(0);
//	    }
//	    if (isStandingOnImpassableTerrain())
//		setVerticalAcceleration(0);
//	    else
//		fall();
//	}
//    }

    @Override
    public void advanceTime(double dt, double timeStep) {
	if (!isDead())
	    while (dt > timeStep && !isDead() && !isTerminated()) {
		updatePosition(timeStep);
		dt -= timeStep;

	    }
	updatePosition(dt);
    }

    private void updatePosition(double dt) {
	final double newPosX = getXPositionActual() + getHorizontalSpeedMeters() * dt
		+ 0.5 * getOrientation() * getHorizontalAcceleration() * dt * dt;
	final double newPosY = getYPositionActual() + getVerticalSpeedMeters() * dt
		+ 0.5 * getVerticalAcceleration() * dt * dt;
	final double newSpeedX = getHorizontalSpeedMeters() + getHorizontalAcceleration() * dt;
	final double newSpeedY = getVerticalSpeedMeters() + getVerticalAcceleration() * dt;
	final int xSize = getXsize();
	final int ySize = getYsize();

	if (getWorld() != null) {
	    final Slime newSlime = new Slime((int) (newPosX * 100), (int) (newPosY * 100), xSize, ySize, 100, 100, 0.0,
		    0.0, 0.0, 0.0, 2.5, 0.7, true, getIdentification(), getSchool(), getSpriteArray());
	    if (getWorld().canPlaceGameObjectAdvanceTime(newSlime, this)) {
		setXPositionActual(newPosX);
		setYPositionActual(newPosY);
		setHorizontalSpeedMeters(newSpeedX);
		setVerticalSpeedMeters(newSpeedY);

	    } else {
		setHorizontalSpeedMeters(0);
		setVerticalSpeedMeters(0);
//		setOrientation(-1 * getOrientation());
		setHorizontalAcceleration(0);

//		setHorizontalAcceleration(-0.7);
//		setSprite(getSpriteArray()[2]);
	    }

//	    for (final Object object : getWorld().getAllObjects())
//		if (((GameObject) object).collidesWith(newSlime)) {
//		    setOrientation(getOrientation() * -1);
//		    setHorizontalAcceleration(0.7 * getOrientation());
//		}

	    if (isStandingOnImpassableTerrain()) {
		setVerticalAcceleration(0);
		setHorizontalAcceleration(0.7);
	    } else
		fall();
	}
    }

    public boolean canPlaceSlimeAdvanceTimeWalls() {
	for (final ImpassableTerrain feature : ImpassableTerrain.values()) {
	    for (int x = getXPositionPixel(); x < getXPositionPixel() + getXsize(); x++) {
		if (getWorld().getGeologicalFeature(x, getYPositionPixel() + 1) == feature.getValue())
		    return false;
		if (getWorld().getGeologicalFeature(x, getYPositionPixel() + getYsize()) == feature.getValue())
		    return false;
	    }
	    for (int y = getYPositionPixel() + 1; y < getYPositionPixel() + getYsize(); y++) {
		if (getWorld().getGeologicalFeature(getXPositionPixel(), y) == feature.getValue())
		    return false;
		if (getWorld().getGeologicalFeature(getXPositionPixel() + getXsize() - 1, y) == feature.getValue())
		    return false;
	    }
	}
	return true;
    }

    public boolean canPlaceSlimeAdvanceTimeSlimes() {
	for (final Object object : getWorld().getAllObjects())
	    if (collidesWith((GameObject) object) && object instanceof Slime && this != object)
		return false;
	return true;
    }
}
