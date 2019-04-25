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
    }

    void setSchool(School school) {
	school.addSlime(this);
	this.school = school;

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

    @Override
    public void advanceTime(double dt, double timeStep) {
	// TODO Auto-generated method stub

    }

}
