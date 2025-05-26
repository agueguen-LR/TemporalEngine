package games.temporalstudio.timecapsule;

public class GameObject implements LifeCycle {
	private String name;
	private Scene currentScene;

	private Component[] components;

	public GameObject(String name, Scene currentScene) {
		this.name = name;
		this.currentScene = currentScene;
		this.components = new Component[0]; // Initialize with an empty array
	}


	@Override
	public void init() {

	}

	@Override
	public void start() {

	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void destroy() {

	}
}
