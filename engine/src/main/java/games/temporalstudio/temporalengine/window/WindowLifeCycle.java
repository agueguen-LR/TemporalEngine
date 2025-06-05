package games.temporalstudio.temporalengine.window;

import games.temporalstudio.temporalengine.LifeCycle;
import games.temporalstudio.temporalengine.LifeCycleContext;

public interface WindowLifeCycle extends LifeCycle{

	// FUNCTIONS
	public abstract void run(LifeCycleContext context);
}
