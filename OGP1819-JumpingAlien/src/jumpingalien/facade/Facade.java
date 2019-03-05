package jumpingalien.facade;

import jumpingalien.model.Mazub;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;

public class Facade implements IFacade {

	@Override
	public boolean isTeamSolution() {
		return true;
	}

	@Override
	public Mazub createMazub(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		Sprite sprite = sprites[0];
		Mazub mazub = new Mazub(pixelLeftX, pixelBottomY, sprite.getWidth(), sprite.getHeight(), 0.0, 1.0, 3.0, 1.0, sprites);
		return mazub;
	}

	@Override
	public double[] getActualPosition(Mazub alien) throws ModelException {
//		if (!alien.isValidAlien())
//			throw new ModelException("The alien is not valid");
		double actualX = alien.getXPositionActual();
		double actualY = alien.getYPositionActual();
		double[] position = new double[] {actualX, actualY};
		return position;
	}

	@Override
	public void changeActualPosition(Mazub alien, double[] newPosition) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
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
//		if (!alien.isValidAlien())
//			throw new ModelException("The alien is not valid");
		
		if (alien.getVerticalSpeedMeters() != 0 || alien.getHorizontalSpeedMeters() != 0)
			return true;
		return false;
	}

	@Override
	public void startMoveLeft(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		alien.startMove(-1);

	}

	@Override
	public void startMoveRight(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		alien.startMove(1);

	}

	@Override
	public void endMove(Mazub alien) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
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
		if (!isJumping(alien))
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

	@Override
	public void advanceTime(Mazub alien, double dt) throws ModelException {
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		alien.advanceTime(dt);

	}

}
