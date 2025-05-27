package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;
import org.joml.Vector2f;

public class PhysicsBody implements Component {
	private float mass;
	private Vector2f velocity;
	private float maxVelocity;
	private Vector2f exertedForce = new Vector2f();

	public PhysicsBody(float mass, float maxVelocity) {
		this.mass = mass;
		this.maxVelocity = maxVelocity;
		this.velocity = new Vector2f();
	}

	public void applyForce(Vector2f force) {
		exertedForce.add(force);
	}

	public void resetForce() {
		exertedForce.set(0, 0);
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
