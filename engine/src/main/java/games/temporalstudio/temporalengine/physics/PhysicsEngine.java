package games.temporalstudio.temporalengine.physics;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import org.joml.Vector2f;

public class PhysicsEngine implements PhysicsEngineLifeCycle{

	public void applyForce(PhysicsBody physicsBody, double deltaTime, Transform transform) {
		Vector2f force = physicsBody.getExertedForce();
		if (force.lengthSquared() == 0) {
			// No force to apply, return early
			return;
		}

		float mass = physicsBody.getMass();
		Vector2f velocity = physicsBody.getVelocity();
		float maxVelocity = physicsBody.getMaxVelocity();
		if (mass == 0) {
			Game.LOGGER.severe("Cannot apply force to a PhysicsBody with zero mass.");
			return;
		}

		velocity.fma((float) (deltaTime / mass), force);
		// Comparing squares to avoid costly square root calculation
		if (velocity.lengthSquared() > maxVelocity * maxVelocity) {
			velocity.normalize(maxVelocity);
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

		applyForce(physicsBody, deltaTime, transform);

		if (physicsBody.getVelocity().lengthSquared() > 0) {
			transform.setPosition(
					transform.getPosition().add(physicsBody.getVelocity().mul((float) deltaTime))
			);
		}
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
