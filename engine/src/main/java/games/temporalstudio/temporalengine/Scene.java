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

	private Set<GameObject> gameObjects = new HashSet<>();

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
		return Collections.unmodifiableSet(gameObjects);
	}
	public Set<GameObject> getGOsByComponent(
		Class<? extends Component> componentClass
	){
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

	public GameObject addGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
		return gameObject;
	}

	// LIFECYLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		if(!(context instanceof Game game)) return;
		game.getLogger().info("Initializing scene: " + name);

		gameObjects.forEach(o -> o.init(this));
	}
	@Override
	public void start(LifeCycleContext context){
		if(!(context instanceof Game game)) return;
		game.getLogger().info("Starting scene: " + name);

		gameObjects.forEach(o -> o.start(this));
	}
	@Override
	public void update(LifeCycleContext context, float delta){
		gameObjects.forEach(o -> o.update(this, delta));
	}
	@Override
	public void destroy(LifeCycleContext context){
		gameObjects.forEach(o -> o.destroy(this));
	}
}
