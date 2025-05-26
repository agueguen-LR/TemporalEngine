package games.temporalstudio.timecapsule;

public interface LifeCycle {
	public void init();
	public void start();
	public void update(float delta);
	public void destroy();
}
