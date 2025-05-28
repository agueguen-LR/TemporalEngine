package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import org.joml.Vector2f;

import java.util.HashSet;
import java.util.Set;

public class PhysicsEngine implements PhysicsEngineLifeCycle{
	private static Set<GameObject> colliders;

	public PhysicsEngine() {
		PhysicsEngine.colliders = new HashSet<>();
	}

	public static void addCollider(GameObject gameObject) {
		if (gameObject == null) {
			Game.LOGGER.severe("Cannot add null GameObject as a collider.");
			return;
		}
		if (!(gameObject.hasComponent(Collider2D.class))) {
			Game.LOGGER.severe(
					"GameObject " + gameObject.getName() + " does not have a Collider2D component, cannot add it as a collider."
			);
		}
		colliders.add(gameObject);
	}

	public static void removeCollider(GameObject gameObject) {
		if (gameObject == null) {
			Game.LOGGER.severe("Cannot remove null GameObject from colliders.");
			return;
		}
		if (!(gameObject.hasComponent(Collider2D.class))) {
			Game.LOGGER.warning(
					"GameObject " + gameObject.getName() + " does not have a Collider2D component, cannot remove it from colliders."
			);
		}
		colliders.remove(gameObject);
	}

	private void applyForce(PhysicsBody physicsBody, double deltaTime) {
		Vector2f force = physicsBody.getExertedForce();
		if (force.lengthSquared() == 0) {
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

		velocity.fma((float) (deltaTime / mass), force);
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
		if (velocity.lengthSquared() < dragForceMagnitude * dragForceMagnitude) {
			physicsBody.setExertedForce(0.0f, 0.0f);
		} else {
			physicsBody.applyForce(velocity.mul(-dragForceMagnitude / velocity.length(), new Vector2f()));
		}
	}

	private void detectCollisions(GameObject gameObject) {
		Collider2D collider = gameObject.getComponent(Collider2D.class);
		if (collider == null) {
			return; // No collider to check for collisions
		}
		collider.updateAABB();
		for (GameObject other : colliders) {
			if (other.equals(gameObject)) {
				continue; // Skip self-collision
			}
			Collider2D otherCollider = other.getComponent(Collider2D.class);
			if (collider.collidesWith(otherCollider)) {
				collider.setColliding(other, true);
				collider.getPhysicsOnCollide().accept(gameObject, other);
				otherCollider.setColliding(gameObject, true);
				otherCollider.getPhysicsOnCollide().accept(other, gameObject);
			} else {
				collider.setColliding(other, false);
				collider.getPhysicsOnSeparate().accept(gameObject, other);
				otherCollider.setColliding(gameObject, false);
				otherCollider.getPhysicsOnSeparate().accept(other, gameObject);
			}
		}
	}

	private void applyPhysicsToGameObject(GameObject gameObject, double deltaTime) {
		PhysicsBody physicsBody = gameObject.getComponent(PhysicsBody.class);
		Transform transform = gameObject.getComponent(Transform.class);
		if (transform == null || physicsBody == null) {
			Game.LOGGER.severe(
					"GameObject " + gameObject.getName() + " does not have a mandatory component, cannot apply Physics to it."
			);
			return;
		}

		applyForce(physicsBody, deltaTime);

		detectCollisions(gameObject);

		if (physicsBody.getVelocity().lengthSquared() > 0) {
			transform.setPosition(
					transform.getPosition().add(physicsBody.getVelocity().mul((float) deltaTime))
			);
		}

		applyDrag(physicsBody, deltaTime);
	}

	@Override
	public void compute(LifeCycleContext context, double deltaTime) {
		if (!(context instanceof Game game)){
			Game.LOGGER.severe("PhysicsEngine compute method requires a Game context.");
			return;
		}
		Scene leftScene = game.getLeftScene();
		leftScene.getGameObjects().forEach(gameObject -> {
			if (gameObject.hasComponent(PhysicsBody.class)){
				applyPhysicsToGameObject(gameObject, deltaTime);
			}
		});
		Scene rightScene = game.getRightScene();
		rightScene.getGameObjects().forEach(gameObject -> {
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
