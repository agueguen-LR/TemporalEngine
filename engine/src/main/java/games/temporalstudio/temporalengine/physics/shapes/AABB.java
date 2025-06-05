package games.temporalstudio.temporalengine.physics.shapes;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.physics.Transform;
import org.joml.Vector2f;

/**
 * Represents an axis-aligned bounding box (AABB) shape for 2D collision detection.
 * Used in physics calculations to determine overlap and collision normals between objects.
 *
 * @author agueguen-LR
 */
public class AABB implements Shape {
	private Transform transform;
	private Vector2f offset;
	private Vector2f magnitude;

	private Vector2f min;
	private Vector2f max;

	/**
	 * Constructs an AABB with the given transform.
	 *
	 * @param transform the transform representing position and scale
	 */
	public AABB(Transform transform) {
		this.transform = transform;
		this.offset = new Vector2f();
		this.magnitude = new Vector2f(1.0f, 1.0f);
		updateShape();
	}

	/**
	 * Creates a copy of this AABB.
	 *
	 * @return a new AABB instance with the same properties
	 */
	public Shape copy() {
		AABB aabb = new AABB(transform);
		aabb.setOffset(new Vector2f(offset));
		aabb.setMagnitude(new Vector2f(magnitude));
		return aabb;
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

	/**
	 * Updates the min and max points of the AABB based on the current transform, offset, and magnitude.
	 */
	public void updateShape() {
		Vector2f position = transform.getPosition();
		Vector2f scale = transform.getScale();
		min = new Vector2f(position).add(offset);
		max = new Vector2f(position).add(offset).add(new Vector2f(magnitude).mul(scale));
	}

	/**
	 * Returns the minimum (bottom-left) point of the AABB.
	 *
	 * @return the minimum point as a Vector2f
	 */
	public Vector2f getMin() {
		return min;
	}

	/**
	 * Returns the maximum (top-right) point of the AABB.
	 *
	 * @return the maximum point as a Vector2f
	 */
	public Vector2f getMax() {
		return max;
	}

	/**
	 * Returns the area of the AABB.
	 *
	 * @return the area as a float
	 */
	public float getArea() {
		return (max.x - min.x) * (max.y - min.y);
	}

	/**
	 * Sets the offset of the AABB and updates its shape.
	 *
	 * @param offset the new offset as a Vector2f
	 */
	public void setOffset(Vector2f offset) {
		this.offset = offset;
		updateShape();
	}

	/**
	 * Sets the magnitude (size) of the AABB and updates its shape.
	 *
	 * @param magnitude the new magnitude as a Vector2f
	 */
	public void setMagnitude(Vector2f magnitude) {
		this.magnitude = magnitude;
		updateShape();
	}

	/**
	 * Checks if this AABB intersects with another shape.
	 * Only supports intersection with other AABB shapes.
	 *
	 * @param other the other shape to check intersection with
	 * @return true if the shapes overlap, false otherwise
	 */
	@Override
	public boolean intersects(Shape other) {
		if (!(other instanceof AABB aabb)) {
			Game.LOGGER.severe("AABB intersects function currently only supports AABB shapes.");
			return false;
		}
		AABBRelation relation = getRelation(this, aabb);
		return relation.equals(AABBRelation.OVERLAPPING);
	}

	/**
	 * Returns the collision normal between this AABB and another shape.
	 * Only supports collision normal calculation with other AABB shapes.
	 *
	 * @param other the other shape to calculate the collision normal with
	 * @return the collision normal as a Vector2f
	 */
	@Override
	public Vector2f collisionNormal(Shape other) {
		if (!(other instanceof AABB otherAABB)) {
			Game.LOGGER.severe("AABB collisionNormal function currently only supports AABB shapes.");
			return new Vector2f(0, 0);
		}
		return switch (getRelation(this, otherAABB)) {
			case LEFT -> new Vector2f(1, 0);
			case RIGHT -> new Vector2f(-1, 0);
			case UP -> new Vector2f(0, -1);
			case DOWN -> new Vector2f(0, 1);
			default -> new Vector2f(0, 0);
		};
	}
}