package games.temporalstudio.temporalengine.component;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.listeners.KeyListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Input implements Component{

	private Map<Integer, Consumer<LifeCycleContext>> controls;

	public Input() {
		controls = new HashMap<>();
	}

	public void addControl(int key, Consumer<LifeCycleContext> action) {
		controls.put(key, action);
	}

	public boolean isControlPressed(Integer key){
		if (controls.containsKey(key)){
			return KeyListener.isKeyPressed(key);
		}
		Game.LOGGER.warning("Control doesn't exist: " + key);
		return false;
	}

	@Override
	public void update(LifeCycleContext context, float delta) {
		controls.forEach((key, action) -> {
			if (KeyListener.isKeyPressed(key)) {
				action.accept(context);
			}
		});
	}

	@Override
	public void init(LifeCycleContext context) {
	}

	@Override
	public void start(LifeCycleContext context) {

	}

	@Override
	public void destroy(LifeCycleContext context) {

	}
}
