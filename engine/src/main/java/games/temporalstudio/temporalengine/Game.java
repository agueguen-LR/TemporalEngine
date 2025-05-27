package games.temporalstudio.temporalengine;

import static org.lwjgl.glfw.GLFW.*;

import java.io.Console;

import games.temporalstudio.temporalengine.listeners.KeyListener;
import games.temporalstudio.temporalengine.rendering.Renderer;
import games.temporalstudio.temporalengine.window.Window;

import org.lwjgl.Version;

public abstract class Game extends App implements LifeCycleContext{

	private Window window;
	private Renderer renderer;
	// private PhysicsEngine physicsEngine;

	private Scene mainMenu;
	private Scene leftScene;
	private Scene rightScene;

	private boolean paused = true;
	private boolean transitioning = false;
	private float transitionTime = 0.5f; // Time in seconds for transitions

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
	public void run(Console console, String[] args){
		throw new RuntimeException();
	}

	public void update(float deltaTime){
		if (deltaTime >= 0){
			if(transitioning){
				transitionTime -= deltaTime;
				if (transitionTime <= 0) {
					transitioning = false;
					transitionTime = 0.5f; // Reset transition time
				}
			}else if(paused){
				mainMenu.update(this, deltaTime);
				if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
					paused = false;
					transitioning = true;
					this.getLogger().info("Transitioning to game");
				}
			}else{
				if (leftScene != null) {
					leftScene.update(this, deltaTime);
				}
				if (rightScene != null) {
					rightScene.update(this, deltaTime);
				}
				if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
					paused = true;
					transitioning = true;
					this.getLogger().info("Transitioning to main menu");
				}
			}
		}

		renderer.render(this);
	}

	public void changeLeftScene(String name){
		if (this.leftScene == null) {
			throw new RuntimeException("Current left scene is null. Cannot change to child: " + name);
		}
		Scene newScene = this.leftScene.getChild(name);
		Scene parentScene = this.leftScene.getParent();
		if (newScene == null && !parentScene.getName().equals(name)) {
			this.getLogger().warning("Left scene not found: " + name);
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
			this.getLogger().warning("Right scene not found: " + name);
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
