package jumpingalien.facade;

import java.util.Set;

import jumpingalien.model.GameObject;
import jumpingalien.model.Mazub;
import jumpingalien.model.Plant;
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
		if (sprites.length < 10 || ((sprites.length % 2) != 0))
			throw new ModelException("The sprites are not valid");
		for (int i = 0; i < sprites.length; i++)
			if (sprites[i] == null)
				throw new ModelException("The sprites are not valid");
		Sprite sprite = sprites[0];
		Mazub mazub = new Mazub(pixelLeftX, pixelBottomY, sprite.getWidth(), sprite.getHeight(), 0.0, 1.0, 3.0, 1.0, sprites);
		return mazub;
	}

	@Override
	public double[] getActualPosition(Mazub alien) throws ModelException {
	if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		double actualX = alien.getXPositionActual();
		double actualY = alien.getYPositionActual();
		double[] position = new double[] {actualX, actualY};
		return position;
	}

	@Override
	public void changeActualPosition(Mazub alien, double[] newPosition) throws ModelException {
		if (newPosition == null)
			throw new ModelException("Position can not be null");
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		if (newPosition.length != 2)
			throw new ModelException("Only 2  coordinates allowed");
		if (!alien.isValidActualXPosition(newPosition[0]) || !alien.isValidActualYPosition(newPosition[1]))
			throw new ModelException("The position is not valid");
		
		alien.setXPositionActual(newPosition[0]);
		alien.setYPositionActual(newPosition[1]);

	}

	@Override
	public int[] getPixelPosition(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		int[] position = new int[] {alien.getXPositionPixel(), alien.getYPositionPixel()};
		return position;
	}

	@Override
	public int getOrientation(Mazub alien) throws ModelException {
	if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		return alien.getOrientation();
	}

	@Override
	public double[] getVelocity(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		double[] velocity = new double[] {alien.getHorizontalSpeedMeters(), alien.getVerticalSpeedMeters()};
		return velocity;
	}

	@Override
	public double[] getAcceleration(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		
		double[] acceleration = new double[] {alien.getHorizontalAcceleration(), alien.getVerticalAcceleration()};
		return acceleration;
	}
	
	public Sprite[] getSprites(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		return alien.getSpriteArray();
	}

	public Sprite getCurrentSprite(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		return alien.getCurrentSprite();
	}
	
	@Override
	public boolean isMoving(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		
		if (alien.getVerticalSpeedMeters() != 0 || alien.getHorizontalSpeedMeters() != 0)
			return true;
		return false;
	}

	@Override
	public void startMoveLeft(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		if (alien.isMoving)
			throw new ModelException("The alien is already moving");
		alien.startMove(-1);

	}

	@Override
	public void startMoveRight(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		if (alien.isMoving)
			throw new ModelException("The alien is already moving");
		alien.startMove(1);

	}

	@Override
	public void endMove(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		if (!alien.isMoving)
			throw new ModelException("The alien is not moving");
		alien.endMove();

	}

	@Override
	public boolean isJumping(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		if (alien.isJumping)
			return true;
		return false;
	}

	@Override
	public void startJump(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		if (isJumping(alien))
			throw new ModelException("The alien is already jumping");
		alien.startJump();

	}

	@Override
	public void endJump(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		if (!isJumping(alien) && !alien.isFalling)
			throw new ModelException("The alien is not jumping");
		alien.endJump();

	}

	@Override
	public boolean isDucking(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		return alien.isDucking;
	}

	@Override
	public void startDuck(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		alien.startDuck();

	}

	@Override
	public void endDuck(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		alien.endDuck();

	}

//	@Override
//	public void advanceTime(Mazub alien, double dt) throws ModelException {
//		if (!alien.isValidAlien())
//			throw new ModelException("The alien is not valid");
//		alien.advanceTime(dt);

//	}

	@Override
	public World createWorld(int tileSize, int nbTilesX, int nbTilesY, int[] targetTileCoordinate,
			int visibleWindowWidth, int visibleWindowHeight, int... geologicalFeatures) throws ModelException {
		return new World(nbTilesX*tileSize,nbTilesY*tileSize,tileSize,targetTileCoordinate[0],
				targetTileCoordinate[1],visibleWindowWidth,
				visibleWindowHeight,geologicalFeatures);
	}

	@Override
	public void terminateWorld(World world) throws ModelException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] getSizeInPixels(World world) throws ModelException {
		return new int[] {world.getWorldSizeX(), world.getWorldSizeY()};
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
		return new int[] {world.getVisibleWindowWidth(), world.getVisibleWindowHeight()};
	}
	@Override
	public int[] getVisibleWindowPosition(World world) throws ModelException {
		//TODO 
		return new int[] {0};
	}

	@Override
	public boolean hasAsGameObject(Object object, World world) throws ModelException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Object> getAllGameObjects(World world) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mazub getMazub(World world) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addGameObject(Object object, World world) throws ModelException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeGameObject(Object object, World world) throws ModelException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] getTargetTileCoordinate(World world) throws ModelException {
		return new int[] {world.getTargetTileX(), world.getTargetTileY()};
	}

	@Override
	public void setTargetTileCoordinate(World world, int[] tileCoordinate) throws ModelException {
		world.setTargetTileX(tileCoordinate[0]);
		world.setTargetTileY(tileCoordinate[1]);
		
	}

	@Override
	public void startGame(World world) throws ModelException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isGameOver(World world) throws ModelException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean didPlayerWin(World world) throws ModelException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void advanceWorldTime(World world, double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Plant createPlant(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		return new Plant(pixelLeftX, pixelBottomY, sprites[0].getWidth(), sprites[0].getHeight(), 0.5, 10, 10, 0.5, 0.5, 0.5, 0, 0,sprites);
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
		return new double[] {((GameObject) gameObject).getXPositionActual(), ((GameObject) gameObject).getYPositionActual()};
	}

	@Override
	public void changeActualPosition(Object gameObject, double[] newPosition) throws ModelException {
		((GameObject) gameObject).setXPositionActual(newPosition[0]);
		((GameObject) gameObject).setYPositionActual(newPosition[1]);
		
	}

	@Override
	public int[] getPixelPosition(Object gameObject) throws ModelException {
		return new int[] {((GameObject) gameObject).getXPositionPixel(), ((GameObject) gameObject).getYPositionPixel()};
 	}

	@Override
	public int getOrientation(Object gameObject) throws ModelException {
		return ((GameObject) gameObject).getOrientation();
	}

	@Override
	public double[] getVelocity(Object gameObject) throws ModelException {
		return new double[] {((GameObject) gameObject).getHorizontalSpeedMeters(), ((GameObject) gameObject).getVerticalSpeedMeters()};
 	}

	@Override
	public World getWorld(Object object) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHitPoints(Object object) throws ModelException {
		return ((GameObject) object).getHitpoints();
	}

	@Override
	public Sprite[] getSprites(Object gameObject) throws ModelException {
		return ((GameObject) gameObject).getSprites();
	}
	@Override
	public Sprite getCurrentSprite(Object gameObject) throws ModelException{
		return ((GameObject) gameObject).getCurrentSprite();
	}

	@Override
	public void advanceTime(Object gameObject, double dt) throws ModelException {
		// TODO Auto-generated method stub
		
	}

}
