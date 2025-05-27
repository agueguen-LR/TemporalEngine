package games.temporalstudio.temporalengine.component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import games.temporalstudio.temporalengine.LifeCycleContext;

public class GameObject implements UpdateLifeCycle, LifeCycleContext{

	private String name;
	private Set<Component> components = new HashSet<>();

	public GameObject(String name){
		this.name = name;
	}

	// GETTERS
	public String getName(){ return name; }
	public boolean hasComponent(Class<? extends Component> componentClass){
		return components.stream()
			.anyMatch(c -> componentClass.isAssignableFrom(c.getClass()));
	}
	public <T extends Component> Collection<T> getComponents(
		Class<T> componentClass
	){
		return components.stream()
			.filter(c -> componentClass.isAssignableFrom(c.getClass()))
			.map(c -> componentClass.cast(c))
			.toList();
	}
	public <T extends Component> T getComponent(Class<T> componentClass){
		return getComponents(componentClass).stream()
			.findFirst().orElseThrow();
	}

	// SETTERS
	public boolean addComponent(Component component){
		if(component == null)
			throw new IllegalArgumentException();

		return components.add(component);
	}
	public <T extends Component> Collection<T> removeAllComponents(
		Class<T> componentClass
	){
		Collection<T> comps = getComponents(componentClass);
		components.removeAll(comps);
		return comps;
	}
	public boolean removeComponent(Component component){
		return components.remove(component);
	}

	// FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		components.forEach(c -> c.init(this));
	}
	@Override
	public void start(LifeCycleContext context){
		components.forEach(c -> c.start(this));
	}
	@Override
	public void update(LifeCycleContext context, float delta){
		components.forEach(c -> c.update(this, delta));
	}
	@Override
	public void destroy(LifeCycleContext context){
		components.forEach(c -> c.destroy(this));
	}
}
