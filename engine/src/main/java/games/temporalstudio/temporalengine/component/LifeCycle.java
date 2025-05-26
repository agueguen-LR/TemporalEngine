package games.temporalstudio.temporalengine.component;

public interface LifeCycle{

	// FUNCTIONS
	public abstract void init(LifeCycleContext context);
	public abstract void start(LifeCycleContext context);
	public abstract void update(LifeCycleContext context, float delta);
	public abstract void destroy(LifeCycleContext context);
}
