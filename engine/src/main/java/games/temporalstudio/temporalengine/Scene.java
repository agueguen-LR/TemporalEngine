package games.temporalstudio.temporalengine;

import java.util.HashMap;
import java.util.Set;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.UpdateLifeCycle;

public class Scene implements UpdateLifeCycle, LifeCycleContext{

	private String name;

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
	public void init(LifeCycleContext context){
		if(!(context instanceof Game game)) return;
		game.getLogger().info("Initializing scene: " + name);
	}

	@Override
	public void start(LifeCycleContext context){
		if(!(context instanceof Game game)) return;
		game.getLogger().info("Starting scene: " + name);
	}

	@Override
	public void update(LifeCycleContext context, float delta){}

	@Override
	public void destroy(LifeCycleContext context){}
}
