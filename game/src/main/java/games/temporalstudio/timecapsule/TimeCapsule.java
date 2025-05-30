package games.temporalstudio.timecapsule;

import static org.lwjgl.glfw.GLFW.*;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Input;
import games.temporalstudio.temporalengine.component.Trigger;
import games.temporalstudio.temporalengine.component.Triggerable;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.PhysicsBody;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.temporalengine.rendering.component.View;

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimeCapsule extends Game{

	private static final String IDENTIFIER = "timecapsule";

	public TimeCapsule(){
		super(null, "icon.png");

		setTitle(getI18n().getSentence("game.title", getVersion()));

		setMainMenu(createMainMenu());
		setFirstLeftScene(this.createPastScenes());
		setFirstRightScene(this.createFutureScenes());
	}

	// GETTERS
	@Override
	public String getIdentifier(){ return IDENTIFIER; }

	// FUNCTIONS
	private Scene createMainMenu(){
		Scene mainMenu = new Scene("MainMenu");

		GameObject camera = new GameObject("camera");
		camera.addComponent(new Transform());
		camera.addComponent(new View(.1f));
		mainMenu.addGameObject(camera);

		return mainMenu;
	}
	private Scene createPastScenes(){
		Scene past = new Scene("Past");

		// Game objects
		GameObject camera = new GameObject("PastCamera");

		GameObject player = createPlayer(new int[]{
			GLFW_KEY_W, GLFW_KEY_S, GLFW_KEY_A, GLFW_KEY_D,
			GLFW_KEY_Q
		});
		GameObject compulsiveMerger = new GameObject("Adrien");

		// Components
		camera.addComponent(new Transform());
		camera.addComponent(new View(.1f));

		compulsiveMerger.addComponent(new Transform(new Vector2f(1, 2)));
		compulsiveMerger.addComponent(new ColorRender(List.of(
			new Vector4f(1, .6f, 0, 1),
			new Vector4f(.75f, .45f, .15f, 1),
			new Vector4f(1, .6f, 0, 1),
			new Vector4f(1, .6f, 0, 1)
		)));

		// Scene
		past.addGameObject(camera);
		past.addGameObject(player);
		past.addGameObject(compulsiveMerger);

		return past;
	}
	private Scene createFutureScenes(){
		Scene future = new Scene("Future");

		GameObject camera = new GameObject("camera");
		camera.addComponent(new Transform());
		camera.addComponent(new View(.1f));

		GameObject button = createButton();
		GameObject player = createPlayer(new int[]{
			GLFW_KEY_UP, GLFW_KEY_DOWN, GLFW_KEY_LEFT, GLFW_KEY_RIGHT,
			GLFW_KEY_SLASH
		});
		GameObject door = createDoor(button);
		GameObject rock = createBreakableRock(GLFW_KEY_SLASH);

		future.addGameObject(camera);
		future.addGameObject(player);
		future.addGameObject(button);
		future.addGameObject(door);
		future.addGameObject(rock);

		return future;
	}

	private GameObject createButton(){
		GameObject button = new GameObject("button");

		Render render = new ColorRender(new Vector4f(0, 1, 0, 1));
		Transform transform = new Transform(new Vector2f(1f, .5f));

		AtomicBoolean triggerActivated = new AtomicBoolean(false);
		Trigger trigger = new Trigger(1 , triggerActivated::get);

		Collider2D collider2D = new Collider2D(transform);
		collider2D.setShape(new AABB(transform));
		collider2D.setOnIntersects(
			(context, other) -> triggerActivated.set(true)
		);

		button.addComponent(transform);
		button.addComponent(render);
		button.addComponent(collider2D);
		button.addComponent(trigger);

		return button;
	}
	private GameObject createPlayer(int[] keys){
		GameObject player = new GameObject("player");

		Render render = new ColorRender(new Vector4f(0, 0, 1, 1));
		Transform transform = new Transform(new Vector2f(5, 2));
		PhysicsBody physicsBody = new PhysicsBody(
			1, 1, .1f, 1
		);
		Collider2D collider2D = new Collider2D(transform);
		collider2D.setShape(new AABB(transform));

		Input input = new Input();
		input.addControl(keys[0], (context) -> {
			physicsBody.applyForce(new Vector2f(0, 100));
		});
		input.addControl(keys[1], (context) -> {
			physicsBody.applyForce(new Vector2f(0, -100));
		});
		input.addControl(keys[2], (context) -> {
			physicsBody.applyForce(new Vector2f(-100, 0));
		});
		input.addControl(keys[3], (context) -> {
			physicsBody.applyForce(new Vector2f(100, 0));
		});
		input.addControl(keys[4], (context) -> {});

		player.addComponent(transform);
		player.addComponent(render);
		player.addComponent(physicsBody);
		player.addComponent(input);
		player.addComponent(collider2D);

		return player;
	}
	private GameObject createDoor(GameObject button){
		GameObject door = new GameObject("door");

		Render render = new ColorRender(new Vector4f(1, 0, 0, 1));
		Transform transform = new Transform(new Vector2f(1, 2));

		Collider2D collider2D = new Collider2D(transform);
		collider2D.setShape(new AABB(transform));
		collider2D.setRigid(true);

		Trigger trigger = button.getComponent(Trigger.class);
		var ref = new Object() {
			Triggerable triggerable = null;
		};
		ref.triggerable = new Triggerable(context -> {
			if(context instanceof GameObject){
				trigger.removeTriggerable(ref.triggerable);
				button.removeComponent(trigger);
				collider2D.disable();
			}else
				Game.LOGGER.warning(
					"Door trigger action executed with non-GameObject context."
				);
		});
		trigger.addTriggerable(ref.triggerable);

		door.addComponent(transform);
		door.addComponent(render);
		door.addComponent(collider2D);
		door.addComponent(ref.triggerable);

		return door;
	}
	private GameObject createBreakableRock(int key){
		GameObject rock = new GameObject("rock");

		Render render = new ColorRender(
			new Vector4f(.5f, .25f, .1f, 1)
		);
		Transform transform = new Transform(new Vector2f(5, .5f));
		Collider2D collider2D = new Collider2D(transform);
		collider2D.setShape(new AABB(transform));
		collider2D.setRigid(true);
		collider2D.setOnCollide((context, other) -> {
			if(other instanceof GameObject player
				&& player.getName().equals("player")
				&& player.getComponent(Input.class)
					.isControlPressed(key)
			){
				Game.LOGGER.info("Rock broken by player!");
				collider2D.disable();
				rock.removeComponent(render);
			}
		});

		rock.addComponent(transform);
		rock.addComponent(render);
		rock.addComponent(collider2D);

		return rock;
	}
}