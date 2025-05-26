package games.temporalstudio.temporalengine;

import java.io.Console;

import games.temporalstudio.temporalengine.component.LifeCycleContext;

public abstract class Game extends App implements LifeCycleContext{

	private static final String DEFAULT_WINDOW_TITLE = "Temporal Engine Game";

	private Window window;
	private Renderer renderer;
	private PhysicsEngine physicsEngine;

	public Game(String title){
		this.window = new Window(this::update, title);
	}
	public Game(){
		this(DEFAULT_WINDOW_TITLE);
	}

	// SETTERS
	public void setTitle(String title){
		this.window.setTitle(title);
	}

	// FUNCTIONS
	@Override
	public void run(String[] args){
		window.run();
	}
	@Override
	public void run(Console console, String[] args) {
		throw new RuntimeException();
	}

	public void update(float deltaTime){
		System.out.println(1/deltaTime + "FPS");
	}
}
