package games.temporalstudio.temporalengine;

public abstract class Game extends App{
	private Window window;
	private Renderer renderer;
	private PhysicsEngine physicsEngine;

	public Game(String title) {
		this.window = new Window(this::update, title);
	}

	@Override
	public void run(String[] args) {
		window.run();
	}

	public void update(float deltaTime) {
		System.out.println(1/deltaTime + "FPS");
	}
}
