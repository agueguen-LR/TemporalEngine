package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.Layer;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.timecapsule.TimeCapsule;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Wall implements TimeObject{
	private final GameObject gameObject;

	/**
	 * Constructs a Wall object with the specified minimum and maximum positions.
	 * Will have a grey color render if debug mode is enabled.
	 *
	 * @param minPosition The minimum position of the wall (bottom-left corner).
	 * @param maxPosition The maximum position of the wall (top-right corner).
	 */
	public Wall(Vector2f minPosition, Vector2f maxPosition) {
		this.gameObject = new GameObject("Wall" + minPosition + maxPosition);
		Transform transform = new Transform(minPosition, new Vector2f(maxPosition).sub(minPosition));
		if (TimeCapsule.DEBUG_SHOW_WALL_COLLISIONS) {
			Render render = new ColorRender(new Vector4f(0.5f, 0.5f, 0.5f, 1), Layer.EFFECT);
			this.gameObject.addComponent(render);
		}

		Collider2D collider = new Collider2D(new AABB(transform));
		collider.setRigid(true);

		this.gameObject.addComponent(transform);
		this.gameObject.addComponent(collider);
	}

	@Override
	public GameObject getGameObject() {
		return gameObject;
	}
}
