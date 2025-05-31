package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;

public interface Level {
	public Scene getPastScene();
	public Scene getFuturScene();
}
