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
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.View;

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TimeCapsule extends Game{

	private static final String IDENTIFIER = "timecapsule";

	public TimeCapsule(){
		super();

		setTitle(getI18n().getSentence("game.title", getVersion()));

		setMainMenu(createMainMenu());
		setFirstLeftScene(createPastScenes());
		setFirstRightScene(createFutureScenes());
	}

	// GETTERS
	@Override
	public String getIdentifier(){ return IDENTIFIER; }

	// FUNCTIONS
	private Scene createMainMenu(){
		Scene mainMenu = new Scene("MainMenu");

		GameObject camera = new GameObject("camera");
		camera.addComponent(new Transform(
			new Vector2f(6.4f, 3.6f), new Vector2f(0, 0)
		));
		camera.addComponent(new View());
		mainMenu.addGameObject(camera);

		return mainMenu;
	}
	private Scene createPastScenes(){
		Scene pastScene = new Scene("Past");

		// Game objects
		GameObject pastCamera = new GameObject("PastCamera");

		GameObject pastGameObject1 = new GameObject("PastGameObject");
		// Define a function that returns a Boolean on whether the trigger condition is met
		Supplier<Boolean> PressedE = () -> KeyListener.isKeyPressed(GLFW_KEY_E);
		Trigger trigger = new Trigger(1.0f, PressedE);

		GameObject pastGameObject2 = new GameObject("PastGameObject2");
		// Define action to be triggered with gameObject passed as context
		Consumer<LifeCycleContext> triggerAction = (context) -> {
			if (context instanceof GameObject object)
				Game.LOGGER.info(
					"Trigger action executed for " + object.getName()
				);
			else
				Game.LOGGER.warning(
					"Trigger action executed with non-GameObject context."
				);
		};
		Triggerable triggerable = new Triggerable(triggerAction);

		// Components
		pastCamera.addComponent(new Transform(
			new Vector2f(6.4f, 3.6f), new Vector2f(0, 0)
		));
		pastCamera.addComponent(new View());

		pastGameObject1.addComponent(trigger);
		trigger.addTriggerable(triggerable);

		pastGameObject2.addComponent(triggerable);

		// Scenes
		pastScene.addGameObject(pastCamera);
		pastScene.addGameObject(pastGameObject1);
		pastScene.addGameObject(pastGameObject2);

		pastScene.addChild(new Scene("PastChild1"));
		pastScene.addChild(new Scene("PastChild2"));
		pastScene.addChild(new Scene("PastChild3"));
		Scene pastChild4 = new Scene("PastChild4");
		pastScene.addChild(pastChild4);
		pastChild4.addChild(new Scene("PastChild4Child1"));
		return pastScene;
	}
	private Scene createFutureScenes(){
		Scene future = new Scene("Future");

		// Game objects
		GameObject futureCamera = new GameObject("FutureCamera");

		GameObject futureGameObject1 = new GameObject("FutureGameObject1");
		Transform transform = new Transform(
			new Vector2f(0.0f, 0.0f), new Vector2f(1, 1)
		);

		GameObject futureGameObject2 = new GameObject("FutureGameObject2");
		Transform transform2 = new Transform(
			new Vector2f(1.0f, 1.0f), new Vector2f(5, 2)
		);
		PhysicsBody physicsBody = new PhysicsBody(
			1.0f, 1.0f, 0.1f, 1.0f
		);
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

		// Components
		futureCamera.addComponent(new Transform(
			new Vector2f(6.4f, 3.6f), new Vector2f(0, 0)
		));
		futureCamera.addComponent(new View());


		futureGameObject1.addComponent(transform);
		futureGameObject1.addComponent(new ColorRender(new Vector4f(
			0, 1, 0, 1
		)));

		futureGameObject2.addComponent(transform2);
		futureGameObject2.addComponent(new ColorRender(new Vector4f(
			1, 0, 0, 1
		)));
		futureGameObject2.addComponent(physicsBody);
		futureGameObject2.addComponent(input);

		// Scenes
		future.addGameObject(futureCamera);
		future.addGameObject(futureGameObject1);
		future.addGameObject(futureGameObject2);

		future.addChild(new Scene("FutureChild1"));
		future.addChild(new Scene("FutureChild2"));
		future.addChild(new Scene("FutureChild3"));
		Scene futureChild4 = new Scene("FutureChild4");
		future.addChild(futureChild4);
		futureChild4.addChild(new Scene("FutureChild4Child1"));
		return future;
	}
}
