package jumpingalien.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

/**
 * A class that implements a school of slimes
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 * @version 1
 *
 */
public class School {

    /**
     * Creates a new school
     * 
     * @param world The world of the school
     * 
     * @post ... | new.getMaxID() == Long.MIN_VALUE
     * @post ... | new.getMnID() == Long.MAX_VALUE
     * @post ... | new.getMinIDSlime() == null
     * @post ... | new.getMaxIDSlime() == null
     * @post ... | new.getAllSlimes().size() == 0
     * @post ... | new.getWorld() == world
     * 
     * @post ... | if world != null then new.world.getAllSchools().contains(this)
     */
    public School(World world) {
	if (world != null) {
	    this.world = world;
	    getWorld().addSchool(this);
	}
	maxID = Long.MIN_VALUE;
	minID = Long.MAX_VALUE;
	minIDSlime = null;
	maxIDSlime = null;
	slimes = new TreeSet<>();
    }

    /**
     * Returns the minimum ID of all the slimes in the school.
     */
    public long getMinID() {
	return minID;
    }

    /**
     * Returns the maximum ID of all the slimes in the school.
     */
    public long getMaxID() {
	return maxID;
    }

    /**
     * Returns a list with all the slimes in the school.
     */
    public Collection<Slime> getAllSlimes() {
	final List<Slime> allSlimes = new ArrayList<>();

	for (final Slime slime : slimes)
	    allSlimes.add(slime);

	return allSlimes;

    }

    /**
     * Adds a slime to the school
     * 
     * @param slime The slime to add
     * 
     * @post ... | new.slime.getSchool() == this
     * @post ... | new.getAllSlimes().contains(slime)
     * 
     * @post ... | if slime.getID() < getMinID() then new.getMinID() ==
     *       slime.getID() and new.getMinIDSlime() == slime
     * @post ... | if slime.getID() > getMaxID() then new.getMaxID() ==
     *       slime.getID() and new.getMaxIDSlime() == slime
     */
    public void addSlime(Slime slime) {
	if (slime.getIdentification() < getMinID()) {
	    setMinID(slime.getIdentification());
	    setMinIDSlime(slime);
	}

	if (slime.getIdentification() > getMaxID()) {
	    setMaxID(slime.getIdentification());
	    setMaxIDSlime(slime);
	}
	slimes.add(slime);
	slime.school = this;
    }

    /**
     * Returns the slime with the highest ID in the school.
     */
    public Slime getMaxIDSlime() {
	return maxIDSlime;
    }

    /**
     * Sets the slime with the highest ID in the school.
     * 
     * @post ... | new.getMaxIDSlime() == slime
     */
    private void setMaxIDSlime(Slime slime) {
	maxIDSlime = slime;

    }

    /**
     * Returns the slime with the lowest ID in the school.
     */
    public Slime getMinIDSlime() {
	return minIDSlime;
    }

    /**
     * Sets the slime with the lowest ID in the school.
     * 
     * @post ... | new.getMinIDSlime() == slime
     */
    private void setMinIDSlime(Slime slime) {
	minIDSlime = slime;

    }

    /**
     * Sets the lowest ID in the school to the given ID
     * 
     * @param identification The ID to set
     * 
     * @post ... | new.getMinID() == identification
     */
    private void setMinID(long identification) {
	minID = identification;
    }

    /**
     * Sets the highest ID in the school to the given ID
     * 
     * @param identification The ID to set
     * 
     * @post ... | new.getMaxID() == identification
     */
    private void setMaxID(long identification) {
	maxID = identification;
    }

    /**
     * Returns the world of the school.
     */
    public World getWorld() {
	return world;
    }

    /**
     * Removes the slime from the school
     * 
     * @param slime The slime to remove
     * 
     * @post ... | new.slime.getSchool() == null
     * @post ... | !new.getAllSlimes().contains(slime)
     * 
     * @post ... | if slime.getID() == getMinID() then new.getMinID() !=
     *       slime.getID() and new.getMinIDSlime() != slime
     * @post ... | if slime.getID() == getMaxID() then new.getMaxID() !=
     *       slime.getID() and new.getMaxIDSlime() != slime
     */
    public void removeSlime(Slime slime) {
	slimes.remove(slime);

	if (slimes.size() != 0) {
	    if (slime.getIdentification() == getMinID()) {
		minID = getMaxID();
		for (final Slime slimy : getAllSlimes())
		    if (slime.getIdentification() < getMinID())
			setMinID(slimy.getIdentification());
	    }
	    if (slime.getIdentification() == getMaxID()) {
		maxID = getMinID();
		for (final Slime slimy : getAllSlimes())
		    if (slime.getIdentification() < getMaxID())
			setMaxID(slimy.getIdentification());
	    }
	} else {
	    setMaxID(0);
	    setMaxIDSlime(null);
	    setMinID(0);
	    setMinIDSlime(null);
	}
	slime.school = null;
    }

    /**
     * Returns whether a slime is in a school
     * 
     * @param slime The slime to check
     */
    public boolean hasSlime(Slime slime) {
	if (slime.getIdentification() == getMaxID() || slime.getIdentification() == getMinID())
	    return true;
	if (getAllSlimes().contains(slime))
	    return true;
	return false;
    }

    /**
     * Terminates the school.
     * 
     * @post ... | new.isTerminated()
     * @post ... | new.getAllSlimes().size() == 0
     * @post ... | new.getWorld() == null
     * @post ... | !new.world.getAllSchools().contains(this)
     * @post ... | for slime in getAllSlimes() : new.slime.isTerminated()
     */
    public void terminate() {
	isTerminated = true;
	for (final Slime slime : getAllSlimes())
	    slime.terminate();
	slimes = null;
	getWorld().removeSchool(this);
	world = null;

    }

    /**
     * Returns true if the school has a slime with the given id
     * 
     * @param id The ID to check
     */
    public boolean hasSlimeWithID(long id) {
	for (final Slime slime : getAllSlimes())
	    if (slime.getIdentification() == id)
		return true;
	return false;
    }

    /**
     * Returns whether the school is terminated
     */
    public boolean isTerminated() {
	return isTerminated;
    }

    /**
     * Sets the world of the school.
     * 
     * @param world The world to set
     * 
     * @post ... | new.getWorld() == world
     * @post ... | new.world.getAllSchools().contains(this)
     */
    public void setWorld(World world) {
	this.world = world;
	world.addSchool(this);
    }

    /**
     * Removes the world of the school.
     * 
     * @param world The world to remove
     * 
     * @post ... | new.getWorld() == null
     * @post ... | !new.world.getAllSchools().contains(this)
     */
    public void removeFromWorld() {
	world.removeSchool(this);
	world = null;
    }

    /**
     * A variable to store the minimum ID in the school.
     */
    private long minID;

    /**
     * A variable to store the slime with the minimum ID in the school.
     */
    private Slime minIDSlime;

    /**
     * A variable to store the maximum ID in the school.
     */
    private long maxID;

    /**
     * A variable to store the slime with the maximum ID in the school.
     */
    private Slime maxIDSlime;

    /**
     * A variable to store the world of this school.
     */
    World world = null;

    /**
     * A set to store all the slimes of this school.
     */
    private TreeSet<Slime> slimes;

    /**
     * A boolean to store if the school is terminated.
     */
    private boolean isTerminated;
}
