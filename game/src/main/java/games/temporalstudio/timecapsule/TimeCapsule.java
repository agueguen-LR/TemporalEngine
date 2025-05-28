package games.temporalstudio.timecapsule;

import static org.lwjgl.glfw.GLFW.*;

import Entity.Enemy;
import Entity.Player;
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

		/*GameObject futureGameObject1 = new GameObject("FutureGameObject1");
		Transform transform = new Transform(new Vector2f(0.0f, 0.0f), new Vector2f(1.0f, 1.0f));

		GameObject futureGameObject2 = new GameObject("FutureGameObject2");
		Transform transform2 = new Transform(new Vector2f(1.0f, 1.0f), new Vector2f(1.0f, 1.0f));
		PhysicsBody physicsBody = new PhysicsBody(1.0f, 1.0f, 0.1f, 1.0f);
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

		futureGameObject1.addComponent(transform);

		future.addGameObject(futureGameObject1);
		future.addGameObject(futureGameObject2);
		futureGameObject2.addComponent(transform2);
		futureGameObject2.addComponent(physicsBody);
		futureGameObject2.addComponent(input);
*/
		Player futureGameObject1 = new Player("FutureGameObject1", new Vector2f(0.0f, 0.0f), new Vector2f(1.0f, 1.0f));
		Player futureGameObject2 = new Player("FutureGameObject2",new Vector2f(1.0f, 1.0f), new Vector2f(1.0f, 1.0f));
		future.addGameObject(futureGameObject1);
		future.addGameObject(futureGameObject2);

		Enemy chauveSouris= new Enemy("chauvesouris", new Vector2f(1.0f, 1.0f), new Vector2f(1.0f, 1.0f),new Vector2f(0.0f, 0.0f),new Vector2f(100.0f, 100.0f));



		future.addChild(new Scene("FutureChild1"));
		future.addChild(new Scene("FutureChild2"));
		future.addChild(new Scene("FutureChild3"));
		Scene futureChild4 = new Scene("FutureChild4");
		future.addChild(futureChild4);
		futureChild4.addChild(new Scene("FutureChild4Child1"));
		return future;
	}
}
