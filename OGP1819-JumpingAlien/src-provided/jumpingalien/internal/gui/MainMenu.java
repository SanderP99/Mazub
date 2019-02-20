package jumpingalien.internal.gui;

import jumpingalien.internal.game.JumpingAlienGame;
import jumpingalien.internal.gui.AlienScreenPanel;
import ogp.framework.gui.Screen;
import ogp.framework.gui.menu.MenuOption;
import ogp.framework.gui.menu.MenuScreen;

public class MainMenu
		extends MenuScreen<JumpingAlienGame, JumpingAlienGUI> {

	protected MainMenu(AlienScreenPanel panel, JumpingAlienGUI gui,
			Screen<JumpingAlienGame, JumpingAlienGUI> previous) {
		super(panel, gui, previous);
	}

	@Override
	protected AlienScreenPanel getPanel() {
		return (AlienScreenPanel) super.getPanel();
	}

	@Override
	protected void registerMenuOptions() {
		addOption(new MenuOption("Start game", this::startGame));

		addOption(new MenuOption("Set debug options", this::setDebugOptions));

		addOption(new MenuOption("Quit (Esc)", this::quit));
	}

	protected void startGame() {
		getPanel().switchToScreen(
				new AlienGameScreen(getPanel(), getGUI(), this));
	}

	protected void setDebugOptions() {
		getPanel().switchToScreen(
				new DebugMenu(getPanel(), getGUI(), this));
	}

	protected void quit() {
		getGUI().exit();
	}
}
