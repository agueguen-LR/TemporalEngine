package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The PhysicsEngine class is responsible for managing the physics simulation in the game.
 * It handles the application of forces, collision detection, and response for GameObjects
 * with PhysicsBody and Collider2D components.
 *
 * @author agueguen-LR
 */
public class PhysicsEngine implements PhysicsEngineLifeCycle{
	private Set<GameObject> colliders;
	private Map<GameObject, Set<GameObject>> collidingObjects;

	public PhysicsEngine() {
		this.colliders = new HashSet<>();
		this.collidingObjects = new HashMap<>();
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

		return sumOfForces.mul(deltaTime / mass); // F = ma, so deltaV = F * deltaTime/m
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
		if (speed2 == 0) {
			return; // No drag force if velocity is zero
		}

		float dragCoefficient = physicsBody.getDrag(); // C_d
		float fluidDensity = physicsBody.getFluidDensity(); // ρ
		float area = collider.getArea(); // A
		float dragMagnitude = 0.5f * dragCoefficient * fluidDensity * area * speed2;
		Vector2f dragForce = velocity.normalize().mul(-dragMagnitude);

		physicsBody.applyForce(dragForce);
	}

	/**
	 * Detects collisions for a given GameObject against all other colliders in the scene.
	 * This method checks if the GameObject's collider intersects with any other colliders,
	 * and if not, checks for potential collisions based on the next position
	 * after applying forces.
	 *
	 * @param gameObject The GameObject to check for collisions.
	 * @param deltaTime The time step for the physics update, used to calculate next position.
	 */
	private void detectCollisions(GameObject gameObject, float deltaTime) {

		Collider2D collider = gameObject.getComponent(Collider2D.class);
		PhysicsBody physicsBody = gameObject.getComponent(PhysicsBody.class);
		if (collider == null) {
			return; // No collider to check for collisions
		}

		for (GameObject other : colliders) {
			if (other.equals(gameObject)) {
				continue; // Skip self-collision
			}

			Collider2D otherCollider = other.getComponent(Collider2D.class);
			if (collider.intersectsWith(otherCollider)) {
				collider.addIntersecting(other);
				otherCollider.addIntersecting(gameObject);
			} else { // Only check for collisions if not intersecting
				collider.removeIntersecting(other);
				otherCollider.removeIntersecting(gameObject);

				// Predict the next position of this collider after applying velocity and forces
				Collider2D nextPosition = new Collider2D(collider);
				Vector2f predictedOffset = new Vector2f(physicsBody.getVelocity())
					.add(convertForceToVelocity(physicsBody, deltaTime))
					.mul(deltaTime);
				nextPosition.setOffset(predictedOffset);

				// Predict the next position of the other collider if it has a PhysicsBody
				Collider2D otherNextPosition = new Collider2D(otherCollider);
				if (other.hasComponent(PhysicsBody.class)) {
					PhysicsBody otherPhysicsBody = other.getComponent(PhysicsBody.class);
					Vector2f otherPredictedOffset = new Vector2f(otherPhysicsBody.getVelocity())
						.add(convertForceToVelocity(otherPhysicsBody, deltaTime))
						.mul(deltaTime);
					otherNextPosition.setOffset(otherPredictedOffset);
				}

				if (nextPosition.intersectsWith(otherNextPosition)){
					if (!collidingObjects.containsKey(other) || !collidingObjects.get(other).contains(gameObject)){
						if (!collidingObjects.containsKey(gameObject)){
							collidingObjects.put(gameObject, new HashSet<>());
						}
						collidingObjects.get(gameObject).add(other);
					}

				} else {
					collider.removeColliding(other);
					otherCollider.removeColliding(gameObject);
				}
			}
		}
	}

	/**
	 * Applies collisions for all colliding GameObjects.
	 * This method iterates through the colliding objects and applies impulses
	 * to simulate a physical response to the collisions.
	 *
	 * @param deltaTime The time step for the physics update, used to calculate impulse application.
	 */
	private void applyCollisions(float deltaTime) {
		collidingObjects.forEach((gameObject, collidingSet) -> collidingSet.forEach(other -> {
			applyImpulses(gameObject, other, deltaTime);
		}));
		collidingObjects.clear(); // Clear after processing all collisions
	}

	/**
	 * Applies impulses to the colliding GameObjects based on their PhysicsBody components.
	 * This method calculates the collision normal, relative velocities, and applies impulses
	 * to both colliding objects to simulate a physical response to the collision.
	 *
	 * @param gameObject The first GameObject involved in the collision.
	 * @param other The second GameObject involved in the collision.
	 * @param deltaTime The time step for the physics update, used to calculate impulse application.
	 */
	private void applyImpulses(GameObject gameObject, GameObject other, float deltaTime) {
		Collider2D thisCollider = gameObject.getComponent(Collider2D.class);
		Collider2D otherCollider = other.getComponent(Collider2D.class);
		thisCollider.addColliding(other);
		otherCollider.addColliding(gameObject);

		if (!other.hasComponent(PhysicsBody.class) && !otherCollider.isRigid()) {
			return; // Other object is not a physics body or rigid collider, no physical collision response needed
		}


		Vector2f collisionNormal = thisCollider.getCollisionNormal(otherCollider);
		if (collisionNormal.lengthSquared() == 0) {
			return; // No valid collision normal, skip collision handling
		}

		PhysicsBody thisPhysicsBody = gameObject.getComponent(PhysicsBody.class);

		// Mass and velocity for 'other'
		float massB = 1f;
		Vector2f velocityB = new Vector2f();
		if (other.hasComponent(PhysicsBody.class)) {

			PhysicsBody otherPhysicsBody = other.getComponent(PhysicsBody.class);
			massB = otherPhysicsBody.getMass();
			velocityB = otherPhysicsBody.getVelocity().add(convertForceToVelocity(otherPhysicsBody, deltaTime));
		}

		// Mass and velocity for 'this'
		float massA = thisPhysicsBody.getMass();
		Vector2f velocityA = thisPhysicsBody.getVelocity().add(convertForceToVelocity(thisPhysicsBody, deltaTime));

		// Relative velocity and projection
		Vector2f relativeVelocity = new Vector2f(velocityA).sub(velocityB);
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
			detectCollisions(gameObject, deltaTime);
		});
		applyCollisions(deltaTime);
	}

	/**
   * Updates the velocities of all physics bodies based on the applied forces.
	 *
	 * @param physicsBodies The set of GameObjects with PhysicsBody components to update.
	 * @param deltaTime The time step for the physics update, used to calculate velocity changes.
	 */
	private void updateVelocities(Set<GameObject> physicsBodies, float deltaTime) {
		physicsBodies.forEach(gameObject -> {
			PhysicsBody physicsBody = gameObject.getComponent(PhysicsBody.class);
			physicsBody.addVelocity(convertForceToVelocity(physicsBody, deltaTime));
			physicsBody.emptyAppliedForces();
		});
	}

	/**
	 * Updates the positions of all physics bodies based on their velocities.
	 *
	 * @param physicsBodies The set of GameObjects with PhysicsBody components to update.
	 * @param deltaTime The time step for the physics update, used to calculate position changes.
	 */
	private void updatePositions(Set<GameObject> physicsBodies, float deltaTime) {
		physicsBodies.forEach(gameObject -> {
			PhysicsBody physicsBody = gameObject.getComponent(PhysicsBody.class);
			Transform transform = gameObject.getComponent(Transform.class);
			transform.move(physicsBody.getVelocity().mul(deltaTime));
			Collider2D collider = gameObject.getComponent(Collider2D.class);
			collider.updateCollider2D();
		});
	}

	/**
	 * Computes the physics simulation for the game.
	 * This method applies forces, updates velocities, and positions of all GameObjects
	 * with PhysicsBody and Collider2D components in both left and right scenes.
	 *
	 * @param context The LifeCycleContext for the game, expected to be a Game instance.
	 * @param deltaTime The time step for the physics update.
	 */
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
		updateVelocities(physicsBodies, deltaTime);
		updatePositions(physicsBodies, deltaTime);

		colliders = rightScene.getGOsByComponent(Collider2D.class);
		physicsBodies = rightScene.getGOsByComponent(PhysicsBody.class);
		applyForcesToPhysicsObjects(physicsBodies, deltaTime);
		updateVelocities(physicsBodies, deltaTime);
		updatePositions(physicsBodies, deltaTime);
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
