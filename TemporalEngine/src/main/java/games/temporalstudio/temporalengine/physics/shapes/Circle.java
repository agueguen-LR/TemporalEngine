package games.temporalstudio.temporalengine.physics.shapes;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.physics.Transform;
import org.joml.Vector2f;

public class Circle implements Shape{
	private float radius;
	private Vector2f center;

	private Transform transform;
	private Vector2f offset;

	/**
	 * Constructs a Circle shape with the specified radius and transform.
	 *
	 * @param radius    The radius of the circle.
	 * @param transform The transform representing the position and scale of the circle.
	 */
	public Circle(float radius, Transform transform) {
		this.radius = radius;
		this.transform = transform;
		this.center = new Vector2f(transform.getPosition());
		this.offset = new Vector2f();
	}

	@Override
	public Shape copy() {
		return new Circle(radius, new Transform(transform.getPosition()));
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	@Override
	public void setOffset(Vector2f offset) {
		this.offset = offset;
	}

	@Override
	public void setMagnitude(Vector2f magnitude) {
		Game.LOGGER.warning("Setting magnitude on Circle shape is not applicable. Circle uses radius for size.");
	}

	@Override
	public float getArea() {
		return (float) (Math.PI * radius * radius);
	}

	@Override
	public void updateShape() {
		this.center = new Vector2f(this.transform.getPosition()).add(this.offset);
	}

	@Override
	public boolean intersects(Shape other) {
		switch (other) {
			case Circle circle -> {
				return this.center.distanceSquared(circle.center) <= (this.radius + circle.radius) * (this.radius + circle.radius);
			}
			default -> throw new UnsupportedOperationException("Intersection not implemented for this shape type.");
		}
	}

	@Override
	public Vector2f collisionNormal(Shape other) {
		switch (other) {
			case Circle circle -> {
				Vector2f direction = new Vector2f(circle.center).sub(this.center);
				float distance = direction.length();
				if (distance == 0) {
					Game.LOGGER.warning("Collision normal calculation: centers are the same, returning zero vector.");
					return new Vector2f(0, 0); // Default normal if centers are the same
				}
				return direction.div(distance); // Normalize the direction vector
			}
			default -> throw new UnsupportedOperationException("Collision normal not implemented for this shape type.");
		}
	}
}
