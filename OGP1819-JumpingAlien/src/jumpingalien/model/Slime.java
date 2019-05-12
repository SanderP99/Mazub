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

    /**
     * Creates a new slime
     * 
     * @param pixelLeftX                The x coordinate of the most left pixel
     * @param pixelBottomY              The y coordinate of the bottom most pixel
     * @param pixelSizeX                The width of the slime in pixels
     * @param pixelSizeY                The height of the slime in pixels
     * @param hitpoints                 The hitpoints of the slime
     * @param maxHitpoints              The maximum hitpoints of the slime
     * @param maxHorizontalSpeedRunning The maximum horizontalSpeed of the slime
     * @param maxHorizontalSpeedDucking The maximum horizontalSpeed of the slime
     * @param minHorizontalSpeed        The minimum horizontalSpeed of the slime
     * @param maxVerticalSpeed          The maximum verticalSpeed of the slime
     * @param horizontalAcceleration    The horizontal acceleration of the slime
     * @param verticalAcceleration      The vertical acceleration of the slime
     * @param tempObject                A boolean to store if the object is a
     *                                  temporary object
     * @param id                        The unique id of the slime
     * @param school                    The school of the slime
     * @param sprites                   The sprites of the slime
     * 
     * @pre ... | sprites.length == 2
     * 
     * @effect super(pixelLeftX, pixelBottomY, pixelSizeX, pixelSizeY, hitpoints,
     *         maxHitpoints, maxHorizontalSpeedRunning, maxHorizontalSpeedDucking,
     *         minHorizontalSpeed, maxVerticalSpeed, horizontalAcceleration,
     *         verticalAcceleration, tempObject, sprites)
     * @effect addID(id)
     * 
     * @post ... | new.getSchool() == school
     * @post ... | new.getIdentification() == id
     * @post ... | new.getOrientation() == 1
     * @post ... | new.getHorizontalAcceleration() == 0.7
     */
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

    /**
     * Adds the id of a slime to a list with all the IDs in use
     * 
     * @param id The id to add
     * 
     * @post ... | new.allIDs.contains(id)
     */
    private static void addID(long id) {
	GameObject.allIDs.add(id);

    }

    /**
     * A varaible to store the unique ID of the slime
     */
    private final long id;

    /**
     * Returns the ID of the slime
     */
    @Basic
    @Immutable
    public long getIdentification() {
	return id;
    }

    /**
     * Removes all the used IDs from the list
     * 
     * @post ... | new.allIDs.size() == 0
     */
    public static void cleanAllIds() {
	GameObject.allIDs = new HashSet<Long>();
    }

    /**
     * A variable to store the school of the slime
     */
    School school;

    /**
     * Returns the school of the slime
     */
    @Basic
    public School getSchool() {
	return school;
    }

    /**
     * Sets the school of the slime
     * 
     * @param school The school to set
     * 
     * @post ... | new.getSchool() == school
     * @effect school.addSlime(this)
     */
    void setSchool(School school) {
	if (school != null) {
	    school.addSlime(this);
	    this.school = school;
	}

    }

    /**
     * Removes the school from the slime
     * 
     * @post ... | new.getSchool() == null
     * 
     * @effect school.removeSlime(this)
     */
    public void removeSchool() {
	if (getSchool() != null)
	    getSchool().removeSlime(this);

	school = null;
    }

    /**
     * Compares the ID of two slimes to store thel in a treeset
     */
    @Override
    public int compareTo(Slime other) {
	return Long.compare(getIdentification(), other.getIdentification());
    }

    /**
     * Switches the slime from school
     * 
     * @param newSchool The school to switch to
     * 
     * @post ... | new.getSchool() == newSchool
     * @post ... | !getSchool().getAllSlimes().contains(this)
     * @post ... | newSchool.getAllSlimes().contains(this)
     * @post ... | getSchool.getAllSlimes.new.getHitPoints() == getHitPoints() + 1
     * @post ... | new.getSchool.getAllSlimes.new.getHitPoints() == getHitPoints() -
     *       1
     * @post ... | new.getHitPoints() == getHitpoints() -
     *       getSchool.getAllSlimes().size() + new.getSchool().getAllSlimes().size()
     */
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

    /*
     * A variable to store the time before the slime can collide with Mazub again
     */
    private double timeBeforeNextHitpointsChange = 0.0;

    /**
     * Returns the time before the next hitpoints change
     */
    private double getTimeBeforeNextHitpointsChange() {
	return timeBeforeNextHitpointsChange;
    }

    /**
     * Sets the time before the next hitpoints change to the given time
     * 
     * @param dt The time to change to
     */
    public void setTimeBeforeNextHitpointsChange(double dt) {
	timeBeforeNextHitpointsChange = dt;

    }

    /*
     * A variable to store the time since the slime has died
     */
    private double timeSinceDeath;

    /*
     * Returns the time since the slime is dead
     */
    private double getTimeSinceDeath() {
	return timeSinceDeath;
    }

    /**
     * Sets the time since the slime is dead
     * 
     * @param d The time to set
     */
    private void setTimeSinceDeath(double d) {
	timeSinceDeath = d;

    }

    private int xPositionMazub = Integer.MAX_VALUE;

    private void updatePosition(double dt) {
	setHorizontalAcceleration(0.7);

	double newPosX = getXPositionActual() + getOrientation() * Math.abs(getHorizontalSpeedMeters()) * dt
		+ 0.5 * getOrientation() * Math.abs(getHorizontalAcceleration()) * dt * dt;
	final double newPosY = getYPositionActual() + getVerticalSpeedMeters() * dt
		+ 0.5 * getVerticalAcceleration() * dt * dt;
	double newSpeedX = getOrientation() * Math.abs(getHorizontalSpeedMeters())
		+ getOrientation() * Math.abs(getHorizontalAcceleration()) * dt;
	final double newSpeedY = getVerticalSpeedMeters() + getVerticalAcceleration() * dt;
	final int xSize = getXsize();
	final int ySize = getYsize();

	if (getWorld() != null && getWorld().getPlayer() != null)
	    if (xPositionMazub == getWorld().getPlayer().getXPositionPixel()) {
		newPosX = getXPositionActual();
		newSpeedX = 0.0;
	    }

	if (getOrientation() == 1)
	    setSprite(getSpriteArray()[0]);
	if (getOrientation() == -1)
	    setSprite(getSpriteArray()[1]);

	if (getWorld() != null) {
	    long id = 1;
	    while (hasSlimeWithID(id))
		id += 1;
	    final Slime newSlime = new Slime((int) (newPosX * 100), (int) (newPosY * 100), xSize, ySize, 1, 1, 0, 0, 0,
		    0, 0, 0, true, id, null, getSpriteArray());
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
		for (final Object slime : getWorld().getAllObjects())
		    if (slime instanceof Slime)
			if (newSlime.collidesWith((GameObject) slime) && this != slime && newSlime != slime) {
			    setOrientation(getOrientation() * -1);
			    if (((Slime) slime).getSchool() != null && getSchool() != null)
				if (getSchool().getAllSlimes().size() < ((Slime) slime).getSchool().getAllSlimes()
					.size())
				    switchSchool(((Slime) slime).getSchool());

			}

		if (getWorld().getPlayer() != null)
		    if (newSlime.collidesWith(getWorld().getPlayer())) {
			if (getTimeBeforeNextHitpointsChange() <= 0) {
			    changeHitPoints(-30);
			    changeSchoolHitPoints();
			    setTimeBeforeNextHitpointsChange(0.6);

			}
			xPositionMazub = getWorld().getPlayer().getXPositionPixel();
		    }
	    }
	    newSlime.terminate();
	}

	if (getOrientation() == 1)
	    setSprite(getSpriteArray()[0]);
	if (getOrientation() == -1)
	    setSprite(getSpriteArray()[1]);

	updateHitpoints(dt);

	setTimeBeforeNextHitpointsChange(getTimeBeforeNextHitpointsChange() - dt);
    }

    /**
     * A boolean to store if the slime is in contact with gas
     */
    private boolean contactWithGas;

    /**
     * A variable to store the time in contact with gas
     */
    private double timeInGas;

    /**
     * A boolean to store if the slime is in contact with magma
     */
    private boolean contactWithMagma;

    /**
     * A boolean to store if the slime is in contact with water
     */
    private boolean contactWithWater;

    /**
     * A variable to store the time in contact with water
     */
    private double timeInWater;

    private void updateHitpoints(double dt) {
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
	for (final Sprite sprite : sprites)
	    if (!sprite.canHaveAsHeight(sprite.getHeight()) || !sprite.canHaveAsName(sprite.getName())
		    || !sprite.canHaveAsWidth(sprite.getWidth()))
		return false;
	return sprites.length == 2;
    }
}
