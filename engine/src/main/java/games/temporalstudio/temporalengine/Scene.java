package games.temporalstudio.temporalengine;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import games.temporalstudio.temporalengine.component.Component;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.UpdateLifeCycle;

public class Scene implements UpdateLifeCycle, LifeCycleContext{

	private String name;
	private Scene parent;
	private Map<String, Scene> children = new HashMap<>();

	private Set<GameObject> gameObjects;
	private Set<GameObject> gameObjectsToAdd = new HashSet<>();
	private Set<GameObject> gameObjectsToRemove = new HashSet<>();

	public Scene(String name){
		this.name = name;
	}

	// GETTERS
	public String getName(){ return name; }
	public Scene getParent(){ return parent; }
	public Scene getChild(String name){
		return children.get(name);
	}

	public Set<GameObject> getGameObjects(){
		if(gameObjects == null){
			return Collections.unmodifiableSet(gameObjectsToAdd);
		}
		return Collections.unmodifiableSet(gameObjects);
	}
	public Set<GameObject> getGOsByComponent(
		Class<? extends Component> componentClass
	){
		if (gameObjects == null) {
			return new HashSet<>(gameObjectsToAdd.stream()
				.filter(go -> go.hasComponent(componentClass))
				.toList()
			);
		}
		return new HashSet<>(getGameObjects().stream()
			.filter(go -> go.hasComponent(componentClass))
			.toList()
		);
	}

	// SETTERS
	public void addChild(Scene child){
		if(child != null){
			child.parent = this;
			children.put(child.getName(), child);
		}
	}

	public boolean addGameObject(GameObject gameObject) {
		return gameObjectsToAdd.add(gameObject);
	}

	public boolean removeGameObject(GameObject gameObject) {
		return gameObjectsToRemove.add(gameObject);
	}

	// LIFECYLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		if(!(context instanceof Game game)) return;
		game.getLogger().info("Initializing scene: " + name);

		gameObjectsToAdd.forEach(o -> o.init(this));
	}
	@Override
	public void start(LifeCycleContext context){
		if(!(context instanceof Game game)) return;
		game.getLogger().info("Starting scene: " + name);

		gameObjectsToAdd.forEach(o -> o.start(this));
		gameObjects = new HashSet<>(gameObjectsToAdd);
		gameObjectsToAdd.clear();
	}
	@Override
	public void update(LifeCycleContext context, float delta){
		gameObjects.forEach(o -> o.update(this, delta));
		gameObjects.removeAll(gameObjectsToRemove);
		gameObjectsToAdd.forEach(o -> {
			o.init(this);
			o.start(this);
		});
		gameObjects.addAll(gameObjectsToAdd);
		gameObjectsToAdd.clear();
		gameObjectsToRemove.clear();
	}
	@Override
	public void destroy(LifeCycleContext context){
		gameObjects.forEach(o -> o.destroy(this));
	}
}
