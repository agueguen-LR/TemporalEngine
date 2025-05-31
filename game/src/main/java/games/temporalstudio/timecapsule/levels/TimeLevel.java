package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;

public interface TimeLevel extends Level{
	/**
	 * Returns the scene for the past time period of the level.
	 * @return The scene for the past time period of the level.
	 */
	public abstract Scene getPastScene();
	/**
	 * Returns the scene for the future time period of the level.
	 * @return The scene for the future time period of the level.
	 */
	public abstract Scene getFuturScene();
}
