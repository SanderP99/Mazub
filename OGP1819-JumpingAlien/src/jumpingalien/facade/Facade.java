package jumpingalien.facade;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import jumpingalien.model.GameObject;
import jumpingalien.model.ImpassableTerrain;
import jumpingalien.model.Mazub;
import jumpingalien.model.School;
import jumpingalien.model.Shark;
import jumpingalien.model.Skullcab;
import jumpingalien.model.Slime;
import jumpingalien.model.Sneezewort;
import jumpingalien.model.World;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;

public class Facade implements IFacade {

    @Override
    public boolean isTeamSolution() {
	return true;
    }

    @Override
    public Mazub createMazub(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
	if (sprites == null)
	    throw new ModelException("The sprites are not valid");
	if (sprites.length < 10 || sprites.length % 2 != 0)
	    throw new ModelException("The sprites are not valid");
	for (final Sprite sprite2 : sprites)
	    if (sprite2 == null)
		throw new ModelException("The sprites are not valid");
	if (pixelLeftX < 0 || pixelBottomY < 0)
	    throw new ModelException("Mazub not in universe");
	final Sprite sprite = sprites[0];
	final Mazub mazub = new Mazub(pixelLeftX, pixelBottomY, sprite.getWidth(), sprite.getHeight(), 0.0, 1.0, 3.0,
		1.0, false, sprites);
	return mazub;
    }

    @Override
    public double[] getActualPosition(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	final double actualX = alien.getXPositionActual();
	final double actualY = alien.getYPositionActual();
	final double[] position = new double[] { actualX, actualY };
	return position;
    }

    @Override
    public void changeActualPosition(Mazub alien, double[] newPosition) throws ModelException {
	if (newPosition == null)
	    throw new ModelException("Position can not be null");
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	if (newPosition.length != 2)
	    throw new ModelException("Only 2  coordinates allowed");
	if (newPosition[0] != newPosition[0] || newPosition[1] != newPosition[1])
	    throw new ModelException("NaN as position argument");
	for (final ImpassableTerrain feature : ImpassableTerrain.values())
	    if (alien.getWorld() != null && alien.getWorld().getGeologicalFeature((int) (newPosition[0] * 100),
		    (int) (newPosition[1] * 100)) == feature.getValue())
		throw new ModelException("New position on impassable terrain");
	if (getWorld(alien) != null && !getWorld(alien).canPlaceObject(alien))
	    throw new ModelException("Can't place new alien");
	if (!alien.isValidActualXPosition(newPosition[0]) || !alien.isValidActualYPosition(newPosition[1]))
	    alien.terminate();
	if (getWorld(alien) != null
		&& !alien.isPositionInWorld((int) (newPosition[0] * 100), (int) (newPosition[1] * 100)))
	    alien.terminate();

	if (alien.getWorld() != null)
	    for (final ImpassableTerrain feature : ImpassableTerrain.values())
		for (double x = newPosition[0]; x < newPosition[0] + (double) alien.getXsize() / 100; x += 0.01)
		    for (double y = newPosition[1] + 0.01; y < newPosition[0]
			    + (double) alien.getYsize() / 100; y += 0.01)
			if (alien.getWorld().getGeologicalFeature((int) (x * 100), (int) (y * 100)) == feature
				.getValue())
			    throw new ModelException("Can't place here");

	alien.setXPositionActual(newPosition[0]);
	alien.setYPositionActual(newPosition[1]);

	if (alien.getWorld() != null)
	    if (!alien.isStandingOnImpassableTerrain())
		alien.setVerticalAcceleration(-10.0);

    }

    @Override
    public int[] getPixelPosition(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	final int[] position = new int[] { alien.getXPositionPixel(), alien.getYPositionPixel() };
	return position;
    }

    @Override
    public int getOrientation(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	return alien.getOrientation();
    }

    @Override
    public double[] getVelocity(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	final double[] velocity = new double[] { alien.getHorizontalSpeedMeters(), alien.getVerticalSpeedMeters() };
	return velocity;
    }

    @Override
    public double[] getAcceleration(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");

	final double[] acceleration = new double[] { alien.getHorizontalAcceleration(),
		alien.getVerticalAcceleration() };
	return acceleration;
    }

    @Override
    public Sprite[] getSprites(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	return alien.getSpriteArray();
    }

    @Override
    public Sprite getCurrentSprite(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	return alien.getCurrentSprite();
    }

    @Override
    public boolean isMoving(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");

	if (alien.getVerticalSpeedMeters() != 0 || alien.getHorizontalSpeedMeters() != 0)
	    return true;
	return false;
    }

    @Override
    public void startMoveLeft(Mazub alien) throws ModelException {
	if (alien.isDead())
	    throw new ModelException("The alien is dead");
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	if (alien.isMoving)
	    throw new ModelException("The alien is already moving");
	alien.startMove(-1);

    }

    @Override
    public void startMoveRight(Mazub alien) throws ModelException {
	if (alien.isDead())
	    throw new ModelException("The alien is dead");
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	if (alien.isMoving)
	    throw new ModelException("The alien is already moving");
	alien.startMove(1);

    }

    @Override
    public void endMove(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	if (!alien.isMoving)
	    throw new ModelException("The alien is not moving");
	alien.endMove();

    }

    @Override
    public boolean isJumping(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	if (alien.isJumping)
	    return true;
	return false;
    }

    @Override
    public void startJump(Mazub alien) throws ModelException {
	if (alien.isDead())
	    throw new ModelException("The alien is dead");
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	if (isJumping(alien))
	    throw new ModelException("The alien is already jumping");

	try {
	    alien.startJump();
	} catch (final RuntimeException e) {
	    throw new ModelException("Runtime exception");
	}

    }

    @Override
    public void endJump(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	if (!isJumping(alien) && !alien.isFalling)
	    throw new ModelException("The alien is not jumping");

	try {
	    alien.endJump();
	} catch (final RuntimeException e) {
	    throw new ModelException("Runtime exception");
	}

    }

    @Override
    public boolean isDucking(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	return alien.isDucking();
    }

    @Override
    public void startDuck(Mazub alien) throws ModelException {
	if (alien.isDead())
	    throw new ModelException("The alien is dead");
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	alien.startDuck();
    }

    @Override
    public void endDuck(Mazub alien) throws ModelException {
	if (!alien.isValidGameObject())
	    throw new ModelException("The alien is not valid");
	alien.endDuck();

    }

    @Override
    public World createWorld(int tileSize, int nbTilesX, int nbTilesY, int[] targetTileCoordinate,
	    int visibleWindowWidth, int visibleWindowHeight, int... geologicalFeatures) throws ModelException {
	try {
	    if (targetTileCoordinate.length != 2)
		throw new ModelException("Target tile not valid");
	    final World world = new World(nbTilesX, nbTilesY, tileSize, targetTileCoordinate[0],
		    targetTileCoordinate[1], visibleWindowWidth, visibleWindowHeight, 100, geologicalFeatures);
	    return world;
	} catch (final RuntimeException e) {
	    throw new ModelException("The window is not valid");
	}
    }

    @Override
    public void terminateWorld(World world) throws ModelException {
	world.terminate();

    }

    @Override
    public int[] getSizeInPixels(World world) throws ModelException {
	return new int[] { world.getWorldSizeX(), world.getWorldSizeY() };
    }

    @Override
    public int getTileLength(World world) throws ModelException {
	return world.getTileLength();
    }

    @Override
    public int getGeologicalFeature(World world, int pixelX, int pixelY) throws ModelException {
	return world.getGeologicalFeature(pixelX, pixelY);
    }

    @Override
    public void setGeologicalFeature(World world, int pixelX, int pixelY, int geologicalFeature) throws ModelException {
	world.setGeologicalFeature(pixelX, pixelY, geologicalFeature);

    }

    @Override
    public int[] getVisibleWindowDimension(World world) throws ModelException {
	return new int[] { world.getVisibleWindowWidth(), world.getVisibleWindowHeight() };
    }

    @Override
    public int[] getVisibleWindowPosition(World world) throws ModelException {
	return world.getVisibleWindowPosition();
    }

    @Override
    public boolean hasAsGameObject(Object object, World world) throws ModelException {
	return world.hasAsGameObject((GameObject) object);
    }

    @Override
    public Set<Object> getAllGameObjects(World world) throws ModelException {
	return world.getAllObjects();
    }

    @Override
    public Mazub getMazub(World world) throws ModelException {
	if (world.getPlayer() == null)
	    return null;
	return (Mazub) world.getPlayer();
    }

    @Override
    public void addGameObject(Object object, World world) throws ModelException {
	try {
	    if (!((GameObject) object).isValidGameObject())
		throw new ModelException("The object is not valid");
	    if (world.isTerminated())
		throw new ModelException("The world is terminated");
	    if (world.hasPlayer() && object instanceof Mazub)
		throw new ModelException("The world already has a Mazub");
	    world.addGameObject((GameObject) object);
	} catch (final Exception e) {
	    throw new ModelException("Too many objects");
	}
    }

    @Override
    public void removeGameObject(Object object, World world) throws ModelException {
	if (world.hasAsGameObject((GameObject) object))
	    world.removeObject((GameObject) object);
	else
	    throw new ModelException("The object to remove does not exist");

    }

    @Override
    public int[] getTargetTileCoordinate(World world) throws ModelException {
	return new int[] { world.getTargetTileX() / getTileLength(world),
		world.getTargetTileY() / getTileLength(world) };
    }

    @Override
    public void setTargetTileCoordinate(World world, int[] tileCoordinate) throws ModelException {
	world.setTargetTileX(tileCoordinate[0] * getTileLength(world));
	world.setTargetTileY(tileCoordinate[1] * getTileLength(world));

    }

    @Override
    public void startGame(World world) throws ModelException {
	if (world.getPlayer() == null)
	    throw new ModelException("No Mazub");
	world.hasStarted = true;
    }

    @Override
    public boolean isGameOver(World world) throws ModelException {
	if (world.getPlayer() == null)
	    return true;
	if (world.getPlayer().isTerminated())
	    return true;
	if (didPlayerWin(world))
	    return true;
	return false;
    }

    @Override
    public boolean didPlayerWin(World world) throws ModelException {
	if (world.getPlayer() == null)
	    return false;

	final List<int[]> tiles = world.getPlayer().getAllOverlappingTiles();
	final int[] targetTile = new int[] { getTargetTileCoordinate(world)[0], getTargetTileCoordinate(world)[1] };

	for (final int[] tile : tiles)
	    if (tile[0] == targetTile[0] && tile[1] == targetTile[1])
		return true;
	return false;
    }

    @Override
    public void advanceWorldTime(World world, double dt) throws ModelException {

	try {
	    world.advanceWorldTime(dt);

	} catch (final IllegalArgumentException e) {
	    throw new ModelException("dt is not valid");
	}

    }

    @Override
    public void terminateGameObject(Object gameObject) throws ModelException {
	((GameObject) gameObject).terminate();

    }

    @Override
    public boolean isTerminatedGameObject(Object gameObject) throws ModelException {
	return ((GameObject) gameObject).isTerminated();
    }

    @Override
    public boolean isDeadGameObject(Object gameObject) throws ModelException {
	return ((GameObject) gameObject).isDead();
    }

    @Override
    public double[] getActualPosition(Object gameObject) throws ModelException {
	return new double[] { ((GameObject) gameObject).getXPositionActual(),
		((GameObject) gameObject).getYPositionActual() };
    }

    @Override
    public void changeActualPosition(Object gameObject, double[] newPosition) throws ModelException {
	if (getWorld(gameObject) != null)
	    for (final ImpassableTerrain feature : ImpassableTerrain.values())
		if (getGeologicalFeature(getWorld(gameObject), (int) (newPosition[0] * 100),
			(int) (newPosition[1] * 100)) == feature.getValue())
		    throw new ModelException("The new position is in impassable terrain");
	((GameObject) gameObject).setXPositionActual(newPosition[0]);
	((GameObject) gameObject).setYPositionActual(newPosition[1]);

    }

    @Override
    public int[] getPixelPosition(Object gameObject) throws ModelException {
	return new int[] { ((GameObject) gameObject).getXPositionPixel(),
		((GameObject) gameObject).getYPositionPixel() };
    }

    @Override
    public int getOrientation(Object gameObject) throws ModelException {
	return ((GameObject) gameObject).getOrientation();
    }

    @Override
    public double[] getVelocity(Object gameObject) throws ModelException {
	return new double[] { ((GameObject) gameObject).getHorizontalSpeedMeters(),
		((GameObject) gameObject).getVerticalSpeedMeters() };
    }

    @Override
    public World getWorld(Object object) throws ModelException {
	if (object instanceof School)
	    return ((School) object).getWorld();
	return ((GameObject) object).getWorld();
    }

    @Override
    public int getHitPoints(Object object) throws ModelException {
	return ((GameObject) object).getHitpoints();
    }

    @Override
    public Sprite[] getSprites(Object gameObject) throws ModelException {
	return ((GameObject) gameObject).getSpriteArray();
    }

    @Override
    public Sprite getCurrentSprite(Object gameObject) throws ModelException {
	return ((GameObject) gameObject).getCurrentSprite();
    }

    @Override
    public void advanceTime(Object gameObject, double dt) throws ModelException {
	if (dt != dt)
	    throw new ModelException("The time is not valid");
	if (((GameObject) gameObject).getWorld() != null)
	    ((GameObject) gameObject).advanceTime(dt,
		    ((GameObject) gameObject).getWorld().getTimeStep((GameObject) gameObject, dt));
	else
	    ((GameObject) gameObject).advanceTime(dt, 0.002);
    }

    @Override
    public double[] getAcceleration(Object gameObject) throws ModelException {
	return new double[] { ((GameObject) gameObject).getHorizontalAcceleration(),
		((GameObject) gameObject).getVerticalAcceleration() };
    }

    @Override
    public Sneezewort createSneezewort(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
	if (sprites == null)
	    throw new ModelException("The sprites are not valid");
	if (sprites.length != 2)
	    throw new ModelException("The sprites are not valid");
	for (final Sprite sprite : sprites)
	    if (sprite == null)
		throw new ModelException("The sprites are not valid");
	return new Sneezewort(pixelLeftX, pixelBottomY, sprites[0].getWidth(), sprites[0].getHeight(), 0.5, 1, 10.0,
		0.5, 0.5, 0.5, 0, 0, sprites);
    }

    @Override
    public Skullcab createSkullcab(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
	if (sprites == null)
	    throw new ModelException("The sprites are not valid");
	if (sprites.length != 2)
	    throw new ModelException("The sprites are not valid");
	for (final Sprite sprite : sprites)
	    if (sprite == null)
		throw new ModelException("The sprites are not valid");
	return new Skullcab(pixelLeftX, pixelBottomY, sprites[0].getWidth(), sprites[0].getHeight(), 0.5, 3, 12.0, 0.5,
		0.5, 0.5, 0, 0, sprites);
    }

    @Override
    public Slime createSlime(long id, int pixelLeftX, int pixelBottomY, School school, Sprite... sprites)
	    throws ModelException {
	if (sprites == null)
	    throw new ModelException("The sprites are not valid");
	for (final Sprite sprite : sprites)
	    if (sprite == null)
		throw new ModelException("The sprites are not valid");
	if (sprites.length != 2)
	    throw new ModelException("The amount of sprites is not valid");
	if (id < 0)
	    throw new ModelException("ID is not valid");

	if (GameObject.hasSlimeWithID(id))
	    throw new ModelException("ID is already in use");

	return new Slime(pixelLeftX, pixelBottomY, sprites[0].getWidth(), sprites[0].getHeight(), 100,
		Integer.MAX_VALUE, 2.5, 2.5, 0.0, 0.0, 0.7, -10.0, false, id, school, sprites);
    }

    @Override
    public long getIdentification(Slime slime) throws ModelException {
	return slime.getIdentification();
    }

    @Override
    public School createSchool(World world) throws ModelException {
	if (world != null)
	    if (world.getMaxNbOfSchools() == world.getNbOfSchools())
		throw new ModelException("Maximum number of schools reached");
	return new School(world);
    }

    @Override
    public boolean hasAsSlime(School school, Slime slime) throws ModelException {
	return school.hasSlime(slime);
    }

    @Override
    public Collection<? extends Slime> getAllSlimes(School school) {
	return school.getAllSlimes();
    }

    @Override
    public void addAsSlime(School school, Slime slime) throws ModelException {
	if (slime.getSchool() != null)
	    throw new ModelException("Slime already in school");
	if (slime.isTerminated())
	    throw new ModelException("Slime is terminated");
	if (school.isTerminated())
	    throw new ModelException("School is terminated");
	school.addSlime(slime);

    }

    @Override
    public void removeAsSlime(School school, Slime slime) throws ModelException {
	if (slime.getSchool() != school)
	    throw new ModelException("Slime not in school");
	school.removeSlime(slime);

    }

    @Override
    public void switchSchool(School newSchool, Slime slime) throws ModelException {
	if (slime.getSchool() == null || slime.getSchool() == newSchool || newSchool == null || slime.isTerminated()
		|| newSchool.isTerminated())
	    throw new ModelException("School not valid");

	slime.switchSchool(newSchool);
    }

    @Override
    public School getSchool(Slime slime) throws ModelException {
	return slime.getSchool();
    }

    @Override
    public Shark createShark(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
	if (sprites == null)
	    throw new ModelException("The sprites are not valid");
	for (final Sprite sprite : sprites)
	    if (sprite == null)
		throw new ModelException("The sprites are not valid");
	return new Shark(pixelLeftX, pixelBottomY, sprites[0].getWidth(), sprites[0].getHeight(), 100, 100, 10, 10, 0,
		2, 1.5, -10.0, false, sprites);
    }

    @Override
    public Set<School> getAllSchools(World world) throws ModelException {
	return world.getAllSchools();
    }

    @Override
    public void terminateSchool(School school) throws ModelException {
	school.terminate();
    }

}