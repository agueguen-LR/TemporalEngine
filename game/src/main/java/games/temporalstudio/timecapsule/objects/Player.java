package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Input;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.PhysicsBody;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Player implements TimeObject {
	private final GameObject gameObject;
	private ArrayList<InventoryObject> inventory = new ArrayList<>();
	private int selectedObject = 0;

	public Player(String name, float x, float y, int[] keys) {
		this.gameObject = new GameObject(name);
		Transform transform = new Transform(new Vector2f(x, y));
		Render render = new ColorRender(new Vector4f(0, 0, 1, 1));
		Collider2D collider = new Collider2D(new AABB(transform));
		collider.setRigid(true);
		PhysicsBody physicsBody = new PhysicsBody(1, 10, .1f, 20f);

		Input input = new Input();
		input.addControl(keys[0], (context) -> {
			physicsBody.applyForce(new Vector2f(0, 10));
		});
		input.addControl(keys[1], (context) -> {
			physicsBody.applyForce(new Vector2f(-10, 0));
		});
		input.addControl(keys[2], (context) -> {
			physicsBody.applyForce(new Vector2f(0, -10));
		});
		input.addControl(keys[3], (context) -> {
			physicsBody.applyForce(new Vector2f(10, 0));
		});
		input.addControl(keys[4], (context) -> {
			useSelectedObject();
		});

		this.gameObject.addComponent(transform);
		this.gameObject.addComponent(collider);
		this.gameObject.addComponent(render);
		this.gameObject.addComponent(physicsBody);
		this.gameObject.addComponent(input);
	}

	@Override
	public GameObject getGameObject() {
		return gameObject;
	}

	/**
	 * Adds an object to the player's inventory.
	 *
	 * @param object The InventoryObject to be added.
	 */
	public void addToInventory(InventoryObject object) {
		Game.LOGGER.info("Adding object to inventory: " + object.getGameObject().getName());
		inventory.add(object);
	}

	public void removeFromInventory(InventoryObject object) {
		Game.LOGGER.info("Removing object from inventory: " + object.getGameObject().getName());
		inventory.remove(object);
		if (selectedObject >= inventory.size()) {
			selectedObject = 0; // Reset selected object if it exceeds the new inventory size
		}
	}

	/**
	 * Switches the selected object in the inventory.
	 *
	 * @param direction 1 to switch to the next object, -1 to switch to the previous object.
	 */
	public void switchSelectedObject(int direction) {
		if (inventory.isEmpty()) return;
		selectedObject = (selectedObject + direction + inventory.size()) % inventory.size();
	}

	/**
	 * Uses the currently selected object in the inventory.
	 */
	public void useSelectedObject() {
		if (inventory.isEmpty()) {
			return;
		}
		InventoryObject selectedObject = inventory.get(this.selectedObject);
		selectedObject.getTriggerable().trigger(selectedObject.getGameObject());
		removeFromInventory(selectedObject);
	}
}
