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

	private enum AABBRelation {
		LEFT, RIGHT, UP, DOWN, OVERLAPPING
	}

	private AABBRelation getRelation(AABB a, AABB b) {
		if (a.getMax().x < b.getMin().x) return AABBRelation.LEFT;
		if (a.getMin().x > b.getMax().x) return AABBRelation.RIGHT;
		if (a.getMin().y > b.getMax().y) return AABBRelation.UP;
		if (a.getMax().y < b.getMin().y) return AABBRelation.DOWN;
		return AABBRelation.OVERLAPPING;
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

	public float getArea() {
		return (max.x - min.x) * (max.y - min.y);
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
		AABBRelation relation = getRelation(this, aabb);
		return relation.equals(AABBRelation.OVERLAPPING);
	}

	@Override
	public AABB cast(Vector2f translation) {
		AABB aabb = new AABB(transform);
		aabb.setOffset(offset.x + translation.x, offset.y + translation.y);
		aabb.setMagnitude(magnitude.x, magnitude.y);
		return aabb;
	}

	@Override
	public Vector2f collisionNormal(Shape other) {
		if (!(other instanceof AABB otherAABB)) {
			Game.LOGGER.severe("AABB collisionNormal function currently only supports AABB shapes.");
			return new Vector2f(0, 0);
		}
		return switch (getRelation(this, otherAABB)) {
			case LEFT -> new Vector2f(-1, 0);
			case RIGHT -> new Vector2f(1, 0);
			case UP -> new Vector2f(0, -1);
			case DOWN -> new Vector2f(0, 1);
			default -> new Vector2f(0, 0);
		};
	}
}
