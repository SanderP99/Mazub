package jumpingalien.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A class that implements a game world
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 * 
 * @version 2
 * 
 *
 * @invar Each World can have its visibleWindowWidth as visibleWindowWidth. |
 *        canHaveAsVisibleWindowWidth(this.getVisibleWindowWidth())
 * @invar Each World can have its visisbleWindowHeigth as visisbleWindowHeigth.
 *        | canHaveAsVisibleWindowHeight(this.getVisibleWindowHeight())
 * @invar The visibleWindowPosition of each World must be a valid
 *        visibleWindowPosition for any World. |
 *        isValidVisibleWindowPosition(getVisibleWindowPosition())
 * 
 * 
 */
public class World {

    /**
     * 
     * @param worldSizeX          The width of the world in pixels
     * @param worldSizeY          The height of the world in pixels
     * @param tileLength          The length of a tile in pixels
     * @param targetTileX         The x coordinate of the targettile in pixels
     * @param targetTileY         The y coordinate of the target tile in pixels
     * @param visibleWindowWidth  The width of the visible window in pixels
     * @param visibleWindowHeight The height of the visible window in pixels
     * @param geologicalFeatures  A list of geological features
     * 
     * @pre 0 < visibleWindowWidth <= worldSizeX
     * @pre 0 < visibleWindowHeight <= worldSizeY
     * 
     * @post The visisbleWindowHeigth of this new World is equal to the given
     *       visisbleWindowHeigth. | new.getVisibleWindowHeight() ==
     *       visibleWindowHeight
     * 
     * @throws RuntimeException This new World cannot have the given
     *                          visisbleWindowHeigth as its visisbleWindowHeigth. |
     *                          !
     *                          canHaveAsVisibleWindowHeight(this.getVisibleWindowHeight())
     * 
     * @post ... | new.getVisibleWindowWidth() == visibleWindowWidth
     * @post ... | new.getVisibleWindowHeight() == visibleWindowHeight
     * @post ... | new.getTileLenght() == tileLenght
     * @post ... | new.getWorldSizeX() == nbTilesX * tileLenght
     * @post ... | new.getWorldSizeY() == nbTilesY * tileLenght
     * @post ... | new.getTargetTileX() == targetTileX * tileLenght
     * @post ... | new.getTargetTileY() == targetTileY * tileLenght
     * @post ... | new.getMaxNbOfObjects() == maxNbOfObjects
     * 
     * @throws RuntimeException This new World cannot have the given
     *                          visibleWindowWidth as its visibleWindowWidth. | !
     *                          canHaveAsVisibleWindowWidth(this.getVisibleWindowWidth())
     * 
     * @effect The visibleWindowPosition of this new World is set to (0,0). |
     *         this.setVisibleWindowPosition(new int[] {0, 0})
     * @effect ... | initializeGeologicalFeatures(nbTilesX, nbTilesY,
     *         geologicalFeatures)
     */
    public World(int nbTilesX, int nbTilesY, int tileLength, int targetTileX, int targetTileY, int visibleWindowWidth,
	    int visibleWindowHeight, int maxNbOfObjects, int... geologicalFeatures) throws RuntimeException {

	setTileLength(tileLength);
	setWorldSizeX(nbTilesX * getTileLength());
	setWorldSizeY(nbTilesY * getTileLength());
	setTargetTileX(targetTileX * getTileLength());
	setTargetTileY(targetTileY * getTileLength());
	if (!canHaveAsVisibleWindowHeight(visibleWindowHeight))
	    throw new RuntimeException();
	setVisibleWindowHeight(visibleWindowHeight);
	if (!canHaveAsVisibleWindowWidth(visibleWindowWidth))
	    throw new RuntimeException();
	setVisibleWindowWidth(visibleWindowWidth);
	setVisibleWindowPosition(new int[] { 0, 0 });
	initializeGeologicalFeatures(nbTilesX, nbTilesY, geologicalFeatures);
	setMaxNbOfObjects(maxNbOfObjects);

    }

    /**
     * A variable to store the maximum number of schools
     */
    public static final int maxNbOfSchools = 10;

    /**
     * Check whether this World can have the given visisbleWindowHeigth as its
     * visisbleWindowHeigth.
     * 
     * @param visibleWindowHeight The visisbleWindowHeigth to check.
     * 
     * @return | result == !(visibleWindowHeight < 0) && !(visibleWindowHeight >
     *         this.getWorldSizeY())
     */
    @Raw
    public boolean canHaveAsVisibleWindowHeight(int visibleWindowHeight) {
	if (visibleWindowHeight < 0)
	    return false;
	if (visibleWindowHeight > getWorldSizeY())
	    return false;
	return true;

    }

    /**
     * Return the visisbleWindowHeigth of this World.
     */
    @Basic
    @Raw
    @Immutable
    public static int getVisibleWindowHeight() {
	return visibleWindowHeight;
    }

    /**
     * Sets the visible window height to the given height
     * 
     * @param visibleWindowHeight The height to set in pixels
     * 
     * @post ... | new.visibleWindowHeight == visibleWindowHeight
     */
    private static void setVisibleWindowHeight(int visibleWindowHeight) {
	World.visibleWindowHeight = visibleWindowHeight;

    }

    /**
     * Variable registering the visisbleWindowHeigth of this World.
     */
    private static int visibleWindowHeight;

    /**
     * Check whether this World can have the given visibleWindowWidth as its
     * visibleWindowWidth.
     * 
     * @param visibleWindowWidth The visibleWindowWidth to check.
     * 
     * @return | result == !(visibleWindowWidth < 0) && !(visibleWindowWidth >
     *         this.getWorldSizeX())
     */
    @Raw
    public boolean canHaveAsVisibleWindowWidth(int visibleWindowWidth) {
	if (visibleWindowWidth < 0)
	    return false;
	if (visibleWindowWidth > getWorldSizeX())
	    return false;
	return true;
    }

    /**
     * Return the visibleWindowWidth of this World.
     */
    @Basic
    @Raw
    @Immutable
    public static int getVisibleWindowWidth() {
	return visibleWindowWidth;
    }

    /**
     * Sets the visible window width to the given width
     * 
     * @param visibleWindowWidth The width to set in pixels
     * 
     * @post ... | new.visibleWindowWidth == visibleWindowWidth
     */
    private static void setVisibleWindowWidth(int visibleWindowWidth) {
	World.visibleWindowWidth = visibleWindowWidth;

    }

    /**
     * Variable registering the visibleWindowWidth of this World.
     */
    private static int visibleWindowWidth;

    /**
     * Check whether the given visibleWindowPosition is a valid
     * visibleWindowPosition for any World.
     * 
     * @param visibleWindowPosition The visibleWindowPosition to check.
     * @return | result == !(visibleWindowPosition.length != 2) &&
     *         !(visibleWindowPosition[0] < 0 || visibleWindowPosition[1] < 0) &&
     *         !(visibleWindowPosition[0] + this.getVisibleWindowWidth() >
     *         this.getWorldSizeX() || visibleWindowPosition[1] +
     *         this.getVisibleWindowHeight() > this.getWorldSizeY())
     */
    public boolean isValidVisibleWindowPosition(int[] visibleWindowPosition) {
	if (visibleWindowPosition.length != 2)
	    return false;
	if (visibleWindowPosition[0] < 0 || visibleWindowPosition[1] < 0)
	    return false;
	if (visibleWindowPosition[0] + getVisibleWindowWidth() > getWorldSizeX()
		|| visibleWindowPosition[1] + getVisibleWindowHeight() > getWorldSizeY())
	    return false;
	return true;
    }

    /**
     * Variable registering the visibleWindowPosition of this World.
     */
    private int[] visibleWindowPosition;

    /**
     * Return the visibleWindowPosition of this World.
     */
    @Basic
    @Raw
    public int[] getVisibleWindowPosition() {
	return visibleWindowPosition;
    }

    /**
     * Set the visibleWindowPosition of this World to the given
     * visibleWindowPosition.
     * 
     * @param visibleWindowPosition The new visibleWindowPosition for this World.
     * @post The visibleWindowPosition of this new World is equal to the given
     *       visibleWindowPosition. | new.getVisibleWindowPosition() ==
     *       visibleWindowPosition
     * @throws RuntimeException The given visibleWindowPosition is not a valid
     *                          visibleWindowPosition for any World. | !
     *                          isValidVisibleWindowPosition(getVisibleWindowPosition())
     */
    @Raw
    public void setVisibleWindowPosition(int[] visibleWindowPosition) throws RuntimeException {
	if (!isValidVisibleWindowPosition(visibleWindowPosition))
	    throw new RuntimeException();
	this.visibleWindowPosition = visibleWindowPosition;
    }

    /**
     * A variable to store the maximum number of objects
     */
    private static int maxNbOfObjects;

    /**
     * Returns the maximum number of objects in the world
     */
    @Basic
    private static int getMaxNbOfObjects() {
	return maxNbOfObjects;
    }

    /**
     * Sets the maximum number of objects the world can have
     * 
     * @param maxNbOfObjects The maximum number of objects
     * 
     * @post this.maxNbOfObjects == maxNbOfObjects
     */
    private static void setMaxNbOfObjects(int maxNbOfObjects) {
	World.maxNbOfObjects = maxNbOfObjects;
    }

    /**
     * A variable to store the width of the world
     */
    private int worldSizeX;

    /**
     * Returns the horizontal size of the world in pixels
     */
    @Basic
    @Immutable
    public int getWorldSizeX() {
	return worldSizeX;
    }

    /**
     * Sets the width of the world to the given amount of pixels
     * 
     * @param worldSizeX The width of the world in pixels
     * 
     * @post if worldSizeX < 0 then this.worldSizeX == -worldSizeX
     * @post if worldSizeX >= 0 then this.worldSizeX == worldSizeX
     */
    private void setWorldSizeX(int worldSizeX) {
	if (worldSizeX < 0)
	    this.worldSizeX = -1 * worldSizeX;
	else
	    this.worldSizeX = worldSizeX;
    }

    /**
     * A variable to store the height of the world
     */
    private int worldSizeY;

    /**
     * Returns the vertical size of the world in pixels
     */
    @Basic
    @Immutable
    public int getWorldSizeY() {
	return worldSizeY;
    }

    /**
     * Sets the height of the world to the given amount of pixels
     * 
     * @param worldSizeY The height of the world in pixels
     * 
     * @post if worldSizeY < 0 then this.worldSizeY == -worldSizeY
     * @post if worldSizeY >= 0 then this.worldSizeY == worldSizeY
     */
    private void setWorldSizeY(int worldSizeY) {
	if (worldSizeY < 0)
	    this.worldSizeY = -1 * worldSizeY;
	else
	    this.worldSizeY = worldSizeY;
    }

    /**
     * A variable to store the length of the tiles.
     */
    private int tileLength;

    /**
     * Returns the length of a tile in pixels
     */
    @Basic
    @Immutable
    public int getTileLength() {
	return tileLength;
    }

    /**
     * Sets the length of a tile to a given length
     * 
     * @param length The length to set in pixels
     * 
     * @post ... | if length > 0 then new.tileLenght == length
     * @post ... | if length <= 0 then new.tileLength == 10
     */
    private void setTileLength(int length) {
	if (length > 0)
	    tileLength = length;
	else
	    tileLength = 10;
    }

    /**
     * A variable to store the target tile x coordinate
     */
    private int targetTileX;

    /**
     * Returns the tile x coordinate of the target tile.
     */
    @Basic
    public int getTargetTileX() {
	return targetTileX;
    }

    /**
     * Sets the target tile x coordinate to the given tile coordinate
     * 
     * @param targetTileX The tile coordinate to set
     * 
     * @pre !isTerminated()
     * 
     * @post ... | new.targetTileX == targetTileX
     */
    public void setTargetTileX(int targetTileX) {
	assert !isTerminated();
	this.targetTileX = targetTileX;
    }

    /**
     * A variable to store the target tile y coordinate
     */
    private int targetTileY;

    /**
     * Returns the tile y coordinate of the target tile.
     */
    @Basic
    public int getTargetTileY() {
	return targetTileY;
    }

    /**
     * Sets the target tile y coordinate to the given tile coordinate
     * 
     * @param targetTileY The tile coordinate to set
     * 
     * @pre !isTerminated()
     * 
     * @post ... | new.targetTileY == targetTileY
     */
    public void setTargetTileY(int targetTileY) {
	assert !isTerminated();
	this.targetTileY = targetTileY;
    }

    /**
     * Returns if the given position is a position in the target tile
     * 
     * @param pixelX The x coordinate
     * @param pixelY The y coordinate
     */
    public boolean isPositionInTargetTile(int pixelX, int pixelY) {
        return pixelX >= getTargetTileX() && pixelX < getTargetTileX() + getTileLength() && pixelY >= getTargetTileY()
        	&& pixelY < getTargetTileY() + getTileLength();
    }

    /**
     * A boolean to store if the given world is terminated
     */
    private boolean isTerminated;

    /**
     * Check whether this world is terminated.
     */
    @Basic
    @Raw
    public boolean isTerminated() {
	return isTerminated;
    }

    /**
     * Terminates the world
     * 
     * @post ... | new.isTerminated == true
     */
    public void terminate() {
	if (!isTerminated())
	    isTerminated = true;
	for (final Object object : getAllObjects())
	    ((GameObject) object).terminate();

	for (final School school : getAllSchools())
	    school.terminate();
    }

    /**
     * Returns whether the geological feature to set is valid
     * 
     * @param geologicalFeature The geological feature to set
     */
    private static boolean isValidGeologicalFeature(int geologicalFeature) {
//	return geologicalFeature == AIR || geologicalFeature == SOLID_GROUND || geologicalFeature == WATER
//		|| geologicalFeature == MAGMA || geologicalFeature == GAS || geologicalFeature == ICE;

	for (final PassableTerrain feature : PassableTerrain.values())
	    if (feature.getValue() == geologicalFeature)
		return true;

	for (final ImpassableTerrain feature : ImpassableTerrain.values())
	    if (feature.getValue() == geologicalFeature)
		return true;

	return false;
    }

    /**
     * Returns the geological feature at the given pixel position. 0 for AIR, 1 for
     * SOLID_GROUND, 2 for WATER and 3 for MAGMA
     * 
     * @param pixelX The x pixel of the feature to get
     * @param pixelY The y pixel of the feature to get
     */
    @Basic
    public int getGeologicalFeature(int pixelX, int pixelY) {
	if (!isValidPixelYPosition(pixelY))
	    pixelY = 0;
	if (!isValidPixelXPosition(pixelX))
	    pixelX = 0;

	for (final PassableTerrain feature : PassableTerrain.values())
	    if (tiles.contains(pixelX / getTileLength() + " " + pixelY / getTileLength() + " " + feature.getValue()))
		return feature.getValue();

	for (final ImpassableTerrain feature : ImpassableTerrain.values())
	    if (tiles.contains(pixelX / getTileLength() + " " + pixelY / getTileLength() + " " + feature.getValue()))
		return feature.getValue();

	return PassableTerrain.AIR.getValue();

    }

    /**
     * Returns the geological feature at the given pixel position. 0 for AIR, 1 for
     * SOLID_GROUND, 2 for WATER and 3 for MAGMA
     * 
     * @param tile_cord The coordinate to check
     * 
     * @effect getGeologicalFeature(tile_cord[0], tile_cord[1])
     */
    public int getGeologicalFeatureTile(int[] tile_cord) {
	for (final PassableTerrain feature : PassableTerrain.values())
	    if (tiles.contains(tile_cord[0] + " " + tile_cord[1] + " " + feature.getValue()))
		return feature.getValue();

	for (final ImpassableTerrain feature : ImpassableTerrain.values())
	    if (tiles.contains(tile_cord[0] + " " + tile_cord[1] + " " + feature.getValue()))
		return feature.getValue();

	return PassableTerrain.AIR.getValue();

    }

    public void setGeologicalFeature(int pixelX, int pixelY, int geologicalFeature) {
	if (!isValidGeologicalFeature(geologicalFeature))
	    geologicalFeature = PassableTerrain.AIR.getValue();
	if (isValidPixelYPosition(pixelY) && isValidPixelXPosition(pixelX)) {
	    tiles.remove(pixelX / getTileLength() + " " + pixelY / getTileLength() + " "
		    + getGeologicalFeature(pixelX, pixelY));

	    tiles.add(pixelX / getTileLength() + " " + pixelY / getTileLength() + " " + geologicalFeature);

	}
    }

    /**
     * Sets the geological features in the world
     * 
     * @param xLength            The number of tiles in the x direction of the world
     * @param yLength            The number of tiles in the y direction of the world
     * @param geologicalFeatures A list of geological features. Starting bottom left
     *                           going to the left and starting a row higher once
     *                           the right boundary is reached.
     */
    private void initializeGeologicalFeatures(int xLength, int yLength, int... geologicalFeatures) {
	tiles = new HashSet<String>();

	for (int i = 0; i < geologicalFeatures.length; i++)
	    tiles.add(i % xLength + " " + i / xLength + " " + geologicalFeatures[i]);

    }

    /**
     * Returns whether the given coordinate is on the canvas.
     * 
     * @param X_pos The given coordinate to check in pixels.
     */
    public boolean isValidPixelXPosition(int X_pos) {
	return X_pos >= 0 && X_pos <= getWorldSizeX();
    }

    /**
     * Returns whether the given coordinate is on the canvas.
     * 
     * @param Y_pos The given coordinate to check in pixels.
     */
    public boolean isValidPixelYPosition(int Y_pos) {
	return Y_pos >= 0 && Y_pos <= getWorldSizeY();
    }

    /**
     * A set to store the tiles and the geological feature
     */
    public HashSet<String> tiles;

    /**
     * A set to store all the objects in the world
     */
    final HashSet<GameObject> objects = new HashSet<GameObject>();

    /**
     * A variable to store the playable Mazub of this world
     */
    Mazub player;

    /**
     * A boolean to store if the world has a playable Mazub
     */
    private boolean hasPlayer;

    public boolean hasStarted;

    /**
     * Returns whether the object can be placed in the world
     * 
     * @param gameObject The object to place
     */
    public boolean canPlaceObject(GameObject gameObject) {
	if (!(gameObject instanceof Plant)) {
	    for (final Object object : getAllObjects())
		if (gameObject.collidesWith((GameObject) object) && !(object instanceof Plant) && gameObject != object)
		    return false;

	    for (int x = gameObject.getXPositionPixel(); x < gameObject.getXPositionPixel()
		    + gameObject.getXsize(); x++)
		for (int y = gameObject.getYPositionPixel() + 1; y < gameObject.getYPositionPixel()
			+ gameObject.getYsize(); y++)
		    for (final ImpassableTerrain feature : ImpassableTerrain.values())
			if (getGeologicalFeature(x, y) == feature.getValue())
			    return false;
	}
	return true;
    }

    public <T> boolean canPlaceGameObjectAdvanceTime(T Object, GameObject other) {
	GameObject gameObject;
	try {
	    gameObject = (GameObject) Object;
	} catch (final ClassCastException e) {
	    throw new IllegalArgumentException();
	}

	if (!(gameObject instanceof Plant)) {
	    for (final Object object : getAllObjects())
		if (gameObject.collidesWith((GameObject) object) && !(object instanceof Plant) && gameObject != object
			&& object != other)
		    return false;

	    gameObject.terminate();

	    for (final ImpassableTerrain feature : ImpassableTerrain.values()) {
		for (int x = gameObject.getXPositionPixel(); x < gameObject.getXPositionPixel()
			+ gameObject.getXsize(); x++) {
		    if (getGeologicalFeature(x, gameObject.getYPositionPixel() + 1) == feature.getValue())
			return false;
		    if (getGeologicalFeature(x, gameObject.getYPositionPixel() + gameObject.getYsize()) == feature
			    .getValue())
			return false;
		}
		for (int y = gameObject.getYPositionPixel() + 1; y < gameObject.getYPositionPixel()
			+ gameObject.getYsize(); y++) {
		    if (getGeologicalFeature(gameObject.getXPositionPixel(), y) == feature.getValue())
			return false;
		    if (getGeologicalFeature(gameObject.getXPositionPixel() + gameObject.getXsize() - 1, y) == feature
			    .getValue())
			return false;
		}
	    }
	}
	return true;
    }

    public boolean canPlaceMazubFullCheck(GameObject gameObject, GameObject other) {
	if (!(gameObject instanceof Plant))
	    for (final Object object : getAllObjects())
		if (gameObject.collidesWith((GameObject) object) && !(object instanceof Plant) && gameObject != object
			&& object != other)
		    return false;
	for (final ImpassableTerrain feature : ImpassableTerrain.values())
	    for (int x = gameObject.getXPositionPixel(); x < gameObject.getXPositionPixel()
		    + gameObject.getXsize(); x++)
		for (int y = gameObject.getYPositionPixel() + 1; y < gameObject.getYPositionPixel()
			+ gameObject.getYsize(); y++)
		    if (getGeologicalFeature(x, y) == feature.getValue())
			return false;
	return true;
    }

    /**
     * Returns a set with all the gameObjects in the world
     */
    @Basic
    public Set<Object> getAllObjects() {
	final HashSet<Object> allObjects = new HashSet<Object>();

	objects.stream().map((object) -> ((Object) object)).forEach((object) -> allObjects.add(object));

	return allObjects;
    }

    /**
     * Returns whether a given object is in the world
     * 
     * @param gameObject The object to check
     */
    public boolean hasAsGameObject(GameObject gameObject) {
	if (getAllObjects().contains(gameObject))
	    return true;
	return false;
    }

    /**
     * Adds a gameObject to the world
     * 
     * @param gameObject The gameObject to add
     * 
     * @throws RuntimeException ... | getAllObjects().size() - 1 ==
     *                          getMaxNbOfObjects()
     * @throws RuntimeException ... | !gameObject.isValidGameObject()
     * @throws RuntimeException ... | gameObject.getXPositionPixel() >=
     *                          getWorldSizeX() || gameObject.getXPositionActual() <
     *                          0
     * @throws RuntimeException ... | gameObject.getYPositionPixel() >=
     *                          getWorldSizeY() || gameObject.getYPositionActual() <
     *                          0
     * @throws RuntimeException ... | gameObject.getWorld() != null
     * @throws RuntimeException ... | !canPlaceObject(gameObject)
     * @throws RuntimeException ... |
     *                          getGeologicalFeature(gameObject.getXPositionPixel(),
     *                          gameObject.getYPositionPixel()) == ImpassableTerrain
     *                          &&
     *                          getGeologicalFeature(gameObject.getXPositionPixel()
     *                          + 1, gameObject.getYPositionPixel() + 1) != AIR
     * 
     * @post ... | new.getAllObjects().contains(gameObject)
     * @post ... | if !hasPlayer and gameObject instanceof Mazub then hasPlayer ==
     *       true and setPlayer(gameObject)
     * @post ... | new.gameObject.world == this
     */
    @Raw
    public void addGameObject(GameObject gameObject) throws RuntimeException {
	if (getAllObjects().size() == getMaxNbOfObjects() && !(gameObject instanceof Mazub))
	    throw new RuntimeException();
	if (!gameObject.isValidGameObject())
	    throw new RuntimeException();
	if (gameObject.getXPositionPixel() >= getWorldSizeX() || gameObject.getXPositionActual() < 0)
	    throw new RuntimeException();
	if (gameObject.getYPositionPixel() >= getWorldSizeY() || gameObject.getYPositionActual() < 0)
	    throw new RuntimeException();
	if (gameObject.getWorld() != null)
	    throw new RuntimeException();
	if (gameIsActive())
	    throw new RuntimeException();

	if (!canPlaceObject(gameObject))
	    throw new RuntimeException();

	for (final ImpassableTerrain impassableFeature : ImpassableTerrain.values())
	    if (getGeologicalFeature(gameObject.getXPositionPixel(),
		    gameObject.getYPositionPixel()) == impassableFeature.getValue()
		    && !PassableTerrain.contains(getGeologicalFeature(gameObject.getXPositionPixel() + 1,
			    gameObject.getYPositionPixel() + 1)))
		throw new RuntimeException();

	if (!hasPlayer() && gameObject instanceof Mazub)
	    setPlayer((Mazub) gameObject);

	if (gameObject instanceof Slime && ((Slime) gameObject).getSchool() != null
		&& !getAllSchools().contains(((Slime) gameObject).getSchool()))
	    allSchools.add(((Slime) gameObject).getSchool());

	objects.add(gameObject);
	gameObject.world = this;
    }

    private boolean gameIsActive() {
	if (hasStarted)
	    return true;
	return false;
    }

    /**
     * Removes a object from the world
     * 
     * @param gameObject The object to remove
     * 
     * @post ... | !new.getAllObjects().contains(gameObject)
     * @post ... | new.gameObject.world == null
     * @post ... | if getPlayer() == gameObject then new.player == null and
     *       new.hasPlayer == false
     */
    @Raw
    public void removeObject(GameObject gameObject) {
	objects.remove(gameObject);
	gameObject.world = null;
	if (!getAllObjects().contains(getPlayer())) {
	    player = null;
	    hasPlayer = false;
	}

    }

    /**
     * Returns whether the world has a playable Mazub
     */
    public boolean hasPlayer() {
	return hasPlayer;
    }

    /**
     * Returns the player for this world
     */
    @Basic
    public Mazub getPlayer() {
	return player;
    }

    /**
     * Sets the given gameObject as player for this world
     * 
     * @param gameObject The gameObject to set as player
     * 
     * @effect advanceVisibleWindow()
     * 
     * @post ... | new.player == gameObject
     * @post ... | new.hasPlayer == true
     * @post ... | new.gameObject.isPlayer == true
     */
    @Raw
    void setPlayer(Mazub gameObject) {
	player = gameObject;
	gameObject.isPlayer = true;
	hasPlayer = true;
	advanceVisibleWindow();
    }

    /**
     * Returns the timeStep for a given gameObject
     * 
     * @param gameObject The gameObject to advance time for
     * @param deltaT     The total time to advance
     */
    public static double getTimeStep(GameObject gameObject, double deltaT) {
	final double velocityRoot = Math.sqrt(
		Math.pow(gameObject.getHorizontalSpeedMeters(), 2) + Math.pow(gameObject.getVerticalSpeedMeters(), 2));
	final double accelerationRoot = Math.sqrt(Math.pow(gameObject.getHorizontalAcceleration(), 2)
		+ Math.pow(gameObject.getVerticalAcceleration(), 2));
	return 0.01 / (velocityRoot + accelerationRoot * deltaT);
    }

    public void advanceWorldTime(double dt) throws RuntimeException {
	if (dt < 0)
	    throw new IllegalArgumentException();
	if (dt > 0.2)
	    throw new IllegalArgumentException();
	if (dt != dt)
	    throw new IllegalArgumentException();

	if (getPlayer() != null)
	    getPlayer().advanceTime(dt, getTimeStep(getPlayer(), dt));

	for (final Object object : getAllObjects()) {
	    double timeStep = getTimeStep((GameObject) object, dt);
	    if (object != getPlayer()) {
		if (timeStep == Double.POSITIVE_INFINITY)
		    timeStep = 0.002;
		((GameObject) object).advanceTime(dt, timeStep);
	    }
	}

	advanceVisibleWindow();
    }

    private void advanceVisibleWindow() {
	int newWindowXPos = 0;
	int newWindowYPos = 0;
	if (getPlayer() == null) {
	    newWindowXPos = getVisibleWindowPosition()[0];
	    newWindowYPos = getVisibleWindowPosition()[1];
	} else if ((getVisibleWindowWidth() - getPlayer().getXsize()) / 2 > 200
		|| (getVisibleWindowHeight() - getPlayer().getYsize()) / 2 > 200) {

	    if (getPlayer().getXPositionPixel() <= 200)
		newWindowXPos = 0;
	    else if (getWorldSizeX() - getPlayer().getXPositionPixel() - getPlayer().getXsize() <= 200)
		newWindowXPos = getWorldSizeX() - getVisibleWindowWidth();
	    else {
		newWindowXPos = getVisibleWindowPosition()[0];
		while (getPlayer().getXPositionPixel() - newWindowXPos < 200)
		    newWindowXPos -= 1;
		while (newWindowXPos + getVisibleWindowWidth() - getPlayer().getXPositionPixel()
			- getPlayer().getXsize() < 200)
		    newWindowXPos += 1;

	    }

	    if (getPlayer().getYPositionPixel() <= 200)
		newWindowYPos = 0;
	    else if (getWorldSizeY() - getPlayer().getYPositionPixel() - getPlayer().getYsize() <= 200)
		newWindowYPos = getWorldSizeY() - getVisibleWindowHeight();
	    else {
		newWindowYPos = getVisibleWindowPosition()[1];
		while (getPlayer().getYPositionPixel() - newWindowYPos < 200)
		    newWindowYPos -= 1;
		while (newWindowYPos + getVisibleWindowHeight() - getPlayer().getYPositionPixel()
			- getPlayer().getYsize() < 200)
		    newWindowYPos += 1;
	    }
	} else {
	    newWindowXPos = getPlayer().getXPositionPixel() - getVisibleWindowWidth() / 2;
	    newWindowYPos = getPlayer().getYPositionPixel() - getVisibleWindowHeight() / 2;
	    if (newWindowXPos < 0)
		newWindowXPos = 0;
	    if (newWindowYPos < 0)
		newWindowYPos = 0;
	    while (newWindowXPos + getVisibleWindowWidth() > getWorldSizeX())
		newWindowXPos -= 1;
	    while (newWindowYPos + getVisibleWindowHeight() > getWorldSizeY())
		newWindowYPos -= 1;

	}
	setVisibleWindowPosition(new int[] { newWindowXPos, newWindowYPos });

    }

    /**
     * A set to store all the schools in the world.
     */
    private final Set<School> allSchools = new HashSet<>();

    /**
     * Returns the current number of schools in this world.
     */
    public int getNbOfSchools() {
	return allSchools.size();
    }

    /**
     * Adds a school to the world
     * 
     * @param school The school to add
     * 
     * @throws RuntimeException If new.getNbOfSchools() > maxNbOfSchools
     * 
     * @post ... | new.getAllSchools().contains(school)
     * @post ... | new.school.getWorld() == this
     */
    @Raw
    void addSchool(School school) throws RuntimeException {
	if (getNbOfSchools() == maxNbOfSchools)
	    throw new RuntimeException();
	allSchools.add(school);
	school.world = this;
    }

    /**
     * Removes the school from the world
     * 
     * @param school The school to remove
     * 
     * @post ... | !new.getAllSchools().contains(school)
     * @post ... | new.school.getWorld() == null
     */
    @Raw
    void removeSchool(School school) {
	allSchools.remove(school);
	school.world = null;
    }

    /**
     * Returns all the schools in the world.
     */
    @Basic
    public Set<School> getAllSchools() {
	final Set<School> allTheSchools = new HashSet<>();

	getIteratorAllSchools().forEachRemaining((school) -> allTheSchools.add(school));

	return allTheSchools;
    }

    private Iterator<School> getIteratorAllSchools() {
	return new Iterator<School>() {

	    private int index = 0;

	    @Override
	    public boolean hasNext() {
		return index < allSchools.size();
	    }

	    @Override
	    public School next() {
		if (!hasNext())
		    throw new NoSuchElementException();
		index += 1;
		return (School) allSchools.toArray()[index - 1];
	    }
	};
    }
}