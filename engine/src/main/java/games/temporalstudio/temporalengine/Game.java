package games.temporalstudio.temporalengine;

import java.io.Console;

import games.temporalstudio.temporalengine.component.LifeCycleContext;
import games.temporalstudio.temporalengine.listeners.KeyListener;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Game extends App implements LifeCycleContext{

	private static final String DEFAULT_WINDOW_TITLE = "Temporal Engine Game";

	private Window window;
	private Renderer renderer;
	private PhysicsEngine physicsEngine;

	private Scene mainMenu;
	private Scene leftScene;
	private Scene rightScene;

	private boolean paused = true;
	private boolean transitioning = false;
	private float transitionTime = 0.5f; // Time in seconds for transitions

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
//		System.out.println(1/deltaTime + "FPS");

		if (deltaTime >= 0){
			if (transitioning) {
				transitionTime -= deltaTime;
				if (transitionTime <= 0) {
					transitioning = false;
					transitionTime = 0.5f; // Reset transition time
				}
				return;
			}
			if (paused) {
				mainMenu.update(this, deltaTime);
				if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
					paused = false;
					transitioning = true;
					System.out.println("Transitioning to game");
				}
			}
			else {
				if (leftScene != null) {
					leftScene.update(this, deltaTime);
				}
				if (rightScene != null) {
					rightScene.update(this, deltaTime);
				}
				if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
					paused = true;
					transitioning = true;
					System.out.println("Transitioning to main menu");
				}
			}
		}
	}

	public void changeLeftScene(String name){
		if (this.leftScene == null) {
			throw new RuntimeException("Current left scene is null. Cannot change to child: " + name);
		}
		Scene newScene = this.leftScene.getChild(name);
		Scene parentScene = this.leftScene.getParent();
		if (newScene == null && !parentScene.getName().equals(name)) {
			System.out.println("Left scene not found: " + name);
			return;
		}
		if (newScene != null) {
			this.leftScene = newScene;
		} else {
			this.leftScene = parentScene;
		}
		this.leftScene.init(this);
		this.leftScene.start(this);
	}

	public void changeRightScene(String name){
		if (this.rightScene == null) {
			throw new RuntimeException("Current right scene is null. Cannot change to child: " + name);
		}
		Scene newScene = this.rightScene.getChild(name);
		Scene parentScene = this.rightScene.getParent();
		if (newScene == null && !parentScene.getName().equals(name)) {
			System.out.println("Right scene not found: " + name);
			return;
		}
		if (newScene != null) {
			this.rightScene = newScene;
		} else {
			this.rightScene = parentScene;
		}
		this.rightScene.init(this);
		this.rightScene.start(this);
	}

	protected void setMainMenu(Scene mainMenu) {
		if (this.mainMenu != null) {
			throw new RuntimeException("Main menu already set. Cannot set to: " + mainMenu.getName());
		}
		this.mainMenu = mainMenu;
		this.mainMenu.init(this);
		this.mainMenu.start(this);
	}

	protected void setFirstLeftScene(Scene leftScene) {
		if (this.leftScene != null) {
			throw new RuntimeException("Left scene already set. Cannot set to: " + leftScene.getName());
		}
		this.leftScene = leftScene;
		this.leftScene.init(this);
		this.leftScene.start(this);
	}

	protected void setFirstRightScene(Scene rightScene) {
		if (this.rightScene != null) {
			throw new RuntimeException("Right scene already set. Cannot set to: " + rightScene.getName());
		}
		this.rightScene = rightScene;
		this.rightScene.init(this);
		this.rightScene.start(this);
	}
}
