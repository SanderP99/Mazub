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
		//TODO
	}

	@Override
	public double[] getActualPosition(Mazub alien) throws ModelException {
		if (alien == null)
			throw new ModelException("The alien is null");
		if (!alien.isValidAlien());
		double actualX = alien.getXPosition();
		double actualY = alien.getYPosition();
		double[] position = new double[] {actualX/100, actualY/100};
		return position;
	}

	@Override
	public void changeActualPosition(Mazub alien, double[] newPosition) throws ModelException {
		if (alien == null)
			throw new ModelException("The alien is null");
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		if (!alien.isValidXPosition(newPosition[0]) || !alien.isValidYPosition(newPosition[1]))
			throw new ModelException("The position is not valid");
		
		alien.setXPosition(newPosition[0]);
		alien.setYPosition(newPosition[1]);

	}

	@Override
	public int[] getPixelPosition(Mazub alien) throws ModelException {
		if (alien == null)
			throw new ModelException("The alien is null");
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		int[] position = new int[] {alien.getXPosition(), alien.getYPosition()};
		return position;
	}

	@Override
	public int getOrientation(Mazub alien) throws ModelException {
		if (alien == null)
			throw new ModelException("The alien is null");
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		return alien.getOrientation();
	}

	@Override
	public double[] getVelocity(Mazub alien) throws ModelException {
		if (alien == null)
			throw new ModelException("The alien is null");
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		double[] velocity = new double[] {alien.getHorizontalSpeedMeters(), alien.getVerticalSpeedMeters()};
		return velocity;
	}

	@Override
	public double[] getAcceleration(Mazub alien) throws ModelException {
		if (alien == null)
			throw new ModelException("The alien is null");
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		
		double[] acceleration = new double[] {alien.getHorizontalAcceleration(), alien.getVerticalAcceleration()};
		return acceleration;
	}

	@Override
	public boolean isMoving(Mazub alien) throws ModelException {
		if (alien == null)
			throw new ModelException("The alien is null");
		if (!alien.isValidAlien())
			throw new ModelException("The alien is not valid");
		else
			if (alien.getVerticalSpeedMeters() != 0 || alien.getHorizontalSpeedMeters() != 0)
				return true;
		return false;
	}

	@Override
	public void startMoveLeft(Mazub alien) throws ModelException {
		alien.startMove(-1);

	}

	@Override
	public void startMoveRight(Mazub alien) throws ModelException {
		alien.startMove(1);

	}

	@Override
	public void endMove(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isJumping(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startJump(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endJump(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDucking(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startDuck(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endDuck(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub

	}

	@Override
	public void advanceTime(Mazub alien, double dt) throws ModelException {
		// TODO Auto-generated method stub

	}

}
