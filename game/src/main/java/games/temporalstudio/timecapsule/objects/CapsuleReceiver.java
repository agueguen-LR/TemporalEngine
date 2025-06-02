package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Triggerable;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 * Represents a receiver object in the game that can accept a Pickupable item sent via a capsule.
 * When triggered, it removes itself from the scene and adds the received item to the scene.
 *
 * @author agueguen-LR
 */
public class CapsuleReceiver implements TimeObject {
	/** The underlying GameObject representing this receiver in the scene. */
	private final GameObject gameObject;

	/** The item to be received and spawned when the receiver is triggered. */
	private Pickupable receivedItem;

	/** The scene this receiver belongs to. */
	private Scene scene;

	/**
	 * Constructs a CapsuleReceiver at the specified position.
	 *
	 * @param name The name of the receiver GameObject.
	 * @param x    The x-coordinate of the receiver's position.
	 * @param y    The y-coordinate of the receiver's position.
	 */
	public CapsuleReceiver(String name, float x, float y) {
		this.gameObject = new GameObject(name);
		Transform transform = new Transform(new Vector2f(x, y));
		Render render = new ColorRender(new Vector4f(0.8f, 0.2f, 0.2f, 1));

		// Triggerable defines what happens when the receiver is triggered (e.g., by a CapsuleSender)
		Triggerable triggerable = new Triggerable((context) -> {
			if (!(context instanceof GameObject other)) {
				Game.LOGGER.severe("CapsuleReceiver triggerable called with non-GameObject context: " + context);
				return;
			}
			if (this.scene == null) {
				Game.LOGGER.severe("CapsuleReceiver scene is not set.");
				return;
			}
			// Remove the receiver's render component (visual feedback)
			this.gameObject.removeComponent(render);
			// Add the received item to the scene
			this.scene.addGameObject(receivedItem.getGameObject());
			// Remove the triggerable from the sender
			this.gameObject.removeComponent(other.getComponent(Triggerable.class));
			// Remove the transform and the receiver itself from the scene
			this.gameObject.removeComponent(transform);
			this.scene.removeGameObject(this.gameObject);
		});

		this.gameObject.addComponent(transform);
		this.gameObject.addComponent(render);
		this.gameObject.addComponent(triggerable);
	}

	/**
	 * Sets the item that will be received and spawned when this receiver is triggered.
	 *
	 * @param item The Pickupable item to receive.
	 */
	public void setReceivedItem(Pickupable item) {
		this.receivedItem = item;
	}

	/**
	 * Returns the underlying GameObject for this receiver.
	 *
	 * @return The GameObject instance.
	 */
	@Override
	public GameObject getGameObject() {
		return gameObject;
	}

	/**
	 * Sets the scene this receiver belongs to.
	 *
	 * @param scene The Scene instance.
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}
}