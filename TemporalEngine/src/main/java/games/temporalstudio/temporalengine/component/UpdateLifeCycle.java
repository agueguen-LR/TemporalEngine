package games.temporalstudio.temporalengine.component;

import games.temporalstudio.temporalengine.LifeCycle;
import games.temporalstudio.temporalengine.LifeCycleContext;

public interface UpdateLifeCycle extends LifeCycle{

	// FUNCTIONS
	public abstract void update(LifeCycleContext context, float delta);
}
