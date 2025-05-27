package games.temporalstudio.temporalengine;

import java.io.Console;

import org.lwjgl.Version;

import games.temporalstudio.temporalengine.rendering.Renderer;
import games.temporalstudio.temporalengine.window.Window;

public abstract class Game extends App implements LifeCycleContext{

	private Window window;
	private Renderer renderer;
	// private PhysicsEngine physicsEngine;

	public Game(String title){
		this.window = new Window(this::update, title);
		this.renderer = new Renderer();
	}
	public Game(){
		this(null);
	}


	// SETTERS
	public void setTitle(String title){
		this.window.setTitle(title);
	}

	// FUNCTIONS
	@Override
	public void run(String[] args){
		getLogger().info("LWJGL version: %s.".formatted(Version.getVersion()));

		window.init(this);
		window.start(this);

		renderer.init(this);

		renderer.start(this);

		window.run(this);
	}
	@Override
	public void run(Console console, String[] args) {
		throw new RuntimeException();
	}

	public void update(float deltaTime){
		renderer.render(this);
	}
}
