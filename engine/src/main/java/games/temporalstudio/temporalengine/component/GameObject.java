package games.temporalstudio.temporalengine.component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import games.temporalstudio.temporalengine.LifeCycleContext;

public class GameObject implements UpdateLifeCycle, LifeCycleContext{

	private String name;
	private Set<Component> components = new HashSet<>();
	private Set<Component> componentsToRemove = new HashSet<>();
	private Set<Component> componentsToAdd = new HashSet<>();

	public GameObject(String name){
		this.name = name;
	}

	// GETTERS
	public String getName(){ return name; }
	public boolean hasComponent(Class<? extends Component> componentClass){
		if (components == null){
			return componentsToAdd.stream()
					.anyMatch(c -> componentClass.isAssignableFrom(c.getClass()));
		}
		return components.stream()
			.anyMatch(c -> componentClass.isAssignableFrom(c.getClass()));
	}

	public <T extends Component> Collection<T> getComponents(
		Class<T> componentClass
	){
		if (components == null){
			return componentsToAdd.stream()
				.filter(c -> componentClass.isAssignableFrom(c.getClass()))
				.map(c -> componentClass.cast(c))
				.toList();
		}
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
		return componentsToAdd.add(component);
	}
	public <T extends Component> Collection<T> removeAllComponents(
		Class<T> componentClass
	){
		Collection<T> comps = getComponents(componentClass);
		componentsToRemove.addAll(comps);
		return comps;
	}
	public boolean removeComponent(Component component){
		return componentsToRemove.add(component);
	}

	// FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		if (componentsToAdd != null){
			componentsToAdd.forEach(c -> c.init(this));
		}
	}
	@Override
	public void start(LifeCycleContext context){
		if (componentsToAdd != null){
		componentsToAdd.forEach(c -> c.start(this));
		components.addAll(componentsToAdd);
		componentsToAdd.clear();
		}
	}

	@Override
	public void update(LifeCycleContext context, float delta){
		if (components != null) {
			components.forEach(c -> c.update(this, delta));
			components.removeAll(componentsToRemove);
			componentsToAdd.forEach(c -> {
				c.init(this);
				c.start(this);
			});
			components.addAll(componentsToAdd);
			componentsToAdd.clear();
			componentsToRemove.clear();
		}

	}
	@Override
	public void destroy(LifeCycleContext context){
		if (components != null) {
			components.forEach(c -> c.destroy(this));
		}
	}
}
