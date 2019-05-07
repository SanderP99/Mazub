package jumpingalien.model;

import java.util.HashSet;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
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
public class Slime extends GameObject implements Comparable<Slime>, HorizontalMovement, VerticalMovement {

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
	addID(id);
    }

    private void addID(long id2) {
	GameObject.allIDs.add(id2);

    }

    void setSchool(School school) {
//	for (final Slime slime : school.getAllSlimes())
//	    slime.changeHitPoints(-1);

	if (school != null) {
	    school.addSlime(this);
	    this.school = school;
	}

    }

    @Basic
    @Immutable
    public long getIdentification() {
	return id;
    }

    @Basic
    public School getSchool() {
	return school;
    }

    public void removeSchool() {
	if (getSchool() != null)
	    getSchool().removeSlime(this);

	school = null;
    }

    @Override
    public int compareTo(Slime other) {
	return Long.compare(getIdentification(), other.getIdentification());
    }

    public static void cleanAllIds() {
	GameObject.allIDs = new HashSet<Long>();
    }

    public void switchSchool(School newSchool) {
	final School oldSchool = getSchool();
	removeSchool();

	for (final Slime slime : oldSchool.getAllSlimes())
	    slime.changeHitPoints(1);

	changeHitPoints(-oldSchool.getAllSlimes().size());

	for (final Slime slime : newSchool.getAllSlimes())
	    slime.changeHitPoints(-1);

	changeHitPoints(newSchool.getAllSlimes().size());

	setSchool(newSchool);
    }

    @Override
    public void advanceTime(double dt, double timeStep) {
	// TODO Auto-generated method stub

    }

}
