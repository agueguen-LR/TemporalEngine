package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;
import org.joml.Vector2f;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a 2D physics body for use in the physics engine.
 * Handles mass, velocity, drag, and force application for a game object.
 * Used to simulate physical movement and interactions such as collisions and drag.
 *
 * @author agueguen-LR
 */
public class PhysicsBody implements Component {
	private float mass;
	private Vector2f velocity;
	private float maxVelocity;
	private float minVelocity;
	private float drag;
	private float fluidDensity;
	private Set<Vector2f> appliedForces;

	/**
	 * Constructs a PhysicsBody with the specified parameters.
	 *
	 * @param mass         The mass of the body.
	 * @param maxVelocity  The maximum velocity magnitude.
	 * @param minVelocity  The minimum velocity magnitude.
	 * @param drag         The drag coefficient.
	 */
	public PhysicsBody(float mass, float maxVelocity, float minVelocity, float drag) {
		this.mass = mass;
		this.maxVelocity = maxVelocity;
		this.minVelocity = minVelocity;
		this.velocity = new Vector2f();
		this.drag = drag;
		this.fluidDensity = 1.0f; // Default to 1.0 for air density
		this.appliedForces = new HashSet<>();
	}

	/**
	 * Applies a force to the body for this update step.
	 *
	 * @param x The x component of the force.
	 * @param y The y component of the force.
	 */
	public void applyForce(float x, float y) {
		appliedForces.add(new Vector2f(x, y));
	}

	/**
	 * Applies a force to the body for this update step.
	 *
	 * @param force The force vector to apply.
	 */
	public void applyForce(Vector2f force) {
		appliedForces.add(force);
	}

	/**
	 * Returns the mass of the body.
	 *
	 * @return The mass.
	 */
	public float getMass() {
		return mass;
	}

	/**
	 * Returns a copy of the current velocity vector.
	 *
	 * @return The velocity vector.
	 */
	public Vector2f getVelocity() {
		return new Vector2f(velocity);
	}

	/**
	 * Returns the set of forces currently applied to the body.
	 *
	 * @return The set of applied forces.
	 */
	public Set<Vector2f> getAppliedForces() {
		return appliedForces;
	}

	/**
	 * Returns the maximum allowed velocity magnitude.
	 *
	 * @return The maximum velocity.
	 */
	public float getMaxVelocity() {
		return maxVelocity;
	}

	/**
	 * Returns the minimum allowed velocity magnitude.
	 *
	 * @return The minimum velocity.
	 */
	public float getMinVelocity() {
		return minVelocity;
	}

	/**
	 * Returns the drag coefficient of the body.
	 *
	 * @return The drag coefficient.
	 */
	public float getDrag() {
		return drag;
	}

	/**
	 * Returns the fluid density the body is moving through.
	 * Default is 1.0 (air).
	 *
	 * @return The fluid density.
	 */
	float getFluidDensity() {
		return fluidDensity;
	}

	/**
	 * Clears all forces applied to the body for this update step.
	 */
	public void emptyAppliedForces() {
		appliedForces.clear();
	}

	/**
	 * Adds the given velocity vector to the current velocity, clamping to max/min velocity as needed.
	 *
	 * @param velocity The velocity vector to add.
	 */
	public void addVelocity(Vector2f velocity) {
		this.velocity.add(velocity);
		if (this.velocity.lengthSquared() > maxVelocity * maxVelocity) {
			this.velocity.normalize(maxVelocity);
		} else if (this.velocity.lengthSquared() < minVelocity * minVelocity) {
			this.velocity.set(0);
		}
	}

	/**
	 * Updates the physics body. (No-op by default, override if needed.)
	 *
	 * @param context The life cycle context.
	 * @param delta   The time delta since the last update.
	 */
	@Override
	public void update(LifeCycleContext context, float delta) {

	}

	/**
	 * Initializes the physics body. (No-op by default, override if needed.)
	 *
	 * @param context The life cycle context.
	 */
	@Override
	public void init(LifeCycleContext context) {

	}

	/**
	 * Starts the physics body. (No-op by default, override if needed.)
	 *
	 * @param context The life cycle context.
	 */
	@Override
	public void start(LifeCycleContext context) {

	}

	/**
	 * Destroys the physics body. (No-op by default, override if needed.)
	 *
	 * @param context The life cycle context.
	 */
	@Override
	public void destroy(LifeCycleContext context) {

	}
}
