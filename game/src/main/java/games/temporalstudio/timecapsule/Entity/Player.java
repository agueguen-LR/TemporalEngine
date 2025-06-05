package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Input;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.PhysicsBody;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.Layer;
import games.temporalstudio.temporalengine.rendering.component.SpriteRender;
import games.temporalstudio.temporalengine.rendering.component.TileRender;
import games.temporalstudio.timecapsule.objects.Chest;
import games.temporalstudio.timecapsule.objects.CompleteKey;
import games.temporalstudio.timecapsule.objects.InventoryObject;
import games.temporalstudio.timecapsule.objects.KeyFragment;

import org.joml.Vector2f;
import org.joml.Vector4f;


import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;

public class Player extends Entity {

    private ArrayList<InventoryObject> inventory = new ArrayList<>();
    private int selectedObject = 0;
	public ArrayList<KeyFragment> fragments = new ArrayList<KeyFragment>();
	private CompleteKey key = null;

    public Player(int x, int y, int[] keyCodes, Vector4f color, String texture) {
        super("player", new Vector2f(x,y), new Vector2f(1,2), new float[]{1,10,0.1f,20f},  color, texture);
        if (keyCodes.length != 7){
            throw new IllegalArgumentException("keyCodes.length must be 7");
        }

        Input input=new Input();
        this.keyControllDefinition(keyCodes, input);
        Collider2D collider=new Collider2D((new AABB(transform)));
        collider.setRigid(true);

        getRender().setAnimChooser(context -> {
            Vector2f vel = physicsBody.getVelocity();
            float angle = vel.angle(new Vector2f(1, 0));
            String tileName;

            if(angle > Math.PI/4*3 || angle < -Math.PI/4*3)
                tileName = "left_walk";
            else if(angle < Math.PI/4*3 && angle > Math.PI/4)
                tileName = "face_walk";
            else if(angle > -Math.PI/4*3 && angle < -Math.PI/4)
                tileName = "back_walk";
            else
                tileName = "right_walk";

            return tileName;
        });

        p.addComponent(input);
        p.addComponent(collider);
    }

    /*public Player(String name, Vector2f scale, Vector2f position, int[] keyCodes, Vector4f color) {
        super(name, scale, position, color);
        if (keyCodes.length !=5){
            throw new IllegalArgumentException("keyCodes.length must be 5");
        }
        input = new Input();
        this.keyControllDefinition(keyCodes);
        inventory = new Inventory();
        this.addComponent(input);
    }*/

    public Input getInput() {return p.getComponent(Input.class);}
    public GameObject getGameObject(){return p;}


    public boolean inventoryContains(Class<? extends InventoryObject> inventoryObject) {
        if (inventoryObject == null) {
            Game.LOGGER.warning("Tried to check inventory for a null object.");
            return false;
        }
        return inventory.stream().anyMatch(obj -> inventoryObject.isAssignableFrom(obj.getClass()));
    }
    public InventoryObject getInventoryObject(Class<? extends InventoryObject> inventoryObject){
        return inventory.stream().filter(
            obj -> inventoryObject.isAssignableFrom(obj.getClass())
        ).findFirst().orElse(null);
    }

    public ArrayList<InventoryObject> getInventory(){return inventory;}

    public void addToInventory(InventoryObject object) {
        Game.LOGGER.info("Adding object to inventory: " + object.getGameObject().getName());
        inventory.add(object);
    }

    public boolean removeFromInventory(InventoryObject object) {
        Game.LOGGER.info("Removing object from inventory: " + object.getGameObject().getName());
        if(inventory.remove(object)){
            if (selectedObject >= inventory.size()) {
                selectedObject = 0; // Reset selected object if it exceeds the new inventory size
            }
            return true;}
        return false;
    }

	/**
	 * Switches the selected object in the inventory.
	 *
	 * @param direction 1 to switch to the next object, -1 to switch to the previous object.
	 */
    public void switchSelectedObject(int direction) {
        if (inventory.isEmpty()) {
            Game.LOGGER.warning(this.p.getName() + " tried to switch selected object, but inventory is empty.");
            return;
        }
        selectedObject = (selectedObject + direction + inventory.size()) % inventory.size();
        Game.LOGGER.info(this.p.getName() + "'s selected object changed to: " + inventory.get(selectedObject).getGameObject().getName());
    }

	/**
	 * Uses the currently selected object in the inventory.
	 */
    public void useSelectedObject() {
        if (inventory.isEmpty()) {
            Game.LOGGER.warning(this.p.getName() + " tried to use selected object, but inventory is empty.");
            return;
        }
        InventoryObject selectedObject = inventory.get(this.selectedObject);
        selectedObject.getTriggerable().trigger(selectedObject.getGameObject());
    }

    public void keyControllDefinition(int[] keyCodes, Input input) {
        input.addControl(keyCodes[0], (context) -> {
            this.moveUp(50); //50
        });
        input.addControl(keyCodes[1], (context) -> {
            this.moveLeft(50); //50
        });
        input.addControl(keyCodes[2], (context) -> {
            this.moveDown(50); //50
        });
        input.addControl(keyCodes[3], (context) -> {
            this.moveRight(50); //50
        });
        input.addControl(keyCodes[4], (context) -> {
            this.useSelectedObject();
        });

        input.addControl(keyCodes[5],
			(context) -> switchSelectedObject(1)
		);
        input.addControl(keyCodes[6], (context) -> useSelectedObject());
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

		if(key == null)
			Game.LOGGER.severe("Complete key not defined.");
		else
			this.addToInventory(key);
	}

	public void setKey(CompleteKey key){
		this.key = key;
	}
}
