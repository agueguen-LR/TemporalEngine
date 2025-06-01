package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Input;
import org.joml.Vector2f;
import org.joml.Vector4f;


import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;

public class Player extends Entity {

    private Input input;
    private Inventory inventory;

    public Player(String name, Vector2f position, int[] keyCodes, Vector4f color) {
        super(name,new Vector2f(1,1),position, color);
        if (keyCodes.length !=5){
            throw new IllegalArgumentException("keyCodes.length must be 5");
        }
        input=new Input();
        System.out.println("Player: "+name);
        this.keyControllDefinition(keyCodes);
        inventory= new Inventory();

        this.addComponent(input);
        this.addComponent(collider);
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


    public Inventory getInventory() {return inventory;}
    public void emptyInventory(){inventory.removeAll();}
    public boolean addItems(ArrayList<Object> items){
        if(inventory.addAll(items)){
            return true;}
        System.out.println("Error: inventory.addItems failed");
        return false;
    }


    public void keyControllDefinition(int[] keyCodes) {
        input.addControl(keyCodes[0], (context) -> {
            this.moveUp();
        });
        input.addControl(keyCodes[1], (context) -> {
            this.moveDown();
        });
        input.addControl(keyCodes[2], (context) -> {
            this.moveLeft();
        });
        input.addControl(keyCodes[3], (context) -> {
            this.moveRight();
        });
        input.addControl(keyCodes[4], (context) -> {
            this.jump();
        });

    }

}
