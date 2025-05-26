package games.temporalstudio.temporalengine;

public abstract class Game extends App{
	private Window window;
	private Renderer renderer;
	private PhysicsEngine physicsEngine;

	@Override
	public void run(String[] args) {
		System.out.println(args[0]);
	}
}
