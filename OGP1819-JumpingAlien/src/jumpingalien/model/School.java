package jumpingalien.model;

import java.util.Collection;

/**
 * A class that implements a school of slimes
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 * @version 1
 *
 */
public class School extends GameObject {

    private final long minID;
    private final long maxID;

    public School(World world) {
	super(world);
	maxID = 0;
	minID = 0;

    }

    public long getMinID() {
	return minID;
    }

    public long getMaxID() {
	return maxID;
    }

    @Override
    public void advanceTime(double dt, double timeStep) {
	for (final Slime slime : getAllSlimes())
	    slime.advanceTime(dt, timeStep);
    }

    private Collection<Slime> getAllSlimes() {
	// TODO Auto-generated method stub
	return null;
    }

    public void addSlime(Slime slime) {
	getWorld().addGameObject(slime);

    }

    public void removeSlime(Slime slime) {
	// TODO
    }

}
