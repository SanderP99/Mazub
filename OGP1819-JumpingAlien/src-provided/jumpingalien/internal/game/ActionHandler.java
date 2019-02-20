package jumpingalien.internal.game;

public class ActionHandler extends AbstractActionHandler {

	public ActionHandler(JumpingAlienGame game) {
		super(game);
	}

	@Override
	public void startJump() {
		addAlienCommand("startJump", getFacade()::startJump);
	}

	@Override
	public void endJump() {
		addAlienCommand("endJump", getFacade()::endJump);
	}

	@Override
	public void startMoveLeft() {
		addAlienCommand("startMoveLeft", getFacade()::startMoveLeft);
	}

	@Override
	public void startMoveRight() {
		addAlienCommand("startMoveRight", getFacade()::startMoveRight);
	}

	@Override
	public void endMoveLeft() {
		addAlienCommand("endMoveLeft", getFacade()::endMove);
	}

	@Override
	public void endMoveRight() {
		addAlienCommand("endMoveRight", getFacade()::endMove);
	}

	@Override
	public void startDuck() {
		addAlienCommand("startDuck", getFacade()::startDuck);
	}

	@Override
	public void endDuck() {
		addAlienCommand("endDuck", getFacade()::endDuck);
	}
}