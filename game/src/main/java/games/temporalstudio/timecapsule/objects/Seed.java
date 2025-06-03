package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Triggerable;

public class Seed implements InventoryObject {
	private final GameObject gameObject;
	private final Triggerable triggerable;

	public Seed(String name, Player player) {
		this.gameObject = new GameObject(name);
		Triggerable triggerable = new Triggerable((thisSeed) -> {
			Game.LOGGER.info("Seed triggerable called with context: " + thisSeed);
			if (!(thisSeed instanceof GameObject seed)) {
				Game.LOGGER.severe("Seed triggerable called without Seed gameObject context.");
				return;
			}
			Game.LOGGER.info("Planting seed: " + seed.getName());
			player.removeFromInventory(this);
		});

		this.gameObject.addComponent(triggerable);
		this.triggerable = triggerable;
	}

	@Override
	public GameObject getGameObject() {
		return gameObject;
	}

	/**
	 * Returns the Triggerable component associated with this ThrowableBottle.
	 *
	 * @return The Triggerable component that defines the behavior when the bottle is thrown.
	 */
	@Override
	public Triggerable getTriggerable() {
		return triggerable;
	}
}
