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
	ArrayList<KeyFragment> fragments = new ArrayList<KeyFragment>();


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
		input.addControl(keys[5], (context) -> {
			switchSelectedObject(1);
		});

		this.gameObject.addComponent(transform);
		this.gameObject.addComponent(collider);
		this.gameObject.addComponent(render);
		this.gameObject.addComponent(physicsBody);
		this.gameObject.addComponent(input);

	}

	public void allFragmentKey(){
		for (InventoryObject object : inventory){
			if (object instanceof KeyFragment){
				fragments.add((KeyFragment) object);
			}
		}
	}

	public void buildCompleteKey(Chest chest){
		for (KeyFragment fragment : fragments){
            inventory.removeIf(object -> object.equals(fragment));
		}
		fragments.clear();
		this.addToInventory(new CompleteKey("Key", 100.0f, 100.0f, this.getGameObject(), this, chest));
		System.out.println("Cle reconstitue !!");
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
		if (inventory.isEmpty()) {
			Game.LOGGER.warning(this.gameObject.getName() + " tried to switch selected object, but inventory is empty.");
			return;
		}
		selectedObject = (selectedObject + direction + inventory.size()) % inventory.size();
		Game.LOGGER.info(this.gameObject.getName() + "'s selected object changed to: " + inventory.get(selectedObject).getGameObject().getName());
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
	}
}
