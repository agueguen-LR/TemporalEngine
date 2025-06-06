package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.LifeCycle;
import games.temporalstudio.temporalengine.LifeCycleContext;

public interface PhysicsEngineLifeCycle extends LifeCycle {
	public abstract void compute(LifeCycleContext context, float deltaTime);
}
