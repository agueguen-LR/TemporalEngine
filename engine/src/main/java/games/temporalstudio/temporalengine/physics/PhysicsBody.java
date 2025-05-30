package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;
import org.joml.Vector2f;

import java.util.HashSet;
import java.util.Set;

public class PhysicsBody implements Component {
	private float mass;
	private Vector2f velocity;
	private float maxVelocity;
	private float minVelocity;
	private float drag;
	private float fluidDensity;
	private Set<Vector2f> appliedForces;

	public PhysicsBody(float mass, float maxVelocity, float minVelocity, float drag) {
		this.mass = mass;
		this.maxVelocity = maxVelocity;
		this.minVelocity = minVelocity;
		this.velocity = new Vector2f();
		this.drag = drag;
		this.fluidDensity = 1.0f; // Default to 1.0 for air density
		this.appliedForces = new HashSet<>();
	}

	public void applyForce(float x, float y) {
		appliedForces.add(new Vector2f(x, y));
	}

	public void applyForce(Vector2f force) {
		appliedForces.add(force);
	}

	public float getMass() {
		return mass;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public Set<Vector2f> getAppliedForces() {
		return appliedForces;
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

	float getFluidDensity() {
		return fluidDensity;
	}

	public void emptyAppliedForces() {
		appliedForces.clear();
	}

	public void addVelocity(Vector2f velocity) {
		this.velocity.add(velocity);
		if (this.velocity.lengthSquared() > maxVelocity * maxVelocity) {
			this.velocity.normalize(maxVelocity);
		} else if (this.velocity.lengthSquared() < minVelocity * minVelocity) {
			this.velocity.set(0);
		}
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
