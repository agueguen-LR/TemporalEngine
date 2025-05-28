package games.temporalstudio.temporalengine.physics.shapes;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.physics.Transform;
import org.joml.Vector2f;

public class AABB implements Shape {
	private Transform transform;
	private Vector2f offset;
	private Vector2f magnitude;

	private Vector2f min;
	private Vector2f max;

	public AABB(Transform transform) {
		this.transform = transform;
		this.offset = new Vector2f();
		this.magnitude = new Vector2f(1.0f, 1.0f);
		updateShape();
	}

	public void updateShape(){
		Vector2f position = transform.getPosition();
		Vector2f scale = transform.getScale();
		min = new Vector2f(position).add(offset);
		max = new Vector2f(position).add(offset).add(new Vector2f(magnitude).mul(scale));
	}

	public Vector2f getMin() {
		return min;
	}

	public Vector2f getMax() {
		return max;
	}

	public void setOffset(float x, float y) {
		this.offset = new Vector2f(x, y);
		updateShape();
	}

	public void setMagnitude(float x, float y) {
		this.magnitude = new Vector2f(x, y);
		updateShape();
	}

	@Override
	public boolean intersects(Shape other) {
		if (!(other instanceof AABB aabb)) {
			Game.LOGGER.severe("AABB intersects function currently only supports AABB shapes.");
			return false;
		}
		Vector2f otherMin = aabb.getMin();
		Vector2f otherMax = aabb.getMax();
		if (this.max.x < otherMin.x || this.min.x > otherMax.x)return false;
		if (this.max.y < otherMin.y || this.min.y > otherMax.y)return false;
		return true;
	}

	@Override
	public AABB cast(Vector2f translation) {
		AABB aabb = new AABB(transform);
		aabb.setOffset(offset.x + translation.x, offset.y + translation.y);
		aabb.setMagnitude(magnitude.x, magnitude.y);
		return aabb;
	}
}
