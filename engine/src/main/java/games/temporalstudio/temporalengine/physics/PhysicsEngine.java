package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import org.joml.Vector2f;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PhysicsEngine implements PhysicsEngineLifeCycle{
	private Set<GameObject> colliders;

	public PhysicsEngine() {
		this.colliders = new HashSet<>();
	}

	private void applyForce(PhysicsBody physicsBody, double deltaTime) {
		Vector2f sumOfForces = physicsBody.getAppliedForces().stream().reduce(new Vector2f(), Vector2f::add);
		if (sumOfForces.lengthSquared() == 0.0f) {
			// No force to apply, return early
			return;
		}

		float mass = physicsBody.getMass();
		Vector2f velocity = physicsBody.getVelocity();
		float maxVelocity = physicsBody.getMaxVelocity();
		float minVelocity = physicsBody.getMinVelocity();
		if (mass == 0) {
			Game.LOGGER.severe("Cannot apply force to a PhysicsBody with zero mass.");
			return;
		}

		velocity.fma((float) (deltaTime / mass), sumOfForces);
		if (velocity.lengthSquared() < minVelocity * minVelocity) {
			physicsBody.setVelocity(0.0f, 0.0f);
			return;
		}
		// Comparing squares to avoid costly square root calculation
		if (velocity.lengthSquared() > maxVelocity * maxVelocity) {
			velocity.normalize(maxVelocity);
		}
	}

	private void applyDrag(PhysicsBody physicsBody, double deltaTime) {
		Vector2f velocity = physicsBody.getVelocity();
		float drag = physicsBody.getDrag();
		if (drag <= 0 || velocity.lengthSquared() == 0) {
			return; // No drag to apply or no velocity
		}

		float dragForceMagnitude = (float) (drag * deltaTime);
		if (velocity.lengthSquared() > dragForceMagnitude * dragForceMagnitude) {
			physicsBody.applyForce(velocity.mul(-dragForceMagnitude / velocity.length(), new Vector2f()));
		}
	}

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

			if (collider.collidesWith(otherCollider, new Vector2f(physicsBody.getVelocity()).mul(deltaTime))){
				// Perform collision response
				collider.addColliding(other);
				otherCollider.addColliding(gameObject);

				if (otherCollider.isRigid()){ // Block this gameObject from entering a rigid collider
					Vector2f newVector = otherCollider.computeRigidCollisionNewVelocity(collider, physicsBody.getVelocity());
					physicsBody.setVelocity(newVector.x, newVector.y);
				}
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

	private void applyPhysicsToGameObject(GameObject gameObject, float deltaTime) {
		PhysicsBody physicsBody = gameObject.getComponent(PhysicsBody.class);
		Transform transform = gameObject.getComponent(Transform.class);
		if (transform == null || physicsBody == null) {
			Game.LOGGER.severe(
					"GameObject " + gameObject.getName() + " does not have a mandatory component, cannot apply Physics to it."
			);
			return;
		}

		applyForce(physicsBody, deltaTime);

		detectCollisions(gameObject, deltaTime);

		if (physicsBody.getVelocity().lengthSquared() > 0) {
			transform.setPosition(
					transform.getPosition().add(physicsBody.getVelocity().mul(deltaTime))
			);
		}

		physicsBody.emptyAppliedForces();

		applyDrag(physicsBody, deltaTime);
	}

	@Override
	public void compute(LifeCycleContext context, float deltaTime) {
		if (!(context instanceof Game game)){
			Game.LOGGER.severe("PhysicsEngine compute method requires a Game context.");
			return;
		}
		Scene leftScene = game.getLeftScene();
		Scene rightScene = game.getRightScene();

		colliders = new HashSet<>(leftScene.getGOsByComponent(Collider2D.class));
		List<GameObject> physicsBodies = colliders.stream().filter(
				gameObject -> gameObject.hasComponent(PhysicsBody.class)
		).toList();
		physicsBodies.forEach(gameObject -> {
			if (gameObject.hasComponent(PhysicsBody.class)){
				applyPhysicsToGameObject(gameObject, deltaTime);
			}
		});

		colliders = new HashSet<>(rightScene.getGOsByComponent(Collider2D.class));
		physicsBodies = colliders.stream().filter(
				gameObject -> gameObject.hasComponent(PhysicsBody.class)
		).toList();
		physicsBodies.forEach(gameObject -> {
			if (gameObject.hasComponent(PhysicsBody.class)){
				applyPhysicsToGameObject(gameObject, deltaTime);
			}
		});
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
