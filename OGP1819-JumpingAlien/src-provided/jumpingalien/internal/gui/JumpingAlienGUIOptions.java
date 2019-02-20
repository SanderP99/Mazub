package jumpingalien.internal.gui;

import ogp.framework.gui.GUIOptions;

public interface JumpingAlienGUIOptions extends GUIOptions {

	@Override
	public default boolean isFullScreenEnabled() {
		return false;
	}
	
	public abstract boolean getDebugShowHistory();
	public abstract void setDebugShowHistory(boolean value);

	public abstract boolean getDebugShowPixels();
	public abstract void setDebugShowPixels(boolean value);

	public abstract boolean getDebugShowObjectLocationAndSize();
	public abstract void setDebugShowObjectLocationAndSize(boolean value);

	public abstract boolean getDebugShowAxes();
	public abstract void setDebugShowAxes(boolean value);

	public abstract boolean getDebugShowInfo();
	public abstract void setDebugShowInfo(boolean value);

}