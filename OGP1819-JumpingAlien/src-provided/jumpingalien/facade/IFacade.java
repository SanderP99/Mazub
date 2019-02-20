package jumpingalien.facade;

import jumpingalien.model.Mazub;
import jumpingalien.util.ModelException;
import jumpingalien.util.ShouldNotImplementException;
import jumpingalien.util.Sprite;

/**
 * Implement this interface to connect your code to the graphical user interface
 * (GUI).
 * 
 * <ul>
 * <li>For separating the code that you wrote from the code that was provided to
 * you, put <b>ALL your code in the <code>src</code> folder.</b> The code that
 * is provided to you stays in the <code>src-provided</code> folder.</li>
 * 
 * <li>You should at least <b>create the following classes</b> in the package
 * <code>jumpingalien.model</code>:
 * <ul>
 * <li>a class <code>Mazub</code> for representing Mazub the alien</li>
 * </ul>
 * You may, of course, add additional classes as you see fit.
 * 
 * <li>You should <b>create a class <code>Facade</code></b> that implements this
 * interface. This class should be placed <b>in the package
 * <code>jumpingalien.facade</code></b>.</li>
 * 
 * <li>
 * The <b>header of that Facade class</b> should look as follows:<br>
 * <code>class Facade implements IFacade { ... }</code><br>
 * Consult the <a href=
 * "http://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html">
 * Java tutorial</a> for more information on interfaces, if necessary.</li>
 *
 * <li>Your <code>Facade</code> class should offer a <b>default constructor</b>.
 * </li>
 * 
 * <li><b>Each method</b> defined in the interface <code>IFacade</code> must be
 * implemented by the class <code>Facade</code>. For example, the implementation
 * of <code>getActualPosition</code> should call one or more methods on the given
 * <code>alien</code> to retrieve its current location.</li>
 * 
 * <li>Methods in this interface are <b>only allowed to throw exceptions of type
 * <code>jumpingalien.util.ModelException</code></b> (this class is provided).
 * No other exception types are allowed. This exception can be thrown only if
 * (1) calling a method of your <code>Mazub</code> class with the given parameters
 * would violate a precondition or (2)if the method of your <code>Mazub</code>
 * class throws an exception (if so, wrap the exception in a
 * <code>ModelException</code>).</li>
 * 
 * <li><b>ModelException should not be used anywhere outside of your Facade
 * implementation.</b></li>
 * 
 * <li>Your Facade implementation should <b>only contain trivial code</b> (for
 * example, calling a method, combining multiple return values into an array,
 * creating @Value instances, catching exceptions and wrapping it in a
 * ModelException). All non-trivial code should be placed in the other classes
 * that you create.</li>
 * 
 * <li>The rules described above and the documentation described below for each
 * method apply only to the class implementing IFacade. <b>Your class for
 * representing aliens should follow the rules described in the assignment.</b></li>
 * 
 * <li><b>Do not modify the signatures</b> of the methods defined in this
 * interface.</li>
 * 
 * </ul>
 *
 */
public interface IFacade {
	
	/**
	 * Return a boolean indicating whether the code at stake is the effort of a
	 * team of two students or the effort of an individual student. 
	 */
	boolean isTeamSolution();

	/**
	 * Create an instance of Mazub with given pixel position and given sprites.
	 *   The actual position of the new Mazub will correspond with the coordinates
	 *   of the left-bottom corner of the left-bottom pixel occupied by the new Mazub.
	 *   The new Mazub will be stationary.
	 */
	Mazub createMazub(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException;

	/**
	 * Return the actual position of the given alien.
	 *   Returns an array of 2 doubles {x, y} that represents the coordinates of the
	 *   bottom-left corner of the given alien in the world.
	 *   Coordinates are expressed in meters.
	 */
	double[] getActualPosition(Mazub alien) throws ModelException;

	/**
	 * Change the actual position of the given alien to the given position.
	 */
	void changeActualPosition(Mazub alien, double[] newPosition) throws ModelException;

	/**
	 * Return the pixel position of the given alien.
	 *   Returns an array of 2 integers {x, y} that represents the coordinates of the
	 *   pixel in the game world occupied by the bottom-left pixel of the given alien.
	 */
	int[] getPixelPosition(Mazub alien) throws ModelException;
	
	/**
	 * Return the orientation of the given alien.
	 *   The resulting value is negative if the given alien is oriented to the left,
	 *   0 if the given alien is oriented to the front and positive if the given
	 *   alien is oriented to the right.
	 */
	int getOrientation(Mazub alien) throws ModelException;
	
	/**
	 * Return the current velocity of the given alien.
	 *   Returns an array consisting of 2 doubles {vx, vy} that represents the
	 *   horizontal and vertical components of the given alien's current
	 *   velocity in m/s.
	 */
	double[] getVelocity(Mazub alien) throws ModelException;
	
	/**
	 * Return the current acceleration of the given alien.
	 *   Returns an array consisting of 2 doubles {ax, ay} that represents the
	 *   horizontal and vertical components of the given alien's current
	 *   acceleration in m/s^2.
	 */
	double[] getAcceleration(Mazub alien) throws ModelException;

	/**
	 * Return the sprites to be used to animate the given alien.
	 * 
	 * This method must only be implemented by teams of 2 students. Students working on
	 * their own may ignore this method. 
	 */
	default Sprite[] getSprites(Mazub alien) throws ModelException, ShouldNotImplementException {
		throw new ShouldNotImplementException("Not to be implemented by individual students");
	}

	/**
	 * Return the current sprite image for the given alien.
	 * 
	 * This method must only be implemented by teams of 2 students. Students working on
	 * their own may stick to the default implementation. 
	 */
	default Sprite getCurrentSprite(Mazub alien) throws ModelException {
		return Sprite.DEFAULT_MAZUB_SPRITE;
	}
	
	/**
	 * Return whether the given alien is moving.
	 */
	boolean isMoving(Mazub alien) throws ModelException;
	
	/**
	 * Make the given alien move to the left.
	 */
	void startMoveLeft(Mazub alien) throws ModelException;

	/**
	 * Make the given alien move to the right.
	 */
	void startMoveRight(Mazub alien) throws ModelException;

	/**
	 * End the given alien's move.
	 */
	void endMove(Mazub alien) throws ModelException;
	
	/**
	 * Return whether the given alien is jumping.
	 */
	boolean isJumping(Mazub alien) throws ModelException;

	/**
	 * Make the given alien jump.
	 */
	void startJump(Mazub alien) throws ModelException;

	/**
	 * End the given alien's jump.
	 */
	void endJump(Mazub alien) throws ModelException;
	
	/**
	 * Return whether the given alien is ducking.
	 */
	boolean isDucking(Mazub alien) throws ModelException;

	/**
	 * Make the given alien duck.
	 */
	void startDuck(Mazub alien) throws ModelException;

	/**
	 * End the given alien's ducking.
	 */
	void endDuck(Mazub alien) throws ModelException;

	/**
	 * Advance the state of the given alien by the given time period.
	 */
	void advanceTime(Mazub alien, double dt) throws ModelException;
}
