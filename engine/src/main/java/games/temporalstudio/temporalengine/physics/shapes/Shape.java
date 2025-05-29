package games.temporalstudio.temporalengine.physics.shapes;

import org.joml.Vector2f;

public interface Shape {
	public boolean intersects(Shape other);
	public void updateShape();
	public void setOffset(float x, float y);
	public void setMagnitude(float x, float y);
	public float getArea();
	public Shape cast(Vector2f translation);
	public Vector2f collisionNormal(Shape other);
}
