package jumpingalien.facade;

import jumpingalien.model.Mazub;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;

public class Facade implements IFacade {

	@Override
	public boolean isTeamSolution() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Mazub createMazub(int pixelLeftX, int pixelBottomY, Sprite... sprites) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getActualPosition(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeActualPosition(Mazub alien, double[] newPosition) throws ModelException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getPixelPosition(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOrientation(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] getVelocity(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getAcceleration(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isMoving(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startMoveLeft(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startMoveRight(Mazub alien) throws ModelException {
		// TODO Auto-generated method stub

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
