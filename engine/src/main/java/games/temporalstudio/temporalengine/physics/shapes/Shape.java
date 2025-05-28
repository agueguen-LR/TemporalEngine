package games.temporalstudio.temporalengine.physics.shapes;

public interface Shape {
	public boolean intersects(Shape other);
	public void updateShape();
	public void setOffset(float x, float y);
	public void setMagnitude(float x, float y);
}
