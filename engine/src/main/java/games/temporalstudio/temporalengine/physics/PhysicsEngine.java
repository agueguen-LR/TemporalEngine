package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import org.joml.Vector2f;

import java.util.HashSet;
import java.util.Set;

public class PhysicsEngine implements PhysicsEngineLifeCycle{
	private Set<GameObject> colliders;

	public PhysicsEngine() {
		this.colliders = new HashSet<>();
	}

	/**
	 * Converts the applied forces of a PhysicsBody into a velocity vector.
	 * This method calculates the net force applied to the PhysicsBody, divides it by its mass,
	 * and scales it by the delta time to get the change in velocity.
	 *
	 * @param physicsBody The PhysicsBody to convert forces for.
	 * @param deltaTime The time step for the physics update, used to scale the velocity change.
	 * @return A Vector2f representing the change in velocity due to the applied forces.
	 */
	private Vector2f convertForceToVelocity(PhysicsBody physicsBody, float deltaTime) {
		Vector2f sumOfForces = physicsBody.getAppliedForces().stream().reduce(new Vector2f(), Vector2f::add);
		if (sumOfForces.lengthSquared() == 0.0f) {
			return new Vector2f(); // No forces to apply
		}

		float mass = physicsBody.getMass();
		if (mass == 0) {
			Game.LOGGER.severe("Cannot apply force to a PhysicsBody with zero mass.");
			return new Vector2f();
		}

		return sumOfForces.mul(deltaTime / mass); // F = ma, so v = F/m * deltaTime
	}

	/**
	 * Applies drag force to a given GameObject.
	 * This method calculates the drag force based on the object's velocity, drag coefficient,
	 * fluid density, and collider area, and applies it to the PhysicsBody of the GameObject.
	 *
	 * @param gameObject The GameObject to apply drag to.
	 */
	private void applyDrag(GameObject gameObject) {
		PhysicsBody physicsBody = gameObject.getComponent(PhysicsBody.class);
		Collider2D collider = gameObject.getComponent(Collider2D.class);
		Vector2f velocity = physicsBody.getVelocity();
		float speed2 = velocity.lengthSquared();
		if (speed2 > 0) {
		    float dragCoefficient = physicsBody.getDrag(); // C_d
		    float fluidDensity = physicsBody.getFluidDensity(); // ρ
		    float area = collider.getArea(); // A
		    float dragMagnitude = 0.5f * dragCoefficient * fluidDensity * area * speed2;
		    Vector2f dragForce = new Vector2f(velocity).normalize().mul(-dragMagnitude);
		    physicsBody.applyForce(dragForce);
		}
	}

	/**
	 * Applies collision detection and response for a given GameObject.
	 * This method checks for collisions with all other colliders in the scene,
	 * updates the collider's state, and applies collision responses if necessary.
	 *
	 * @param gameObject The GameObject to check for collisions.
	 * @param deltaTime The time step for the physics update, used to calculate velocity changes.
	 */
	private void applyCollisions(GameObject gameObject, float deltaTime) {
		Collider2D collider = gameObject.getComponent(Collider2D.class);
		PhysicsBody physicsBody = gameObject.getComponent(PhysicsBody.class);
		if (collider == null) {
			return; // No collider to check for collisions
		}
		collider.updateCollider2D();
		for (GameObject other : colliders) {
			if (other.equals(gameObject)) {
				continue; // Skip self-collision
			}
			Collider2D otherCollider = other.getComponent(Collider2D.class);
			Vector2f nextPosition = new Vector2f(physicsBody.getVelocity())
					.add(convertForceToVelocity(physicsBody, deltaTime))
					.mul(deltaTime);

			if (collider.collidesWith(otherCollider, nextPosition)){
				collider.addColliding(other);
				otherCollider.addColliding(gameObject);
				applyCollision(gameObject, other, deltaTime);
			} else {
				collider.removeColliding(other);
				otherCollider.removeColliding(gameObject);
			}

			if (collider.intersectsWith(otherCollider)) {
				collider.addIntersecting(other);
				otherCollider.addIntersecting(gameObject);
			} else {
				collider.removeIntersecting(other);
				otherCollider.removeIntersecting(gameObject);
			}
		}
	}

	/**
	 * Applies collision response between two GameObjects.
	 * This method calculates the collision normal, relative velocities, and applies impulses
	 * to both objects based on their masses and restitution coefficients.
	 *
	 * @param gameObject The GameObject that is being processed for collision.
	 * @param other The other GameObject involved in the collision.
	 * @param deltaTime The time step for the physics update, used to calculate velocity changes.
	*/
	private void applyCollision(GameObject gameObject, GameObject other, float deltaTime) {
		Collider2D otherCollider = other.getComponent(Collider2D.class);
		if (!other.hasComponent(PhysicsBody.class) && !otherCollider.isRigid()) {
			return; // Other object is not a physics body or rigid collider, no collision response needed
		}

		Collider2D thisCollider = gameObject.getComponent(Collider2D.class);
		Vector2f collisionNormal = thisCollider.getCollisionNormal(otherCollider);
		if (collisionNormal.lengthSquared() == 0) {
			return; // No valid collision normal, skip collision handling
		}

		PhysicsBody thisPhysicsBody = gameObject.getComponent(PhysicsBody.class);

		// Mass and velocity for 'other'
		float massB = 1f;
		Vector2f predictedVelocityB = new Vector2f();
		if (other.hasComponent(PhysicsBody.class)) {
			PhysicsBody otherPhysicsBody = other.getComponent(PhysicsBody.class);

			massB = otherPhysicsBody.getMass();
			Vector2f totalForceB = otherPhysicsBody.getAppliedForces().stream().reduce(new Vector2f(), Vector2f::add);
			Vector2f velocityB = new Vector2f(otherPhysicsBody.getVelocity());

			predictedVelocityB = velocityB.fma((deltaTime / massB), totalForceB);
		}

		// Mass and velocity for 'this'
		float massA = thisPhysicsBody.getMass();
		Vector2f totalForceA = thisPhysicsBody.getAppliedForces().stream().reduce(new Vector2f(), Vector2f::add);
		Vector2f velocityA = new Vector2f(thisPhysicsBody.getVelocity());

		Vector2f predictedVelocityA = velocityA.fma((deltaTime / massA), totalForceA);

		// Relative velocity and projection
		Vector2f relativeVelocity = new Vector2f(predictedVelocityA).sub(predictedVelocityB);
		float velocityAlongNormal = relativeVelocity.dot(collisionNormal);

		// Restitution
		float restitution = Math.min(thisCollider.getRestitution(), otherCollider.getRestitution());

		// Impulse calculation
		float impulseDenominator = other.hasComponent(PhysicsBody.class) ? (1f / massA + 1f / massB) : (1f / massA);
		float impulseMagnitude = -(1f + restitution) * velocityAlongNormal / impulseDenominator;
		Vector2f impulse = new Vector2f(collisionNormal).mul(impulseMagnitude);

		// Apply forces
		Vector2f forceOnA = new Vector2f(impulse).mul(1f / deltaTime);
		thisPhysicsBody.applyForce(forceOnA);

		if (other.hasComponent(PhysicsBody.class)) {
			Vector2f forceOnB = new Vector2f(impulse).mul(-1f / deltaTime);
			other.getComponent(PhysicsBody.class).applyForce(forceOnB);
		}
	}

	/**
	 * Applies forces to all physics objects in the scene.
	 * This method iterates through all GameObjects with PhysicsBody components,
	 * applies drag, and checks for collisions.
	 *
	 * @param gameObjects The set of GameObjects to apply forces to.
	 * @param deltaTime The time step for the physics update, used to calculate velocity changes.
	 */
	private void applyForcesToPhysicsObjects(Set<GameObject> gameObjects, float deltaTime) {
		gameObjects.forEach(gameObject -> {
			applyDrag(gameObject);
			applyCollisions(gameObject, deltaTime);
		});
	}

	/**
	 * Updates the velocities and positions of all physics bodies in the scene.
	 * This method calculates the new velocity based on applied forces and updates
	 * the position of each GameObject accordingly.
	 *
	 * @param physicsBodies The set of GameObjects with PhysicsBody components to update.
	 * @param deltaTime The time step for the physics update, used to calculate position changes.
	 */
	private void updateVelocitiesAndPositions(Set<GameObject> physicsBodies, float deltaTime) {
		physicsBodies.forEach(gameObject -> {
			PhysicsBody physicsBody = gameObject.getComponent(PhysicsBody.class);
			physicsBody.addVelocity(convertForceToVelocity(physicsBody, deltaTime));
			Transform transform = gameObject.getComponent(Transform.class);
			transform.move(new Vector2f(physicsBody.getVelocity()).mul(deltaTime));
			physicsBody.emptyAppliedForces();
		});
	}

	@Override
	public void compute(LifeCycleContext context, float deltaTime) {
		if (!(context instanceof Game game)){
			Game.LOGGER.severe("PhysicsEngine compute method requires a Game context.");
			return;
		}
		Scene leftScene = game.getLeftScene();
		Scene rightScene = game.getRightScene();
		Set<GameObject> physicsBodies;

		colliders = leftScene.getGOsByComponent(Collider2D.class);
		physicsBodies = leftScene.getGOsByComponent(PhysicsBody.class);
		applyForcesToPhysicsObjects(physicsBodies, deltaTime);
		updateVelocitiesAndPositions(physicsBodies, deltaTime);

		colliders = rightScene.getGOsByComponent(Collider2D.class);
		physicsBodies = rightScene.getGOsByComponent(PhysicsBody.class);
		applyForcesToPhysicsObjects(physicsBodies, deltaTime);
		updateVelocitiesAndPositions(physicsBodies, deltaTime);
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
