package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Input;
import org.joml.Vector2f;
import org.joml.Vector4f;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;

public class Player extends Entity {

    private float gauge;
    private Input input;
    private Inventory<GameObject> inventory;

    public Player(String name, Vector2f position, int[] keyCodes, Vector4f color) {
        super(name, new Vector2f(1.0f, 2.0f),position, color);
        if (keyCodes.length !=5){
            throw new IllegalArgumentException("keyCodes.length must be 5");
        }
        gauge=0.0f;
        input=new Input();
        this.keyControllDefinition(keyCodes);
        inventory= new Inventory<>();
        this.addComponent(input);
        this.addComponent(inventory);
    }

    public Player(String name, Vector2f scale, Vector2f position, int[] keyCodes, Vector4f color) {
        super(name, scale, position, color);
        if (keyCodes.length !=5){
            throw new IllegalArgumentException("keyCodes.length must be 5");
        }
        gauge = 0.0f;
        input = new Input();
        this.keyControllDefinition(keyCodes);
        inventory = new Inventory<>();
        this.addComponent(input);
        this.addComponent(inventory);
    }

    public void fedding(){
        gauge = 1.0f;
    }

    public void keyControllDefinition(int[] keyCodes) {
        input.addControl(keyCodes[0], (context) -> {
            this.moveUp(gauge);
        });
        input.addControl(keyCodes[1], (context) -> {
            this.moveDown(gauge);
        });
        input.addControl(keyCodes[2], (context) -> {
            this.moveLeft(gauge);
        });
        input.addControl(keyCodes[3], (context) -> {
            this.moveRight(gauge);
        });
        input.addControl(keyCodes[4], (context) -> {
            this.jump();
        });

    }

}
