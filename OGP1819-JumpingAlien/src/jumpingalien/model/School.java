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

    private long minID;
    private long maxID;
    private World world;
    private TreeSet<Slime> slimes;
    private Slime minIDSlime;
    private Slime maxIDSlime;

    public School(World world) {
	this.world = world;
	getWorld().addSchool(this);
	maxID = Long.MIN_VALUE;
	minID = Long.MAX_VALUE;
	minIDSlime = null;
	maxIDSlime = null;
	slimes = new TreeSet<>();
    }

    public long getMinID() {
	return minID;
    }

    public long getMaxID() {
	return maxID;
    }

    public Collection<Slime> getAllSlimes() {
	final List<Slime> allSlimes = new ArrayList<>();

	for (final Slime slime : slimes)
	    allSlimes.add(slime);

	return allSlimes;

    }

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

    public Slime getMaxIDSlime() {
	return maxIDSlime;
    }

    private void setMaxIDSlime(Slime slime) {
	maxIDSlime = slime;

    }

    public Slime getMinIDSlime() {
	return minIDSlime;
    }

    private void setMinIDSlime(Slime slime) {
	minIDSlime = slime;

    }

    private void setMinID(long identification) {
	minID = identification;
    }

    private void setMaxID(long identification) {
	maxID = identification;
    }

    World getWorld() {
	return world;
    }

    public void removeSlime(Slime slime) {
	slimes.remove(slime);

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
	slime.school = null;
    }

    public boolean hasSlime(Slime slime) {
	if (slime.getIdentification() == getMaxID() || slime.getIdentification() == getMinID())
	    return true;
	if (getAllSlimes().contains(slime))
	    return true;
	return false;
    }

    private void terminate() {
	if (slimes.size() != 0)
	    throw new RuntimeException();
	slimes = null;
	getWorld().removeSchool(this);
	world = null;

    }
}
