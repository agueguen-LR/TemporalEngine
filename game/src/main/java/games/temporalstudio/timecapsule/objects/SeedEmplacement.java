package games.temporalstudio.timecapsule.objects;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.component.Input;
import games.temporalstudio.temporalengine.component.Trigger;
import games.temporalstudio.temporalengine.component.Triggerable;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;
import games.temporalstudio.timecapsule.Entity.Player;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class SeedEmplacement implements TimeObject{
    private GameObject gameObject;
    private Scene scene;
    private Seed seed;



    public SeedEmplacement(String name, float x, float y, Player player, int keyPlayerUse) {
        this.gameObject = new GameObject(name);
        Transform transform = new Transform(new Vector2f(x, y));
        Render render = new ColorRender(new Vector4f(0.8f, 0.2f, 0.2f, 1));

        Collider2D collider2D = new Collider2D(new AABB(transform));
        collider2D.setOnIntersects((context, other) -> {
            if (other instanceof Player) {
                //Checking if the player have a seed
                if (player.inventoryContain(seed)){
                    if (player.getGameObject().getComponent(Input.class).isControlPressed(keyPlayerUse)){
                        //Adding the seed of the player to the emplacement
                        this.seed = (Seed) player.getInventoryObject(seed);
                        //Remove the seed from player inventory
                        player.removeFromInventory(seed);
                        this.scene.addGameObject((this.seed.getGameObject()));


                    }
                }
            }
        });
        this.gameObject.addComponent(transform);
        this.gameObject.addComponent(render);
        this.gameObject.addComponent(collider2D);



    }

    @Override
    public GameObject getGameObject() {
        return gameObject;
    }


}
