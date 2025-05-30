package games.temporalstudio.temporalengine.physics.shapes;

import org.joml.Vector2f;

public interface Shape {
	public boolean intersects(Shape other);
	public void updateShape();
	public void setOffset(Vector2f offset);
	public void setMagnitude(Vector2f magnitude);
	public float getArea();
	public Shape copy();
	public Vector2f collisionNormal(Shape other);
}
