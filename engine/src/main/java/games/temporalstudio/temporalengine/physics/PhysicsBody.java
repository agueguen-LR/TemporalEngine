package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;
import org.joml.Vector2f;

public class PhysicsBody implements Component {
	private float mass;
	private Vector2f velocity;
	private float maxVelocity;
	private float minVelocity;
	private float drag;
	private Vector2f exertedForce = new Vector2f();

	public PhysicsBody(float mass, float maxVelocity, float minVelocity, float drag) {
		this.mass = mass;
		this.maxVelocity = maxVelocity;
		this.minVelocity = minVelocity;
		this.velocity = new Vector2f();
		this.drag = drag;
	}

	public void applyForce(Vector2f force) {
		exertedForce.add(force);
	}

	public float getMass() {
		return mass;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public Vector2f getExertedForce() {
		return exertedForce;
	}

	public float getMaxVelocity() {
		return maxVelocity;
	}

	public float getMinVelocity() {
		return minVelocity;
	}

	public float getDrag() {
		return drag;
	}

	public void setExertedForce(float x, float y) {
		this.exertedForce.set(x, y);
	}

	public void setVelocity(float x, float y) {
		this.velocity.set(x, y);
	}

	@Override
	public void update(LifeCycleContext context, float delta) {

	}

	@Override
	public void init(LifeCycleContext context) {

	}

	@Override
	public void start(LifeCycleContext context) {

	}

	@Override
	public void destroy(LifeCycleContext context) {

	}
}
