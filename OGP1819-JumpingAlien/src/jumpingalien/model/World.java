package jumpingalien.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
/**
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 *
 * @invar  Each World can have its visibleWindowWidth as visibleWindowWidth.
 *       | canHaveAsVisibleWindowWidth(this.getVisibleWindowWidth())
 * @invar  Each World can have its visisbleWindowHeigth as visisbleWindowHeigth.
 *       | canHaveAsVisibleWindowHeight(this.getVisibleWindowHeight())  
 * @invar  The visibleWindowPosition of each World must be a valid visibleWindowPosition for any
 *         World.
 *       | isValidVisibleWindowPosition(getVisibleWindowPosition()) 
 *    
 */
public class World {
	/**
	 * 
	 * @param worldSizeX
	 * @param worldSizeY
	 * @param tileLength
	 * @param targetTileX
	 * @param targetTileY
	 * @param visibleWindowWidth
	 * @param visibleWindowHeight
	 * @param geologicalFeature
	 * 
	 * @pre 0 < visibleWindowWidth < worldSizeX
	 * @pre 0 < visibleWindowHeight < worldSizeY
	 * 
	 * @post   The visisbleWindowHeigth of this new World is equal to the given
	 *         visisbleWindowHeigth.
	 *       | new.getVisibleWindowHeight() == visibleWindowHeight
	 * @throws RuntimeException
	 *         This new World cannot have the given visisbleWindowHeigth as its visisbleWindowHeigth.
	 *       | ! canHaveAsVisibleWindowHeight(this.getVisibleWindowHeight())
	 * @post   The visibleWindowWidth of this new World is equal to the given
	 *         visibleWindowWidth.
	 *       | new.getVisibleWindowWidth() == visibleWindowWidth
	 * @throws RuntimeException
	 *         This new World cannot have the given visibleWindowWidth as its visibleWindowWidth.
	 *       | ! canHaveAsVisibleWindowWidth(this.getVisibleWindowWidth())
	 * @effect The visibleWindowPosition of this new World is set to
	 *         (0,0).
	 *       | this.setVisibleWindowPosition(new int[] {0, 0})
	 */
	World(int worldSizeX, int worldSizeY, int tileLength, int targetTileX, int targetTileY, int visibleWindowWidth, int visibleWindowHeight, int... geologicalFeatures) throws RuntimeException{
		setTileLength(tileLength);
		setWorldSizeX(worldSizeX);
		setWorldSizeY(worldSizeY);
		setTargetTileX(targetTileX);
		setTargetTileY(targetTileY);
		if (! canHaveAsVisibleWindowHeight(visibleWindowHeight))
			throw new RuntimeException();
		this.visibleWindowHeight = visibleWindowHeight;
		if (! canHaveAsVisibleWindowWidth(visibleWindowWidth))
			throw new RuntimeException();
		this.visibleWindowWidth = visibleWindowWidth;
		this.setVisibleWindowPosition(new int[] {0, 0});
	}
	
	@Basic
	public int getWorldSizeX() {
		return this.worldSizeX;
	}
	
	private void setWorldSizeX(int worldSizeX) {
		if (worldSizeX % this.getTileLength() == 0)
			this.worldSizeX = worldSizeX;
		else
			this.worldSizeX = worldSizeX + (this.getTileLength() - (worldSizeX % this.getTileLength()));
	}
	
	private int worldSizeX;
	
	@Basic
	public int getWorldSizeY() {
		return this.worldSizeY;
	}
	
	private void setWorldSizeY(int worldSizeY) {
		if (worldSizeY % this.getTileLength() == 0)
			this.worldSizeY = worldSizeY;
		else
			this.worldSizeY = worldSizeY + (this.getTileLength() - (worldSizeY % this.getTileLength()));
	}

	private int worldSizeY;
	
	@Basic
	public int getTileLength() {
		return this.tileLength;
	}
	
	private void setTileLength(int length) {
		if (length > 0)
			this.tileLength = length;
		else
			this.tileLength = 1;
	}
	
	private int tileLength;
	
	
	@Basic
	public int getTargetTileX() {
		return this.targetTileX;
	}
	
	private void setTargetTileX(int targetTileX) {
		assert isValidTargetTileX(targetTileX);
		assert !isTerminated();
		this.targetTileX = targetTileX;
	}
	
	private boolean isValidTargetTileX(int targetTileX) {
		if (targetTileX > (getWorldSizeX()/getTileLength() - 1) || targetTileX < 0)
			return false;
		return true;
	}
	
	private int targetTileX;
	
	@Basic
	public int getTargetTileY() {
		return this.targetTileY;
	}
	
	private void setTargetTileY(int targetTileY) {
		assert isValidTargetTileY(targetTileY);
		assert !isTerminated();
		this.targetTileY = targetTileY;
	}
	
	private boolean isValidTargetTileY(int targetTileY) {
		if (targetTileY > (getWorldSizeY()/getTileLength() - 1) || targetTileY < 0)
			return false;
		return true;
	}
	
	private int targetTileY;
	
	/**
	 * Check whether this world is terminated.
	 */
	@Basic
	@Raw
	public boolean isTerminated() {
		return this.isTerminated;
	}
	
	public void terminate() {
		if (!isTerminated()) {
			this.isTerminated = true;
		}
	}
	
	private boolean isTerminated;

	/**
	 * Return the visisbleWindowHeigth of this World.
	 */
	@Basic @Raw @Immutable
	public int getVisibleWindowHeight() {
		return this.visibleWindowHeight;
	}
	
	/**
	 * Check whether this World can have the given visisbleWindowHeigth as its visisbleWindowHeigth.
	 *  
	 * @param  visibleWindowHeight
	 *         The visisbleWindowHeigth to check.
	 * @return 
	 *       | result == !(visibleWindowHeight < 0) && !(visibleWindowHeight > this.getWorldSizeY())
	*/
	@Raw
	public boolean canHaveAsVisibleWindowHeight(int visibleWindowHeight) {
		if (visibleWindowHeight < 0)
			return false;
		if (visibleWindowHeight > this.getWorldSizeY())
			return false;
		return true;
		
	}
	
	/**
	 * Variable registering the visisbleWindowHeigth of this World.
	 */
	private final int visibleWindowHeight;
	
	/**
	 * Return the visibleWindowWidth of this World.
	 */
	@Basic @Raw @Immutable
	public int getVisibleWindowWidth() {
		return this.visibleWindowWidth;
	}
	
	/**
	 * Check whether this World can have the given visibleWindowWidth as its visibleWindowWidth.
	 *  
	 * @param  visibleWindowWidth
	 *         The visibleWindowWidth to check.
	 * @return 
	 *       | result == !(visibleWindowWidth < 0) && !(visibleWindowWidth > this.getWorldSizeX())
	*/
	@Raw
	public boolean canHaveAsVisibleWindowWidth(int visibleWindowWidth) {
		if (visibleWindowWidth < 0)
			return false;
		if (visibleWindowWidth > this.getWorldSizeX())
			return false;
		return true;
	}
	
	/**
	 * Variable registering the visibleWindowWidth of this World.
	 */
	private final int visibleWindowWidth;

	/**
	 * Return the visibleWindowPosition of this World.
	 */
	@Basic @Raw
	public int[] getVisibleWindowPosition() {
		return this.visibleWindowPosition;
	}
	
	/**
	 * Check whether the given visibleWindowPosition is a valid visibleWindowPosition for
	 * any World.
	 *  
	 * @param  visibleWindowPosition
	 *         The visibleWindowPosition to check.
	 * @return 
	 *       | result == !(visibleWindowPosition.length != 2) && !(visibleWindowPosition[0] < 0 || visibleWindowPosition[1] < 0) 
	 *       && !(visibleWindowPosition[0] + this.getVisibleWindowWidth() > this.getWorldSizeX() || visibleWindowPosition[1] + this.getVisibleWindowHeight() > this.getWorldSizeY())
	 */
	public boolean isValidVisibleWindowPosition(int[] visibleWindowPosition) {
		if (visibleWindowPosition.length != 2)
			return false;
		if (visibleWindowPosition[0] < 0 || visibleWindowPosition[1] < 0)
			return false;
		if (visibleWindowPosition[0] + this.getVisibleWindowWidth() > this.getWorldSizeX() || visibleWindowPosition[1] + this.getVisibleWindowHeight() > this.getWorldSizeY())
			return false;
		return true;
	}
	
	/**
	 * Set the visibleWindowPosition of this World to the given visibleWindowPosition.
	 * 
	 * @param  visibleWindowPosition
	 *         The new visibleWindowPosition for this World.
	 * @post   The visibleWindowPosition of this new World is equal to
	 *         the given visibleWindowPosition.
	 *       | new.getVisibleWindowPosition() == visibleWindowPosition
	 * @throws RuntimeException
	 *         The given visibleWindowPosition is not a valid visibleWindowPosition for any
	 *         World.
	 *       | ! isValidVisibleWindowPosition(getVisibleWindowPosition())
	 */
	@Raw
	public void setVisibleWindowPosition(int[] visibleWindowPosition) 
			throws RuntimeException {
		if (! isValidVisibleWindowPosition(visibleWindowPosition))
			throw new RuntimeException();
		this.visibleWindowPosition = visibleWindowPosition;
	}
	
	/**
	 * Variable registering the visibleWindowPosition of this World.
	 */
	private int[] visibleWindowPosition;
		
}
