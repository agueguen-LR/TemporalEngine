package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;

public interface SingleLevel extends Level{
	/**
	 * Returns the scene for the single level.
	 *
	 * @return The scene for the single level.
	 */
	public abstract Scene getScene();
}
