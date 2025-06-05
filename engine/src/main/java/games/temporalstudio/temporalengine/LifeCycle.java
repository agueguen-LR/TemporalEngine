package games.temporalstudio.temporalengine;

public interface LifeCycle{

	// FUNCTIONS
	public abstract void init(LifeCycleContext context);
	public abstract void start(LifeCycleContext context);
	public abstract void destroy(LifeCycleContext context);
}
