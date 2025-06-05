package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Trigger;
import games.temporalstudio.temporalengine.component.Triggerable;
import games.temporalstudio.timecapsule.Entity.Player;

/**
 * Represents a sender object that can send a Pickupable item to a CapsuleReceiver.
 * When triggered, it activates the receiver, transfers the item, and removes itself from the player's inventory.
 *
 * @author agueguen-LR
 */
public class CapsuleSender implements InventoryObject {
	/** The underlying GameObject representing this sender in the scene. */
	private final GameObject gameObject;

	/** The Triggerable component that defines the sender's behavior when triggered. */
	private final Triggerable triggerable;

	/**
	 * Constructs a CapsuleSender that sends a Pickupable item to a specified receiver.
	 *
	 * @param name        The name of the sender GameObject.
	 * @param player      The player who owns this sender.
	 * @param sentObject  The Pickupable item to send.
	 * @param receiver    The CapsuleReceiver that will receive the item.
	 */
	public CapsuleSender(String name, Player player, Pickupable sentObject, CapsuleReceiver receiver) {
		this.gameObject = new GameObject(name);

		// Define the triggerable behavior: activate the receiver, remove from inventory, and clean up.
		Triggerable triggerable = new Triggerable((thisItem) -> {
			if (!(thisItem instanceof GameObject sender)) {
				Game.LOGGER.severe("CapsuleSender triggerable called without CapsuleSender gameObject context.");
				return;
			}
			// Trigger the receiver's triggerable, passing the sender GameObject as context
			receiver.getGameObject().getComponent(Triggerable.class).trigger(sender);
			// Remove this sender from the player's inventory
			player.removeFromInventory(this);
			// Remove the triggerable component from the sender GameObject
			sender.removeComponent(sender.getComponent(Triggerable.class));
		});

		// Set the item to be received by the receiver
		receiver.setReceivedItem(sentObject);

		// Add the triggerable component to the sender GameObject
		this.gameObject.addComponent(triggerable);
		this.triggerable = triggerable;
	}

	/**
	 * Returns the underlying GameObject for this sender.
	 *
	 * @return The GameObject instance.
	 */
	@Override
	public GameObject getGameObject() {
		return gameObject;
	}

	/**
	 * Returns the Triggerable component associated with this sender.
	 *
	 * @return The Triggerable component that defines the behavior when triggered.
	 */
	@Override
	public Triggerable getTriggerable() {
		return triggerable;
	}
}