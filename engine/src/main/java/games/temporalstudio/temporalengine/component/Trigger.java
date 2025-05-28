package games.temporalstudio.temporalengine.component;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Game;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class Trigger implements Component {

	private boolean triggered = false;
	private boolean coolingDown = false;
	private float cooldown;

	private Supplier<Boolean> triggerCondition;

	private Set<Triggerable> triggerables;

	public Trigger(float cooldown) {
		this.cooldown = cooldown;
		this.triggerables = new HashSet<>();
		this.triggerCondition = () -> false;
	}

	/**
	 * Creates a Trigger with a specified cooldown.
	 * @param cooldown the cooldown time in seconds before the trigger can be activated again
	 */
	public Trigger(float cooldown, Supplier<Boolean> triggerCondition) {
		this.cooldown = cooldown;
		this.triggerables = new HashSet<>();
		if (triggerCondition == null) {
			Game.LOGGER.severe("Trigger condition is null.");
		}
		this.triggerCondition = triggerCondition;
	}

	public void addTriggerable(Triggerable triggerable) {
		if (triggerable != null) {
			triggerables.add(triggerable);
		}
	}

	public void removeTriggerable(Triggerable triggerable) {
		if (triggerable != null) {
			triggerables.remove(triggerable);
		}
	}

	public void trigger(LifeCycleContext context) {
		if (!(context instanceof GameObject object)){
			Game.LOGGER.warning("Trigger can only be used with GameObject context.");
			return;
		}
		Game.LOGGER.info("GameObject " + object.getName() + " triggered!");
		for (Triggerable triggerable : triggerables) {
			triggerable.trigger(context);
		}
	}

	public void setTriggerCondition(Supplier<Boolean> triggerCondition) {
		if (triggerCondition == null) {
			Game.LOGGER.severe("Trigger condition cannot be set to null.");
			return;
		}
		this.triggerCondition = triggerCondition;
	}

	@Override
	public void update(LifeCycleContext context, float delta) {
		if (coolingDown){
			if (cooldown < 0){
				coolingDown = false;
			}
			cooldown -= delta;
		} if (triggered) {
			trigger(context);
			triggered = false;
			coolingDown = true;
		} else if (triggerCondition.get()) {
			triggered = true;
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
