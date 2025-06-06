package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Triggerable;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.PhysicsBody;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.temporalengine.rendering.component.TileRender;
import games.temporalstudio.timecapsule.Entity.Player;
import org.joml.Vector2f;

public class ThrowableBottle implements InventoryObject {
	private final GameObject gameObject;
	private final Triggerable triggerable;

	public ThrowableBottle(String name, Player player) {
		this.gameObject = new GameObject(name);
		Triggerable triggerable = new Triggerable((thisBottle) -> {
			if (!(thisBottle instanceof GameObject bottleObject)) {
				Game.LOGGER.severe("ThrowableBottle triggerable called without ThrowableBottle gameObject context.");
				return;
			}
			Game.LOGGER.info("Throwing bottle: " + bottleObject.getName());
			player.removeFromInventory(this);
			Transform transform = new Transform(new Vector2f(player.getGameObject().getComponent(Transform.class).getPosition()));
			Render render = new TileRender("future", "bottle");
			Collider2D collider = new Collider2D(new AABB(transform));
			PhysicsBody physicsBody = new PhysicsBody(1, 50, .1f, 2f);
			bottleObject.addComponent(transform);
			bottleObject.addComponent(collider);
			bottleObject.addComponent(physicsBody);
			bottleObject.addComponent(render);
			physicsBody.applyForce(1000, 0); // Throw to the right
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
