package games.temporalstudio.timecapsule;

import java.util.HashMap;
import java.util.Set;

public class Scene implements LifeCycle{
	private String name;

	private TimeCapsule timeCapsule = TimeCapsule.get();
	private Scene parent;
	private HashMap<String, Scene> children;

	private Set<GameObject> gameObjects;

	public Scene(String name) {
		this.name = name;
		this.children = new HashMap<>();
	}

	public String getName() {
		return name;
	}

	public Scene getParent() {
		return parent;
	}

	public void addChild(Scene child) {
		if (child != null) {
			child.parent = this;
			children.put(child.getName(), child);
		}
	}

	public Scene getChild(String name) {
		return children.get(name);
	}

	public GameObject addGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
		return gameObject;
	}

	public Set<GameObject> getGameObjects() {
		return gameObjects;
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
