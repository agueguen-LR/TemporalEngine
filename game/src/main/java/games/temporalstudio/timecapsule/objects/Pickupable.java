package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Pickupable implements TimeObject{
	private final GameObject gameObject;

	public Pickupable(String name, float x, float y, Player player, InventoryObject pickedUpObject) {
		this.gameObject = new GameObject(name);
		Transform transform = new Transform(new Vector2f(x, y));
		Render render = new ColorRender(new Vector4f(0, 1, 0, 1));
		Collider2D collider = new Collider2D(new AABB(transform));
		collider.setOnIntersects((thisPickupable, other) -> {
			if (other instanceof GameObject playerObject && playerObject == player.getGameObject()) {
				player.addToInventory(pickedUpObject);
				this.gameObject.removeComponent(render);
				this.gameObject.removeComponent(collider);
				this.gameObject.removeComponent(transform);
			}
		});


		this.gameObject.addComponent(transform);
		this.gameObject.addComponent(collider);
		this.gameObject.addComponent(render);
	}

	@Override
	public GameObject getGameObject() {
		return gameObject;
	}
}
