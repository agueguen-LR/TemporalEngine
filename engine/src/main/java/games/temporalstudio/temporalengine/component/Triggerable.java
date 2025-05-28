package games.temporalstudio.temporalengine.component;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;

import java.util.function.Consumer;

public class Triggerable implements Component{

	private boolean triggered = false;
	private Consumer<LifeCycleContext> action;

	public Triggerable(Consumer<LifeCycleContext> action) {
		if (action == null) {
			Game.LOGGER.severe("Triggerable action cannot be null.");
		}
		this.action = action;
	}

	public void trigger(LifeCycleContext context) {
		if (!(context instanceof GameObject object)){
			Game.LOGGER.warning("Trigger can only be used with GameObject context.");
			return;
		}
		if (!triggered){
			this.triggered = true;
		} else {
			Game.LOGGER.warning("Triggerable already triggered, ignoring.");
		}
	}

	@Override
	public void update(LifeCycleContext context, float delta) {
		if (triggered){
			if (!(context instanceof GameObject object)){
				Game.LOGGER.severe("Triggerable can only be used with GameObject context.");
				return;
			}
			Game.LOGGER.info("Triggerable " + object.getName() + " has been triggered, performing action.");
			action.accept(context);
			triggered = false;
		}
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
