package games.temporalstudio.timecapsule;

import java.util.HashMap;

public class GameObject implements LifeCycle {
	private String name;
	private Scene currentScene;

	private HashMap<Class<? extends Component>, Component> components;

	public GameObject(String name, Scene currentScene) {
		this.name = name;
		this.currentScene = currentScene;
		this.components = new HashMap<>(); // Initialize with an empty array
	}

	public void addComponent(Component component) {
		if (component != null) {
			components.put(component.getClass(), component);
		}
	}

	public <T extends Component> T getComponent(Class<T> componentClass) {
		return componentClass.cast(components.get(componentClass));
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
