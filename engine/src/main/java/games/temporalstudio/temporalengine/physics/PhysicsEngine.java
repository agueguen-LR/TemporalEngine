package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import org.joml.Vector2f;

import java.util.*;

/**
 * The PhysicsEngine class is responsible for managing the physics simulation in the game.
 * It handles the application of forces, collision detection, and response for GameObjects
 * with PhysicsBody and Collider2D components.
 *
 * @author agueguen-LR
 */
public class PhysicsEngine implements PhysicsEngineLifeCycle{
	private Set<Map.Entry<GameObject, GameObject>> collidingObjects;
	private Map<PhysicsBody, Vector2f> impulses;

	public PhysicsEngine() {
		this.collidingObjects = new HashSet<>();
		this.impulses = new HashMap<>();
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
	 * Detects collisions between physics bodies and colliders.
	 * This method checks the next predicted positions of physics bodies against colliders
	 * to determine if they intersect, and updates the collidingObjects set accordingly.
	 *
	 * @param physicsBodies The set of GameObjects with PhysicsBody components to check for collisions.
	 * @param colliders The set of GameObjects with Collider2D components to check for collisions.
	 * @param nextPositions A map containing GameObjects and their predicted Collider2D positions.
	 */
	private void detectCollisions(
			Set<GameObject> physicsBodies, Set<GameObject> colliders,
			Map<GameObject, Collider2D> nextPositions
	) {

		physicsBodies.forEach(gameObject -> {
			colliders.forEach(other -> {
				if (other.equals(gameObject)) {
					return; // Skip self-collision
				}

				Collider2D thisNextPosition = nextPositions.get(gameObject);
				Collider2D otherNextPosition = nextPositions.get(other);

				if (thisNextPosition.intersectsWith(otherNextPosition)) {
					collidingObjects.add(Map.entry(gameObject, other));
				} else {
					collidingObjects.removeIf(
							entry -> entry.getKey().equals(gameObject) && entry.getValue().equals(other)
					);
				}
			});
		});
	}

	/**
	 * Detects intersections between colliders in the provided set.
	 * This method checks each collider against every other collider to find intersections
	 * and updates their intersecting sets accordingly.
	 *
	 * @param colliders The set of GameObjects with Collider2D components to check for intersections.
	 */
	private void detectIntersections(Set<GameObject> colliders) {
		colliders.forEach(gameObject -> {
			colliders.forEach(other -> {
				if (other.equals(gameObject) || gameObject.hashCode() > other.hashCode()) {
					return; // Skip self-intersection or duplicate checks
				}

				Collider2D currentPosition = gameObject.getComponent(Collider2D.class);
				Collider2D otherPosition = other.getComponent(Collider2D.class);

				if (currentPosition.intersectsWith(otherPosition)) {
					currentPosition.addIntersecting(other);
					otherPosition.addIntersecting(gameObject);
				} else {
					currentPosition.removeIntersecting(other);
					otherPosition.removeIntersecting(gameObject);
				}
			});
		});
	}

	/**
	 * Applies collisions for all colliding GameObjects.
	 * This method iterates through the colliding objects and applies impulses
	 * to simulate a physical response to the collisions.
	 *
	 * @param deltaTime The time step for the physics update, used to calculate impulse application.
	 */
	private void applyCollisions(float deltaTime) {
		collidingObjects.forEach(entry -> {
			Vector2f impulse = impulseForce(entry.getKey(), entry.getValue(), deltaTime);
			this.impulses.put(entry.getKey().getComponent(PhysicsBody.class), impulse);
		}); // Store impulses for each PhysicsBody involved in collisions
		this.impulses.forEach(PhysicsBody::applyForce); // Apply the stored impulses to the respective PhysicsBodies
		collidingObjects.clear(); // Clear after processing all collisions
		this.impulses.clear(); // Clear impulses after applying them
	}

	/**
	 * Applies impulse to the colliding GameObject based on their PhysicsBody component.
	 * This method calculates the collision normal, relative velocities, and applies impulses
	 * to both colliding objects to simulate a physical response to the collision.
	 *
	 * @param gameObject The first GameObject involved in the collision.
	 * @param other The second GameObject involved in the collision.
	 * @param deltaTime The time step for the physics update, used to calculate impulse application.
	 */
	private Vector2f impulseForce(GameObject gameObject, GameObject other, float deltaTime) {
		Collider2D thisCollider = gameObject.getComponent(Collider2D.class);
		Collider2D otherCollider = other.getComponent(Collider2D.class);
		thisCollider.addColliding(other);
		otherCollider.addColliding(gameObject);

		if (!other.hasComponent(PhysicsBody.class) && !otherCollider.isRigid()) {
			return new Vector2f(); // Other object is not a physics body or rigid collider, no physical collision response needed
		}

		Game.LOGGER.info("Collision detected between " + gameObject.getName() + " and " + other.getName());
		Vector2f collisionNormal = thisCollider.getCollisionNormal(otherCollider);
		if (collisionNormal.lengthSquared() == 0) {
			return new Vector2f(); // No valid collision normal, skip collision handling
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

		// Return the impulse force vector
		return new Vector2f(impulse).mul(1f / deltaTime);
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
	 * Predicts the next positions of all physics bodies based on their current velocities and applied forces.
	 * This method calculates the predicted position by applying the current velocity and any forces
	 * to the collider's offset for each GameObject with a PhysicsBody component.
	 *
	 * @param physicsBodies The set of GameObjects with PhysicsBody components to predict positions for.
	 * @param deltaTime The time step for the physics update, used to calculate predicted positions.
	 * @return A map containing GameObjects and their predicted Collider2D positions.
	 */
	private Map<GameObject, Collider2D> predictNextPositions(Set<GameObject> physicsBodies, float deltaTime) {
		Map<GameObject, Collider2D> predictedPositions = new HashMap<>();
		physicsBodies.forEach(gameObject -> {

			Collider2D collider = gameObject.getComponent(Collider2D.class);
			if (!(gameObject.hasComponent(PhysicsBody.class))){
				predictedPositions.put(gameObject, new Collider2D(collider));
			} else{

				PhysicsBody physicsBody = gameObject.getComponent(PhysicsBody.class);
				Vector2f predictedOffset = physicsBody.getVelocity()
						.add(convertForceToVelocity(physicsBody, deltaTime))
						.mul(deltaTime);
				Collider2D predictedCollider = new Collider2D(collider);
				predictedCollider.setOffset(predictedOffset);
				predictedPositions.put(gameObject, predictedCollider);
			}
		});
		return predictedPositions;
	}

	/**
	 * Computes the physics simulation for a given Scene.
	 * This method applies forces, detects intersections and collisions, and updates
	 * the positions and velocities of all GameObjects with PhysicsBody and Collider2D components.
	 *
	 * @param scene The Scene to compute physics for.
	 * @param deltaTime The time step for the physics update.
	 */
	private void computeForScene(Scene scene, float deltaTime) {
		// Get all colliders
		Set<GameObject> colliders = scene.getGOsByComponent(Collider2D.class);
		// Get all physics bodies
		Set<GameObject> physicsBodies = scene.getGOsByComponent(PhysicsBody.class);
		// Get all rigid colliders
		Set<GameObject> rigidColliders = colliders.stream()
				.filter(gameObject -> gameObject.getComponent(Collider2D.class).isRigid())
				.collect(HashSet::new, Set::add, Set::addAll);
		// Get next positions of physics bodies
		Map<GameObject, Collider2D> nextPositions = predictNextPositions(physicsBodies, deltaTime);
		// Add rigid colliders that are not physics bodies to next positions
		rigidColliders.stream().filter(go -> !go.hasComponent(PhysicsBody.class))
				.forEach(go -> nextPositions.put(go, go.getComponent(Collider2D.class)));
		// Apply drag to all physics bodies
		physicsBodies.forEach(this::applyDrag);
		// Detect intersections
		detectIntersections(colliders);
		// Detect collisions
		detectCollisions(physicsBodies, rigidColliders, nextPositions);

		// Apply impulses for collisions
		applyCollisions(deltaTime);
		// Update velocities and positions
		updateVelocities(physicsBodies, deltaTime);
		updatePositions(physicsBodies, deltaTime);
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
		computeForScene(leftScene, deltaTime);
		computeForScene(rightScene, deltaTime);
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
