package games.temporalstudio.temporalengine.component;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;

import java.util.function.Consumer;

/**
 * Represents a component that can be triggered to perform a specific action.
 * Used to define custom behaviors that are executed when triggered by a {@link Trigger} or other game logic.
 *
 * @author agueguen-LR
 */
public class Triggerable implements Component {

	/** Indicates whether this Triggerable has been triggered. */
	private boolean triggered = false;
	private float cooldown;
	private float workingCooldown;
	private boolean isOnCooldown;

	/** The action to perform when this Triggerable is triggered. */
	private Consumer<LifeCycleContext> action;

	/**
	 * Constructs a Triggerable with the specified action.
	 *
	 * @param action The action to perform when triggered. Must not be null.
	 */
	public Triggerable(Consumer<LifeCycleContext> action) {
		if (action == null) {
			Game.LOGGER.severe("Triggerable action cannot be null.");
		}
		this.action = action;
	}

	/**
	 * Constructs a Triggerable with the specified action.
	 *
	 * @param action The action to perform when triggered. Must not be null.
	 */
	public Triggerable(Consumer<LifeCycleContext> action, float cooldown) {
		if (action == null) {
			Game.LOGGER.severe("Triggerable action cannot be null.");
		}
		this.action = action;
		this.cooldown = cooldown;
	}

	/**
	 * Triggers this component, marking it as triggered if not already triggered.
	 * The action will be performed on the next update.
	 *
	 * @param context The context in which the trigger occurs (should be a GameObject).
	 */
	public void trigger(LifeCycleContext context) {
		if (!(context instanceof GameObject object)) {
			Game.LOGGER.warning("Trigger can only be used with GameObject context.");
			return;
		}
		if (!triggered) {
			this.triggered = true;
		}
	}

	/**
	 * Updates the Triggerable, performing the action if it has been triggered.
	 *
	 * @param context The context in which the update occurs.
	 * @param delta   The time delta since the last update.
	 */
	@Override
	public void update(LifeCycleContext context, float delta) {
		if (isOnCooldown) {
			workingCooldown -= delta;
			if (workingCooldown <= 0) {
				isOnCooldown = false;
				this.triggered = false; // Reset triggered state after cooldown
			}
		} else if (triggered) {
			if (!(context instanceof GameObject object)) {
				Game.LOGGER.severe("Triggerable can only be used with GameObject context.");
				return;
			}
			workingCooldown = cooldown; // Reset cooldown timer
			this.isOnCooldown = true;
			action.accept(context);
		}
	}

	/**
	 * Initializes the component. No-op by default.
	 *
	 * @param context The life cycle context.
	 */
	@Override
	public void init(LifeCycleContext context) {
	}

	/**
	 * Starts the component. No-op by default.
	 *
	 * @param context The life cycle context.
	 */
	@Override
	public void start(LifeCycleContext context) {
	}

	/**
	 * Destroys the component. No-op by default.
	 *
	 * @param context The life cycle context.
	 */
	@Override
	public void destroy(LifeCycleContext context) {
	}
}