package games.temporalstudio.timecapsule;

import static org.lwjgl.glfw.GLFW.*;

import games.temporalstudio.temporalengine.component.Input;
import games.temporalstudio.timecapsule.Entity.Enemy;
import games.temporalstudio.timecapsule.Entity.Player;
import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Trigger;
import games.temporalstudio.temporalengine.component.Triggerable;
import games.temporalstudio.temporalengine.listeners.KeyListener;
import games.temporalstudio.temporalengine.physics.*;
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
	public Scene createFutureScenes() {
		Scene future = new Scene("Future");

		GameObject camera = new GameObject("camera");
		camera.addComponent(new Transform(
				new Vector2f(6.4f, 3.6f), new Vector2f(0, 0)
		));
		camera.addComponent(new View());
		future.addGameObject(camera);



		Player futureGameObject1 = new Player("FutureGameObject1",new Vector2f(0.0f, 0.0f),
			new int[]{GLFW_KEY_W, GLFW_KEY_S, GLFW_KEY_A, GLFW_KEY_D,GLFW_KEY_SPACE},
			new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		Player futureGameObject2 = new Player("FutureGameObject2",new Vector2f(1.0f, 1.0f),
				new int[]{GLFW_KEY_UP, GLFW_KEY_DOWN, GLFW_KEY_LEFT, GLFW_KEY_RIGHT,GLFW_KEY_LEFT_SHIFT},
				new Vector4f(1.0f, 1.0f, 0.1f, 1.0f)
		);

		Vector2f[] coords={new Vector2f(), new Vector2f(100.0f, 100.0f)};
		Enemy chauveSouris= new Enemy("chauvesouris",
				new Vector2f(1.0f, 1.0f), new Vector2f(1.0f, 1.0f),
				new Vector4f(1.0f,1.0f,0.0f,0f),coords);


		future.addGameObject(futureGameObject1);
		future.addGameObject(futureGameObject2);
		future.addGameObject(chauveSouris);

		return future;
	}
}
