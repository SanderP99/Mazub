package jumpingalien.model;

import jumpingalien.util.Sprite;

public class Slime extends School {

    private final long id;
    private School school;

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

    private void setSchool(School school) {
	this.school = school;
	school.addSlime(this);

    }

    public long getIdentification() {
	return id;
    }

    public School getSchool() {
	return school;
    }

    @Override
    public void advanceTime(double dt, double timeStep) {
	// TODO Auto-generated method stub

    }

}
