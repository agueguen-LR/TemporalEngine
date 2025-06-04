package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Input;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.PhysicsBody;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.timecapsule.objects.InventoryObject;
import org.joml.Vector2f;
import org.joml.Vector4f;


import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;

public class Player extends Entity {

    private ArrayList<InventoryObject> inventory = new ArrayList<>();
    private int selectedObject = 0;

    public Player(int x, int y, int[] keyCodes, Vector4f color) {
        super("player", new Vector2f(x,y), new Vector2f(1,2), new float[]{1,10,0.1f,20f},  color);
        if (keyCodes.length !=5){
            throw new IllegalArgumentException("keyCodes.length must be 5");
        }
        Input input=new Input();
        this.keyControllDefinition(keyCodes, input);
        Collider2D collider=new Collider2D((new AABB(transform)));
        collider.setRigid(true);

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


    public boolean inventoryContain(InventoryObject inventoryObject){
        return inventory.stream().anyMatch(obj -> obj.getClass().equals(inventoryObject.getClass()));
    }
    public InventoryObject getInventoryObject(InventoryObject inventoryObject){
        return inventory.stream().filter(obj -> obj.getClass().equals(inventoryObject.getClass())).findFirst().orElse(null);
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

    public void switchSelectedObject(int direction) {
        if (inventory.isEmpty()) {
            Game.LOGGER.warning(this.p.getName() + " tried to switch selected object, but inventory is empty.");
            return;
        }
        selectedObject = (selectedObject + direction + inventory.size()) % inventory.size();
        Game.LOGGER.info(this.p.getName() + "'s selected object changed to: " + inventory.get(selectedObject).getGameObject().getName());
    }

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
            this.moveUp(50);
        });
        input.addControl(keyCodes[1], (context) -> {
            this.moveLeft(50);
        });
        input.addControl(keyCodes[2], (context) -> {
            this.moveDown(50);
        });
        input.addControl(keyCodes[3], (context) -> {
            this.moveRight(50);
        });
        input.addControl(keyCodes[4], (context) -> {
            this.useSelectedObject();
        });

    }

}
