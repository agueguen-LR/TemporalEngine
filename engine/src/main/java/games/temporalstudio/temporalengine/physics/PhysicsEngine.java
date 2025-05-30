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

		return convertForceToDeltaVelocity(sumOfForces, mass, deltaTime);
	}

	private Vector2f convertForceToDeltaVelocity(Vector2f force, float mass, float deltaTime) {
		if (mass == 0) {
			Game.LOGGER.severe("Cannot apply force to a PhysicsBody with zero mass.");
			return new Vector2f();
		}
		return new Vector2f(force).mul(deltaTime / mass); // F = ma, so deltaV = F * deltaTime/m
	}

	private Vector2f convertDeltaVelocityToRequiredForce(Vector2f deltaVelocity, float mass, float deltaTime) {
		if (mass == 0) {
			Game.LOGGER.severe("Cannot apply force to a PhysicsBody with zero mass.");
			return new Vector2f();
		}
		return new Vector2f(deltaVelocity).mul(mass / deltaTime); // F = ma, so F = deltaV * m/deltaTime
	}

	/**
	 * Applies drag force to a given GameObject.
	 * This method calculates the drag force based on the object's velocity, drag coefficient,
	 * fluid density, and collider area, and applies it to the PhysicsBody of the GameObject.
	 *
	 * @param gameObject The GameObject to apply drag to.
	 */
	private void applyDrag(GameObject gameObject, float deltaTime) {
		PhysicsBody physicsBody = gameObject.getComponent(PhysicsBody.class);
		Collider2D collider = gameObject.getComponent(Collider2D.class);
		Vector2f velocity = new Vector2f(physicsBody.getVelocity());

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

		collider.updateCollider2D();
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

				Collider2D nextPosition = new Collider2D(collider);
				nextPosition.setOffset(new Vector2f(physicsBody.getVelocity()).mul(deltaTime));
				Collider2D otherNextPosition = new Collider2D(otherCollider);
				if (other.hasComponent(PhysicsBody.class)){
					otherNextPosition.setOffset(new Vector2f(other.getComponent(PhysicsBody.class).getVelocity()).mul(deltaTime));
				}

				if (nextPosition.intersectsWith(otherNextPosition)){

					if (!collidingObjects.containsKey(other) || !collidingObjects.get(other).contains(gameObject)){
						if (!collidingObjects.containsKey(gameObject)){
							collidingObjects.put(gameObject, new HashSet<>());
						}
						collidingObjects.get(gameObject).add(other);
						Game.LOGGER.info("Collision detected between " + gameObject.getName() + " and " + other.getName() + "" + collidingObjects.size());

					}

				} else {
					collider.removeColliding(other);
					otherCollider.removeColliding(gameObject);
				}
			}
		}
	}

	private void applyCollisions(float deltaTime) {
		collidingObjects.forEach((gameObject, collidingSet) -> collidingSet.forEach(other -> {
			applyCollision(gameObject, other, deltaTime);
		}));
		collidingObjects.clear(); // Clear after processing all collisions
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
			velocityB = new Vector2f(otherPhysicsBody.getVelocity());
		}

		// Mass and velocity for 'this'
		float massA = thisPhysicsBody.getMass();
		Vector2f velocityA = new Vector2f(thisPhysicsBody.getVelocity());

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
		Game.LOGGER.info("Applying force on " + gameObject.getName() + " : " + forceOnA);

		if (other.hasComponent(PhysicsBody.class)) {
			Vector2f forceOnB = new Vector2f(impulse).mul(-1f / deltaTime);
			other.getComponent(PhysicsBody.class).applyForce(forceOnB);
			Game.LOGGER.info("Applying force on " + other.getName() + " : " + forceOnB);
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
			applyDrag(gameObject, deltaTime);
			detectCollisions(gameObject, deltaTime);
		});
		applyCollisions(deltaTime);
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
			Vector2f before = new Vector2f(physicsBody.getVelocity());
			physicsBody.addVelocity(convertForceToVelocity(physicsBody, deltaTime));
			Vector2f change = new Vector2f(before).sub(physicsBody.getVelocity());
			if (before.lengthSquared() > 0 && change.lengthSquared() > 0) {
				Game.LOGGER.info("change in velocity for " + gameObject.getName() + " : " + before + "-" + physicsBody.getVelocity() + " = " + change);
			}
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
