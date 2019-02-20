package jumpingalien.internal.game;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import jumpingalien.facade.IFacade;
import jumpingalien.internal.game.AlienInfoProvider;
import jumpingalien.internal.game.IActionHandler;
import jumpingalien.internal.game.JumpingAlienGameOptions;
import jumpingalien.internal.game.WorldInfoProvider;
import jumpingalien.internal.gui.sprites.JumpingAlienSprites;
import jumpingalien.model.Mazub;
import jumpingalien.util.ModelException;
import jumpingalien.util.Sprite;
import ogp.framework.command.Command;
import ogp.framework.game.Game;
import ogp.framework.messages.Message;
import ogp.framework.messages.MessageType;

public class JumpingAlienGame extends Game {

	private static final int WORLD_HEIGHT = 768;
	private static final int WORLD_WIDTH = 1024;

	
	private static final int MAX_MISSED_DEADLINES = 4;

	private static final double MAX_TIME_STEP = 0.200;

	private Mazub alien;
	
	private IActionHandler handler;

	private int visibleScreenWidth = -1;
	private int visibleScreenHeight = -1;

	private double elapsedTime = 0;
	private boolean running;

	private final IFacade facade;

	public JumpingAlienGame(JumpingAlienGameOptions options, IFacade facade) {
		super(options);
		this.handler = createActionHandler();
		this.worldInfoProvider = createWorldInfoProvider();
		this.alienInfoProvider = createAlienInfoProvider();
		this.facade = facade;
	}

	public IFacade getFacade() {
		return facade;
	}

	@Override
	public JumpingAlienGameOptions getOptions() {
		return (JumpingAlienGameOptions) super.getOptions();
	}

	public void restart() {
		start();
	}

	@Override
	public void start() {
		if (visibleScreenWidth < 0 || visibleScreenHeight < 0) {
			throw new IllegalStateException("Visible screen size not set");
		}

		createModel();

		running = true;
	}

	public void setPause(boolean value) {
		running = !value;
	}

	public void stop() {
		running = false;
	}

	protected void createModel() {
		int initialPositionX = 0;
		int initialPositionY = 0;

		setAlien(getFacade().createMazub(initialPositionX, initialPositionY,
				JumpingAlienSprites.ALIEN_SPRITESET));
	}
	
	private void setAlien(Mazub alien) {
		if (this.alien != null) {
			throw new IllegalStateException("Mazub already created!");
		}
		this.alien = alien;
	}

	Mazub getAlien() {
		return alien;
	}


	protected IActionHandler createActionHandler() {
		return new ActionHandler(this);
	}
	

	@Override
	public void load() {		
	}


	/**
	 * Hack to skip first call to update after game has started (time interval
	 * too large due to initial screen painting)
	 */
	private boolean firstUpdate = true;

	@Override
	protected void doUpdate(double dt) {
		if (isRunning()) {
			if (!firstUpdate) {
				dt = applyTimescale(dt);
				executePendingCommands();
				try {
					advanceTime(dt);
					elapsedTime += dt;
				} catch (ModelException e) {
					addMessage(new Message(MessageType.ERROR, e.getMessage()));
					System.out.println("Could not advance time by dt=" + dt
							+ ": " + e.getMessage());
					e.printStackTrace();
				}
			}
			firstUpdate = false;
		}
	}

	/**
	 * Scale the given time interval based on the game options.
	 * 
	 * If the scaled time in MAX_MISSED_DEADLINES subsequent invocations exceeds
	 * MAX_TIME_STEP, the time scale is adapted.
	 * 
	 * The returned time interval is always guaranteed to be smaller than or
	 * equal to MAX_TIME_STEP.
	 */
	protected double applyTimescale(double dt) {
		double scaledDT = dt / getOptions().getTimescale();
		if (scaledDT > MAX_TIME_STEP) {
			scaledDT = MAX_TIME_STEP;

			deadlineMissed(dt);
		} else {
			deadlineMet(dt);
		}
		return scaledDT;
	}

	private int nbSubsequentDeadlinesMissed = 0;
	private double totalMissedTime;

	private void deadlineMissed(double dt) {
		nbSubsequentDeadlinesMissed++;
		totalMissedTime += dt;
		if (nbSubsequentDeadlinesMissed >= MAX_MISSED_DEADLINES) {
			adjustTimescale(totalMissedTime / nbSubsequentDeadlinesMissed);
			nbSubsequentDeadlinesMissed = 0;
			totalMissedTime = 0;
		}
	}

	private void deadlineMet(double dt) {
		nbSubsequentDeadlinesMissed = 0;
		totalMissedTime = 0;
	}

	private void adjustTimescale(double dt) {
		double newScale = 1.05 * dt / MAX_TIME_STEP; // 5% higher than
														// theoretically
														// necessary timescale
		getOptions().setTimescale(newScale);
		System.out
				.println(String
						.format("Warning: Your advanceTime code seems too slow to ensure dt <= %.3f with the current framerate.\n         In-game time will run slower than real time (1 in-game second = %.2f real-world seconds)",
								MAX_TIME_STEP, newScale));
	}

	public boolean isRunning() {
		return running;
	}

	protected void advanceTime(double dt) {
		getFacade().advanceTime(getAlien(), dt);
	}

	public double getElapsedTime() {
		return elapsedTime;
	}

	public void setVisibleScreenSize(int width, int height) {
		this.visibleScreenWidth = width;
		this.visibleScreenHeight = height;
	}

	public IActionHandler getActionHandler() {
		return handler;
	}

	public int getVisibleScreenWidth() {
		return visibleScreenWidth;
	}

	public int getVisibleScreenHeight() {
		return visibleScreenHeight;
	}

	private final AlienInfoProvider<Mazub> alienInfoProvider;

	protected AlienInfoProvider<Mazub> createAlienInfoProvider() {
		return new AlienInfoProvider<Mazub>() {
			
			@Override
			public Mazub getAlien() {
				return alien;
			}

			@Override
			public Optional<int[]> getAlienXYPixel() {
				return catchErrorGet(() -> getFacade().getPixelPosition(getAlien()));
			}
			
			@Override
			public Optional<double[]> getAlienXYPrecise() {
				return catchErrorGet(() -> getFacade().getActualPosition(getAlien()));
			}

			@Override
			public Optional<double[]> getAlienVelocity() {
				return catchErrorGet(() -> getFacade().getVelocity(getAlien()));
			}

			@Override
			public Optional<double[]> getAlienAcceleration() {
				return catchErrorGet(() -> getFacade().getAcceleration(
						getAlien()));
			}

			@Override
			public Optional<int[]> getAlienSize() {
				return getPlayerSprite().map(s -> new int[] { s.getWidth(), s.getHeight() });
			}

			@Override
			public Optional<Sprite> getPlayerSprite() {
				return catchErrorGet(() -> getFacade().getCurrentSprite(
						getAlien()));
			}
		};
	}

	private final WorldInfoProvider worldInfoProvider;

	protected WorldInfoProvider createWorldInfoProvider() {
		return new WorldInfoProvider() {

			@Override
			public Optional<int[]> getWorldSize() {
				return Optional.of(new int[] { WORLD_WIDTH, WORLD_HEIGHT });
			}
		};
	}

	public AlienInfoProvider<Mazub> getAlienInfoProvider() {
		return alienInfoProvider;
	}

	public WorldInfoProvider getWorldInfoProvider() {
		return worldInfoProvider;
	}

	@Override
	public void addCommand(Command command) {
		super.addCommand(command);
	}

	private final Consumer<ModelException> errorHandler = new Consumer<ModelException>() {

		@Override
		public void accept(ModelException error) {
			addMessage(new Message(MessageType.ERROR, error.getMessage()));
			error.printStackTrace();
		}
	};

	protected <T> Optional<T> catchErrorGet(Supplier<T> action) {
		try {
			return Optional.ofNullable(action.get());
		} catch (ModelException e) {
			errorHandler.accept(e);
			return Optional.empty();
		}
	}

	public <T> void catchErrorAction(Runnable action) {
		try {
			action.run();
		} catch (ModelException e) {
			errorHandler.accept(e);
		}
	}

}