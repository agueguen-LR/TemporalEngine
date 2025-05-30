package games.temporalstudio.temporalengine.physics.shapes;

import org.joml.Vector2f;

/**
 * Represents a 2D geometric shape used for collision detection in the physics engine.
 * Implementations define specific shape types (e.g., AABB, circle) and provide methods
 * for intersection tests, area calculation, and collision response.
 *
 * @author agueguen-LR
 */
public interface Shape {
	/**
	 * Checks if this shape intersects with another shape.
	 *
	 * @param other the other shape to test for intersection
	 * @return true if the shapes intersect, false otherwise
	 */
	boolean intersects(Shape other);

	/**
	 * Updates the internal representation of the shape (e.g., after a transform change).
	 */
	void updateShape();

	/**
	 * Sets the offset of the shape relative to its origin.
	 *
	 * @param offset the new offset as a Vector2f
	 */
	void setOffset(Vector2f offset);

	/**
	 * Sets the magnitude (size) of the shape.
	 *
	 * @param magnitude the new magnitude as a Vector2f
	 */
	void setMagnitude(Vector2f magnitude);

	/**
	 * Returns the area of the shape.
	 *
	 * @return the area as a float
	 */
	float getArea();

	/**
	 * Creates a copy of this shape.
	 *
	 * @return a new Shape instance with the same properties
	 */
	Shape copy();

	/**
	 * Returns the collision normal between this shape and another shape.
	 *
	 * @param other the other shape to calculate the collision normal with
	 * @return the collision normal as a Vector2f
	 */
	Vector2f collisionNormal(Shape other);
}