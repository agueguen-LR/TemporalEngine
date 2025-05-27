package games.temporalstudio.temporalengine.component;

import java.util.HashMap;

import games.temporalstudio.temporalengine.LifeCycleContext;

public class GameObject implements UpdateLifeCycle, LifeCycleContext{

	private String name;
	private HashMap<Class<? extends Component>, Component> components;

	public GameObject(String name){
		this.name = name;
		this.components = new HashMap<>();
	}

	// GETTERS
	public String getName(){ return name; }
	public boolean hasComponent(Class<? extends Component> componentClass){
		return components.containsKey(componentClass);
	}
	public Component getComponent(Class<? extends Component> componentClass){
		return components.get(componentClass);
	}

	// SETTERS
	public void addComponent(Component component){
		if(component == null)
			throw new IllegalArgumentException();
		else if(hasComponent(component.getClass()))
			throw new IllegalArgumentException(
				"Component of same type already present;"
			);

		components.put(component.getClass(), component);
	}
	public Component removeComponent(Component component){
		if(component == null)
			throw new IllegalArgumentException();
		else if(!hasComponent(component.getClass()))
			throw new IllegalArgumentException(
				"No such Component of same type;"
			);

		return components.remove(component.getClass());
	}

	// FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		components.forEach((k, c) -> c.init(this));
	}
	@Override
	public void start(LifeCycleContext context){
		components.forEach((k, c) -> c.start(this));
	}
	@Override
	public void update(LifeCycleContext context, float delta){
		components.forEach((k, c) -> c.update(this, delta));
	}
	@Override
	public void destroy(LifeCycleContext context){
		components.forEach((k, c) -> c.destroy(this));
	}
}
