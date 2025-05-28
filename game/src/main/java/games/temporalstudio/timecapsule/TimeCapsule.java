package games.temporalstudio.timecapsule;

import static org.lwjgl.glfw.GLFW.*;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Input;
import games.temporalstudio.temporalengine.component.Trigger;
import games.temporalstudio.temporalengine.component.Triggerable;
import games.temporalstudio.temporalengine.listeners.KeyListener;
import games.temporalstudio.temporalengine.physics.*;
import org.joml.Vector2f;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TimeCapsule extends Game{

	private static final String IDENTIFIER = "timecapsule";

	public TimeCapsule(){
		super();

		setTitle(getI18n().getSentence("game.title", getVersion()));
		setMainMenu(new Scene("MainMenu"));
		setFirstLeftScene(this.createPastScenes());
		setFirstRightScene(this.createFutureScenes());
	}

	// GETTERS
	@Override
	public String getIdentifier(){ return IDENTIFIER; }

	public Scene createPastScenes(){
		Scene pastScene = new Scene("Past");

		GameObject pastGameObject1 = new GameObject("PastGameObject");
		// Define a function that returns a Boolean on whether the trigger condition is met
		Supplier<Boolean> PressedE = () -> KeyListener.isKeyPressed(GLFW_KEY_E);
		Trigger trigger = new Trigger(1.0f, PressedE);
		pastGameObject1.addComponent(trigger);
		pastScene.addGameObject(pastGameObject1);


		GameObject pastGameObject2 = new GameObject("PastGameObject2");
		// Define action to be triggered with gameObject passed as context
		Consumer<LifeCycleContext> triggerAction = (context) -> {
			if (context instanceof GameObject object) {
				Game.LOGGER.info("Trigger action executed for " + object.getName());
			} else {
				Game.LOGGER.warning("Trigger action executed with non-GameObject context.");
			}
		};
		Triggerable triggerable = new Triggerable(triggerAction);

		pastGameObject2.addComponent(triggerable);
		pastScene.addGameObject(pastGameObject2);

		trigger.addTriggerable(triggerable);

		pastScene.addChild(new Scene("PastChild1"));
		pastScene.addChild(new Scene("PastChild2"));
		pastScene.addChild(new Scene("PastChild3"));
		Scene pastChild4 = new Scene("PastChild4");
		pastScene.addChild(pastChild4);
		pastChild4.addChild(new Scene("PastChild4Child1"));
		return pastScene;
	}

	public Scene createFutureScenes() {
		Scene future = new Scene("Future");

		GameObject button = createButton();
		GameObject player = createPlayer();
		GameObject door = createDoor(button);

		future.addGameObject(player);
		future.addGameObject(button);
		future.addGameObject(door);

		return future;
	}

	private GameObject createButton() {
		GameObject button = new GameObject("button");
		Transform transform = new Transform(new Vector2f(1.0f, 1.0f), new Vector2f(1.0f, 1.0f));

		AtomicBoolean triggerActivated = new AtomicBoolean(false);
		Trigger trigger = new Trigger(1.0f , triggerActivated::get);

		Collider2D collider2D = new Collider2D(transform);
		collider2D.setGameOnCollide((context, other) -> triggerActivated.set(true));
		collider2D.setGameOnSeparate((context, other) -> triggerActivated.set(false));

		button.addComponent(transform);
		button.addComponent(collider2D);
		button.addComponent(trigger);

		PhysicsEngine.addCollider(button);
		return button;
	}

	private GameObject createPlayer() {
		GameObject player = new GameObject("player");
		Transform transform = new Transform(new Vector2f(1.0f, 1.0f), new Vector2f(3.0f, 3.0f));
		PhysicsBody physicsBody = new PhysicsBody(1.0f, 1.0f, 0.1f, 1.0f);
		Collider2D collider2D = new Collider2D(transform);

		Input input = new Input();
		input.addControl(GLFW_KEY_W, (context) -> {
			physicsBody.applyForce(new Vector2f(0.0f, 100.0f));
		});
		input.addControl(GLFW_KEY_S, (context) -> {
			physicsBody.applyForce(new Vector2f(0.0f, -100.0f));
		});
		input.addControl(GLFW_KEY_A, (context) -> {
			physicsBody.applyForce(new Vector2f(-100.0f, 0.0f));
		});
		input.addControl(GLFW_KEY_D, (context) -> {
			physicsBody.applyForce(new Vector2f(100.0f, 0.0f));
		});

		player.addComponent(transform);
		player.addComponent(physicsBody);
		player.addComponent(input);
		player.addComponent(collider2D);

		PhysicsEngine.addCollider(player);

		return player;
	}

	private GameObject createDoor(GameObject button) {
		GameObject door = new GameObject("door");
		Transform transform = new Transform(new Vector2f(1.0f, 1.0f), new Vector2f(1.0f, 3.0f));

		Collider2D collider2D = new Collider2D(transform);
		collider2D.setPhysicsOnCollide((context, other) -> {
			if (!(other instanceof GameObject otherObject)) {
				Game.LOGGER.severe("Collider2D onCollide called with non-GameObject context.");
				return;
			}
			PhysicsBody physicsBody = otherObject.getComponent(PhysicsBody.class);
			if (physicsBody == null) {
				Game.LOGGER.severe("Collider2D collided with GameObject that has no PhysicsBody.");
				return;
			}
			physicsBody.setVelocity(0.0f, 0.0f);
		});

		Trigger trigger = button.getComponent(Trigger.class);
		var ref = new Object() {
			Triggerable triggerable = null;
		};
		ref.triggerable = new Triggerable(context -> {
			if (context instanceof GameObject object) {
				PhysicsEngine.removeCollider(door);
				trigger.removeTriggerable(ref.triggerable);
				button.removeComponent(trigger);
			} else {
				Game.LOGGER.warning("Door trigger action executed with non-GameObject context.");
			}
		});
		trigger.addTriggerable(ref.triggerable);

		door.addComponent(transform);
		door.addComponent(collider2D);
		door.addComponent(ref.triggerable);

		PhysicsEngine.addCollider(door);

		return door;
	}
}
