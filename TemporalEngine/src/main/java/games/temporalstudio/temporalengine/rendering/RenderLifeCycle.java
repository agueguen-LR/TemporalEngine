package games.temporalstudio.temporalengine.rendering;

import games.temporalstudio.temporalengine.LifeCycle;
import games.temporalstudio.temporalengine.LifeCycleContext;

public interface RenderLifeCycle extends LifeCycle{

	// FUNCTIONS
	public abstract void render(LifeCycleContext context);
}
