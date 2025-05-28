package games.temporalstudio.temporalengine.physics;

import org.joml.Vector2f;

class AABB {
	private Transform transform;
	private Vector2f offset;
	private Vector2f magnitude;

	private Vector2f min;
	private Vector2f max;

	public AABB(Transform transform) {
		this.transform = transform;
		this.offset = new Vector2f();
		this.magnitude = new Vector2f(1.0f, 1.0f);
		updateMin();
		updateMax();
	}

	void updateMin() {
		Vector2f position = transform.getPosition();
		min = new Vector2f(position).add(offset);
	}

	void updateMax(){
		Vector2f position = transform.getPosition();
		Vector2f scale = transform.getScale();
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
		updateMin();
		updateMax();
	}

	public void setMagnitude(float x, float y) {
		this.magnitude = new Vector2f(x, y);
		updateMax();
	}
}
