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
    private double timeSinceDeath;
    private double timeBeforeNextHitpointsChange = 0.0;
    private boolean contactWithGas;
    private boolean contactWithMagma;
    private boolean contactWithWater;
    private double timeInWater;
    private double timeInGas;

    public Slime(int pixelLeftX, int pixelBottomY, int pixelSizeX, int pixelSizeY, int hitpoints, int maxHitpoints,
	    double maxHorizontalSpeedRunning, double maxHorizontalSpeedDucking, double minHorizontalSpeed,
	    double maxVerticalSpeed, double horizontalAcceleration, double verticalAcceleration, boolean tempObject,
	    long id, School school, Sprite[] sprites) {
	super(pixelLeftX, pixelBottomY, pixelSizeX, pixelSizeY, hitpoints, maxHitpoints, maxHorizontalSpeedRunning,
		maxHorizontalSpeedDucking, minHorizontalSpeed, maxVerticalSpeed, horizontalAcceleration,
		verticalAcceleration, tempObject, sprites);
	if (!isValidSpriteArray(sprites))
	    throw new RuntimeException();
	this.id = id;
	setSchool(school);
	setOrientation(1);
	setHorizontalAcceleration(0.7);
	addID(id);
    }

    private static void addID(long id) {
	GameObject.allIDs.add(id);

    }

    void setSchool(School school) {
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
	if (!isDead()) {
	    double nextTimeStep = timeStep;
	    while (dt > nextTimeStep && !isDead() && !isTerminated()) {
		updatePosition(nextTimeStep);
		dt -= nextTimeStep;
		if (getWorld() != null)
		    nextTimeStep = World.getTimeStep(this, dt);
		else
		    nextTimeStep = timeStep;
	    }
	    updatePosition(dt);
	    dt = 0;
	} else if (getTimeSinceDeath() < 0.6) {
	    if (dt < 0.6 - getTimeSinceDeath())
		setTimeSinceDeath(dt + getTimeSinceDeath());
	    else {
		setTimeSinceDeath(dt + getTimeSinceDeath());
		getWorld().removeObject(this);
		terminate();
	    }

	} else if (dt < 0.6 - getTimeSinceDeath())
	    setTimeSinceDeath(dt + getTimeSinceDeath());
	else {
	    setTimeSinceDeath(dt + getTimeSinceDeath());
	    if (getWorld() != null)
		getWorld().removeObject(this);
	    terminate();
	}

    }

    private double getTimeBeforeNextHitpointsChange() {
	return timeBeforeNextHitpointsChange;
    }

    public void setTimeBeforeNextHitpointsChange(double dt) {
	timeBeforeNextHitpointsChange = dt;

    }

    private void setTimeSinceDeath(double d) {
	timeSinceDeath = d;

    }

    private double getTimeSinceDeath() {
	return timeSinceDeath;
    }

    private void updatePosition(double dt) {
	setHorizontalAcceleration(0.7);
	final double newPosX = getXPositionActual() + getOrientation() * Math.abs(getHorizontalSpeedMeters()) * dt
		+ 0.5 * getOrientation() * Math.abs(getHorizontalAcceleration()) * dt * dt;
	final double newPosY = getYPositionActual() + getVerticalSpeedMeters() * dt
		+ 0.5 * getVerticalAcceleration() * dt * dt;
	final double newSpeedX = getOrientation() * Math.abs(getHorizontalSpeedMeters())
		+ getOrientation() * Math.abs(getHorizontalAcceleration()) * dt;
	final double newSpeedY = getVerticalSpeedMeters() + getVerticalAcceleration() * dt;
	final int xSize = getXsize();
	final int ySize = getYsize();

	if (getOrientation() == 1)
	    setSprite(getSpriteArray()[0]);
	if (getOrientation() == -1)
	    setSprite(getSpriteArray()[1]);

	if (getWorld() != null) {
	    long id = 1;
	    while (hasSlimeWithID(id))
		id += 1;
	    final Slime newSlime = new Slime((int) (newPosX * 100), (int) (newPosY * 100), xSize, ySize, 1, 1, 0, 0, 0,
		    0, 0, 0, true, id, getSchool(), getSpriteArray());
	    if (getWorld().canPlaceGameObjectAdvanceTime(newSlime, this)) {
		setXPositionActual(newPosX);
		setYPositionActual(newPosY);
		setHorizontalSpeedMeters(newSpeedX);
		setVerticalSpeedMeters(newSpeedY);
	    } else {
		setHorizontalSpeedMeters(0);
		setVerticalSpeedMeters(0);
		setHorizontalAcceleration(0.7);
	    }

	    if (getWorld() != null) {
		for (final School school : getWorld().getAllSchools())
		    for (final Slime slime : school.getAllSlimes())
			if (newSlime.collidesWith(slime) && this != slime && newSlime != slime) {
			    setOrientation(getOrientation() * -1);
			    if (getSchool().getAllSlimes().size() < slime.getSchool().getAllSlimes().size())
				switchSchool(slime.getSchool());

			}

		if (getWorld().getPlayer() != null)
		    if (newSlime.collidesWith(getWorld().getPlayer()))
			if (getTimeBeforeNextHitpointsChange() <= 0) {
			    changeHitPoints(-30);
			    changeSchoolHitPoints();
			    setTimeBeforeNextHitpointsChange(0.6);
			}
//			setHorizontalSpeedMeters(0);

	    }
	    newSlime.terminate();
	}

	if (getOrientation() == 1)
	    setSprite(getSpriteArray()[0]);
	if (getOrientation() == -1)
	    setSprite(getSpriteArray()[1]);

	if (getWorld() != null)
	    setContactTiles();
	else {
	    contactWithGas = false;
	    contactWithMagma = false;
	    contactWithWater = false;
	}

	if (contactWithWater)
	    timeInWater += dt;
	else
	    timeInWater = 0;

	if (contactWithGas)
	    timeInGas += dt;
	else
	    timeInGas = 0;

	if (timeInWater >= 0.4) {
	    timeInWater -= 0.4;
	    if (!contactWithMagma) {
		changeHitPoints(-4);
		changeSchoolHitPoints();
	    }
	}

	if (timeInGas >= 0.3) {
	    timeInGas -= 0.3;
	    if (!contactWithMagma)
		changeHitPoints(2);
	}
	if (contactWithMagma) {
	    isDead = true;
	    timeSinceDeath = 0.0;
	}

	if (getHitpoints() == 0)
	    isDead = true;

	setTimeBeforeNextHitpointsChange(getTimeBeforeNextHitpointsChange() - dt);
    }

    private void changeSchoolHitPoints() {
	if (getSchool() != null)
	    for (final Slime slime : getSchool().getAllSlimes())
		if (slime != this)
		    slime.changeHitPoints(-1);

    }

    private void setContactTiles() {
	contactWithGas = false;
	contactWithMagma = false;
	contactWithWater = false;
	for (final int[] tile : getAllOverlappingTiles()) {
	    if (getWorld().getGeologicalFeatureTile(tile) == PassableTerrain.GAS.getValue())
		contactWithGas = true;
	    if (getWorld().getGeologicalFeatureTile(tile) == PassableTerrain.MAGMA.getValue())
		contactWithMagma = true;
	    if (getWorld().getGeologicalFeatureTile(tile) == PassableTerrain.WATER.getValue())
		contactWithWater = true;
	}

    }

    /**
     * @post ... | sprites.length == 2
     */
    @Override
    public boolean isValidSpriteArray(Sprite[] sprites) {
	return sprites.length == 2;
    }
}
