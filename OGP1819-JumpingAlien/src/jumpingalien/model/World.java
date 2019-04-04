package jumpingalien.model;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * 
 * @author Warre Dreesen
 * @author Sander Prenen
 *
 * @invar Each World can have its visibleWindowWidth as visibleWindowWidth. |
 *        canHaveAsVisibleWindowWidth(this.getVisibleWindowWidth())
 * @invar Each World can have its visisbleWindowHeigth as visisbleWindowHeigth.
 *        | canHaveAsVisibleWindowHeight(this.getVisibleWindowHeight())
 * @invar The visibleWindowPosition of each World must be a valid
 *        visibleWindowPosition for any World. |
 *        isValidVisibleWindowPosition(getVisibleWindowPosition())
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
     * @param geologicalFeatures
     * 
     * @pre 0 < visibleWindowWidth < worldSizeX
     * @pre 0 < visibleWindowHeight < worldSizeY
     * 
     * @post The visisbleWindowHeigth of this new World is equal to the given
     *       visisbleWindowHeigth. | new.getVisibleWindowHeight() ==
     *       visibleWindowHeight
     * @throws RuntimeException This new World cannot have the given
     *                          visisbleWindowHeigth as its visisbleWindowHeigth. |
     *                          !
     *                          canHaveAsVisibleWindowHeight(this.getVisibleWindowHeight())
     * @post The visibleWindowWidth of this new World is equal to the given
     *       visibleWindowWidth. | new.getVisibleWindowWidth() == visibleWindowWidth
     * @throws RuntimeException This new World cannot have the given
     *                          visibleWindowWidth as its visibleWindowWidth. | !
     *                          canHaveAsVisibleWindowWidth(this.getVisibleWindowWidth())
     * @effect The visibleWindowPosition of this new World is set to (0,0). |
     *         this.setVisibleWindowPosition(new int[] {0, 0})
     */
    public World(int nbTilesX, int nbTilesY, int tileLength, int targetTileX, int targetTileY, int visibleWindowWidth,
	    int visibleWindowHeight, int maxNbOfObjects, int... geologicalFeatures) throws RuntimeException {
	setTileLength(tileLength);
	setWorldSizeX(nbTilesX * getTileLength());
	setWorldSizeY(nbTilesY * getTileLength());
	setTargetTileX(targetTileX);
	setTargetTileY(targetTileY);
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

    private void setVisibleWindowWidth(int visibleWindowWidth) {
	World.visibleWindowWidth = visibleWindowWidth;

    }

    private void setVisibleWindowHeight(int visibleWindowHeight) {
	World.visibleWindowHeight = visibleWindowHeight;

    }

    /**
     * Sets the maximum number of objects the world can have
     * 
     * @param maxNbOfObjects The maximum number of objects
     * 
     * @post this.maxNbOfObjects == maxNbOfObjects
     */
    private void setMaxNbOfObjects(int maxNbOfObjects) {
	this.maxNbOfObjects = maxNbOfObjects;
    }

    /**
     * Returns the maximum number of objects in the world
     */
    private int getMaxNbOfObjects() {
	return maxNbOfObjects;
    }

    /**
     * A variable to store the maximum number of objects
     */
    private int maxNbOfObjects;

    /**
     * Returns the horizontal worldsize in pixels
     */
    @Basic
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
     * A variable to store the width of the world
     */
    private int worldSizeX;

    /**
     * Returns the vertical worldsize in pixels
     */
    @Basic
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
     * A variable to store the height of the world
     */
    private int worldSizeY;

    @Basic
    public int getTileLength() {
	return tileLength;
    }

    private void setTileLength(int length) {
	if (length > 0)
	    tileLength = length;
	else
	    tileLength = 10;
    }

    private int tileLength;

    @Basic
    public int getTargetTileX() {
	return targetTileX;
    }

    public void setTargetTileX(int targetTileX) {
	assert !isTerminated();
	this.targetTileX = targetTileX;
    }

    private int targetTileX;

    @Basic
    public int getTargetTileY() {
	return targetTileY;
    }

    public void setTargetTileY(int targetTileY) {
	assert !isTerminated();
	this.targetTileY = targetTileY;
    }

    private int targetTileY;

    /**
     * Check whether this world is terminated.
     */
    @Basic
    @Raw
    public boolean isTerminated() {
	return isTerminated;
    }

    public void terminate() {
	if (!isTerminated())
	    isTerminated = true;
    }

    private boolean isTerminated;

    /**
     * Return the visisbleWindowHeigth of this World.
     */
    @Basic
    @Raw
    @Immutable
    public int getVisibleWindowHeight() {
	return visibleWindowHeight;
    }

    /**
     * Check whether this World can have the given visisbleWindowHeigth as its
     * visisbleWindowHeigth.
     * 
     * @param visibleWindowHeight The visisbleWindowHeigth to check.
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
     * Variable registering the visisbleWindowHeigth of this World.
     */
    private static int visibleWindowHeight;

    /**
     * Return the visibleWindowWidth of this World.
     */
    @Basic
    @Raw
    @Immutable
    public int getVisibleWindowWidth() {
	return visibleWindowWidth;
    }

    /**
     * Check whether this World can have the given visibleWindowWidth as its
     * visibleWindowWidth.
     * 
     * @param visibleWindowWidth The visibleWindowWidth to check.
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
     * Variable registering the visibleWindowWidth of this World.
     */
    private static int visibleWindowWidth;

    /**
     * Return the visibleWindowPosition of this World.
     */
    @Basic
    @Raw
    public int[] getVisibleWindowPosition() {
	return visibleWindowPosition;
    }

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
     * Variable registering the visibleWindowPosition of this World.
     */
    private int[] visibleWindowPosition;

    public void setGeologicalFeature(int pixelX, int pixelY, int geologicalFeature) {
	if (!isValidGeologicalFeature(geologicalFeature))
	    geologicalFeature = AIR;
	if (isValidPixelYPosition(pixelY) && isValidPixelXPosition(pixelX)) {
	    tiles.remove(pixelX / getTileLength() + " " + pixelY / getTileLength() + " "
		    + getGeologicalFeature(pixelX, pixelY));
//			this.tiles.remove(new int[] {pixelX/getTileLength(),pixelY/getTileLength(),
//					getGeologicalFeature(pixelX,pixelY)});
	    tiles.add(pixelX / getTileLength() + " " + pixelY / getTileLength() + " " + geologicalFeature);

//			this.tiles.add(new int[] {pixelX/getTileLength(),pixelY/getTileLength(),geologicalFeature});
	}
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

    public int getGeologicalFeature(int pixelX, int pixelY) {
	if (!isValidPixelYPosition(pixelY))
	    pixelY = 0;
	if (!isValidPixelXPosition(pixelX))
	    pixelX = 0;

	if (tiles.contains(pixelX / getTileLength() + " " + pixelY / getTileLength() + " " + AIR))
	    return AIR;
	if (tiles.contains(pixelX / getTileLength() + " " + pixelY / getTileLength() + " " + SOLID_GROUND))
	    return SOLID_GROUND;
	if (tiles.contains(pixelX / getTileLength() + " " + pixelY / getTileLength() + " " + WATER))
	    return WATER;
	if (tiles.contains(pixelX / getTileLength() + " " + pixelY / getTileLength() + " " + MAGMA))
	    return MAGMA;
	return AIR;
    }

    public int getGeologicalFeatureTile(int[] tile_cord) {
	if (tiles.contains(tile_cord[0] + " " + tile_cord[1] + " " + AIR))
	    return AIR;
	if (tiles.contains(tile_cord[0] + " " + tile_cord[1] + " " + SOLID_GROUND))
	    return SOLID_GROUND;

	if (tiles.contains(tile_cord[0] + " " + tile_cord[1] + " " + WATER))
	    return WATER;
	if (tiles.contains(tile_cord[0] + " " + tile_cord[1] + " " + MAGMA))
	    return MAGMA;
	return AIR;

    }

    public final static int AIR = 0;
    public final static int SOLID_GROUND = 1;
    public final static int WATER = 2;
    public final static int MAGMA = 3;

    private boolean isValidGeologicalFeature(int geologicalFeature) {
	return geologicalFeature == AIR || geologicalFeature == SOLID_GROUND || geologicalFeature == WATER
		|| geologicalFeature == MAGMA;

    }

    private void initializeGeologicalFeatures(int xLength, int yLength, int... geologicalFeatures) {
	tiles = new HashSet<String>();

	for (int i = 0; i < geologicalFeatures.length; i++)
	    tiles.add(i % xLength + " " + i / xLength + " " + geologicalFeatures[i]);

    }

    public HashSet<String> tiles;

    private final HashSet<GameObject> objects = new HashSet<GameObject>();

    GameObject player;
    private boolean hasPlayer;

    public void addGameObject(GameObject gameObject) throws RuntimeException {
	if (getAllObjects().size() - 1 == getMaxNbOfObjects())
	    throw new RuntimeException();
	if (!gameObject.isValidGameObject())
	    throw new RuntimeException();
	if (gameObject.getXPositionPixel() >= getWorldSizeX() || gameObject.getXPositionActual() < 0)
	    throw new RuntimeException();
	if (gameObject.getYPositionPixel() >= getWorldSizeY() || gameObject.getYPositionActual() < 0)
	    throw new RuntimeException();
	if (gameObject.getWorld() != null)
	    throw new RuntimeException();

	if (!canPlaceObject(gameObject))
	    throw new RuntimeException();

	if (getGeologicalFeature(gameObject.getXPositionPixel(), gameObject.getYPositionPixel()) == SOLID_GROUND
		&& getGeologicalFeature(gameObject.getXPositionPixel() + 1, gameObject.getYPositionPixel() + 1) != AIR)
	    throw new RuntimeException();

	if (!hasPlayer() && gameObject instanceof Mazub)
	    setPlayer(gameObject);

	objects.add(gameObject);
	gameObject.world = this;
    }

    public boolean canPlaceObject(GameObject gameObject) {
	if (!(gameObject instanceof Plant)) {
	    for (final Object object : getAllObjects())
		if (gameObject.collidesWith((GameObject) object) && !(object instanceof Plant) && gameObject != object)
		    return false;

	    for (int x = gameObject.getXPositionPixel(); x < gameObject.getXPositionPixel()
		    + gameObject.getXsize(); x++)
		for (int y = gameObject.getYPositionPixel() + 1; y < gameObject.getYPositionPixel()
			+ gameObject.getYsize(); y++)
		    if (getGeologicalFeature(x, y) == SOLID_GROUND)
			return false;
	}
	return true;
    }

    public boolean canPlaceMazubAdvanceTime(GameObject gameObject, GameObject other) {
	if (!(gameObject instanceof Plant)) {
	    for (final Object object : getAllObjects())
		if (gameObject.collidesWith((GameObject) object) && !(object instanceof Plant) && gameObject != object
			&& object != other)
		    return false;

	    for (int x = gameObject.getXPositionPixel(); x < gameObject.getXPositionPixel()
		    + gameObject.getXsize(); x++) {
		if (getGeologicalFeature(x, gameObject.getYPositionPixel() + 1) == SOLID_GROUND)
		    return false;
		if (getGeologicalFeature(x, gameObject.getYPositionPixel() + gameObject.getYsize() - 1) == SOLID_GROUND)
		    return false;
	    }
	    for (int y = gameObject.getYPositionPixel() + 1; y < gameObject.getYPositionPixel()
		    + gameObject.getYsize(); y++) {
		if (getGeologicalFeature(gameObject.getXPositionPixel(), y) == SOLID_GROUND)
		    return false;
		if (getGeologicalFeature(gameObject.getXPositionPixel() + gameObject.getXsize() - 1, y) == SOLID_GROUND)
		    return false;
	    }
	}
	return true;
    }

    private boolean hasPlayer() {
	return hasPlayer;
    }

    void setPlayer(GameObject gameObject) {
	player = gameObject;
	((Mazub) gameObject).isPlayer = true;
	hasPlayer = true;
    }

    public GameObject getPlayer() {
	return player;
    }

    public Set<Object> getAllObjects() {
	final HashSet<Object> allObjects = new HashSet<Object>(objects.size());
	for (final GameObject object : objects) {
	    final Object gameObjectAsObject = object;
	    allObjects.add(gameObjectAsObject);
	}
	return allObjects;
    }

    public void removeObject(GameObject gameObject) {
	objects.remove(gameObject);
	gameObject.world = null;
	if (!getAllObjects().contains(getPlayer())) {
	    player = null;
	    hasPlayer = false;
	}

    }

    public boolean hasAsGameObject(GameObject gameObject) {
	if (getAllObjects().contains(gameObject))
	    return true;
	return false;
    }

    public double getTimeStep(GameObject gameObject, double deltaT) {
	final double velocityRoot = Math.sqrt(
		Math.pow(gameObject.getHorizontalSpeedMeters(), 2) + Math.pow(gameObject.getVerticalSpeedMeters(), 2));
	final double accelerationRoot = Math.sqrt(Math.pow(gameObject.getHorizontalAcceleration(), 2)
		+ Math.pow(gameObject.getVerticalAcceleration(), 2));
	return 0.01 / (velocityRoot + accelerationRoot * deltaT);
    }

    public void advanceWorldTime(double dt) {
	for (final Object object : getAllObjects()) {
	    final double timeStep = getTimeStep((GameObject) object, dt);
	    ((GameObject) object).advanceTime(dt, timeStep);
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
}