package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Input;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;

public class Player extends Entity {

    private float gauge;
    private Input input;
    private Inventory<GameObject> inventory;

    public Player(String name, Vector2f position) {
        super(name, position, new Vector2f());
        gauge=0.0f;
        input=new Input();
        this.keyControllDefinition();
        inventory= new Inventory<>();
        this.addComponent(input);
        this.addComponent(inventory);
    }

    public Player(String name, Vector2f scale, Vector2f position) {
        super(name, scale, position);
        gauge = 0.0f;
        input = new Input();
        this.keyControllDefinition();
        inventory = new Inventory<>();
        this.addComponent(input);
        this.addComponent(inventory);
    }

    public void fedding(){
        gauge = 1.0f;
    }

    public void keyControllDefinition(){
        this.input.addControl(GLFW_KEY_W, (context) -> {this.moveUp(gauge);});
        input.addControl(GLFW_KEY_S, (context) -> {this.moveDown(gauge);});
        input.addControl(GLFW_KEY_A, (context) -> {this.moveLeft(gauge);});
        input.addControl(GLFW_KEY_D, (context) -> {this.moveRight(gauge);});
        input.addControl(GLFW_KEY_SPACE, (context) -> {this.jump();});
        /*input.addControl(GLFW_KEY_UP, (context) -> {this.moveUp(gauge);});
        input.addControl(GLFW_KEY_DOWN, (context) -> {this.moveDown(gauge);});
        input.addControl(GLFW_KEY_LEFT, (context) -> {this.moveLeft(gauge);});
        input.addControl(GLFW_KEY_RIGHT, (context) -> {this.moveRight(gauge);});
        input.addControl(GLFW_KEY_RIGHT_SHIFT, (context) -> {this.jump();});*/
    }

}
